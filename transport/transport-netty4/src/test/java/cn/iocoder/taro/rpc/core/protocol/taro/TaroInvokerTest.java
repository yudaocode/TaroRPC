package cn.iocoder.taro.rpc.core.protocol.taro;

import cn.iocoder.taro.rpc.core.protocol.Exporter;
import cn.iocoder.taro.rpc.core.proxy.ProxyFactory;
import cn.iocoder.taro.rpc.core.proxy.jdk.JdkProxyFactory;
import cn.iocoder.taro.rpc.core.rpc.Invoker;
import cn.iocoder.taro.rpc.demo.DemoService;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaroInvokerTest {

//    private static Invoker invoker;
    private static DemoService demoService;
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
        final DemoService demoServiceImpl = new DemoServiceImpl();
        ProxyFactory proxyFactory = new JdkProxyFactory();
        Invoker<DemoService> demoServiceInvoker = proxyFactory.getInvoker(demoServiceImpl, DemoService.class);

        final TaroProtocol protocol = new TaroProtocol();
        exporter = protocol.export(demoServiceInvoker);

//        invoker = new TaroInvoker(new NettyClient("127.0.0.1", 8080, null, new TaroCodec()));
        Invoker<DemoService> invoker = protocol.refer(DemoService.class);

        demoService = (DemoService) proxyFactory.getProxy(invoker);
//        demoService = (DemoService) proxyFactory.getProxy(invoker);
    }

    @Test
    public void testInvoke() {
//        RpcInvocation invocation = new RpcInvocation();
//        invocation.setInterfaceClass(DemoService.class);
//        invocation.setMethodName("hello");
//        invocation.setArguments(new Object[]{"nihao"});
//        Result result = invoker.invoke(invocation);
//        System.out.println(result.getValue());

        String result = demoService.hello("nihao");
        System.out.println("返回结果：" + result);
    }

    @Test
    public void testHeartbeat() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

}