package cn.iocoder.taro.rpc.core.protocol.taro;

import cn.iocoder.taro.rpc.core.protocol.Exporter;
import cn.iocoder.taro.rpc.core.rpc.Invoker;

public class TaroExporter<T> implements Exporter<T> {

    private final Invoker<T> invoker;

    public TaroExporter(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    @Override
    public Invoker<T> getInvoker() {
        return invoker;
    }

    @Override
    public void unexport() {
        // TODO 芋艿，等待实现
    }

}
