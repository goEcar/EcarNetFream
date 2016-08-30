package com.ecar.ecarnetwork.http.exception;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常(针对所有的Respone校验)：
 * 1.强制重新登录
 * 2.校验码错误
 */
public class InvalidException extends RuntimeException {

    public static final String FLAG_ERROR_RELOGIN = "2000";
    public static final String FLAG_ERROR_RESPONCE_CHECK = "2001";

    public static Map<String,String> exComMaps = new HashMap<>();
    static{
        exComMaps.put(FLAG_ERROR_RELOGIN, "登录过期，请重新登录");
        exComMaps.put(FLAG_ERROR_RESPONCE_CHECK, "数据校验失败,请重新操作");
    }

    private String code = "";
    private String msg = "";

    public InvalidException(String code) {
        this.code = code;
    }

    public InvalidException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }



    public String getCode() {
        return code;
    }


    public String getMsg() {
        String reMsg = "";
        if(TextUtils.isEmpty(msg)){
            reMsg = exComMaps.get(code);
        }
        return reMsg;
    }

    @Override
    public String toString() {
        return String.format("code=%s, msg=%s", code, msg);
    }
}
