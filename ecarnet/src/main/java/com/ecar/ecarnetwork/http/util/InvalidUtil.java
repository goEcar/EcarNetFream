package com.ecar.ecarnetwork.http.util;


import com.ecar.ecarnetwork.db.SettingPreferences;
import com.ecar.ecarnetwork.http.api.ApiBox;
import com.ecar.ecarnetwork.util.major.Major;

/**
 * ===============================================
 * <p>
 * 类描述: 校验类
 * <p>
 * 创建人:   happy
 * <p>
 * 创建时间: 2016/7/26 0026 上午 11:08
 * <p>
 * 修改人:   happy
 * <p>
 * 修改时间: 2016/7/26 0026 上午 11:08
 * <p>
 * 修改备注:
 * <p>
 * ===============================================
 */
public class InvalidUtil {


    /**
     * 验证是否符合sign规则
     *
     * @param
     * @return true 符合sign规则
     */
    public static boolean checkSign(String sign,String content) {
        return Major.eUtil.checkSign(sign,content, SettingPreferences.getDefault(ApiBox.getInstance().application).getReqkey());
    }






}
