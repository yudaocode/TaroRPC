package cn.iocoder.taro.rpc.core.protocol.taro;

import cn.iocoder.taro.rpc.core.rpc.Invocation;
import cn.iocoder.taro.rpc.core.rpc.Result;
import cn.iocoder.taro.rpc.core.rpc.support.AbstractInvoker;
import cn.iocoder.taro.rpc.core.rpc.support.RpcResult;
import cn.iocoder.taro.rpc.core.transport.Client;
import cn.iocoder.taro.rpc.core.transport.exception.TransportException;
import cn.iocoder.taro.rpc.core.transport.exchange.Response;

public class TaroInvoker extends AbstractInvoker {

    private final Client client;

    public TaroInvoker(Client client) {
        this.client = client;
    }

    @Override
    protected Result doInvoke(Invocation invocation) {
        // TODO 同步、异步、oneway
        // TODO 超时
        try {
            Response response = client.requestSync(invocation, 1000 * 1000L);
            // TODO 芋艿，在 Dubbo 中，如果同步调用超时，直接抛出异常。
            if (response.getErrorMsg() != null) {
                return new RpcResult(new TransportException(null, null, response.getErrorMsg()));
            }
            return (Result) response.getData(); // 可以使用 Result 强制转换的原因是，Server 返回的就是 Result 对象
        } catch (InterruptedException e) {
//            e.printStackTrace();
            return new RpcResult(e);
        } catch (TransportException e) {
//            e.printStackTrace();
            return new RpcResult(e);
        } catch (Throwable e) {
            return new RpcResult(e);
        }
    }

}