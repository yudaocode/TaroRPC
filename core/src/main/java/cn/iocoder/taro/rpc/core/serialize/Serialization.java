package cn.iocoder.taro.rpc.core.serialize;

public interface Serialization {

    /**
     * Serialization 的编号。每种序列化方式的编号必须唯一，用于传输时，表示数据的序列化方式。
     *
     * @return 序列化编号
     */
    int getSerializationId();

    byte[] serialize(Object obj);

    <T> T deserialize(byte[] data, Class<T> clazz);

}