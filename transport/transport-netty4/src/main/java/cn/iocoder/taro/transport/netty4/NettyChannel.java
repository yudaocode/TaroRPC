package cn.iocoder.taro.transport.netty4;

import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.InvokeFuture;
import cn.iocoder.taro.rpc.core.transport.support.AbstractChannel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

public class NettyChannel extends AbstractChannel {

    /**
     * {@link io.netty.channel.Channel} 存储 {@link NettyChannel} 的 KEY 。
     */
    public static final AttributeKey<NettyChannel> ATTR_CHANNEL = AttributeKey.valueOf("channel");

    private final io.netty.channel.Channel channel;

    public NettyChannel(io.netty.channel.Channel channel) {
        channel.attr(ATTR_CHANNEL).set(this);
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
    public void send(final Object message) {
        ChannelFuture writeFuture = channel.writeAndFlush(message);
        writeFuture.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(final ChannelFuture writeFuture) {
                if (writeFuture.isSuccess()) { // 成功不处理
                    return;
                }
                if (!(message instanceof Request)) { // 只处理请求
                    return;
                }
                Request request = (Request) message;
                if (request.isOneway()) { // 只有需要响应的请求，才有 ResponseFuture
                    return;
                }
                InvokeFuture future = InvokeFuture.getFuture(request.getId());
                if (future != null) {
                    future.notifyResponse(InvokeFuture.createSendErrorResponse(future));
                }
            }

        });
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
