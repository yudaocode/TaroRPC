package cn.iocoder.taro.rpc.transport;

import cn.iocoder.taro.rpc.core.transport.Client;
import cn.iocoder.taro.rpc.core.transport.Server;
import cn.iocoder.taro.rpc.core.transport.TransportException;
import cn.iocoder.taro.rpc.core.transport.exchange.ExchangeHandler;
import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.Response;
import cn.iocoder.taro.rpc.core.transport.heartbeat.HeartbeatMessageHandler;
import cn.iocoder.taro.transport.netty4.NettyClient;
import cn.iocoder.taro.transport.netty4.NettyServer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClientHeartbeatHandlerTest {

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
                if (request.getData().equals("\"hello\"")) {
                    response.setValue("world");
                } else {
                    response.setValue("unknown");
                }

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

    @Test
    public void testHeartbeat() throws TransportException, InterruptedException {
        Request heartbeat = HeartbeatMessageHandler.createHeartbeatRequest();
        Response response = client.requestSync(heartbeat, 1000);
        System.out.println(response.getValue());
        Assert.assertEquals(heartbeat.getId(), response.getId());
        Assert.assertEquals(heartbeat.getData(), Response.DATA_EVENT_HEARTBEAT);
    }

}
