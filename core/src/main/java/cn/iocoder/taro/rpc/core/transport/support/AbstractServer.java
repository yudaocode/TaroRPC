package cn.iocoder.taro.rpc.core.transport.support;

import cn.iocoder.taro.rpc.core.transport.Server;
import cn.iocoder.taro.rpc.core.transport.TransportException;

import java.net.InetSocketAddress;

public abstract class AbstractServer implements Server {

    protected final int port;

    protected AbstractServer(int port) {
        this.port = port;

        this.open();
    }

    @Override
    public boolean isBond() {
        return false;
    }

//    @Override
//    public Collection<Channel> getChannels() {
//        return null;
//    }
//
//    @Override
//    public Channel getChannel(InetSocketAddress address) {
//        return null;
//    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public void send(Object message) throws TransportException {
    }

    @Override
    public void close(int timeout) {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

}
