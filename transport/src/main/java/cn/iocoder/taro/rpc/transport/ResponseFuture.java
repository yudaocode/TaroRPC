package cn.iocoder.taro.rpc.transport;

import cn.iocoder.taro.rpc.core.transport.Response;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

public class ResponseFuture {

    private static final ConcurrentMap<Long, ResponseFuture> futures = new ConcurrentHashMap<Long, ResponseFuture>();

//    private final Object lock = new Object();
    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile Response response;

    // TODO 芋艿：callback
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

}