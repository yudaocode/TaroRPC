package cn.iocoder.taro.rpc.transport;

import cn.iocoder.taro.rpc.core.transport.Channel;
import cn.iocoder.taro.rpc.core.transport.Client;
import cn.iocoder.taro.rpc.core.transport.Server;
import cn.iocoder.taro.rpc.core.transport.TransportException;
import cn.iocoder.taro.rpc.core.transport.exchange.ExchangeHandler;
import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.Response;
import cn.iocoder.taro.transport.netty4.NettyClient;
import cn.iocoder.taro.transport.netty4.NettyServer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NettyClientTest {

    private static Client client;
    private static Server server;

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

                Response response = new Response(request.getId());
                response.setEvent(false);
                response.setStatus(Response.STATUS_SUCCESS);
                response.setValue(request.getData());

                return response;
            }
        });
        client = new NettyClient("127.0.0.1", 8080, null);
    }

    @Test
    public void testReconnect() throws TransportException, InterruptedException {
        // Server 关闭所有连接
        closeAll();
        // 尝试发送请求，应该是失败的
        try {
            client.requestSync("hello");
        } catch (Exception e) {
            e.printStackTrace();
            client.reconnect();
            Response result = client.requestSync("hello");
            Assert.assertEquals(result.getValue(), "hello");
        }
    }

    /**
     * Server 关闭所有连接
     */
    public void closeAll() throws InterruptedException {
        while (server.getChannels().isEmpty()) {
            Thread.sleep(100L);
        }
        for (Channel channel : server.getChannels()) {
            channel.close();
        }
    }

}
