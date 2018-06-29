package cn.iocoder.taro.rpc.core.rpc.support;

import cn.iocoder.taro.rpc.core.rpc.Invocation;

public class RpcInvocation implements Invocation {

    private Class<?> interfaceClass;
    private String methodName;
    private Object[] arguments;
    private Class<?>[] argumentTypes;

    @Override
    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Class<?>[] getArgumentTypes() {
        return argumentTypes;
    }

    public RpcInvocation setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
        return this;
    }

    public RpcInvocation setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public RpcInvocation setArguments(Object[] arguments) {
        this.arguments = arguments;
        return this;
    }

    public RpcInvocation setArgumentTypes(Class<?>[] argumentTypes) {
        this.argumentTypes = argumentTypes;
        return this;
    }

}