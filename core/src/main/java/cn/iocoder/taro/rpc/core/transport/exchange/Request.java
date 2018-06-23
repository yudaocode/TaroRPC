package cn.iocoder.taro.rpc.core.transport.exchange;

import java.util.concurrent.atomic.AtomicLong;

public class Request {

    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    public static final String DATA_EVENT_HEARTBEAT = null;
    public static final String DATA_EVENT_READ_ONLY = "readOnly";

    private long id;
    /**
     * 是否单向消息。
     *
     * 单向消息不需要response
     */
    private boolean oneway;
    /**
     * 是否为事件
     */
    private boolean evnet;
    private Object data;

    public Request(long id) {
        this.id = id;
    }

    public Request() {
        this(ID_GENERATOR.incrementAndGet());
    }

    public long getId() {
        return id;
    }

    public Object getData() {
        return data;
    }

    public Request setData(Object data) {
        this.data = data;
        return this;
    }

    public boolean isOneway() {
        return oneway;
    }

    public Request setOneway(boolean oneway) {
        this.oneway = oneway;
        return this;
    }

    public boolean isEvnet() {
        return evnet;
    }

    public Request setEvnet(boolean evnet) {
        this.evnet = evnet;
        return this;
    }
}