package cn.iocoder.taro.rpc.core.transport;

import java.util.concurrent.atomic.AtomicLong;

public class Request {

    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    private long id;

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

}