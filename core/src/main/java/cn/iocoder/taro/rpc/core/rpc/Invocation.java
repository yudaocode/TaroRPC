package cn.iocoder.taro.rpc.core.rpc;

public interface Invocation {

    Class<?> getInterfaceClass();

    String getMethodName();

    Object[] getArguments();

    Class<?>[] getArgumentTypes();

}