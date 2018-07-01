package cn.iocoder.taro.rpc.core.protocol.taro;

import cn.iocoder.taro.rpc.core.protocol.Exporter;
import cn.iocoder.taro.rpc.core.proxy.ProxyFactory;
import cn.iocoder.taro.rpc.core.proxy.jdk.JdkProxyFactory;
import cn.iocoder.taro.rpc.core.rpc.Invoker;
import cn.iocoder.taro.rpc.core.rpc.Result;
import cn.iocoder.taro.rpc.core.rpc.support.RpcInvocation;
import cn.iocoder.taro.rpc.demo.DemoService;
import cn.iocoder.taro.transport.netty4.NettyClient;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaroInvokerTest {

    private static Invoker invoker;
//    private static Server server;
    private static Exporter exporter;

    public static class DemoServiceImpl implements DemoService {

        @Override
        public String hello(String word) {
            return "你好：" + word;
        }

    }

    @BeforeClass
    public static void before() {
        final DemoService demoService = new DemoServiceImpl();
        ProxyFactory proxyFactory = new JdkProxyFactory();
        Invoker<DemoService> demoServiceInvoker = proxyFactory.getInvoker(demoService, DemoService.class);

        final TaroProtocol protocol = new TaroProtocol();
        exporter = protocol.export(demoServiceInvoker);

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