package cn.iocoder.taro.transport.netty4.heartbeat;

import cn.iocoder.taro.transport.netty4.NettyChannel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerHeartbeatHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        // 非空闲事件，父类继续处理
        if (!(event instanceof IdleStateEvent)) {
            super.userEventTriggered(ctx, event);
            return;
        }
        // 强制关闭空闲的客户端连接
        NettyChannel channel = ctx.channel().attr(NettyChannel.ATTR_CHANNEL).get();
        channel.close();
    }

}