package cn.iocoder.taro.rpc.transport.codec;

import cn.iocoder.taro.rpc.core.transport.Request;
import cn.iocoder.taro.rpc.core.transport.Response;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        System.out.println("准备发送消息：" + msg);

        if (msg instanceof Request) {
            Request request = (Request) msg; // TODO 芋艿，优化
            out.writeShort((short) 0xdabb);
            out.writeByte(0);
            out.writeByte(request.isOneway() ? 1 : 0); // oneway
            out.writeLong(request.getId());
            String dataString = JSON.toJSONString(request.getData());
            out.writeInt(dataString.length());
            for (char ch : dataString.toCharArray()) {
                out.writeChar(ch);
            }
        } else if (msg instanceof Response ) {
            Response response = (Response) msg; // TODO 芋艿，优化
            out.writeShort((short) 0xdabb);
            out.writeByte(1);
            out.writeByte(0); // oneway
            out.writeLong(response.getId());
            String dataString = JSON.toJSONString(response.getValue());
            out.writeInt(dataString.length());
            for (char ch : dataString.toCharArray()) {
                out.writeChar(ch);
            }
        }

//        out.writeBytes(buffer.array());
//        ctx.writeAndFlush();
    }

}
