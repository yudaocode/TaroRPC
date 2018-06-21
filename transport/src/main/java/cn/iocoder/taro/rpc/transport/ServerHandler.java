package cn.iocoder.taro.rpc.transport;

import cn.iocoder.taro.rpc.core.transport.Request;
import cn.iocoder.taro.rpc.core.transport.Response;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class ServerHandler extends ChannelDuplexHandler {



//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("Server start read");
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req, "UTF-8");
//
//        System.out.println("The time server receive order : " + body);
//        String currentTime = "Query Time Order".equalsIgnoreCase(body) ? new java.util.Date(
//                System.currentTimeMillis()).toString() : "Bad Order";
//        //异步发送应答消息给客户端: 这里并没有把消息直接写入SocketChannel,而是放入发送缓冲数组中
//        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
//        ctx.write(resp);
//    }
//
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.flush();
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
//            throws Exception {
//        ctx.close();
//    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("disconnect");
        super.disconnect(ctx, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接收到消息：" + msg);
        Request request = (Request) msg;
        if (request.isOneway()) {
            System.out.println("oneway 消息，不进行响应");
            return;
        }

        Response response = new Response();
        response.setId(request.getId());
        if (request.getData().equals("\"hello\"")) {
            response.setValue("world");
        } else {
            response.setValue("unknown");
        }

        ctx.channel().writeAndFlush(response);
    }

//    private String getRemoteIp(ChannelHandlerContext ctx) {
//        String ip = "";
//        SocketAddress remote = ctx.channel().remoteAddress();
//        if (remote != null) {
//            try {
//                ip = ((InetSocketAddress) remote).getAddress().getHostAddress();
//            } catch (Exception ignored) {
//            }
//        }
//        return ip;
//    }

}