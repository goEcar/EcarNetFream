package com.ecar.ecarnetwork.util.time;

import android.text.TextUtils;

import java.io.IOException;
import java.util.Date;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.HttpDate;

/**
 * 2. 利用OkHttp的Interceptor自动同步时间（不懂Interceptor？）
 * 网络响应头包含Date字段（世界时间）
 * 利用Interceptor记录每次请求响应时间，如果本次网络操作的时间小于上一次网络操作的时间，则获取Date字段，转换时区后更新本地TimeManager。
 * 这样时间就只会越来越精确了
 * 3. 此方案的不足之处
 * 连接服务器的过程是需要时间的，服务器收到请求时刻的时间与应用收到响应存在一定的时间差，导致误差的存在（误差=服务器发出响应->到本机收到响应这个时间）。通过上面的TimeCalibrationInterceptor每次判断，可以使得误差逐渐降低
 * 考虑到机器重启的原因，所以时间没有持久化
 */
public class TimeCalibrationInterceptor implements Interceptor {
    long minResponseTime = Long.MAX_VALUE;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.nanoTime();
        Response response = chain.proceed(request);
        long responseTime = System.nanoTime() - startTime;

        Headers headers = response.headers();
        calibration(responseTime, headers);
        return response;
    }

    private void calibration(long responseTime, Headers headers) {
        if (headers == null) {
            return;
        }

        //如果这一次的请求响应时间小于上一次，则更新本地维护的时间
        if (responseTime >= minResponseTime) {
            return;
        }

        String standardTime = headers.get("Date");
        if (!TextUtils.isEmpty(standardTime)) {
            Date parse = HttpDate.parse(standardTime);
            if (parse != null) {
                // 客户端请求过程一般大于比收到响应时间耗时，所以没有简单的除2 加上去，而是直接用该时间
                TimeManager.getInstance().initServerTime(parse.getTime());
                minResponseTime = responseTime;
            }
        }
    }
}