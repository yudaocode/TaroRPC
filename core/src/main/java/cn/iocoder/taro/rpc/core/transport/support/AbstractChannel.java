package cn.iocoder.taro.rpc.core.transport.support;

import cn.iocoder.taro.rpc.core.transport.*;
import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.Response;
import cn.iocoder.taro.rpc.core.transport.exchange.ResponseCallback;
import cn.iocoder.taro.rpc.core.transport.exchange.ResponseFuture;

public abstract class AbstractChannel implements Channel {

    @Override
    public void oneway(Object request) throws TransportException {
        Request req = buildRequest(request, true);
        send(req);
    }

    @Override
    public Response requestSync(Object request) throws InterruptedException, TransportException {
        ResponseFuture future = this.requestAsync(request);
        return future.getValue();
    }

    @Override
    public ResponseFuture requestAsync(Object request) throws TransportException {
        Request req = buildRequest(request, false);
        send(req);
        return new ResponseFuture(req.getId());
    }

    @Override
    public void requestWithCallback(Object request, ResponseCallback callback) throws TransportException {
        ResponseFuture future = this.requestAsync(request);
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
