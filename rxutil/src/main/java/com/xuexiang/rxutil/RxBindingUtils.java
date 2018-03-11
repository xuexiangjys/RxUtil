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

import android.view.View;
import android.widget.AdapterView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.AdapterViewItemClickEvent;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.xuexiang.rxutil.subsciber.SimpleThrowableAction;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * RxBinding工具
 *
 * @author xuexiang
 * @date 2018/3/4 上午12:23
 */
public final class RxBindingUtils {
    private final static String TAG = "RxBindingUtils";

    /**
     * 自定义控件监听
     *
     * @param v 监听控件
     * @return
     */
    public static Observable<Void> setViewClicks(View v) {
        return setViewClicks(v, 1, TimeUnit.SECONDS);
    }

    /**
     * 自定义控件监听
     *
     * @param v        监听控件
     * @param duration 点击时间间隔
     * @param unit     时间间隔单位
     * @return
     */
    public static Observable<Void> setViewClicks(View v, long duration, TimeUnit unit) {
        return RxView.clicks(v)
                .throttleFirst(duration, unit)//取1s间隔内最后一次事件
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 简单的控件点击监听
     *
     * @param v       监听控件
     * @param action1 监听事件
     * @return
     */
    public static Subscription setViewClicks(View v, Action1<Void> action1) {
        return setViewClicks(v, 1, TimeUnit.SECONDS, action1, new SimpleThrowableAction(TAG));
    }

    /**
     * 简单的控件点击监听
     *
     * @param v           监听控件
     * @param duration    点击时间间隔
     * @param unit        时间间隔单位
     * @param action1     监听事件
     * @return
     */
    public static Subscription setViewClicks(View v, long duration, TimeUnit unit, Action1<Void> action1) {
        return setViewClicks(v, duration, unit, action1, new SimpleThrowableAction(TAG));
    }

    /**
     * 简单的控件点击监听
     *
     * @param v           监听控件
     * @param duration    点击时间间隔
     * @param unit        时间间隔单位
     * @param action1     监听事件
     * @param errorAction 出错的事件
     * @return
     */
    public static Subscription setViewClicks(View v, long duration, TimeUnit unit, Action1<Void> action1, Action1<Throwable> errorAction) {
        return RxView.clicks(v)
                .throttleFirst(duration, unit)//取1s间隔内最后一次事件
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, errorAction);
    }

    /**
     * AdapterView控件点击监听
     *
     * @param view    监听控件
     * @param action1 条目点击监听事件
     */
    public static Subscription setItemClicks(AdapterView<?> view, Action1<AdapterViewItemClickEvent> action1) {
        return setItemClicks(view, 1, TimeUnit.SECONDS, action1, new SimpleThrowableAction(TAG));
    }

    /**
     * AdapterView控件点击监听
     *
     * @param view        监听控件
     * @param action1     条目点击监听事件
     * @param errorAction 出错的事件
     */
    public static Subscription setItemClicks(AdapterView<?> view, long duration, TimeUnit unit, Action1<AdapterViewItemClickEvent> action1, Action1<Throwable> errorAction) {
        return RxAdapterView.itemClickEvents(view)
                .throttleFirst(duration, unit)//取1s间隔内最后一次事件
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, errorAction);
    }

}
