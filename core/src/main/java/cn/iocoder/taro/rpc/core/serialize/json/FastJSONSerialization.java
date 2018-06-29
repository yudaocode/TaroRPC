package cn.iocoder.taro.rpc.core.serialize.json;

import cn.iocoder.taro.rpc.core.serialize.Serialization;
import com.alibaba.fastjson.JSON;

public class FastJSONSerialization implements Serialization {

    @Override
    public int getSerializationId() {
        return 0;
    }

    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }

    public static void main(String[] args) {
        String s = "123";
        FastJSONSerialization serialization = new FastJSONSerialization();
        byte[] data = serialization.serialize(s);
        Object result = serialization.deserialize(data, Object.class);
        System.out.println(result);
    }

}