package cn.iocoder.taro.transport.netty4.codec;

import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.Response;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class NettyDecoder extends ByteToMessageDecoder {

    // TODO 芋艿，优化，decode 只解析部分，更细的解析，在多线程里。即 motan NettyMessage 的做法

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("解析消息：");
        // short 2
        // long requestId
        // int data length
        // string data
        if (in.readableBytes() <= 2 + 1 + 1 + 8 + 1) {
            return;
        }
        in.markReaderIndex();
        short magicNumber = in.readShort(); // magic number
        if (magicNumber != (short) 0xdabb) {
            in.resetReaderIndex();
            throw new RuntimeException("wlgc 不对");
        }
        byte requestFlag = in.readByte(); // 标识
        byte oneway = in.readByte(); // oneway
        byte event = in.readByte(); // event
        long requestId = in.readLong(); // id
        if (in.readableBytes() <= 4) { // 不够
            in.resetReaderIndex();
            return;
        }

        if (requestFlag == 0) {
            int length = in.readInt();
            if (in.readableBytes() < length) { // 不够
                in.resetReaderIndex();
                return;
            }
            StringBuilder jsonStr = new StringBuilder();
            for (int i = 0; i < length; i++) {
                jsonStr.append(in.readChar());
            }
            Request request = new Request(requestId);
            request.setOneway(oneway == 1);
            request.setEvent(event == 1);
            request.setData(JSON.parse(jsonStr.toString()));
            out.add(request);
            System.out.println("接收请求：" + jsonStr);
        } else if (requestFlag == 1) {
            if (in.readableBytes() < 1 + 4) {
                in.resetReaderIndex();
                return;
            }
            byte status = in.readByte();
            assert status == Response.STATUS_SUCCESS; // TODO 芋艿，后面修改下
            int length = in.readInt();
            if (in.readableBytes() < length) { // 不够
                in.resetReaderIndex();
                return;
            }
            StringBuilder jsonStr = new StringBuilder();
            for (int i = 0; i < length; i++) {
                jsonStr.append(in.readChar());
            }

            Response response = new Response(requestId);
            response.setEvent(false);
            response.setStatus(Response.STATUS_SUCCESS);
            response.setValue(JSON.parse(jsonStr.toString()));
            out.add(response);
            System.out.println("接收响应：" + jsonStr);
        }
    }

}
