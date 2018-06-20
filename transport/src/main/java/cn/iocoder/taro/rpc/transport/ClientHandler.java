package cn.iocoder.taro.rpc.transport;

import cn.iocoder.taro.rpc.core.transport.Response;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandler extends ChannelDuplexHandler {

//    @Override
//    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
//        super.connect(ctx, remoteAddress, localAddress, promise);
//    }
//
//    @Override
//    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
//        super.disconnect(ctx, promise);
//    }
//
//    @Override
//    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
//        super.close(ctx, promise);
//    }
//
//    @Override
//    public void read(ChannelHandlerContext ctx) throws Exception {
////        super.read(ctx);
//    }

//    @Override
//    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//       super.write(ctx, msg, promise);
////        System.out.println("发送消息：" + msg);
//
//    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接收到消息：" + msg);
        Response response = (Response) msg;
        ResponseFuture future = ResponseFuture.getFuture(response.getId());
        if (future == null) {
            return; // TODO 芋艿，后续完善
        }
        future.setResponse(response);
    }

}