package cn.iocoder.taro.transport.heartbeat;

import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.ResponseCallback;
import cn.iocoder.taro.transport.NettyChannel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateHandler;

public class ClientHeartbeatHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 非空闲事件，父类继续处理
        if (!(evt instanceof IdleStateHandler)) {
            super.userEventTriggered(ctx, evt);
            return;
        }
        // 发送心跳
        NettyChannel channel = ctx.channel().attr(NettyChannel.ATTR_CHANNEL).get();
        Request heartbeat = new Request().setOneway(false).setEvnet(true).setData(Request.DATA_EVENT_HEARTBEAT);
        channel.requestWithCallback(heartbeat, new ResponseCallback() {
            @Override
            public void onSuccess(Object value) {
                System.out.println("心跳成功：");
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("心跳失败：");
            }
        });
    }

}