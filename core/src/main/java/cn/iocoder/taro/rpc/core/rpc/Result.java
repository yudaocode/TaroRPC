package cn.iocoder.taro.rpc.core.rpc;

public interface Result {

    Object getValue();

    Throwable getException();

    Object recreate() throws Throwable;

    // TODO 芋艿，还有更多的方法

}