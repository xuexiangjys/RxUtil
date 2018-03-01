/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xuexiang.rxutil.rxbus;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * RxBus辅助工具类
 * @author xuexiang
 * @date 2018/3/1 上午10:41
 */
public class RxBusUtils {
    private final static String TAG = "RxBusUtils";

    private static RxBusUtils sInstance;
    private RxBus mRxBus = RxBus.get();
    /**
     * 管理Subscribers订阅，防止内存泄漏
     */
    private ConcurrentHashMap<Object, CompositeSubscription> maps = new ConcurrentHashMap<Object, CompositeSubscription>();

    private RxBusUtils() {

    }

    public static RxBusUtils get() {
        if (sInstance == null) {
            synchronized (RxBusUtils.class) {
                if (sInstance == null) {
                    sInstance = new RxBusUtils();
                }
            }
        }
        return sInstance;
    }
    //===============================RxBus==================================//

    /**
     * RxBus注入监听（订阅发生在主线程）
     *
     * @param eventName   事件名
     * @param action1     订阅动作
     */
    public <T> Observable<T> onMainThread(Object eventName, Action1<T> action1) {
        return onMainThread(eventName, action1, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "订阅发生错误：" + Log.getStackTraceString(throwable));
            }
        });
    }

    /**
     * RxBus注入监听（订阅发生在主线程）
     *
     * @param eventName   事件名
     * @param action1     订阅动作
     * @param errorAction 错误订阅
     */
    public <T> Observable<T> onMainThread(Object eventName, Action1<T> action1, Action1<Throwable> errorAction) {
        Observable<T> Observable = mRxBus.register(eventName);
        /* 订阅管理 */
        add(eventName, Observable.observeOn(AndroidSchedulers.mainThread()).subscribe(action1, errorAction));
        return Observable;
    }

    /**
     * RxBus注入监听（订阅线程不变）
     *
     * @param eventName   事件名
     * @param action1     订阅动作
     */
    public <T> Observable<T> on(Object eventName, Action1<T> action1) {
        return on(eventName, action1, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "订阅发生错误：" + Log.getStackTraceString(throwable));
            }
        });
    }

    /**
     * RxBus注入监听（订阅线程不变）
     *
     * @param eventName   事件名
     * @param action1     订阅动作
     * @param errorAction 错误订阅
     */
    public <T> Observable<T> on(Object eventName, Action1<T> action1, Action1<Throwable> errorAction) {
        Observable<T> Observable = mRxBus.register(eventName);
        /* 订阅管理 */
        add(eventName, Observable.subscribe(action1, errorAction));
        return Observable;
    }

    /**
     * 单纯的Observables 和Subscribers管理
     *
     * @param eventName 事件名
     * @param m         订阅信息
     */
    public void add(Object eventName, Subscription m) {
		/* 订阅管理 */
        CompositeSubscription subscription = maps.get(eventName);
        if (subscription == null) {
            subscription = new CompositeSubscription();
            maps.put(eventName, subscription);
        }
        subscription.add(m);
    }

    /**
     * 单个presenter生命周期结束，取消订阅和所有rxbus观察
     *
     * @param eventName 事件名
     */
    public void clear(@NonNull Object eventName) {
        CompositeSubscription subscription = maps.get(eventName);
        if (subscription != null) {
            subscription.unsubscribe(); //取消订阅
            maps.remove(eventName);
        }
        mRxBus.unregisterAll(eventName);

    }

    /**
     * 发送指定tag的事件(不带内容)
     *
     * @param tag 注册标识
     */
    public void post(Object tag) {
        mRxBus.post(tag);
    }

    /**
     * 发送指定tag的事件
     *
     * @param tag     注册标识
     * @param content 发生内容
     */
    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }

}
