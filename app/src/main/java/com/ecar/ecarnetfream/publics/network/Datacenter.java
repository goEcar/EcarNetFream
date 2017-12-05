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

import org.reactivestreams.Subscriber;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;



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

//    public Observable<ResLogin> login(String mobileno, String pwd) {
//        Observable<TreeMap<String, String>> treeMapObservable = Observable.create(new ObservableOnSubscribe<TreeMap<String, String>>() {
//            @Override
//            public void subscribe(ObservableEmitter<TreeMap<String, String>> subscriber) throws Exception {
//                TreeMap<String, String> tMap = Major.getTreeMapNoneKey();
//                tMap.put(Major.PARAMS_USER_PHONE_NAME, mobileno);
//                tMap.put("userPwd", MD5Util.to32Md5(pwd));
//                tMap.put(Major.PARAMS_METHOD, "appLogin");
//                /**
//                 * 获得加密的sign TreeMap
//                 */
//                TreeMap<String, String> reMap = Major.securityKeyMethodEnc(tMap);
//                subscriber.onNext(reMap);
//            }
//        });


    public Flowable<ResLogin> login(String mobileno, String pwd) {
        Flowable<TreeMap<String, String>> treeMapObservable = Flowable.create(new FlowableOnSubscribe<TreeMap<String, String>>() {
            @Override
            public void subscribe(FlowableEmitter<TreeMap<String, String>> subscriber) throws Exception {
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
        }, BackpressureStrategy.BUFFER);


//        Observable<TreeMap<String, String>> treeMapObservable = Observable.create(new Observable.OnSubscribe<TreeMap<String, String>>() {
//            @Override
//            public void call(Subscriber<? super TreeMap<String, String>> subscriber) {
//                TreeMap<String, String> tMap = Major.getTreeMapNoneKey();
//                tMap.put(Major.PARAMS_USER_PHONE_NAME, mobileno);
//                tMap.put("userPwd", MD5Util.to32Md5(pwd));
//                tMap.put(Major.PARAMS_METHOD, "appLogin");
//                /**
//                 * 获得加密的sign TreeMap
//                 */
//                TreeMap<String, String> reMap = Major.securityKeyMethodEnc(tMap);
//                subscriber.onNext(reMap);
//            }
//        });

//        TreeMap<String, String> treeMap = treeMapObservable.toBlocking().first();
        TreeMap<String, String> treeMap = treeMapObservable.blockingFirst();

        /**
         * 代理类调用方法获取Observable
         */
        Flowable<ResLogin> observable = apiService.login(treeMap);//ApiBox.getInstance().createService(ApiService.class, HttpUrl.Base_Url).login(reMap);
//        ApiBox.getInstance().cancleAllRequest();
//        Observable<ResLogin> observable = apiService.login("https://218.17.99.52:1322/memberapi/Index.aspx?versontype=1&method=login&mobileno=18670006357&pwd=dc483e80a7a0bd9ef71d8cf973673924&ostype=android_4.3&phonetype=X9007&appversion=1.2.6&appkey=101280918&security=0d7c4e48454c3f94e310686fe6772dee&mobilecode=865568026165332&timestamp=20171124091129&sign=0de90d7cc480bbf449f6df4d943e8d25");//ApiBox.getInstance().createService(ApiService.class, HttpUrl.Base_Url).login(reMap);

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

    public Flowable<ResLogin> testTJ() {
        // 添加参数到集合
        TreeMap<String, String> tMap = new TreeMap<>();
        tMap.put("versontype", "1");
        tMap.put("method", "haspaypwdandbank");
        tMap.put("parkuserid", "17061213510128148");
        tMap.put("banktype", "00");
        tMap.put("appkey", "101280918");
        tMap.put("security", "3341810880d39411bbfff3a6b25b6618");
        tMap.put("mobilecode", "866657025103524");
        tMap.put("timestamp", "20171114103847");
        tMap.put("SID", "sid6eb141fe937f79dfbab5ae595a9129af");
        tMap.put("sign", "e65e2a2524b0a74d55b7d7d10f1e6563");
        Flowable<ResLogin> observable = apiService.login(tMap);//ApiBox.getInstance().createService(ApiService.class, HttpUrl.Base_Url).login(reMap);
        return observable;
    }

    public Flowable<ResLogin> testSaas() {
        // 添加参数到集合
        TreeMap<String, String> tMap = new TreeMap<>();
        tMap.put("ClientType", "android");
        tMap.put("appId", "005424930");
        tMap.put("comid", "200000005");
        tMap.put("method", "getMebParkStatus");
        tMap.put("module", "app");
        tMap.put("service", "Std");
        tMap.put("sign", "75ce6ae732307c8d0dd424f9c3c48d8d");
        tMap.put("ts", "1510654906647");
        tMap.put("u", "20160623105229613269811315537012");
        tMap.put("v", "20171114182146529370876523953850");
        tMap.put("ve", "2");
        Flowable<ResLogin> observable = apiService.login(tMap);//ApiBox.getInstance().createService(ApiService.class, HttpUrl.Base_Url).login(reMap);
        return observable;
    }
}
