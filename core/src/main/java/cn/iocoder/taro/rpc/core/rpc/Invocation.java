package cn.iocoder.taro.rpc.core.rpc;

public interface Invocation {

    String getMethodName();

    Object[] getArguments();

    Class<?>[] getArgumentTypes();

}