/**
 * Copyright © stathry@126.com All Rights Reserved
 */
package org.stathry.nettyx.util;

/**
 * 系统响应码枚举
 */
public enum ErrorEnum {
	
	SUCCESS( "成功"),
    PARAMETER_INVALID("参数错误"),
    SYSTEM_ERROR( "系统异常"),
    SYSTEM_TIMEOUT("系统繁忙"),
	;
	
	public static final String SYS = "SYS1";
	private String msg;
	
	/**
	 * @param msg
	 */
	private ErrorEnum(String msg) {
		this.msg = msg;
	}

	public String code() {
		return new StringBuilder(SYS).append('.').append(name()).toString();
	}

	public String msg() {
		return msg;
	}

    @Override
    public String toString() {
        return "ErrorEnum{" +
                "code='" + code() + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public static void main(String[] args) {
        for (ErrorEnum e : values()) {
            System.out.println(e.code() + "," + e.msg());
        }
    }

}
