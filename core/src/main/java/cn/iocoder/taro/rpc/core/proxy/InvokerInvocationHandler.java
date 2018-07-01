package cn.iocoder.taro.rpc.core.proxy;

import cn.iocoder.taro.rpc.core.rpc.Invoker;
import cn.iocoder.taro.rpc.core.rpc.support.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvokerInvocationHandler implements InvocationHandler {

    private final Invoker invoker;

    public InvokerInvocationHandler(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        // wait 等方法，直接反射调用
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(invoker, args);
        }
        // 基础方法，不使用 RPC 调用
        if ("toString".equals(methodName) && parameterTypes.length == 0) {
            return invoker.toString();
        }
        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
            return invoker.hashCode();
        }
        if ("equals".equals(methodName) && parameterTypes.length == 1) {
            return invoker.equals(args[0]);
        }
        // RPC 调用
        RpcInvocation invocation = new RpcInvocation();
        invocation.setInterfaceClass(method.getDeclaringClass()); // TODO 芋艿，可能会有问题
        invocation.setMethodName(methodName);
        invocation.setArgumentTypes(parameterTypes);
        invocation.setArguments(args);
        return invoker.invoke(invocation).recreate();
    }

}
