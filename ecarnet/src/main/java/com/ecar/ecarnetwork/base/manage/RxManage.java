package com.ecar.ecarnetwork.base.manage;



import com.ecar.ecarnetwork.util.rx.RxUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;


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
    private CompositeDisposable subscriptions;//管理订阅者者。取消订阅后不能再次订阅

    public RxManage() {
        this.subscriptions = new CompositeDisposable();
    }

    /**
     * 将订阅者添至管理
     *
     * @param m
     */
    public void add(Subscription m) {
        if (subscriptions == null) {
            subscriptions = new CompositeDisposable();
        }
        if (m != null && !subscriptions.isDisposed()) {
            subscriptions.add(m);
        }
    }

    /**
     * 取消订阅
     */
    public void clear() {
        if (subscriptions != null) {
            subscriptions.unsubscribe();
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
    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (observable != null && subscriber != null && subscriber.isUnsubscribed()) {
            subscriptions.add(observable.compose(RxUtils.rxScheduler()).subscribe(subscriber));
        }
    }
}
