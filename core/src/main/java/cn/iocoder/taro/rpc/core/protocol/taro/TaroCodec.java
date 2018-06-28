package cn.iocoder.taro.rpc.core.protocol.taro;

import cn.iocoder.taro.rpc.core.transport.Channel;
import cn.iocoder.taro.rpc.core.transport.Codec;

public class TaroCodec implements Codec {

    @Override
    public byte[] encodeBody(Channel channel, Object data) {
        // Request
        // Response
        return new byte[0];
    }

    @Override
    public Object decodeBody(Channel channel, byte[] bytes) {
        return null;
    }

}