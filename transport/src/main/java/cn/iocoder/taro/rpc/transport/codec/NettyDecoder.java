package cn.iocoder.taro.rpc.transport.codec;

import cn.iocoder.taro.rpc.core.transport.Request;
import cn.iocoder.taro.rpc.core.transport.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class NettyDecoder extends ByteToMessageDecoder {

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("解析消息：");
        // short 2
        // long requestId
        // int data length
        // string data
        if (in.readableBytes() <= 2 + 1 + 8) {
            return;
        }
        in.markReaderIndex();
        short magicNumber = in.readShort();
        if (magicNumber != (short) 0xdabb) {
            in.resetReaderIndex();
            throw new RuntimeException("wlgc 不对");
        }
        byte requestFlag = in.readByte();
        long requestId = in.readLong();
        if (in.readableBytes() <= 4) { // 不够
            in.resetReaderIndex();
            return;
        }
        int length = in.readInt();
        if (in.readableBytes() < length) { // 不够
            in.resetReaderIndex();
            return;
        }
        StringBuilder jsonStr = new StringBuilder();
        for (int i = 0; i < length; i++) {
            jsonStr.append(in.readChar());
        }
        if (requestFlag == 0) {
            Request request = new Request(requestId);
            request.setData(jsonStr);
            out.add(request);
            System.out.println("接收请求：" + jsonStr);
        } else if (requestFlag == 1) {
            Response response = new Response();
            response.setId(requestId);
            response.setValue(jsonStr);
            out.add(response);
            System.out.println("接收响应：" + jsonStr);
        }

    }

}
