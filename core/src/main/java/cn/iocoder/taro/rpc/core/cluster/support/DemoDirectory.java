package cn.iocoder.taro.rpc.core.cluster.support;

import cn.iocoder.taro.rpc.core.cluster.Directory;
import cn.iocoder.taro.rpc.core.rpc.Invocation;
import cn.iocoder.taro.rpc.core.rpc.Invoker;

import java.util.List;

public class DemoDirectory<T> implements Directory<T> {

    @Override
    public List<Invoker<T>> list(Invocation invocation) {
        return null;
    }

}
