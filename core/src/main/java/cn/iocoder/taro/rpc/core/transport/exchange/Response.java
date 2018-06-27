package cn.iocoder.taro.rpc.core.transport.exchange;

public class Response {

    /**
     * 状态 - 成功
     */
    public static final byte STATUS_SUCCESS = 0;
    public static final byte STATUS_SEND_ERROR = 1; // 发送请求失败
    public static final byte STATUS_TIMEOUT = 2; // 等待响应超时
    public static final byte STATUS_SERVER_ERROR = 3; // 服务器异常
    public static final byte STATUS_SERVICE_ERROR = 4; // 服务异常。不同于 STATUS_SERVER_ERROR ，它用来表示服务执行发生异常

    public static final String DATA_EVENT_HEARTBEAT = "heartbeat";

    private final long id;
    private boolean event;
    private byte status;
    /**
     * 正常响应数据
     */
    private Object data;
    /**
     * 异常错误提示
     */
    private String errorMsg;

    public Response(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Object getData() {
        return data;
    }

    public Response setData(Object data) {
        this.data = data;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public Response setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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
                ", data=" + data +
                ", errorMsg=" + errorMsg +
                '}';
    }

}