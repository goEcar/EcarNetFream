package com.ecar.ecarnetwork.base.manage;

import junit.framework.TestCase;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

/**
 * 功能：
 * 创建者：ruiqin.shen
 * 创建日期：2018/1/25
 * 版权所有：深圳市亿车科技有限公司
 */
public class RxManageTest extends TestCase {
    RxManage mRxManage;

    public void setUp() throws Exception {
        super.setUp();
        mRxManage = new RxManage();
    }

    /**
     * 测试添加
     *
     * @throws Exception
     */
    public void testAdd() throws Exception {
        Disposable subscribe = Flowable.timer(1, TimeUnit.DAYS).subscribe();
        mRxManage.add("login", subscribe);
        mRxManage.clear("login");
    }

    public void testClear() throws Exception {
        mRxManage.clear("login");
    }

}