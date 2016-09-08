package com.ecar.ecarnetfream.publics.network;/*
 *===============================================
 *
 * 文件名:${type_name}
 *
 * 描述: 
 *
 * 作者:
 *
 * 版权所有:深圳市亿车科技有限公司
 *
 * 创建日期: ${date} ${time}
 *
 * 修改人:   金征
 *
 * 修改时间:  ${date} ${time} 
 *
 * 修改备注: 
 *
 * 版本:      v1.0 
 *
 *===============================================
 */

import android.os.Environment;

import com.ecar.ecarnetfream.login.entity.ResLogin;
import com.ecar.ecarnetfream.publics.network.api.ApiService;
import com.ecar.ecarnetfream.publics.util.MD5Util;
import com.ecar.ecarnetfream.publics.util.TagUtil;
import com.ecar.ecarnetwork.bean.ResBase;
import com.ecar.ecarnetwork.http.api.ApiBox;
import com.ecar.ecarnetwork.http.exception.UserException;
import com.ecar.ecarnetwork.util.major.Major;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;


public class Datacenter {

    public static Datacenter datacenter;
    private static ApiService apiService;

    private Datacenter() {
    }

    public static synchronized Datacenter get() {
        if (datacenter == null) {
            datacenter = new Datacenter();
        }
        if (apiService == null) {
            apiService = ApiBox.getInstance().createService(ApiService.class, HttpUrl.Base_Url);
        }
        return datacenter;
    }

    public Observable<ResLogin> login(String mobileno, String pwd) {
        Observable<TreeMap<String, String>> treeMapObservable = Observable.create(new Observable.OnSubscribe<TreeMap<String, String>>() {
            @Override
            public void call(Subscriber<? super TreeMap<String, String>> subscriber) {
                TreeMap<String, String> tMap = Major.getTreeMapNoneKey();
                tMap.put(Major.PARAMS_USER_PHONE_NAME, mobileno);
                tMap.put("userPwd", MD5Util.to32Md5(pwd));
                tMap.put(Major.PARAMS_METHOD, "appLogin");
                /**
                 * 获得加密的sign TreeMap
                 */
                TreeMap<String, String> reMap = Major.securityKeyMethodEnc(tMap);
                subscriber.onNext(reMap);
            }
        });

        TreeMap<String, String> treeMap = treeMapObservable.toBlocking().first();

        /**
         * 代理类调用方法获取Observable
         */
        Observable<ResLogin> observable = apiService.login(treeMap);//ApiBox.getInstance().createService(ApiService.class, HttpUrl.Base_Url).login(reMap);
//        ApiBox.getInstance().cancleAllRequest();

        return observable;
    }

    public Observable<ResLogin> sign(String name, String pass) {
        return null;
    }

    public Observable<ResBase> uploadPic(String path) {
        // 添加参数到集合
        TreeMap<String, String> tMap = Major.getTreeMapByV();
        tMap.put(Major.PARAMS_METHOD, "upLoadImgForApp");
        tMap.put(Major.PARAMS_MODULE, "sys");
        tMap.put(Major.PARAMS_SERVICE, "File");
        tMap.put("img_type", "png");

        /**
         * 加密treeMap
         */
        TreeMap<String, String> reMap = Major.securityKeyMethodEnc(tMap);

        Map<String, RequestBody> partMap = new HashMap<>();
        //post 请求体
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/parking/camera1/1467362758050.png");
        if (file.exists()) {
            TagUtil.showLogError("file exists");
        } else {
            throw new UserException("文件不存在");
        }
        /**
         * 单个文件 （已成功）
         */
        RequestBody fileOne = RequestBody.create(MediaType.parse("image/png"), file);//"这里是模拟文件的内容"
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("content", "1467362758050.png", fileOne);
        return apiService.uploadPic(HttpUrl.Base_Url_upClient, reMap, filePart);
    }
}
