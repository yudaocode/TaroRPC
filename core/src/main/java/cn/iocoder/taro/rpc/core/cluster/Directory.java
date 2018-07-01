package cn.iocoder.taro.rpc.core.cluster;

import cn.iocoder.taro.rpc.core.rpc.Invocation;
import cn.iocoder.taro.rpc.core.rpc.Invoker;

import java.util.List;

public interface Directory<T> {

    List<Invoker<T>> list(Invocation invocation);

}