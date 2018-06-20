package cn.iocoder.taro.rpc.core.transport;

public class Response {

    private long id;
    private Object value;
    private Throwable exception;

    public long getId() {
        return id;
    }

    public Response setId(long id) {
        this.id = id;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public Response setValue(Object value) {
        this.value = value;
        return this;
    }

    public Throwable getException() {
        return exception;
    }

    public Response setException(Throwable exception) {
        this.exception = exception;
        return this;
    }

}
