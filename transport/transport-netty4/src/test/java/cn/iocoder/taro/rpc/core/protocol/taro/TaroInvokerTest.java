package cn.iocoder.taro.rpc.core.protocol.taro;

import cn.iocoder.taro.rpc.core.proxy.ProxyFactory;
import cn.iocoder.taro.rpc.core.proxy.jdk.JdkProxyFactory;
import cn.iocoder.taro.rpc.core.rpc.Invoker;
import cn.iocoder.taro.rpc.core.rpc.Result;
import cn.iocoder.taro.rpc.core.rpc.support.RpcInvocation;
import cn.iocoder.taro.rpc.core.transport.Server;
import cn.iocoder.taro.rpc.core.transport.exchange.ExchangeHandler;
import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.Response;
import cn.iocoder.taro.rpc.demo.DemoService;
import cn.iocoder.taro.transport.netty4.NettyClient;
import cn.iocoder.taro.transport.netty4.NettyServer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TaroInvokerTest {

    private static TaroInvoker invoker;
    private static Server server;

    public static class DemoSerivceImpl implements DemoService {

        @Override
        public String hello(String word) {
            return "你好：" + word;
        }

    }

    @BeforeClass
    public static void before() {
        final DemoService demoService = new DemoSerivceImpl();

        ProxyFactory proxyFactory = new JdkProxyFactory();
        Invoker<DemoService> demoServiceInvoker = proxyFactory.getInvoker(demoService, DemoService.class);
        final Map<Class, Invoker> serviceInvokers = new HashMap<Class, Invoker>();
        serviceInvokers.put(DemoService.class, demoServiceInvoker);

        server = new NettyServer(8080, new ExchangeHandler() {

            @Override
            public Response reply(Request request) {
                System.out.println("接收到消息：" + request);

                Response response = new Response(request.getId());
                response.setEvent(false);
                response.setStatus(Response.STATUS_SUCCESS);

                if (request.getData() instanceof RpcInvocation) {
                    RpcInvocation invocation = (RpcInvocation) request.getData();
//                    if (DemoService.class == invocation.getInterfaceClass()) {
//                        if () {
//
//                        }
//                    }
//                    response.setData(new RpcResult("word"));
                    Invoker invoker = serviceInvokers.get(invocation.getInterfaceClass());
                    response.setData(invoker.invoke(invocation));
                } else {
                    response.setData(request.getData());
                }

                return response;
            }

        }, new TaroCodec());
        invoker = new TaroInvoker(new NettyClient("127.0.0.1", 8080, null, new TaroCodec()));
    }

    @Test
    public void testInvoke() {
        RpcInvocation invocation = new RpcInvocation();
        invocation.setInterfaceClass(DemoService.class);
        invocation.setMethodName("hello");
        invocation.setArguments(new Object[]{"nihao"});
        Result result = invoker.invoke(invocation);
        System.out.println(result.getValue());
    }

}