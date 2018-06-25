package cn.iocoder.taro.rpc.core.transport.support;

import cn.iocoder.taro.rpc.core.transport.*;
import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.Response;
import cn.iocoder.taro.rpc.core.transport.exchange.ResponseCallback;
import cn.iocoder.taro.rpc.core.transport.exchange.ResponseFuture;

public abstract class AbstractChannel implements Channel {

    @Override
    public void oneway(Object request, long timeoutMillis) throws TransportException {
        Request req = buildRequest(request, true);
        send(req, timeoutMillis);
    }

    @Override
    public Response requestSync(Object request, long timeoutMillis) throws InterruptedException, TransportException { // 参考自 sofa-bolt ，不同于 dubbo 和 motan 的方式。避免无效的扫描。
//        ResponseFuture future = this.requestAsync(request);
//        return future.waitResponse();
        Request req = buildRequest(request, false);
        ResponseFuture future = new ResponseFuture(this, req);
        send(req, timeoutMillis);
        // 等待结果
        Response response = future.waitResponse(timeoutMillis);
        // 正常结果
        if (response != null) {
            return response;
        }
        // 超时结果
        response = ResponseFuture.createTimeoutResponse(req.getId());
        return response;
    }

    @Override
    public ResponseFuture requestAsync(Object request, long timeoutMillis) throws TransportException {
        Request req = buildRequest(request, false);
        ResponseFuture future = new ResponseFuture(this, req);
        ResponseFuture.addTimeoutTask(future, timeoutMillis); // 设置超时任务
        send(req, timeoutMillis);
        return future;
    }

    @Override
    public void requestWithCallback(Object request, ResponseCallback callback, long timeoutMillis) throws TransportException {
        ResponseFuture future = this.requestAsync(request, timeoutMillis);
        future.setCallback(callback);
    }

    private Request buildRequest(Object data, boolean oneway) {
        Request request;
         if (data instanceof Request) {
            request = new Request(((Request) data).getId());
            request.setData(((Request) data).getData());
            request.setEvent(((Request) data).isEvent());
        } else {
            request = new Request();
            request.setData(data);
            request.setEvent(false);
        }
        request.setOneway(oneway);
        return request;
    }

//    @Override // 子类实现
//    public void send(Object message) throws TransportException {
//
//    }

//    @Override
//    public InetSocketAddress getRemoteAddress() {
//        return null;
//    }

//    @Override
//    public InetSocketAddress getLocalAddress() {
//        return null;
//    }

//    @Override
//    public void open() {
//
//    }
//
//    @Override
//    public void close() {
//
//    }
//
//    @Override
//    public void close(int timeout) {
//
//    }
//
//    @Override
//    public boolean isClosed() {
//        return false;
//    }
//
//    @Override
//    public boolean isAvailable() {
//        return false;
//    }

}
