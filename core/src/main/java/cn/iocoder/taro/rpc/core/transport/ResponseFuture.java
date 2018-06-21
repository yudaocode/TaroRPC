package cn.iocoder.taro.rpc.core.transport;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

public class ResponseFuture {

    private static final ConcurrentMap<Long, ResponseFuture> futures = new ConcurrentHashMap<Long, ResponseFuture>();

//    private final Object lock = new Object();
    private final CountDownLatch latch = new CountDownLatch(1); // TODO 芋艿，性能，后续对比三种方式的性能差别
    private volatile Response response;
    private volatile ResponseCallback callback;
    // TODO 芋艿：超时

    public ResponseFuture(Long id) {
        futures.put(id, this);
    }

    public Response getValue() throws InterruptedException {
        if (isDone()) {
            return response;
        }
        latch.await();
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
        // 唤醒
        latch.countDown();
    }

    public boolean isDone() {
        return response != null;
    }

    public static ResponseFuture getFuture(long requestId) {
        return futures.get(requestId);
    }

    public ResponseCallback getCallback() {
        return callback;
    }

    public ResponseFuture setCallback(ResponseCallback callback) {
        this.callback = callback;
        return this;
    }

}