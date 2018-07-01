package cn.iocoder.taro.rpc.core.proxy;

import cn.iocoder.taro.rpc.core.exception.TaroFrameworkException;
import cn.iocoder.taro.rpc.core.rpc.Invocation;
import cn.iocoder.taro.rpc.core.rpc.Invoker;
import cn.iocoder.taro.rpc.core.rpc.Result;
import cn.iocoder.taro.rpc.core.rpc.support.RpcResult;
import cn.iocoder.taro.rpc.core.util.ExceptionUtil;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractProxyInvoker<T> implements Invoker<T> {

    private final T proxy;

    protected AbstractProxyInvoker(T proxy) {
        this.proxy = proxy;
    }


    @Override
    public Result invoke(Invocation invocation) {
        try {
            return new RpcResult(doInvoke(proxy, invocation.getMethodName(), invocation.getArguments(), invocation.getArgumentTypes()));
        } catch (InvocationTargetException ex) { // 反射调用的业务方法，自身发生异常，则进行包装成 RpcResult ，并返回
            return new RpcResult(ex.getTargetException());
        } catch (Throwable th) {
            throw new TaroFrameworkException("Failed to invoke remote proxy method " + invocation.getMethodName() + ", cause=" + ExceptionUtil.getMessage(th), th);
        }
    }

    // 通过该抽象类，屏蔽 Invocation 和 Result 。
    protected abstract Object doInvoke(T proxy, String methodName, Object[] arguments, Class<?>[] argumentTypes) throws Throwable;

}