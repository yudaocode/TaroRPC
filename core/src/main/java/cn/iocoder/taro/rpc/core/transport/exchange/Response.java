package cn.iocoder.taro.rpc.core.transport.exchange;

public class Response {

    /**
     * 状态 - 成功
     */
    public static final byte STATUS_SUCCESS = 0;
    public static final byte STATUS_TIMEOUT = 1; // TODO 芋艿，不区分超时类型。客户端超时 or 服务端超时

    public static final String DATA_EVENT_HEARTBEAT = "heartbeat";

    private final long id;
    private boolean event;
    private byte status;
    private Object value;
    private Throwable exception;

    public Response(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

    public byte getStatus() {
        return status;
    }

    public Response setStatus(byte status) {
        this.status = status;
        return this;
    }

    public boolean isEvent() {
        return event;
    }

    public Response setEvent(boolean event) {
        this.event = event;
        return this;
    }

    @Override
    public String toString() {
        return "Response{" +
                "id=" + id +
                ", event=" + event +
                ", status=" + status +
                ", value=" + value +
                ", exception=" + exception +
                '}';
    }

}