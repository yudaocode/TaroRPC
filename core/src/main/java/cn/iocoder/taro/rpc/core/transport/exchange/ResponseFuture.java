package cn.iocoder.taro.rpc.core.transport.exchange;

import cn.iocoder.taro.rpc.core.transport.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ResponseFuture {

    private static final ConcurrentMap<Long, ResponseFuture> FUTURES = new ConcurrentHashMap<Long, ResponseFuture>();

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
        Thread th = new Thread(new TimeoutFutureScanner());
        th.setDaemon(true);
        th.start();
    }

    public ResponseFuture(Channel channel, Request request) {
        this.channel = channel;
        this.request = request;
    }

//    public ResponseFuture(Long id) {
//        FUTURES.put(id, this);
//    }

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

    public void setResponse(Response response) {
        try {
            this.response = response;
            // 回调
            onCallback(response, callback);
        } finally {
            // 唤醒
            latch.countDown();
            // 移除
            FUTURES.remove(response.getId());
        }
    }

    public boolean isDone() {
        return response != null;
    }

    public ResponseCallback getCallback() {
        return callback;
    }

    public ResponseFuture setCallback(ResponseCallback callback) {
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

    public static void addTimeoutTask(ResponseFuture future, long timeoutMillis) {
        future.startTimeMillis = System.currentTimeMillis();
        future.timeoutMillis = future.startTimeMillis + timeoutMillis;
        FUTURES.put(future.request.getId(), future);
    }

    private static class TimeoutFutureScanner implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                for (Map.Entry<Long, ResponseFuture> entry : FUTURES.entrySet()) {
                    ResponseFuture future = entry.getValue();
                    if (System.currentTimeMillis() > future.timeoutMillis) {
                        continue;
                    }
                    // 超时
                    Response response = createTimeoutResponse(entry.getKey());
                    future.setResponse(response);
                    // 等待下一个轮回
                    Thread.sleep(30);
                }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static ResponseFuture getFuture(long requestId) {
        return FUTURES.get(requestId);
    }

    public static Response createTimeoutResponse(long id) {
        return new Response(id).setEvent(false).setStatus(Response.STATUS_TIMEOUT)
                .setException(new RuntimeException("超时")); // TODO 芋艿，悲剧
    }

}