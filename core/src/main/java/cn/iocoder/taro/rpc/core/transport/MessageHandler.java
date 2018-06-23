package cn.iocoder.taro.rpc.core.transport;

public interface MessageHandler {

    // 参考 Motan 的 MessageHandler 的设计。差别在于 Motan 返回 Object 对象，会回写给 Channel ，而笔者觉得如果是 MessageHandler ，应该参考 Message 模型，还是不回写，而是在实现方法里回写。
    // 当然，如果方法名改成 reply ，倒是可以考虑做成 Request / Response 模型
    void handle(Channel channel, Object message);

}