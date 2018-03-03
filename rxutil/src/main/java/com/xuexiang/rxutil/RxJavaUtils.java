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

package com.xuexiang.rxutil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * RxJava工具
 *
 * @author xuexiang
 * @date 2018/3/4 上午1:20
 */
public final class RxJavaUtils {

    private final static String TAG = "RxJavaUtils";

    /**
     * 轮询操作
     *
     * @param interval 轮询间期
     * @param action1  监听事件
     */
    public static Subscription polling(long interval, Action1 action1) {
        return polling(0, interval, action1);
    }

    /**
     * 轮询操作
     *
     * @param initialDelay 初始延迟
     * @param interval     轮询间期
     * @param action1      监听事件
     */
    public static Subscription polling(long initialDelay, long interval, Action1 action1) {
        return polling(initialDelay, interval, TimeUnit.SECONDS, action1, new SimpleThrowableAction(TAG));
    }

    /**
     * 轮询操作
     *
     * @param initialDelay 初始延迟
     * @param interval     轮询间期
     * @param unit         轮询间期时间单位
     * @param action1      监听事件
     * @param errorAction  出错的事件
     */
    public static Subscription polling(long initialDelay, long interval, TimeUnit unit, Action1 action1, Action1<Throwable> errorAction) {
        return Observable.interval(initialDelay, interval, unit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, errorAction);
    }

    /**
     * 延迟操作
     *
     * @param delayTime 延迟时间
     * @param action1   监听事件
     */
    public static Subscription delay(long delayTime, Action1 action1) {
        return delay(delayTime, TimeUnit.SECONDS, action1, new SimpleThrowableAction(TAG));
    }

    /**
     * 延迟操作
     *
     * @param delayTime 延迟时间
     * @param unit      延迟时间单位
     * @param action1   监听事件
     * @param errorAction  出错的事件
     */
    public static Subscription delay(long delayTime, TimeUnit unit, Action1 action1, Action1<Throwable> errorAction) {
        return Observable.timer(delayTime, unit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, errorAction);
    }

    /**
     * 遍历数组
     * @param t 数组
     * @param action1 动作
     * @return
     */
    public static <T> Subscription foreach(T[] t, Action1<T> action1) {
        return Observable.from(t).subscribe(action1, new SimpleThrowableAction(TAG));
    }

    /**
     * 遍历集合
     * @param t 数组
     * @param action1 动作
     * @return
     */
    public static <T> Subscription foreach(Iterable<T> t, Action1<T> action1) {
        return Observable.from(t).subscribe(action1, new SimpleThrowableAction(TAG));
    }

}
