package com.ecar.ecarnetwork.http.exception;

import android.text.TextUtils;

import com.ecar.ecarnetwork.bean.ResBase;

import java.util.HashMap;
import java.util.Map;


/**
 * 统一处理的异常封装类
 */
public class CommonException extends Exception {

    //通常的异常
    public static final String FLAG_UNKNOWN = "1001";//异常
    public static final String FLAG_NET_ERROR = "1002";//网络异常标志
    public static final String FLAG_NET_TIME_OUT = "10021";//网络异常标志
    public static final String FLAG_PARSE_ERROR = "1003";//解析异常
    public static final String FLAG_PERMISSION_ERROR = "1004";//权限异常


    public static Map<String,String> exApiMaps = new HashMap<>();

    static{
        exApiMaps.put(FLAG_NET_ERROR, "请求超时");
        exApiMaps.put(FLAG_PARSE_ERROR, "解析异常");
        exApiMaps.put(FLAG_PERMISSION_ERROR, "未许可相关权限");
        exApiMaps.put(FLAG_UNKNOWN, "未标记的异常");
    }

    private String code = "";
    private String msg ="";
    private ResBase resObj;
    private boolean isDoNothing;

    public CommonException(Throwable throwable, String code) {
        super(throwable);
        this.code = code;
    }

//    public CommonException(Throwable throwable, String code, String msg) {
//        this(throwable,code);
//        this.msg = msg;
//    }

//    public CommonException(Throwable throwable,String code, String msg, ResBase resObj) {
//        this(throwable,code);
//        this.msg = msg;
//        this.resObj = resObj;
//    }

    public CommonException(UserException e) {
        this(e,e.getCode());
        this.msg = e.getMsg();
        this.resObj = e.getResObj();
    }

    public String getCode() {
        return TextUtils.isEmpty(code)?"":code;
    }

    public ResBase getResObj() {
        return resObj;
    }

    public boolean isDoNothing() {
        return isDoNothing;
    }

    public void setDoNothing(boolean doNothing) {
        isDoNothing = doNothing;
    }

    public String getMsg() {
        String reMsg = "";
        if(!TextUtils.isEmpty(msg)){
            reMsg = msg;
        }else if(TextUtils.isEmpty(msg)&&!TextUtils.isEmpty(code)){
            reMsg =  exApiMaps.get(code);
        }
        return reMsg;
    }
}
