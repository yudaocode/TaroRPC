package cn.iocoder.taro.rpc.core.rpc.support;

import cn.iocoder.taro.rpc.core.rpc.Result;

public class RpcResult implements Result {

    private Object value;
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
