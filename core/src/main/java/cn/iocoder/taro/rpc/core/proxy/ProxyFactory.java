package cn.iocoder.taro.rpc.core.proxy;

import cn.iocoder.taro.rpc.core.rpc.Invoker;

/**
 * 代理工厂接口
 *
 * 参考 Dubbo 的实现，相比 Motan 来说，我认为 Dubbo 更清晰干净
 */
public interface ProxyFactory {

    /**
     * 将 Invoker 代理成对应的 Proxy 对象
     *
     * 目前用于，服务引用时
     *
     * @param invoker Invoker 对象，例如 {@link cn.iocoder.taro.rpc.core.protocol.taro.TaroInvoker}
     * @param <T> 泛型，一般为 Service 接口
     * @return Proxy 对象
     */
    <T> T getProxy(Invoker<T> invoker);

    /**
     * 将代理对象，包装成对应的 Invoker 对象
     *
     * 目前用于，服务暴露时
     *
     * @param proxy 代理对象
     * @param type Service 接口
     * @param <T> 泛型，一般为 Service 接口
     * @return Invoker 对象
     */
    <T> Invoker<T> getInvoker(T proxy, Class<T> type);

}