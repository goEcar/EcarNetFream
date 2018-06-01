package com.ecar.ecarnetfream.login.presenter;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.ecar.ecarnetfream.login.entity.ResLogin;
import com.ecar.ecarnetfream.login.interfaces.LoginContract;
import com.ecar.ecarnetfream.publics.network.Datacenter;
import com.ecar.ecarnetfream.publics.util.TagUtil;
import com.ecar.ecarnetwork.base.BaseSubscriber;
import com.ecar.ecarnetwork.bean.ResBase;
import com.ecar.ecarnetwork.http.exception.CommonException;
import com.ecar.ecarnetwork.http.exception.InvalidException;
import com.ecar.ecarnetwork.http.exception.UserException;
import com.ecar.ecarnetwork.util.rx.RxUtils;

import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends LoginContract.Presenter {

    /**
     * 单元测试 采用依赖参数 构造时 一起注入，方便mockito
     *
     * @param context
     * @param view
     * @param model
     */
    public LoginPresenter(Activity context, LoginContract.View view, LoginContract.Model model) {
        super(context, view, model);
    }

    @Override
    public void login(String name, String pwd) {
        if (TextUtils.isEmpty(name) || name.length() < 11) {
            TagUtil.showToast(context, "账号或密码有误");
            return;
        }
        if (TextUtils.isEmpty(pwd) || name.length() < 6) {
            TagUtil.showToast(context, "账号或密码有误");
            return;
        }
        if (TextUtils.isEmpty(pwd) || name.length() < 6) {
            return;
        }

        rxLoginO(name, pwd);
//        rxLogin3(name, pwd);
//        testTJ();
    }

    private void testTJ() {

        //1.订阅者 泛型：最终想要获取的数据类型
        //一般弹toast的失败处理已处理，若需改写重写 onUserError 并去掉super(xx).
        BaseSubscriber<ResBase> subscriber = new BaseSubscriber<ResBase>(context, view) {

            @Override
            protected void onUserSuccess(ResBase resBase) {

            }
        };


        Datacenter.get().testSaas()
                .compose(RxUtils.<ResBase>getScheduler(true, view))
                .subscribe(subscriber);

        //一个请求（登录）
        Disposable disposable = Datacenter.get().testSaas()
                .compose(RxUtils.<ResBase>getScheduler(true, view))
                .subscribeWith(subscriber);
        //添加到订阅集合中
        rxManage.add(disposable);
    }

    private void rxLoginO(String name, String pwd) {

        //1.订阅者 泛型：最终想要获取的数据类型
        //一般弹toast的失败处理已处理，若需改写重写 onUserError 并去掉super(xx).
        BaseSubscriber<ResBase> subscriber = new BaseSubscriber<ResBase>(context, view) {

            @Override
            protected void onUserSuccess(ResBase resBase) {
                view.showMsg("单个请求" + resBase.msg);
            }

            /**
             * 第三方 返回校验失败
             * @param context
             * @param commonException
             */
            @Override
            protected void onCheckNgisFailed(Context context, InvalidException commonException) {
                super.onCheckNgisFailed(context, commonException);
                if (commonException.getResObj() != null) {
                    ResBase resBase = commonException.getResObj();
                }
            }

            @Override
            protected void onUnifiedError(CommonException ex) {
                super.onUnifiedError(ex);

            }
        };

        //一个请求（登录）
        Disposable disposable = Datacenter.get()
                .login(name, pwd)
                .compose(RxUtils.<ResBase>getScheduler(true, view))
                .subscribeWith(subscriber);
        //添加到订阅集合中
        rxManage.add(disposable);
    }

    private void rxLogin3(String name, String pwd) {
        //1.订阅者 泛型：最终想要获取的数据类型
        BaseSubscriber<ResBase> subscriber = new BaseSubscriber<ResBase>(context, view) {
            @Override
            protected void onUserSuccess(ResBase resBase) {
                view.showMsg(resBase.msg);
            }
        };

        //链式请求（登录成功后上传图片）
        //2.此处有两步操作，登录成功后切换线程 在主线程显示成功提示（观察的线程可以有多次切换，订阅线程只能指定一次）

        //登录成功后回调
        Flowable flowable = Datacenter.get().login(name, pwd).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<ResLogin>() {
                    @Override
                    public void accept(ResLogin resLogin) throws Exception {
                        /**
                         * 此处会出错，还在子线程中. 执行前需要先指定观察的线程位置,即login(xx,xx)后面的observeOn
                         */
                        view.showMsg("链式请求第一个响应" + resLogin.msg);
//                        view.loginSuccess(resLogin);//使用 当前获得的数据。区别于map 改变数据的操作
                    }
                }).flatMap(new Function<ResLogin, Publisher<?>>() {
                    @Override
                    public Publisher<?> apply(ResLogin resLogin) throws Exception {
                        Publisher<ResBase> result = null;
                        try {
                            //登录成功后请求上传（写死的一张 酷派手机上的图）
                            result = Datacenter.get().uploadPic("")
                                    .compose(RxUtils.<ResBase>getScheduler(false, view));
                        } catch (UserException e) {
                            throw e;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return result;
                    }
                }).delay(4000, TimeUnit.MILLISECONDS).compose(RxUtils.getScheduler(true, view));

        Disposable disposable = (Disposable) flowable.subscribeWith(subscriber);
        //添加到订阅集合中
        rxManage.add(disposable);
    }


    private void rxLogin2(String name, String pwd) {
        // 异步网络请求User数据，并在onNext(ResBase)返回 ---测试单元测试使用 例子
        Scheduler scheduler = AndroidSchedulers.mainThread();
        Disposable disposable = Datacenter.get().login(name, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(scheduler)
                .subscribe(new Consumer<ResLogin>() {
                    @Override
                    public void accept(ResLogin resLogin) throws Exception {
                        view.loginSuccess(resLogin);
                    }
                });
    }

    @Override
    public void sign(String name, String pass) {

    }

}
