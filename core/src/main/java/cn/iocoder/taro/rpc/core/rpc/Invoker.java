package cn.iocoder.taro.rpc.core.rpc;

public interface Invoker<T> {
    
    // TODO 芋艿，URL

    Result invoke(Invocation invocation);

}