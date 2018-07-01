package cn.iocoder.taro.rpc.core.cluster;

import cn.iocoder.taro.rpc.core.rpc.Invoker;

public interface Cluster {

    <T> Invoker<T> join(Directory<T> directory);

}