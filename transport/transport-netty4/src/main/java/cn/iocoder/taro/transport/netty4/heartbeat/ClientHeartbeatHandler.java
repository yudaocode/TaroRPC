package cn.iocoder.taro.transport.netty4.heartbeat;

import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.ResponseCallback;
import cn.iocoder.taro.rpc.core.transport.heartbeat.HeartbeatMessageHandler;
import cn.iocoder.taro.transport.netty4.NettyChannel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

public class ClientHeartbeatHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        // 非空闲事件，父类继续处理
        if (!(event instanceof IdleStateEvent)) {
            super.userEventTriggered(ctx, event);
            return;
        }
        // 发送心跳
        NettyChannel channel = ctx.channel().attr(NettyChannel.ATTR_CHANNEL).get();
        Request heartbeat = HeartbeatMessageHandler.createHeartbeatRequest();
        channel.requestWithCallback(heartbeat, new ResponseCallback() {

            @Override
            public void onSuccess(Object value) {
                System.out.println("心跳成功：");
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("心跳失败：");
            }

        }, 1000); // TODO 芋艿，超时
    }

}