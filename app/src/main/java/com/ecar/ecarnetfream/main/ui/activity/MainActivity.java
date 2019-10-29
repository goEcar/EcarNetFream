package com.ecar.ecarnetfream.main.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ecar.ecarnetfream.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FlowableSubscriber  ResourceSubscrible
        Flowable.just("123").subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

        Observable.just("123").subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


        Flowable.just("123").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        }, Throwable::printStackTrace);


        Observable.just("123").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        }, Throwable::printStackTrace);


    }
}
