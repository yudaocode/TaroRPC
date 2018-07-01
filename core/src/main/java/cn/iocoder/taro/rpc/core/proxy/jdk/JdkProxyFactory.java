package cn.iocoder.taro.rpc.core.proxy.jdk;

import cn.iocoder.taro.rpc.core.proxy.AbstractProxyInvoker;
import cn.iocoder.taro.rpc.core.proxy.InvokerInvocationHandler;
import cn.iocoder.taro.rpc.core.proxy.ProxyFactory;
import cn.iocoder.taro.rpc.core.rpc.Invoker;
import cn.iocoder.taro.rpc.core.util.ClassUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Invoker<T> invoker) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{ClassUtil.forName("cn.iocoder.taro.rpc.demo.DemoService")}, // TODO 芋艿，URL 未来完成
                new InvokerInvocationHandler(invoker));
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