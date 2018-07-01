package cn.iocoder.taro.rpc.core.protocol;

import cn.iocoder.taro.rpc.core.rpc.Invoker;

public interface Protocol {

    Exporter export(Invoker invoker);

    <T> Invoker<T> refer(Class<T> clazz);

}