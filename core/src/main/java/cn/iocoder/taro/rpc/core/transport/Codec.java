package cn.iocoder.taro.rpc.core.transport;

/**
 * 编解码接口
 *
 * TODO 重构：每个不同的 nio 框架，所以都有自己的 Buffer ，而 core 模块是上层项目，所以不能依赖 nio 框架。
 *           那么只能在 core 模块中，实现自己的 Buffer 接口，而每个子项目，实现 Buffer 接口。从而装饰好，解决依赖问题。
 *           因此，暂时考虑，做的简单一些，让子类自己实现 ExchangeCodec 类( 在 Encoder 和 Decoder 中 )。相当于说，core 项目中，只处理 body 部分。
 */
public interface Codec {

    byte[] encode(Channel channel, Object data);

    Object decode(Channel channel, byte[] bytes);

}