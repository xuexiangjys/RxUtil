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

package com.xuexiang.rxutil.rxjava;

import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * RxJava订阅池
 *
 * @author xuexiang
 * @since 2018/6/10 下午7:12
 */
public class SubscriptionPool {

    private static SubscriptionPool sInstance;

    //===================================网络请求订阅管理=======================================//
    /**
     * 网络请求订阅池，管理Subscribers订阅，防止内存泄漏
     */
    private ConcurrentHashMap<Object, CompositeSubscription> maps = new ConcurrentHashMap<>();

    private SubscriptionPool() {

    }

    /**
     * 获取订阅池
     * @return
     */
    public static SubscriptionPool get() {
        if (sInstance == null) {
            synchronized (SubscriptionPool.class) {
                if (sInstance == null) {
                    sInstance = new SubscriptionPool();
                }
            }
        }
        return sInstance;
    }


    /**
     * 根据tagName管理订阅【注册订阅信息】
     * @param tagName 标志
     * @param m 订阅信息
     */
    public Subscription add(@NonNull Object tagName, Subscription m) {
		/* 订阅管理 */
        CompositeSubscription subscription = maps.get(tagName);
        if (subscription == null) {
            subscription = new CompositeSubscription();
            maps.put(tagName, subscription);
        }
        subscription.add(m);
        return m;
    }

    /**
     * 根据tagName管理订阅【注册订阅信息】
     * @param m 订阅信息
     * @param tagName 标志
     *
     */
    public Subscription add(Subscription m, @NonNull Object tagName) {
		/* 订阅管理 */
        CompositeSubscription subscription = maps.get(tagName);
        if (subscription == null) {
            subscription = new CompositeSubscription();
            maps.put(tagName, subscription);
        }
        subscription.add(m);
        return m;
    }

    /**
     * 取消订阅【取消标志内所有订阅信息】
     * @param tagName 标志
     */
    public void remove(@NonNull Object tagName) {
        CompositeSubscription subscription = maps.get(tagName);
        if (subscription != null) {
            subscription.unsubscribe(); //取消订阅
            maps.remove(tagName);
        }
    }

    /**
     * 取消订阅【单个订阅取消】
     * @param tagName 标志
     * @param m 订阅信息
     */
    public void remove(@NonNull Object tagName, Subscription m) {
        CompositeSubscription subscription = maps.get(tagName);
        if (subscription != null) {
            subscription.remove(m);
            if (!subscription.hasSubscriptions()) {
                maps.remove(tagName);
            }
        }
    }

    /**
     * 取消所有订阅
     */
    public void removeAll() {
        Iterator<Map.Entry<Object, CompositeSubscription>> it = maps.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Object, CompositeSubscription> entry = it.next();
            CompositeSubscription subscription = entry.getValue();
            if (subscription != null) {
                subscription.unsubscribe(); //取消订阅
                it.remove();
            }
        }
        maps.clear();
    }


    /**
     * 取消订阅
     * @param subscription 订阅信息
     */
    public static void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

}
