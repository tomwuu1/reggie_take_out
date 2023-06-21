package com.itheima.reggie.common;
/**
 * 自定义异常
 */
public class CustomException extends RuntimeException {
    private String code;
    private String message;
    public CustomException(String code) {
        this.code = code;
        this.message = message;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

