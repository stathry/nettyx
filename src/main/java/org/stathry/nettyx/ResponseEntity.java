package org.stathry.nettyx;

import org.stathry.nettyx.util.ErrorEnum;

import java.io.Serializable;

/**
 * ResponseEntity
 *
 */
public class ResponseEntity implements Serializable {

    /** 请求标识 */
    private String sessionId;
    /** 请求处理标识 */
    private String bizNo;
	private boolean success;
	private String code;
	private String msg;
	private Object data;

	public ResponseEntity() {}

	public ResponseEntity(ErrorEnum e) {
        fromErrorEnum(e);
    }

    public void fromErrorEnum(ErrorEnum e) {
        success = e == ErrorEnum.SUCCESS ? true : false;
        code = e.code();
        msg = e.msg();
    }

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "sessionId='" + sessionId + '\'' +
                ", bizNo='" + bizNo + '\'' +
                ", success=" + success +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }
}
