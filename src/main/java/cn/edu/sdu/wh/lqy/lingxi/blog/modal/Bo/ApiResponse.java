package cn.edu.sdu.wh.lqy.lingxi.blog.modal.Bo;

/**
 * rest返回对象
 */
public class ApiResponse<T> {

    /**
     * 服务器响应数据
     */
    private T payload;

    /**
     * 请求是否成功
     */
    private boolean success;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 状态码
     */
    private int code = -1;

    /**
     * 服务器响应时间
     */
    private long timestamp;

    public ApiResponse() {
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public ApiResponse(boolean success) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
    }

    public ApiResponse(boolean success, T payload) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
    }

    public ApiResponse(boolean success, T payload, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.payload = payload;
        this.code = code;
    }

    public ApiResponse(boolean success, String msg) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
    }

    public ApiResponse(boolean success, String msg, int code) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.success = success;
        this.msg = msg;
        this.code = code;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static ApiResponse ok() {
        return new ApiResponse(true);
    }

    public static <T> ApiResponse ok(T payload) {
        return new ApiResponse(true, payload);
    }

    public static <T> ApiResponse ok(int code) {
        return new ApiResponse(true, null, code);
    }

    public static <T> ApiResponse ok(T payload, int code) {
        return new ApiResponse(true, payload, code);
    }

    public static ApiResponse fail() {
        return new ApiResponse(false);
    }

    public static ApiResponse fail(String msg) {
        return new ApiResponse(false, msg);
    }

    public static ApiResponse fail(int code) {
        return new ApiResponse(false, null, code);
    }

    public static ApiResponse fail(int code, String msg) {
        return new ApiResponse(false, msg, code);
    }

}