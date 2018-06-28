package cn.iocoder.taro.transport.netty4.codec;

import cn.iocoder.taro.rpc.core.transport.Channel;
import cn.iocoder.taro.rpc.core.transport.Codec;
import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.Response;
import cn.iocoder.taro.transport.netty4.NettyChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyEncoder extends MessageToByteEncoder<Object> {

    private final Codec codec;

    public NettyEncoder(Codec codec) {
        this.codec = codec;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        System.out.println("准备发送消息：" + msg);
        Channel channel = ctx.channel().attr(NettyChannel.ATTR_CHANNEL).get();

        if (msg instanceof Request) {
            Request request = (Request) msg; // TODO 芋艿，优化
            out.writeShort((short) 0xdabb); // magic number
            out.writeByte(0); // 请求标识
            out.writeByte(request.isOneway() ? 1 : 0); // oneway
            out.writeByte(request.isEvent() ? 1 : 0); // event
            out.writeLong(request.getId()); // id
            byte[] dataBytes = codec.encodeBody(channel, request.getData());
            out.writeInt(dataBytes.length);
            out.writeBytes(dataBytes);
//            String dataString = JSON.toJSONString(request.getData());
//            out.writeInt(dataString.length()); // data length
//            for (char ch : dataString.toCharArray()) { // data content
//                out.writeChar(ch);
//            }
        } else if (msg instanceof Response ) {
            Response response = (Response) msg; // TODO 芋艿，优化
            out.writeShort((short) 0xdabb); // magic number
            out.writeByte(1); // 响应标识
            out.writeByte(1); // oneway
            out.writeByte(response.isEvent() ? 1 : 0); // event
            out.writeLong(response.getId()); // id
            out.writeByte(response.getStatus()); // status
            byte[] dataBytes;
            if (response.getStatus() == Response.STATUS_SUCCESS) {
                dataBytes = codec.encodeBody(channel, response.getData());
            } else {
                dataBytes = codec.encodeBody(channel, response.getErrorMsg());
            }
            out.writeInt(dataBytes.length);
            out.writeBytes(dataBytes);
//            String dataString;
//            if (response.getStatus() == Response.STATUS_SUCCESS) {
//                dataString = JSON.toJSONString(response.getData());
//            } else {
//                dataString = JSON.toJSONString(response.getErrorMsg());
//            }
//            out.writeInt(dataString.length()); // data length
//            for (char ch : dataString.toCharArray()) { // data content
//                out.writeChar(ch);
//            }
        }

//        out.writeBytes(buffer.array());
//        ctx.writeAndFlush();
    }

}
