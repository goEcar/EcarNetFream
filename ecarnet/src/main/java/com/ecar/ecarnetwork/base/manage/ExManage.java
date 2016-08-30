package com.ecar.ecarnetwork.base.manage;

import android.content.Context;
import android.net.ParseException;
import android.text.TextUtils;

import com.ecar.ecarnetwork.base.BaseSubscriber;
import com.ecar.ecarnetwork.http.exception.CommonException;
import com.ecar.ecarnetwork.http.exception.InvalidException;
import com.ecar.ecarnetwork.http.exception.UserException;
import com.ecar.ecarnetwork.http.util.ConstantsLib;
import com.ecar.ecarnetwork.http.util.TagLibUtil;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * ===============================================
 * <p>
 * 类描述: 这个类不能直接在 订阅者基类中处理，子类无法实现自定义的onUserError方法
 *         若subscribe(Action1,Action1) 注入的是这种 参数形式的话，第二个参数对象 对Error 的处理可以考虑使用此类，还需暴露 可以复写onUserError方法
 * <p>
 * 创建人:   happy
 * <p>
 * 创建时间: 2016/7/7 0007 下午 16:03
 * <p>
 * 修改人:   happy
 * <p>
 * 修改时间: 2016/7/7 0007 下午 16:03
 * <p>
 * 修改备注:
 * <p>
 * ===============================================
 */
public class ExManage {

    private Context context;//处理强制登录需要
    public ExManage(Context context) {
        this.context = context;
    }

    public void onError(Throwable e) {
        CommonException ex = null;
        if (e instanceof UserException) {   // 用户自定义需要处理的异常
            /**
             * 1.自定义异常处理
             */
            UserException resultException = (UserException) e;
            ex = new CommonException(e, resultException.getCode(),resultException.getMsg());
            onUserError(ex);
        }else if(e instanceof InvalidException){
            /**
             * 2.非法异常处理：2.1 强制重新登录 2.2 校验错误 2.3so on
             */
            InvalidException commonException = (InvalidException) e;
            if(InvalidException.FLAG_ERROR_RELOGIN.equals(commonException.getCode())){//重新登录
                showDialog(context,commonException.getMsg());
            }else if(InvalidException.FLAG_ERROR_RESPONCE_CHECK.equals(commonException.getCode())){//校验错误
                showMsg(context,commonException.getMsg());
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            /**
             * 2.解析错误
             */
            ex = new CommonException(e, CommonException.FLAG_PARSE_ERROR);
            onUnifiedError(ex);   //
        } else if (e instanceof HttpException
                || e instanceof ConnectException
                || e instanceof SocketTimeoutException
                || e instanceof SocketException) {
            /**
             * 3.网络错误
             */
            ex = new CommonException(e, CommonException.FLAG_NET_ERROR);
            onUnifiedError(ex);
        }
        else {
            /**
             * 4.未知错误
             */
            ex = new CommonException(e, CommonException.FLAG_UNKNOWN);
            onUnifiedError(ex);   //未知错误
        }
        resetContext();
        if(ConstantsLib.DEBUG && ex!=null){
            String errorMsg = ex.getMessage().toString();
            TagLibUtil.showLogError(BaseSubscriber.class.getSimpleName() + ": " + errorMsg);
            //bug写入日志
//            FileUtil.writeDebugTextFile(errorMsg);//不能简单的这么写，要考虑弄线程处理
        }
    }

    public void onCompleted() {
        resetContext();
    }

    //    protected abstract void onError(ApiException ex);
//    protected abstract void onResult1Error(ApiException ex);

    /**
     * 统一的 默认处理
     * 默认自动处理网络、解析、未知异常；弹提示
     * 有需要子类可以重写，不走super.onError 就可以屏蔽掉弹提示
     * @param ex
     */
    protected void onUnifiedError(CommonException ex){
        showMsg(context,ex.getMsg());
    }

    /**
     * 自定义处理：
     * 接口返回 state 失败、自定义处理返回失败
     * 需要处理时 子类重写
     */
    protected void onUserError(CommonException ex){
        showMsg(context,ex.getMsg());
    }


    /**
     * 上下文置空
     */
    private void resetContext(){
        context = null;
    }

    private static void showMsg(Context context,String msg){
        if(context!=null){
            String reMsg = TextUtils.isEmpty(msg) ? "" : msg;
            TagLibUtil.showToast(context, reMsg);
        }else{
            TagLibUtil.showLogDebug("Subscriber 上下文为空");
        }
    }

    /**
     *
     * @param msg
     */
    private static void showDialog(final Context context,String msg) {
//        if (context != null) {
//            TagUtil.showLogDebug("显示登出dialog" + (context instanceof Activity));
//            final UpdateDialog dialog = new UpdateDialog(context);
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
//            dialog.setProgressMsg(msg);
//            dialog.hideCustomBtn1();
//            dialog.setProgressMsgDes("");
//            dialog.findViewById(R.id.prompt_sub).setBackgroundResource(
//                    R.drawable.pub_btn_submit_selector);
//            dialog.setButton2OnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
////                    Intent intent = new Intent(context, UpdateService.class);
////                    context.stopService(intent);// 关闭服务
//                    App.getInstance().quit();
//                    SettingPreferences.getDefault(context).clearUserMsg();
//                    context.startActivity(new Intent(context,
//                            LoginActivity.class));
//                    dialog.dismiss();
//                }
//            }, "确定", R.drawable.btn_yellow_selector);
//
//        } else {
//            TagUtil.showLogDebug("Subscriber 上下文为空");
//        }
    }
}
