package cn.iocoder.taro.rpc.transport;

import cn.iocoder.taro.rpc.core.transport.Client;
import cn.iocoder.taro.rpc.core.transport.Server;
import cn.iocoder.taro.rpc.core.transport.TransportException;
import cn.iocoder.taro.rpc.core.transport.exchange.*;
import cn.iocoder.taro.transport.netty4.NettyClient;
import cn.iocoder.taro.transport.netty4.NettyServer;
import org.junit.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class ChannelTest {

    private static Client client;
    private static Server server;

    private static volatile boolean mockTimeoutFlag = true;

    @BeforeClass
    public static void before() {
        server = new NettyServer(8080, new ExchangeHandler() {
            @Override
            public Response reply(Request request) {
                System.out.println("接收到消息：" + request);
                if (request.isOneway()) {
                    System.out.println("oneway 消息，不进行响应");
                    return null;
                }

                if (mockTimeoutFlag) {
                    try {
                        Thread.sleep(60 * 1000L);
                    } catch (InterruptedException e) {
                    }
                }

                Response response = new Response(request.getId());
                response.setEvent(false);
                response.setStatus(Response.STATUS_SUCCESS);
                response.setValue(request.getData());

                return response;
            }
        });
        client = new NettyClient("127.0.0.1", 8080, null);
    }

    @AfterClass
    public static void after() {
        client.close();
        server.close();
    }

    @Before
    public void beforeMethod() {
        mockTimeoutFlag = false;
    }

    @Test
    public void testOneway() throws TransportException {
        client.oneway("hello", 1000);
    }

    @Test
    public void testSync() throws TransportException, InterruptedException {
        Response response = client.requestSync("hello", 10000);
        Assert.assertEquals(response.getValue(), "hello");
    }

    @Test
    public void testAsync() throws TransportException, InterruptedException {
        InvokeFuture future = client.requestAsync("hello", 1000);
        Assert.assertEquals(future.waitResponse().getValue(), "hello");
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testCallbackSuccess() throws TransportException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Object> result = new AtomicReference<Object>();
        client.requestWithCallback("hello", new ResponseCallback() {

            @Override
            public void onSuccess(Object value) {
                result.set(value);
                latch.countDown();
            }

            @Override
            public void onFailure(Throwable throwable) {
                result.set(throwable);
                latch.countDown();
            }

        }, 1000);
        latch.await();
        Assert.assertEquals(result.get(), "hello");
    }

    @Test
    @SuppressWarnings("Duplicates")
    public void testCallbackFailure() throws InterruptedException, TransportException {
        mockTimeoutFlag = true;
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Object> result = new AtomicReference<Object>();
        client.requestWithCallback("hello", new ResponseCallback() {

            @Override
            public void onSuccess(Object value) {
                result.set(value);
                latch.countDown();
            }

            @Override
            public void onFailure(Throwable throwable) {
                result.set(throwable);
                latch.countDown();
            }

        }, 1000);
        latch.await();
        Assert.assertEquals(result.get().getClass(), RuntimeException.class); // 芋艿，后续优化
    }

}
