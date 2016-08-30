package com.ecar.ecarnetwork.http.exception;


import com.ecar.ecarnetwork.bean.ResBase;

/**
 * 自定义异常：
 * 1.自定义处理失败逻辑
 */
public class UserException extends RuntimeException {
    private static final String DEFAULT_CODE = "-1";
    private String code;
    private String msg;
    private ResBase resObj;

    public UserException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public UserException(String code, String msg, ResBase resObj) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.resObj = resObj;
    }

    public UserException(String msg) {
        super(msg);
        this.code = DEFAULT_CODE;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }

    public ResBase getResObj() {
        return resObj;
    }

    @Override
    public String toString() {
        return String.format("code=%s, msg=%s", code, msg);
    }
}
