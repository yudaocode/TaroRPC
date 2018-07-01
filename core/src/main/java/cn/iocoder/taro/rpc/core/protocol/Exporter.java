package cn.iocoder.taro.rpc.core.protocol;

import cn.iocoder.taro.rpc.core.rpc.Invoker;

public interface Exporter<T> {

    /**
     * 获得 Invoker 对象。
     *
     * 例如，{@link cn.iocoder.taro.rpc.core.protocol.taro.TaroInvoker}
     *
     * @return Invoker 对象
     */
    Invoker<T> getInvoker();

    /**
     * 取消暴露
     */
    void unexport();

}
