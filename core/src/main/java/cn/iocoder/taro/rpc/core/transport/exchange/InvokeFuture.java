package cn.iocoder.taro.rpc.core.transport.exchange;

import cn.iocoder.taro.rpc.core.transport.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class InvokeFuture {

    private static final ConcurrentMap<Long, InvokeFuture> FUTURES = new ConcurrentHashMap<Long, InvokeFuture>();
    private static final TimeoutTaskScanner SCANNER = new TimeoutTaskScanner();

    //    private final Object lock = new Object();
    private final CountDownLatch latch = new CountDownLatch(1); // TODO 芋艿，性能，后续对比三种方式的性能差别
    private final Channel channel;
    private final Request request;
    private volatile Response response;
    private volatile ResponseCallback callback;
    /**
     * 创建开始时间，用于超时逻辑
     */
    private long startTimeMillis = 0;
    private long timeoutMillis = 0;

    static {
        Thread th = new Thread(SCANNER);
        th.setDaemon(true);
        th.start();
    }

    public InvokeFuture(Channel channel, Request request) {
        this.channel = channel;
        this.request = request;
        // 添加到 FUTURES 中
        FUTURES.put(request.getId(), this);
    }

    public Response waitResponse() throws InterruptedException {
        if (isDone()) {
            return response;
        }
        // 阻塞等待
        latch.await();
        return response;
    }

    public Response waitResponse(long timeoutMillis) throws InterruptedException {
        if (isDone()) {
            return response;
        }
        // 带超时的阻塞等待
        latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
        return response;
    }

    public boolean notifyResponse(Response response) {
        // 通过移除，保证只有一个结果设置成功
        InvokeFuture future = FUTURES.remove(response.getId());
        if (future == null) {
            return false;
        }
        // 移除超时任务
        SCANNER.removeTask(future);
        // 设置响应
        try {
            this.response = response;
            // 回调
            onCallback(response, callback);
        } finally {
            // 唤醒
            latch.countDown();
        }
        return true;
    }

    public boolean isDone() {
        return response != null;
    }

    public ResponseCallback getCallback() {
        return callback;
    }

    public InvokeFuture setCallback(ResponseCallback callback) {
        this.callback = callback;
        // 回调
        onCallback(response, callback);
        return this;
    }

    private void onCallback(Response response, ResponseCallback callback) {
        if (response != null && callback != null) {
            if (response.getValue() != null) {
                callback.onSuccess(response.getValue());
            } else if (response.getException() != null) {
                callback.onFailure(response.getException());
            }
        }
    }

    public static void addTimeoutTask(InvokeFuture future, long timeoutMillis) {
        SCANNER.addTask(future, timeoutMillis);
    }

    private static class TimeoutTaskScanner implements Runnable {

        private final ConcurrentMap<Long, InvokeFuture> tasks = new ConcurrentHashMap<Long, InvokeFuture>();

        @Override
        public void run() {
            while (true) {
                try {
                    for (Map.Entry<Long, InvokeFuture> entry : tasks.entrySet()) {
                        InvokeFuture future = entry.getValue();
                        if (System.currentTimeMillis() < future.timeoutMillis) {
                            continue;
                        }
                        // 超时
                        Response response = createTimeoutResponse(entry.getKey());
                        boolean success = future.notifyResponse(response);
                        if (!success) { //
                            removeTask(future);
                        }
                    }
                    // 等待下一个轮回
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        void addTask(InvokeFuture future, long timeoutMillis) {
            future.startTimeMillis = System.currentTimeMillis();
            future.timeoutMillis = future.startTimeMillis + timeoutMillis;
            this.tasks.put(future.request.getId(), future);
        }

        void removeTask(InvokeFuture future) {
            tasks.remove(future.request.getId());
        }

    }

    public static InvokeFuture getFuture(long requestId) {
        return FUTURES.get(requestId);
    }

    public static Response createTimeoutResponse(long id) {
        return new Response(id).setEvent(false).setStatus(Response.STATUS_TIMEOUT)
                .setException(new RuntimeException("超时")); // TODO 芋艿，悲剧
    }

}