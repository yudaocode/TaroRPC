package cn.iocoder.taro.rpc.core.transport.support;

import cn.iocoder.taro.rpc.core.transport.*;

public abstract class AbstractClient implements Client {

    protected final String host;
    protected final int port;
    private volatile boolean closed = false;

    public AbstractClient(String host, int port) {
        this.host = host;
        this.port = port;

        open();
    }

    @Override
    public void oneway(Object request) throws TransportException {
        getChannel().oneway(request);
    }

    @Override
    public Response requestSync(Object request) throws InterruptedException, TransportException {
        return getChannel().requestSync(request);
    }

    @Override
    public ResponseFuture requestAsync(Object request) throws TransportException {
        return getChannel().requestAsync(request);
    }

    @Override
    public void requestWithCallback(Object request, ResponseCallback callback) throws TransportException {
        getChannel().requestWithCallback(request, callback);
    }

    @Override
    public void send(Object message) throws TransportException {
        getChannel().send(message);
    }

    @Override
    public void close() {
        this.closed = true;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

//    @Override
//    public InetSocketAddress getLocalAddress() {
//        return null;
//    }
//
//    @Override
//    public InetSocketAddress getRemoteAddress() {
//        return null;
//    }
//

//
//    @Override
//    public void open() {
//    }
//

//
//    @Override
//    public void close(int timeout) {
//
//    }
//

//
//    @Override
//    public boolean isAvailable() {
//        return false;
//    }

}
