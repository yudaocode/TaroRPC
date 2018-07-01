package cn.iocoder.taro.rpc.core.cluster.support;

import cn.iocoder.taro.rpc.core.cluster.Cluster;
import cn.iocoder.taro.rpc.core.cluster.Directory;
import cn.iocoder.taro.rpc.core.rpc.Invocation;
import cn.iocoder.taro.rpc.core.rpc.Invoker;
import cn.iocoder.taro.rpc.core.rpc.Result;

import java.util.List;

public class DemoCluster implements Cluster {

    @Override
    public <T> Invoker<T> join(final Directory<T> directory) {
        // TODO 芋艿，实现多种 Cluster ，每个对应一个 Invoker 。
        return new Invoker<T>() {

            @Override
            public Result invoke(Invocation invocation) {
                // TODO 芋艿，写个 demo
                List<Invoker<T>> invokers = directory.list(invocation);
                // TODO 每次调用，使用 LoadBalance 选择一个 Invoker 进行调用。
                // TODO 调用失败，根据 Cluster 策略，看看是否需要进行重试。
                for (Invoker<T> invoker : invokers) {
                    return invoker.invoke(invocation);
                }
                return null;
            }

        };
    }

}