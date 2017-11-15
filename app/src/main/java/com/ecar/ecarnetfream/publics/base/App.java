package com.ecar.ecarnetfream.publics.base;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;


import com.ecar.ecarnetfream.publics.util.Constants;
import com.ecar.ecarnetwork.http.api.ApiBox;
import com.ecar.ecarnetwork.util.file.EFileUtil;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * ===============================================
 * <p>
 * 类描述:
 * <p>
 * 创建人:   happy
 * <p>
 * 创建时间: 2016/6/1 0001 上午 9:35
 * <p>
 * 修改人:   happy
 * <p>
 * 修改时间: 2016/6/1 0001 上午 9:35
 * <p>
 * 修改备注:
 * <p>
 * ===============================================
 */
public class App extends Application{
    private String CER_12306 = "-----BEGIN CERTIFICATE-----\n" +
            "MIICmjCCAgOgAwIBAgIIbyZr5/jKH6QwDQYJKoZIhvcNAQEFBQAwRzELMAkGA1UEBhMCQ04xKTAn\n" +
            "BgNVBAoTIFNpbm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMB4X\n" +
            "DTA5MDUyNTA2NTYwMFoXDTI5MDUyMDA2NTYwMFowRzELMAkGA1UEBhMCQ04xKTAnBgNVBAoTIFNp\n" +
            "bm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMIGfMA0GCSqGSIb3\n" +
            "DQEBAQUAA4GNADCBiQKBgQDMpbNeb34p0GvLkZ6t72/OOba4mX2K/eZRWFfnuk8e5jKDH+9BgCb2\n" +
            "9bSotqPqTbxXWPxIOz8EjyUO3bfR5pQ8ovNTOlks2rS5BdMhoi4sUjCKi5ELiqtyww/XgY5iFqv6\n" +
            "D4Pw9QvOUcdRVSbPWo1DwMmH75It6pk/rARIFHEjWwIDAQABo4GOMIGLMB8GA1UdIwQYMBaAFHle\n" +
            "tne34lKDQ+3HUYhMY4UsAENYMAwGA1UdEwQFMAMBAf8wLgYDVR0fBCcwJTAjoCGgH4YdaHR0cDov\n" +
            "LzE5Mi4xNjguOS4xNDkvY3JsMS5jcmwwCwYDVR0PBAQDAgH+MB0GA1UdDgQWBBR5XrZ3t+JSg0Pt\n" +
            "x1GITGOFLABDWDANBgkqhkiG9w0BAQUFAAOBgQDGrAm2U/of1LbOnG2bnnQtgcVaBXiVJF8LKPaV\n" +
            "23XQ96HU8xfgSZMJS6U00WHAI7zp0q208RSUft9wDq9ee///VOhzR6Tebg9QfyPSohkBrhXQenvQ\n" +
            "og555S+C3eJAAVeNCTeMS3N/M5hzBRJAoffn3qoYdAO1Q8bTguOi+2849A==\n" +
            "-----END CERTIFICATE-----";

    private String testKey = "-----BEGIN CERTIFICATE-----\n" +
            "MIIC4TCCAcmgAwIBAgIQEpLlheFrLI1A0J1gz7/0vDANBgkqhkiG9w0BAQUFADAYMRYwFAYDVQQD\n" +
            "Ew1XTVN2Yy1TRVJWRVJCMB4XDTE0MDUwNTA2MjIxM1oXDTI0MDUwMjA2MjIxM1owGDEWMBQGA1UE\n" +
            "AxMNV01TdmMtU0VSVkVSQjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAL1vYpDFlhsQ\n" +
            "TMdMNEnWl6V8rw8yEwuFoaEZnBtcL65l8SgR7vXhbyHKMxi9YUzDK897XhLP2Vi4012OvhuE4awa\n" +
            "fbN2Bbn7ndPA4pzW5vbLd4IoD3bVwNEfrAAwy4Et0suo95jIHrqLOQBfX7g89UIsp9jLo1THWuxj\n" +
            "SWDsj237qoIyTV4Guk4VNJfzZt2Zi15BUl7TCCUsVN54O2IhGLsQB+mfUVT+ilw280u3Z8Egv2bf\n" +
            "9oliCaZxO8vrqKN/N3RhUtnO4crHIDeEG99FEVqV6PfrdkCQ6IP1gpcHjYem5GcLLZ0t5GIQooTX\n" +
            "qic2lX7B+KyqHWUoei9leorwp2UCAwEAAaMnMCUwEwYDVR0lBAwwCgYIKwYBBQUHAwEwDgYDVR0P\n" +
            "BAcDBQCwAAAAMA0GCSqGSIb3DQEBBQUAA4IBAQBxGYzLfnNTJgeB3gaPlMmOEtGAtyiHRa0w1YDP\n" +
            "vDgj6tuU5RMRDPQ4gadRrJGT/mHAQig6ViEwsG+3BTMQE8oaK8bbaNp+1eBwL/kr1BKYgDeUvqZ8\n" +
            "aBYGoLT+T8Nj1zDeKh1XOtj5wWvTmjWgQWSeHlHO8LnNhvqwPQIxRnkZhObxLOdsreVSoSnjy/i2\n" +
            "qTf85oJQrf2+zaDAJs5PBiOX9VeE07PA7vz3ol9CJXqLD3WbhUlhODdv9xvWs1/yjOVnhu7v+J02\n" +
            "F7vPNJ1+u559MaMKocRJiZXQQagcENOTq+ydUVVvq+amcwixbC4elYfDvXxD2ACKQYNyVCiq2bXl\n" +
            "-----END CERTIFICATE-----";

    private String saasKey = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDlDCCAnwCCQCIihzwQnzB3jANBgkqhkiG9w0BAQUFADCBizELMAkGA1UEBhMCY24xDjAMBgNV\n" +
            "BAgMBWNoaW5hMREwDwYDVQQHDAhzaGVuemhlbjEcMBoGA1UECgwTRGVmYXVsdCBDb21wYW55IEx0\n" +
            "ZDEZMBcGA1UEAwwQKi5lY2FyYXlwYXJrLmNvbTEgMB4GCSqGSIb3DQEJARYRd3VsaW5nQGVjYXJh\n" +
            "eS5jb20wHhcNMTcxMTE0MDk1ODEzWhcNMjcxMTEyMDk1ODEzWjCBizELMAkGA1UEBhMCQ04xDjAM\n" +
            "BgNVBAgMBWNoaW5hMREwDwYDVQQHDAhzaGVuemhlbjEcMBoGA1UECgwTRGVmYXVsdCBDb21wYW55\n" +
            "IEx0ZDEZMBcGA1UEAwwQKi5lY2FyYXlwYXJrLmNvbTEgMB4GCSqGSIb3DQEJARYRd3VsaW5nQGVj\n" +
            "YXJheS5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC8lmgIUZqcPoTd8dUvTsvB\n" +
            "g1SFmlp35szeIUB1xiH4kLEtq5BwMrGlewrK3YzMLFzlWMvMUB4v/GJNkgkYKbMZ/0tsJKYzaZKj\n" +
            "d6/a6Bm8wUf8T+Dc1IxoAjHpfzILpniTiLcqC2YR/np/Ouaz5DJ7iTHnv1ESGDjuT9PVedTdajQb\n" +
            "yJNfAx5eNwI6zqz/HYaC+xHuT4JWoKgyocqSuAtnwKenGHo3Juw7Wt6HAiKW1lWlos1UKHMCC7C9\n" +
            "vBKYHEOEuftSOCZSGU96xrFclBv/YLq4ZjRjkMu2sNF6VgGdNfuILRo9rGyTP/DZwCKz9JvFKs4O\n" +
            "E7HUqdq2Q+pk/T9pAgMBAAEwDQYJKoZIhvcNAQEFBQADggEBAAwzNt+u8elzPZMDmW/Q3BS1pG6Y\n" +
            "CN8Pb7LCBvALxfDVlUevoHpwCokewCmvH2jlp7dlZYF1BK5Pfch+VmTk3PxXCVm8c0gJaGA7AOkz\n" +
            "wM0gSmJ6CwFTN90U6ibSLa/j/cOYaf0VlIZ4whq9ZHJfN6VqVtCjDJ8csMdNxie2KRFtQzO0AL6L\n" +
            "olxnBitvQ0qlqoM3fcOPAaNLGhkXsrDMvs6fTuH+tBVaR2JJCOYP0ObCeZOiNEst8d+5/tIGZ90D\n" +
            "ZA3Ji0u+WX89XQwGvmNJA3hrjZ08uy8adsCewvRbfmn5rIoVVpoXUinCblIIq2m7L6fnUB9Z1wtK\n" +
            "TVAk4efaOB8=\n" +
            "-----END CERTIFICATE-----";

    private static App mApp;
    private List<Activity> activityList = new LinkedList<Activity>();
    private List<Activity> payActivityList = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        ApiBox.Builder builder = new ApiBox.Builder();
        builder.application(this).debug(Constants.DEBUG).reqKey(Constants.REQUEST_KEY).appId(
//                "110100 110011 1100110 110110 1100100 110000 110100 1100110 110010 110000 110111 110000 1100100 1100010 110110 111000 1100100 110001 110010 110101 110100 111000 110110 110011 1100110 110011 111001 110001 111000 110110 110110 111001"
                ""
        );
//        //添加证书 方式1：使用转换后的字符串去生成流； 转换方式C:\Users\Administrator>keytool -printcert -rfc -file C:\Users\Administrator\Desktop\1.cer
//        InputStream[] inputStreams = EFileUtil.getInputStreams(saasKey);
//        builder.inputStreams(inputStreams).build();
//        //添加证书 方式1：使用资产目录下的证书文件
        InputStream[] inputStreams = EFileUtil.getAssetsInputStream(this,"ecaraypark.cer");//在资产目录下的证书文件名
        builder.inputStreams(inputStreams).build();

    }

    public static App getInstance() {
        return mApp;
    }
    public static Resources getAppResources() {
        return mApp.getResources();
    }


    /**
     * 添加Activity 到容器中
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if(activity!=null){
            activityList.add(activity);
        }
    }

    /**
     * 移除
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            if(activityList.contains(activity)) {
                activityList.remove(activity);
            }
        }
    }

    /**
     * 添加支付activity到容器
     */
    public void addPayActivity(Activity activity) {
        if (activity != null) {
            payActivityList.add(activity);
        }
    }

    /**
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if(activityList.contains(activity)) {
                activityList.remove(activity);
                activity.finish();
            }
        }
    }

    /**
     * 遍历所有Activity 并finish
     */
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }

    /**
     * 遍历所有Activity 并quit
     */
    public void quit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    /**
     * 遍历所有Activity 并quit
     */
    public void quitPayActivity() {
        for (Activity activity : payActivityList) {
            activity.finish();
        }
    }


    /**
     * 获取Activity 数量
     *
     * @param
     */
    public int getActivityCount() {
        if(activityList !=null){
            return activityList.size();
        }
        return 0;
    }

    /*
     * 功能:结束指定类名的Activity。 其位置栈上面的其他activity
     */
    public void finishBeforeActivity(Class<?> cls) {
        int index = -1;
        int size = activityList.size();
        for (int i = size - 1; i >= 0; i--) {
            if (activityList.get(i).getClass().equals(cls)) {
//				index = activityList.indexOf(activity);
                index = i;
                break;
            }
        }
        if (index != -1) {
            for (int i = size - 1; i > index; i--) {
                finishActivity(activityList.get(i));
            }
        }

    }

    /*
     * 功能:找到最靠近栈顶的Activity
     */
    public Activity getTopActivity(Class<?> cls) {
        int index = -1;
        int size = activityList.size();
        for (int i = size - 1; i >= 0; i--) {
            if (activityList.get(i).getClass().equals(cls)) {
//				index = .indexOf(activity);
                index = i;
                return activityList.get(i);
            }
        }
        return null;
    }


    /**
     * 功能:设置当前Activity（堆栈中最后一个压入的）
     */
    public Activity setCurrentActivity(Activity activity) {
        boolean isContains = activityList.contains(activity);
        if (isContains) {
            activityList.remove(activity);
        }
        activityList.add(activity);
        return activity;
    }



}
