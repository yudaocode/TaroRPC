package cn.iocoder.taro.rpc.core.rpc;

public interface Result {

    Object getValue();

    Throwable getException();

    // TODO 芋艿，还有更多的方法

}