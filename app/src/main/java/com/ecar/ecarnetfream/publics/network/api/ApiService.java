package com.ecar.ecarnetfream.publics.network.api;


import com.ecar.ecarnetfream.login.entity.ResLogin;
import com.ecar.ecarnetwork.bean.ResBase;

import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


/**
 * ===============================================
 * <p>
 * 类描述: retrofit 网络请求示例类
 * <p>
 * 创建人:   happy
 * <p>
 * 创建时间: 2016/6/22 0022 上午 10:29
 * <p>
 * 修改人:   happy
 * <p>
 * 修改时间: 2016/6/22 0022 上午 10:29
 * <p>
 * 修改备注:
 * <p>
 * ===============================================
 */
public interface ApiService {

    @GET(".")
    Flowable<ResLogin> login(@QueryMap TreeMap<String, String> map);

    @GET
    Flowable<ResLogin> login(@Url String str);

    @Multipart
    @POST("")
    Flowable<ResBase> uploadPic(@Url String url, @QueryMap TreeMap<String, String> map, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(".")
    Flowable<ResBase> getResult(@FieldMap(encoded = true) Map<String, Object> params);

    @POST(".")
    Flowable<ResBase> getResult2(@Body RequestBody requestBody);

    @FormUrlEncoded
    @POST("?zipFlag=0")
    Flowable<ResBase> getResult3(@Query("commLen") String commLen, @Field(value = "jsonContent", encoded = true) String content);
}



