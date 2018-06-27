package cn.iocoder.taro.rpc.core.transport.exchange;

import cn.iocoder.taro.rpc.core.transport.Channel;
import cn.iocoder.taro.rpc.core.transport.exception.TransportException;
import cn.iocoder.taro.rpc.core.transport.exception.TransportTimeoutException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class InvokeFuture {

    private static final ConcurrentMap<Long, InvokeFuture> FUTURES = new ConcurrentHashMap<Long, InvokeFuture>();
    private static final TimeoutTaskScanner SCANNER = new TimeoutTaskScanner();

    private final CountDownLatch latch = new CountDownLatch(1);
    private final Channel channel;
    private final Request request;
    private volatile Response response;
    private volatile ResponseCallback callback;
    /**
     * 创建开始时间，用于超时逻辑
     */
    private final long startTimeMillis;
    /**
     * 超时时间，用于超时逻辑
     */
    private final long timeoutMillis;

    static {
        Thread th = new Thread(SCANNER);
        th.setDaemon(true);
        th.start();
    }

    public InvokeFuture(Channel channel, Request request, long timeoutMillis) {
        this.channel = channel;
        this.request = request;
        this.startTimeMillis = System.currentTimeMillis();
        this.timeoutMillis = this.startTimeMillis + timeoutMillis;
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
            doCallback(response, callback);
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
        doCallback(response, callback);
        return this;
    }

    private void doCallback(Response response, ResponseCallback callback) {
        if (response != null && callback != null) {
            try {
                if (response.getData() != null) {
                    callback.onSuccess(response.getData());
                } else if (response.getErrorMsg() != null) {
                    TransportException exception;
                    switch (response.getStatus()) {
                        case Response.STATUS_TIMEOUT:
                            exception = new TransportTimeoutException(channel.getLocalAddress(), channel.getRemoteAddress(), response.getErrorMsg());
                            break;
                        default:
                            exception = new TransportException(channel.getLocalAddress(), channel.getRemoteAddress(), response.getErrorMsg());
                    }
                    callback.onFailure(exception);
                }
            } catch (Throwable throwable) {
                // TODO 芋艿，打印日志
            }
        }
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
                        Response response = createTimeoutResponse(future);
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

        void addTask(InvokeFuture future) {
            this.tasks.put(future.request.getId(), future);
        }

        void removeTask(InvokeFuture future) {
            tasks.remove(future.request.getId());
        }

    }

    public static void addTimeoutTask(InvokeFuture future) {
        SCANNER.addTask(future);
    }

    public static InvokeFuture getFuture(long requestId) {
        return FUTURES.get(requestId);
    }

    public static Response createTimeoutResponse(InvokeFuture future) {
        return new Response(future.request.getId()).setEvent(false).setStatus(Response.STATUS_TIMEOUT)
                .setErrorMsg(String.format("等待响应时间超时：client=%s, server=%s, startTimeMillis=%d, timeoutMillis=%d, endTimeMillis=%d",
                        future.channel.getLocalAddress(), future.channel.getRemoteAddress(), future.startTimeMillis, future.timeoutMillis, System.currentTimeMillis()));
    }

    public static Response createSendErrorResponse(InvokeFuture future) {
        return new Response(future.request.getId()).setEvent(false).setStatus(Response.STATUS_SEND_ERROR)
                .setErrorMsg(String.format("发送请求失败：client=%s, server=%s, startTimeMillis=%d, timeoutMillis=%d, endTimeMillis=%d",
                        future.channel.getLocalAddress(), future.channel.getRemoteAddress(), future.startTimeMillis, future.timeoutMillis, System.currentTimeMillis()));
    }

}