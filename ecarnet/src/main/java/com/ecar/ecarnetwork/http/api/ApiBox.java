package com.ecar.ecarnetwork.http.api;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.ecar.ecarnetwork.db.SettingPreferences;
import com.ecar.ecarnetwork.http.converter.ConverterFactory;
import com.ecar.ecarnetwork.http.util.ConstantsLib;
import com.ecar.ecarnetwork.http.util.HttpsUtils;
import com.ecar.ecarnetwork.http.util.NetWorkUtil;
import com.ecar.ecarnetwork.util.major.Major;
import com.ecar.ecarnetwork.util.time.TimeCalibrationInterceptor;
import com.ecar.util.TagUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * ===============================================
 * <p>
 * 类描述: retrofit 代理包装类
 * <p>
 * 创建人:   happy
 * <p>
 * 创建时间: 2016/6/22 0022 上午 10:42
 * <p>
 * 修改人:   happy
 * <p>
 * 修改时间: 2016/6/22 0022 上午 10:43
 * <p>
 * 修改备注: BaseUrl 、Debug 、应用上下文需要作为参数注入
 * <p>
 * ===============================================
 */
public class ApiBox {
    /**
     *  ApiBox.Builder builder = new ApiBox.Builder();
     builder.veriNgis(false);
     builder.application(this).debug(Constants.DEBUG).reqKey(Constants.REQUEST_KEY).appId(Constants.APP_ID);
     REQUEST_KEY初始化是reqKey(Constants.REQUEST_KEY)传递过来的
     */
    public static   String REQUEST_KEY = "";   //缓存目录名称
    //可能有多个代理类，每个代理类要求的时间不一样
    private int CONNECT_TIME_OUT = 10 * 1000;//跟服务器连接超时时间
    private int READ_TIME_OUT = 10 * 1000;    // 数据读取超时时间
    private int WRITE_TIME_OUT = 10 * 1000;   //数据写入超时时间
    private static final String CACHE_NAME = "cache";   //缓存目录名称
    /**
     * Log 日志开关 发布版本设为false
     */
    private boolean DEBUG = false;
    private boolean VeriNgis = true;

    public boolean isDEBUG() {
        return DEBUG;
    }

    public void setDEBUG(boolean DEBUG) {
        this.DEBUG = DEBUG;
    }

    public boolean isVeriNgis() {
        return VeriNgis;
    }

    public void setVeriNgis(boolean veriNgis) {
        VeriNgis = veriNgis;
    }

    /**
     * 单例 持有引用
     */
    private final Gson gson;
    private final OkHttpClient okHttpClient;

    public Application application;//应用上下文(需注入参数)
    private File cacheFile;//缓存路径
    private final InputStream[] inputStreams;

    private Map<String, Object> serviceMap;


    /**
     * 在访问时创建单例
     */
    private static class SingletonHolder {
        private static ApiBox INSTANCE;// = new ApiBox();
    }

    /**
     * 获取单例
     */
    public static ApiBox getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 构造方法
     */
    private ApiBox(Builder builder) {
        //1.设置应用上下文、debug参数
        DEBUG = builder.debug;
        VeriNgis = builder.veriNgis;
        this.application = builder.application;
        this.cacheFile = builder.cacheDir;
        this.inputStreams = builder.inputStreams;
        SettingPreferences sp = SettingPreferences.getDefault(application);
        sp.setReqkey(builder.reqKey);
        this.serviceMap = new HashMap<>();
        if (builder.connetTimeOut > 0) {
            this.CONNECT_TIME_OUT = builder.connetTimeOut;
        }
        if (builder.readTimeOut > 0) {
            this.READ_TIME_OUT = builder.readTimeOut;
        }

        if (builder.writeTimeOut > 0) {
            this.WRITE_TIME_OUT = builder.writeTimeOut;
        }

        //2.gson
        gson = getReponseGson();

        //3.okhttp
        okHttpClient = getClient();
    }

    /**
     * 提供一个动态创建代理的 方法。
     */
    public <T> T createService(Class<T> serviceClass, String baseUrl) {
        //4.创建retrofit. 3.1 请求客户端 3.2 GsonAdpter转换类型 3.3 支持Rxjava 3.4.基url

        //1.缓存中获取
        if (TextUtils.isEmpty(baseUrl)) {
            baseUrl = "";
        }
        Object serviceObj = serviceMap.get(serviceClass.getName() + baseUrl);
        if (serviceObj != null) {
            return (T) serviceObj;
        }

        //2.创建
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(ConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        T service = retrofit.create(serviceClass);
        serviceMap.put(serviceClass.getName() + baseUrl, service);
        return service;
    }

    /**
     * Builder
     */
    public static final class Builder {
        private Application application;//应用上下文(需注入参数)
        private File cacheDir;//缓存路径
        private boolean debug;
        private String appId;

        private String reqKey;
        private int connetTimeOut;
        private int readTimeOut;
        private int writeTimeOut;
        private boolean veriNgis = true;
        private InputStream[] inputStreams;

        public Builder application(Application application) {
            this.application = application;
            this.cacheDir = new File(application.getCacheDir(), CACHE_NAME);
            return this;
        }

        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder veriNgis(boolean veriNgis) {
            this.veriNgis = veriNgis;//设置绕过参数
            return this;
        }

        public Builder reqKey(String reqKey) {
            this.reqKey = reqKey;
            REQUEST_KEY = reqKey;
            SettingPreferences sp = SettingPreferences.getDefault(application);
            if(!sp.getReqkey().equals("")){
                REQUEST_KEY= sp.getReqkey();
            }
            return this;
        }

        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder connetTimeOut(int connetTime) {
            this.connetTimeOut = connetTime;
            return this;
        }

        public Builder readTimeOut(int readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        public Builder writeTimeOut(int writeTimeOut) {
            this.writeTimeOut = writeTimeOut;
            return this;
        }

        public Builder inputStreams(InputStream[] inputStreams) {
            this.inputStreams = inputStreams;
            return this;
        }

        public ApiBox build() {
            if (SingletonHolder.INSTANCE == null) {
                ApiBox apiBox = new ApiBox(this);
                SingletonHolder.INSTANCE = apiBox;
            } else {
                SingletonHolder.INSTANCE.application = this.application;
                SingletonHolder.INSTANCE.DEBUG = this.debug;
                SingletonHolder.INSTANCE.VeriNgis = this.veriNgis;
                SettingPreferences sp = SettingPreferences.getDefault(application);
                sp.setReqkey(this.reqKey);
                TagUtil.IS_SHOW_LOG = this.debug;
            }
            SettingPreferences sp = SettingPreferences.getDefault(application);
            sp.setReqkey(this.reqKey);
            sp.setAppId(Major.eUtil.binstrToStr(TextUtils.isEmpty(appId) ? "" : appId));

            return SingletonHolder.INSTANCE;
        }

    }

    /**
     * 创建ok客户端
     */
    private OkHttpClient getClient() {
        //1. 设置打印log
//        HttpLoggingInterceptor interceptor = getLogInterceptor();

        //2.支持https
//        SSLSocketFactory sslSocketFactory = HttpsUtils.getSslSocketFactory(null, null, null);
        HostnameVerifier hostnameVerifier = HttpsUtils.getHostnameVerifier();
        // 如果使用到HTTPS，我们需要创建SSLSocketFactory，并设置到client
        SSLSocketFactory sslSocketFactory = HttpsUtils.getSslFactory();
        if (inputStreams != null) {
//            InputStream inputStream = new Buffer().writeUtf8(saasKey).inputStream();
            sslSocketFactory = HttpsUtils.setCertificates(inputStreams);
        }


        //3.缓存
        Cache cache = getReponseCache();

        //4.配置创建okhttp客户端
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(getLogInterceptor())//
                .addInterceptor(getTimeIntercepter())
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS) //与服务器连接超时时间
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)//路由等失败自动重连
                .sslSocketFactory(sslSocketFactory)//https 绕过验证
                .hostnameVerifier(hostnameVerifier)
//                .cache(cache)//缓存
//                .addNetworkInterceptor(new HttpCacheInterceptor())//
//                .cookieJar()//cookie
                ;
//        builder.interceptors().add(interceptor);
//        if(HttpsUtils.isLowVersion()){
//            okHttpClientBuilder = HttpsUtils.enableTls12OnPreLollipop(okHttpClientBuilder);
//        }else {
//            okHttpClientBuilder.sslSocketFactory(HttpsUtils.getSslFactory());
//        }
        return okHttpClientBuilder.build();
    }

    /**
     * 设置打印log
     * 开发模式记录整个body，否则只记录基本信息如返回200，http协议版本等
     *
     * @return
     */
    private HttpLoggingInterceptor getLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        return interceptor;
    }

    private Interceptor getTimeIntercepter(){
        return new TimeCalibrationInterceptor();
    }

    /**
     * 缓存路径
     *
     * @return
     */
    private Cache getReponseCache() {
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        return cache;
    }

    /**
     * 配置gson对象
     */
    private Gson getReponseGson() {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();
        return gson;
    }

    /**
     * 方法描述：取消所有请求
     * <p>
     */
    public void cancleAllRequest() {
        okHttpClient.dispatcher().cancelAll();
    }

    /**
     * 缓存拦截器
     */
    private Interceptor cacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtil.isNetConnected(application)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .url(path)
                        .build();
            }

            Response response = chain.proceed(request);
            if (NetWorkUtil.isNetConnected(application)) {//有网保持一小时
                int maxAge = 60 * 60; // read from cache for 1 minute
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {//无网保持四周
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    };


    public    void  resetPreferencesReqkey(Context context,String ReqkeyFromNet){
        SettingPreferences sp = SettingPreferences.getDefault(context);
        sp.setReqkey(ReqkeyFromNet);
    }
}