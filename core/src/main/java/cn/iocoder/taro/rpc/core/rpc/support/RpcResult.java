package cn.iocoder.taro.rpc.core.rpc.support;

import cn.iocoder.taro.rpc.core.rpc.Result;

public class RpcResult implements Result {

    /**
     * 正常返回结果
     */
    private Object value;
    /**
     * 异常返回结果
     *
     * 这里要注意下，如果 Service 执行逻辑，抛出 Exception 时，此时 {@link cn.iocoder.taro.rpc.core.transport.exchange.Response#status} 还是返回成功的。
     * 这块，保持和 Dubbo 一致。
     */
    private Throwable exception;

    public RpcResult(Object value) {
        this.value = value;
    }

    public RpcResult(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Throwable getException() {
        return exception;
    }

}