package cn.iocoder.taro.transport.netty4;

import cn.iocoder.taro.rpc.core.transport.MessageHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

public class NettyChannelHandler extends ChannelDuplexHandler {

    private final MessageHandler messageHandler;

    public NettyChannelHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // TODO 芋艿，逻辑线程池
        NettyChannel channel = ctx.channel().attr(NettyChannel.ATTR_CHANNEL).get();
        messageHandler.handle(channel, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace(); // TODO 芋艿
        ctx.channel().close();
    }

}