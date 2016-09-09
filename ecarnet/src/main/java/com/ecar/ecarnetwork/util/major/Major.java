package com.ecar.ecarnetwork.util.major;

import android.text.TextUtils;

import com.ecar.ecarnetwork.db.SettingPreferences;
import com.ecar.ecarnetwork.http.api.ApiBox;
import com.ecar.ecarnetwork.http.util.ConstantsLib;
import com.ecar.ecarnetwork.http.util.InvalidUtil;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;


/**
 * ===============================================
 * <p>
 * 类描述:
 * <p>
 * 创建人:   happy
 * <p>
 * 创建时间: 2016/7/7 0007 上午 11:46
 * <p>
 * 修改人:   happy
 * <p>
 * 修改时间: 2016/7/7 0007 上午 11:46
 * <p>
 * 修改备注:
 * <p>
 * ===============================================
 */
public class Major {

    protected final static String PARAMS_CLIENT_TYPE = "ClientType";//

    protected final static String PARAMS_ANDROID = "android";//

    public final static String PARAMS_MODULE = "module";//

    public final static String PARAMS_SERVICE = "service";//

    public final static String PARAMS_METHOD = "method";//

    protected final static String comid = "200000002";

    public static final String PARAMS_USER_PHONE_NAME = "userPhoneNum";

    protected static SettingPreferences spUtil = SettingPreferences.getDefault(ApiBox.getInstance().application);
    private static StringBuilder sb = new StringBuilder();

    /**
     * @throws Exception
     * @功能：获取带tkey的treemap
     * @param：
     * @return：
     */
    public static TreeMap<String, String> getTreeMapByT() {
        TreeMap<String, String> tMap = new TreeMap<String, String>();
        tMap.put(PARAMS_CLIENT_TYPE, PARAMS_ANDROID);
        if (spUtil.getU() != null) {
            tMap.put("u", spUtil.getU());
            tMap.put("t", spUtil.getT());
            tMap.put("ts", spUtil.getTs());
        }
        tMap.put(PARAMS_MODULE, "app");
        tMap.put(PARAMS_SERVICE, "Std");
        return tMap;
    }

    /**
     * @throws Exception
     * @功能：获取带vkey的treemap
     * @param：
     * @return：
     */
    public static TreeMap<String, String> getTreeMapByV() {
        TreeMap<String, String> tMap = new TreeMap<String, String>();
        tMap.put(PARAMS_CLIENT_TYPE, PARAMS_ANDROID);
        tMap.put("u", spUtil.getU());
        tMap.put("v", spUtil.getV());
        tMap.put("ts", spUtil.getTs());
        tMap.put(PARAMS_MODULE, "app");
        tMap.put(PARAMS_SERVICE, "Std");
        return tMap;
    }

    /**
     * @throws Exception
     * @功能：获取不带key的treemap
     * @param：
     * @return：
     */
    public static TreeMap<String, String> getTreeMapNoneKey() {
        TreeMap<String, String> tMap = new TreeMap<String, String>();
        tMap.put(PARAMS_MODULE, "app");
        tMap.put(PARAMS_SERVICE, "Std");
        tMap.put(PARAMS_CLIENT_TYPE, PARAMS_ANDROID);
        if(!TextUtils.isEmpty(spUtil.getTs())){
            tMap.put("ts", spUtil.getTs());
        }
        return tMap;
    }

    /******************************end 获取三种keyMap end****************************************/

    /**
     * @throws Exception
     * @功能：加密方法（加encode）
     * @param：
     * @return：
     */
    public static TreeMap<String, String> securityKeyMethodEnc(TreeMap<String, String> tMap) {
        return getSecurityMapKeys(tMap, true,true);
    }

    /**
     * @throws Exception
     * @功能：加密方法（不加encode）
     * @param：
     * @return：
     */
    private static TreeMap<String, String> securityKeyMethodNoEnc(TreeMap<String, String> tMap) {
        return getSecurityMapKeys(tMap, false,true);
    }

    /**
     * @throws Exception
     * @功能：获取拼接后的请求字符串
     * @param：encode:是否添加encode
     * @return：
     */
    protected static TreeMap<String, String> getSecurityMapKeys(TreeMap<String, String> tMap, boolean encode,boolean isSign) {
        if(!TextUtils.isEmpty(ConstantsLib.APP_ID)){ //一体化（appid不为空）处理：根据校验标志设置appId值
            tMap.put("ve", "2");
            if (isSign) {
                tMap.put("appId", InvalidUtil.BinstrToStr(ConstantsLib.APP_ID));
            } else {
                tMap.put("appId", "");
            }
        }
        Set<String> keys = tMap.keySet();
        Iterator<String> iterator = keys.iterator();
        String parmas = "";
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = tMap.get(key);
            parmas = InvalidUtil.addText(sb, parmas, key, "=", value, "&");
        }
        parmas = InvalidUtil.addText(sb, parmas, "requestKey=",
                InvalidUtil.BinstrToStr(ConstantsLib.REQUEST_KEY));
        String md5 = InvalidUtil.md5(parmas).toString();
        tMap.remove("requestKey");
        Iterator<String> iterator1 = keys.iterator();
        parmas = "";
        while (iterator1.hasNext()) {
            String key = iterator1.next();
            String value = tMap.get(key);
            if (encode) {
                value = InvalidUtil.getEncodedStr(value);
                tMap.put(key,value);
            }
            parmas = InvalidUtil.addText(sb, parmas, key, "=", value, "&");
        }

        if(!TextUtils.isEmpty(ConstantsLib.APP_ID)){ //一体化（appid不为空）处理：根据校验标志设置sign值
            if(isSign){
                tMap.put("sign", md5);
            }else{
                tMap.put("sign", "");
            }
        }else{
            tMap.put("sign", md5);
        }

        return tMap;
    }

}
