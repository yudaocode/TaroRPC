package cn.iocoder.taro.rpc.transport;

import cn.iocoder.taro.rpc.core.transport.TransportException;
import cn.iocoder.taro.rpc.core.transport.support.AbstractChannel;

import java.net.InetSocketAddress;

public class NettyChannel extends AbstractChannel {

    private final io.netty.channel.Channel channel;

    public NettyChannel(io.netty.channel.Channel channel) {
        this.channel = channel;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) channel.localAddress();
    }

    @Override
    public void send(Object message) throws TransportException {
        channel.writeAndFlush(message);
    }

    @Override
    public void open() {
        throw new UnsupportedOperationException("等待实现");
    }

    @Override
    public void close() {
        channel.close();
    }

    @Override
    public void close(int timeout) {
        // TODO 芋艿，等待实现
    }

    @Override
    public boolean isClosed() {
        // TODO 芋艿，等待实现
        return false;
    }

    @Override
    public boolean isAvailable() {
        // TODO 芋艿，等待实现
        return false;
    }

}
