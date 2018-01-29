package com.ecar.ecarnetwork.base.manage;


import com.ecar.ecarnetwork.util.rx.RxUtils;

import java.util.HashMap;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.ResourceSubscriber;


/**
 * ===============================================
 * <p>
 * 类描述: Rx 事件 管理者
 * <p>
 * 创建人:   happy
 * <p>
 * 创建时间: 2016/6/22 0022 下午 14:43
 * <p>
 * 修改人:   happy
 * <p>
 * 修改时间: 2016/6/22 0022 下午 14:43
 * <p>
 * 修改备注:
 * <p>
 * ===============================================
 */
public class RxManage {
    /**
     * 管理订阅者者。取消订阅后不能再次订阅
     */
    private CompositeDisposable subscriptions;

    /**
     * 添加有标识的订阅者
     */
    private HashMap<String, Disposable> mDisposableHashMap;

    public RxManage() {
        this.subscriptions = new CompositeDisposable();
        this.mDisposableHashMap = new HashMap<>();
    }

    /**
     * 将订阅者添至管理
     */
    public void add(Disposable m) {
        if (subscriptions == null) {
            subscriptions = new CompositeDisposable();
        }
        if (m != null && !subscriptions.isDisposed()) {
            subscriptions.add(m);
        }
    }

    /**
     * 将指定的订阅者添至管理
     */
    public void add(String key, Disposable m) {
        if (m != null) {
            mDisposableHashMap.put(key, m);
            add(m);
        }
    }

    /**
     * 取消指定的 disposable订阅
     *
     * @param key
     */
    public void clear(String key) {
        Disposable disposable = mDisposableHashMap.get(key);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            subscriptions.delete(disposable);
        }
    }

    /**
     * 取消订阅
     */
    public void clear() {
        if (subscriptions != null) {
            subscriptions.dispose();
            subscriptions = null;
        }
    }

    /**
     * 被观察者、订阅者 作为参数传入
     * 将订阅者添至管理
     *
     * @param observable
     * @param subscriber
     */
    public void addSubscription(Flowable observable, ResourceSubscriber subscriber) {
        if (observable != null && subscriber != null) {
            subscriptions.add((Disposable) observable.compose(RxUtils.rxScheduler()).subscribeWith(subscriber));
        }
    }
}
