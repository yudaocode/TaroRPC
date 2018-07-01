package cn.iocoder.taro.rpc.core.proxy.jdk;

import cn.iocoder.taro.rpc.core.proxy.AbstractProxyInvoker;
import cn.iocoder.taro.rpc.core.proxy.ProxyFactory;
import cn.iocoder.taro.rpc.core.rpc.Invoker;

import java.lang.reflect.Method;

public class JdkProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Invoker<T> invoker) {
        return null;
    }

    @Override
    public <T> Invoker<T> getInvoker(T proxy, Class<T> type) {
        return new AbstractProxyInvoker<T>(proxy) {

            @Override
            protected Object doInvoke(T proxy, String methodName, Object[] arguments, Class<?>[] argumentTypes) throws Throwable {
                Method method = proxy.getClass().getMethod(methodName, argumentTypes);
                return method.invoke(proxy, arguments);
            }

        };
    }

}