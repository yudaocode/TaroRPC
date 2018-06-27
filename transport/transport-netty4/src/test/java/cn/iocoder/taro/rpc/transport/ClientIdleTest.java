package cn.iocoder.taro.rpc.transport;

import cn.iocoder.taro.rpc.core.common.TaroConstants;
import cn.iocoder.taro.rpc.core.transport.Client;
import cn.iocoder.taro.rpc.core.transport.Server;
import cn.iocoder.taro.rpc.core.transport.exception.TransportException;
import cn.iocoder.taro.rpc.core.transport.exchange.ExchangeHandler;
import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.Response;
import cn.iocoder.taro.transport.netty4.NettyClient;
import cn.iocoder.taro.transport.netty4.NettyServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClientIdleTest {

    private static Client client;
    private static Server server;

    @BeforeClass
    public static void before() {
        server = new NettyServer(8080, new ExchangeHandler() {
            @Override
            public Response reply(Request request) {
                return null;
            }
        });
        TaroConstants.TRANSPORT_CLIENT_IDLE = 1; // 1000 ms 空闲
        client = new NettyClient("127.0.0.1", 8080, null);
    }

    @AfterClass
    public static void after() {
        client.close();
        server.close();
    }

    @Test
    public void testIdle() throws TransportException, InterruptedException {
        Thread.sleep(1000000L); // TODO 芋艿，暂时没想到很好的测试方式
    }

}
