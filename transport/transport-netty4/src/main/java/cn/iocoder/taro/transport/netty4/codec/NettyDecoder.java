package cn.iocoder.taro.transport.netty4.codec;

import cn.iocoder.taro.rpc.core.transport.Channel;
import cn.iocoder.taro.rpc.core.transport.Codec;
import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.Response;
import cn.iocoder.taro.transport.netty4.NettyChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class NettyDecoder extends ByteToMessageDecoder {

    private final Codec codec;

    public NettyDecoder(Codec codec) {
        this.codec = codec;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("解析消息：");
        Channel channel = ctx.channel().attr(NettyChannel.ATTR_CHANNEL).get();

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
            byte[] dataBytes = new byte[length];
            in.readBytes(dataBytes);
//            StringBuilder jsonStr = new StringBuilder();
//            for (int i = 0; i < length; i++) {
//                jsonStr.append(in.readChar());
//            }
            Request request = new Request(requestId);
            request.setOneway(oneway == 1);
            request.setEvent(event == 1);
//            request.setData(JSON.parse(jsonStr.toString()));
            request.setData(codec.decodeBody(channel, dataBytes));
            out.add(request);
            System.out.println("接收请求：" + request.getData());
        } else if (requestFlag == 1) {
            if (in.readableBytes() < 1 + 4) {
                in.resetReaderIndex();
                return;
            }
            byte status = in.readByte();
            Response response = new Response(requestId);
            response.setEvent(event == 1);
            response.setStatus(status);
            int length = in.readInt();
            if (in.readableBytes() < length) { // 不够
                in.resetReaderIndex();
                return;
            }
            byte[] dataBytes = new byte[length];
            in.readBytes(dataBytes);
//            StringBuilder jsonStr = new StringBuilder();
//            for (int i = 0; i < length; i++) {
//                jsonStr.append(in.readChar());
//            }
            if (status == Response.STATUS_SUCCESS) {
//                response.setData(JSON.parse(jsonStr.toString()));
                response.setData(codec.decodeBody(channel, dataBytes));
                System.out.println("接收正常响应：" + response.getData());
            } else {
//                response.setErrorMsg(JSON.parseObject(jsonStr.toString(), String.class));
                response.setErrorMsg((String) codec.decodeBody(channel, dataBytes));
                System.out.println("接收异常响应：" + response.getData());
            }
            out.add(response);
        }
    }

}
