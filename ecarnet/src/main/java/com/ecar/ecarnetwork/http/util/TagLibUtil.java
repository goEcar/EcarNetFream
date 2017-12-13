package com.ecar.ecarnetwork.http.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ecar.ecarnetwork.http.api.ApiBox;


public class TagLibUtil {
    private final static String TAG = "TagLibUtil";

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示debug 数据
     *
     * @param str
     */
    public static void showLogDebug(String str) {
        if (ApiBox.getInstance().isDEBUG()) {
            Log.d(TAG, str);
        }
    }

    /**
     * 显示debug 数据
     *
     * @param str
     */
    public static void showLogDebug(Class context, String str) {
        if (ApiBox.getInstance().isDEBUG()) {
            Log.d(TAG, "<" + context.getName().toString() + ">--" + str);
        }
    }

    public static void showLogError(String str) {
        if (ApiBox.getInstance().isDEBUG()) {
            Log.e(TAG, str);
        }
    }

    /**
     * 显示debug 数据
     *
     * @param str
     */
    public static void showLogError(Class context, String str) {
        if (ApiBox.getInstance().isDEBUG()) {
            Log.e(TAG, "<" + context.getName().toString() + ">--" + str);
        }
    }

    public static void showLogDebug(String tag, String content) {
        if (ApiBox.getInstance().isDEBUG()) {
            Log.d(tag, content);
        }
    }

}
