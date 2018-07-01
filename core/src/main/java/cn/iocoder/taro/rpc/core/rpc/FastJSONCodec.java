package cn.iocoder.taro.rpc.core.rpc;

import cn.iocoder.taro.rpc.core.transport.Channel;
import cn.iocoder.taro.rpc.core.transport.Codec;
import com.alibaba.fastjson.JSON;

public class FastJSONCodec implements Codec {

    @Override
    public byte[] encodeBody(Channel channel, Object message, Object body) {
        return JSON.toJSONBytes(body);
    }

    @Override
    public Object decodeBody(Channel channel, Object message, byte[] bodyBytes) {
        return JSON.parse(bodyBytes);
    }

}
