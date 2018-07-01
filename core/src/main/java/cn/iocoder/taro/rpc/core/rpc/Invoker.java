package cn.iocoder.taro.rpc.core.rpc;

public interface Invoker<T> {




    Result invoke(Invocation invocation);

}