package cn.iocoder.taro.rpc.core.rpc.support;

import cn.iocoder.taro.rpc.core.rpc.Invocation;
import cn.iocoder.taro.rpc.core.rpc.Invoker;
import cn.iocoder.taro.rpc.core.rpc.Result;

public abstract class AbstractInvoker implements Invoker {

    @Override
    public Result invoke(Invocation invocation) {
        // TODO 芋艿，隐式传参
        try {
            return doInvoke(invocation);
        } catch (Throwable th) {
            return new RpcResult(th);
        }
    }

    protected abstract Result doInvoke(Invocation invocation);

}