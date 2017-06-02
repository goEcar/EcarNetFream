package com.ecar.ecarnetfream.login.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.ecar.ecarnetfream.R;
import com.ecar.ecarnetfream.login.interfaces.LoginContract;
import com.ecar.ecarnetfream.login.model.LoginModel;
import com.ecar.ecarnetfream.login.presenter.LoginPresenter;
import com.ecar.ecarnetfream.publics.base.App;
import com.ecar.ecarnetfream.publics.base.BaseActivity;
import com.ecar.ecarnetfream.publics.util.TagUtil;
import com.ecar.ecarnetfream.publics.view.prompt.UpdateDialog;
import com.ecar.ecarnetwork.bean.ResBase;
import com.ecar.ecarnetwork.db.SettingPreferences;
import com.ecar.factory.EncryptionUtilFactory;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/1/14.
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {


    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.btn_submit)
    TextView btnSubmit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initPresenter() {
        presenter = new LoginPresenter(context, this, new LoginModel());
    }


    @Override
    public void loginSuccess(ResBase resBase) {
        showMsg(resBase.msg);
    }

    @Override
    public void showMsg(String msg) {
        TagUtil.showToast(this, msg);
    }


    @OnClick(R.id.btn_submit)
    public void onClick() {
//        TagUtil.showToast(this,"click");
        presenter.login(etName.getText().toString(), etPwd.getText().toString());
        isEquels("getSecurityMapKeys",
                "{ClientType=android, appId=904075102, appcode=4, appname=roadapp, comid=200000002, method=checkForUpdate, module=app, service=Std, sign=afb9350413ed213e1fc64eb299cd8527, ve=2, versionCode=1.1.4-%E5%BC%80%E5%8F%91}",
                EncryptionUtilFactory.getDefault(true).createEpark().getSecurityMapKeys(
//                        "{ClientType=android, appId=904075102, appcode=4, appname=roadapp, comid=200000002, method=checkForUpdate, module=app, requestKey=D3029C73406221B02026B684BB00579C, service=Std, ve=2, versionCode=1.1.4-开发}",
//                        "{ClientType=android, applyduration=30, applytype=1, berthcode=B100002, channel=1, comid=200000018, method=prepaymentParkingPay, module=app, paytype=4, price=0.81, service=Std, t=7196546423623734915703, ts=1496397719654, u=20170504114006093960886040500491, vehicletype=2}",
                        "{ClientType=android, berthcode=B100002, comid=200000018, method=prepaidParkTime, module=app, orderid=, parktime=30, service=Std, ts=1496401739985, u=20170504114006093960886040500491, v=20170602185905681582624078867184, vehicletype=1}",
                        false,
                        true,
                        true,
                        "510832011",
                        "510adc3949ba59abbe56e057f20f883a"
                ));
    }

    @Override
    public void reLogin(Context context, String msg) {
        if (context != null) {
            TagUtil.showLogDebug("显示登出dialog" + (context instanceof Activity));
            final UpdateDialog dialog = new UpdateDialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            dialog.setProgressMsg(msg);
            dialog.hideCustomBtn1();
            dialog.setProgressMsgDes("");
            dialog.findViewById(R.id.prompt_sub).setBackgroundResource(
                    R.drawable.pub_btn_submit_selector);
            dialog.setButton2OnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, UpdateService.class);
//                    context.stopService(intent);// 关闭服务
                    App.getInstance().quit();
                    SettingPreferences.getDefault(context).clearUserMsg();
                    context.startActivity(new Intent(context,
                            LoginActivity.class));
                    dialog.dismiss();
                }
            }, "确定", R.drawable.btn_yellow_selector);

        } else {
            TagUtil.showLogDebug("Subscriber 上下文为空");
        }

    }

    private void isEquels(String method, String str1, String str2) {
        Log.d("tagutil", method + "标准值=" + str1);
        Log.d("tagutil", method + "加密后=" + str2);

        if (!TextUtils.isEmpty(str1) && !TextUtils.isEmpty(str2)) {
            Log.d("tagutil", method + "  result: " + str1.trim().equals(str2.trim()));
        } else {
            Log.d("tagutil", method + "  result: " + false);
        }
    }
}
