package cn.iocoder.taro.rpc.core.transport.support;

import cn.iocoder.taro.rpc.core.transport.*;

public abstract class AbstractChannel implements Channel {

    @Override
    public void oneway(Object request) throws TransportException {
        Request req = new Request();
        req.setOneway(true);
        req.setData(req);
        send(req);
    }

    @Override
    public Response requestSync(Object request) throws InterruptedException, TransportException {
        ResponseFuture future = this.requestAsync(request);
        return future.getValue();
    }

    @Override
    public ResponseFuture requestAsync(Object request) throws TransportException {
        Request req = new Request();
        req.setOneway(false);
        req.setData(req);
        send(req);

        return new ResponseFuture(req.getId());
    }

    @Override
    public void requestWithCallback(Object request, ResponseCallback callback) throws TransportException {
        ResponseFuture future = this.requestAsync(request);
        future.setCallback(callback);
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
