package cn.iocoder.taro.rpc.core.transport.heartbeat;

import cn.iocoder.taro.rpc.core.transport.*;
import cn.iocoder.taro.rpc.core.transport.exchange.Request;
import cn.iocoder.taro.rpc.core.transport.exchange.Response;
import cn.iocoder.taro.rpc.core.util.ObjectUtil;

public class HeartbeatMessageHandler implements MessageHandler {

    private final MessageHandler messageHandler;

    public HeartbeatMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public static Request createHeartbeatRequest() {
        return new Request().setEvent(true).setData(Request.DATA_EVENT_HEARTBEAT);
    }

    public static boolean isHeartbeatRequest(Request request) {
        return request.isEvent()
                && ObjectUtil.equals(request.getData(), Request.DATA_EVENT_HEARTBEAT);
    }

    public static Response createHeartbeatResponse(long id) {
        return new Response(id).setEvent(true).setStatus(Response.STATUS_SUCCESS).setValue(Response.DATA_EVENT_HEARTBEAT);
    }

    public static boolean isHeartbeatResponse(Response response) {
        return response.isEvent()
                && response.getStatus() == Response.STATUS_SUCCESS
                && ObjectUtil.equals(response.getValue(), Response.DATA_EVENT_HEARTBEAT);
    }

    @Override
    public void handle(Channel channel, Object message) {
        if (message instanceof Request) {
            Request request = (Request) message;
            if (isHeartbeatRequest(request)) {
                Response response = createHeartbeatResponse(request.getId()); // 心跳的情况下，默认为 oneway 。
                try {
                    channel.send(response, 1000); // TODO 芋艿，超时
                } catch (TransportException e) {
                    e.printStackTrace(); // TODO 芋艿，后续在考虑下这个异常的处理。
                }
            } else {
                messageHandler.handle(channel, message);
            }
        } else if (message instanceof Response) {
            messageHandler.handle(channel, message);
        } else {
            throw new IllegalArgumentException("未知的消息类型");
        }
    }

}
