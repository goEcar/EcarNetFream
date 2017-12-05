package com.ecar.ecarnetwork.http.util;

/**
 * 常量类
 */
public class ConstantsLib {


    private ConstantsLib() {

    }


    public static ConstantsLib getInstance() {
        return SingletonHolder.constantsLib;
    }


    private static class SingletonHolder {
        private static final ConstantsLib constantsLib = new ConstantsLib();
    }


    /**
     * 3G模式读取文件缓存时间限制
     */
    public final int CONFIG_CACHE_MOBILE_TIMEOUT = 60000 * 60 * 10; // 10小时

    /**
     * wifi模式读取文件缓存时间限制
     */
    public final int CONFIG_CACHE_WIFI_TIMEOUT = 60000 * 60 * 5; // 5小时

    /**
     * REQUEST_KEY
     */
    public String REQUEST_KEY;

    /**
     * APP_ID
     */
    public String APP_ID = "";

    /**
     * Log 日志开关 发布版本设为false
     */
    public boolean DEBUG = false;
    public boolean VeriNgis = true;

}
