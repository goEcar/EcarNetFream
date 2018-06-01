## 2018-6-1更新
* BaseSubscriber继承DisposableSubscriber
* RxUtils支持非lambda调用
````
Datacenter.get().testSaas()
        .compose(RxUtils.<ResBase>getScheduler(true, view))
        .subscribe(subscriber);
````

## 更改Rxjava2 2017-12-5
* 目前只支持Flowable，不支持Observable

## 注意事项
* [解决ImageLoader加载HTTPS图片证书校验异常问题](http://www.cnblogs.com/csdnLion2016/p/5720590.html)
