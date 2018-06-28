package cn.iocoder.taro.rpc.core.serialize;

public interface Serialization {

    byte[] serialize(Object obj);

    <T> T deserialize(byte[] data, Class<T> clazz);

}