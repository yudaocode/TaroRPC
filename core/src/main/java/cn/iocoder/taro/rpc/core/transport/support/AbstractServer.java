package cn.iocoder.taro.rpc.core.transport.support;

import cn.iocoder.taro.rpc.core.transport.MessageHandler;
import cn.iocoder.taro.rpc.core.transport.Server;
import cn.iocoder.taro.rpc.core.transport.exchange.ExchangeHandler;
import cn.iocoder.taro.rpc.core.transport.exchange.ExchangeMessageHandler;
import cn.iocoder.taro.rpc.core.transport.heartbeat.HeartbeatMessageHandler;

import java.net.InetSocketAddress;

public abstract class AbstractServer implements Server {

    protected final int port;

    protected MessageHandler messageHandler;

    protected AbstractServer(int port, ExchangeHandler exchangeHandler) {
        this.port = port;
        this.messageHandler = new HeartbeatMessageHandler(new ExchangeMessageHandler(null, exchangeHandler)); // TODO 芋艿，逻辑线程池的设置方式。

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
