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

import com.xuexiang.rxutil.rxjava.task.RxAsyncTask;
import com.xuexiang.rxutil.rxjava.task.RxIOTask;
import com.xuexiang.rxutil.rxjava.task.RxIteratorTask;
import com.xuexiang.rxutil.rxjava.task.RxUITask;
import com.xuexiang.rxutil.subsciber.BaseSubscriber;
import com.xuexiang.rxutil.subsciber.SimpleThrowableAction;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * RxJava工具
 *
 * @author xuexiang
 * @since 2018/6/10 下午7:10
 */
public final class RxJavaUtils {

    private final static String TAG = "RxJavaUtils";

    private RxJavaUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    //========================线程任务==========================//

    /**
     * 在ui线程中工作
     *
     * @param uiTask 在UI线程中操作的任务
     * @param <T>
     * @return
     */
    public static <T> Subscription doInUIThread(@NonNull RxUITask<T> uiTask) {
        return doInUIThread(uiTask, new SimpleThrowableAction(TAG));
    }

    /**
     * 在ui线程中工作
     *
     * @param uiTask      在UI线程中操作的任务
     * @param errorAction 出错的处理
     * @param <T>
     * @return
     */
    public static <T> Subscription doInUIThread(@NonNull RxUITask<T> uiTask, @NonNull Action1<Throwable> errorAction) {
        return Observable.just(uiTask)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RxUITask<T>>() {
                    @Override
                    public void call(RxUITask rxUITask) {
                        rxUITask.doInUIThread(rxUITask.getInData());
                    }
                }, errorAction);
    }

    /**
     * 在IO线程中执行任务
     *
     * @param ioTask 在io线程中操作的任务
     * @param <T>
     * @return
     */
    public static <T> Subscription doInIOThread(@NonNull RxIOTask<T> ioTask) {
        return doInIOThread(ioTask, new SimpleThrowableAction(TAG));
    }

    /**
     * 在IO线程中执行任务
     *
     * @param ioTask      在io线程中操作的任务
     * @param errorAction 出错的处理
     * @param <T>
     * @return
     */
    public static <T> Subscription doInIOThread(@NonNull RxIOTask<T> ioTask, @NonNull Action1<Throwable> errorAction) {
        return Observable.just(ioTask)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<RxIOTask<T>>() {
                    @Override
                    public void call(RxIOTask<T> rxIOTask) {
                        rxIOTask.doInIOThread(rxIOTask.getInData());
                    }
                }, errorAction);
    }
    //========================轮询操作==========================//

    /**
     * 轮询操作
     *
     * @param interval 轮询间期
     */
    public static Observable<Long> polling(long interval) {
        return polling(0, interval, TimeUnit.SECONDS);
    }

    /**
     * 轮询操作
     *
     * @param interval 轮询间期
     * @param action1  监听事件
     */
    public static Subscription polling(long interval, @NonNull Action1<Long> action1) {
        return polling(0, interval, action1);
    }

    /**
     * 轮询操作
     *
     * @param initialDelay 初始延迟
     * @param interval     轮询间期
     * @param action1      监听事件
     */
    public static Subscription polling(long initialDelay, long interval, @NonNull Action1<Long> action1) {
        return polling(initialDelay, interval, TimeUnit.SECONDS, action1, new SimpleThrowableAction(TAG));
    }

    /**
     * 轮询操作
     *
     * @param initialDelay 初始延迟
     * @param interval     轮询间期
     * @param unit         轮询间期时间单位
     */
    public static Observable<Long> polling(long initialDelay, long interval, TimeUnit unit) {
        return Observable.interval(initialDelay, interval, unit)
                .observeOn(AndroidSchedulers.mainThread());
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
    public static Subscription polling(long initialDelay, long interval, TimeUnit unit, @NonNull Action1<Long> action1, @NonNull Action1<Throwable> errorAction) {
        return Observable.interval(initialDelay, interval, unit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, errorAction);
    }

    //=================倒计时=================//

    /**
     * 倒计时操作【间隔1秒】
     *
     * @param totalTime  倒计时总时长
     * @param subscriber 事件订阅
     * @return
     */
    public static Subscription countDown(final long totalTime, @NonNull BaseSubscriber<Long> subscriber) {
        return countDown(totalTime, 1, TimeUnit.SECONDS).subscribe(subscriber);
    }

    /**
     * 倒计时操作【间隔1秒】
     *
     * @param totalTime 倒计时总时长
     * @return
     */
    public static Observable<Long> countDown(final long totalTime) {
        return countDown(totalTime, 1, TimeUnit.SECONDS);
    }

    /**
     * 倒计时操作
     *
     * @param totalTime 倒计时总时长
     * @param interval  倒计时间隔
     * @param unit      时间间隔单位
     * @return
     */
    public static Observable<Long> countDown(final long totalTime, long interval, TimeUnit unit) {
        return Observable.interval(0, interval, unit)
                .take((int) Math.floor(totalTime / (double) interval) + 1)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long time) {
                        return totalTime - time;
                    }
                })
                .compose(RxSchedulerUtils.<Long>_io_main());
    }

    //========================延迟操作==========================//

    /**
     * 延迟操作
     *
     * @param delayTime 延迟时间
     * @param unit      延迟时间单位
     */
    public static Observable<Long> delay(long delayTime, TimeUnit unit) {
        return Observable.timer(delayTime, unit)
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 延迟操作
     *
     * @param delayTime 延迟时间
     * @param action1   监听事件
     */
    public static Subscription delay(long delayTime, @NonNull Action1<Long> action1) {
        return delay(delayTime, TimeUnit.SECONDS, action1, new SimpleThrowableAction(TAG));
    }

    /**
     * 延迟操作
     *
     * @param delayTime   延迟时间
     * @param unit        延迟时间单位
     * @param action1     监听事件
     * @param errorAction 出错的事件
     */
    public static Subscription delay(long delayTime, TimeUnit unit, @NonNull Action1<Long> action1, @NonNull Action1<Throwable> errorAction) {
        return Observable.timer(delayTime, unit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, errorAction);
    }

    /**
     * 延迟操作
     *
     * @param t         发射源
     * @param delayTime 延迟时间
     * @param unit      延迟时间单位
     */
    public static <T> Observable<T> delay(@NonNull T t, long delayTime, @NonNull TimeUnit unit) {
        return Observable.just(t)
                .delay(delayTime, unit)
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 延迟操作
     *
     * @param t          发射源
     * @param delayTime  延迟时间
     * @param unit       延迟时间单位
     * @param subscriber 订阅的事件
     */
    public static <T> Subscription delay(@NonNull T t, long delayTime, @NonNull TimeUnit unit, @NonNull BaseSubscriber<T> subscriber) {
        return Observable.just(t)
                .delay(delayTime, unit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //=====================AsyncTask=========================//

    /**
     * 执行Rx通用任务 (IO线程中执行耗时操作 执行完成调用UI线程中的方法)
     *
     * @param rxTask 执行任务
     * @param <T>
     * @return
     */
    public static <T, R> Subscription executeAsyncTask(@NonNull RxAsyncTask<T, R> rxTask) {
        return executeAsyncTask(rxTask, new SimpleThrowableAction(TAG));
    }

    /**
     * 执行Rx通用任务 (IO线程中执行耗时操作 执行完成调用UI线程中的方法)
     *
     * @param executor    指定执行任务的线程池
     * @param rxTask 执行任务
     * @param <T>
     * @return
     */
    public static <T, R> Subscription executeAsyncTask(final Executor executor, @NonNull RxAsyncTask<T, R> rxTask) {
        return executeAsyncTask(executor, rxTask, new SimpleThrowableAction(TAG));
    }

    /**
     * 执行Rx通用任务 (IO线程中执行耗时操作 执行完成调用UI线程中的方法)
     *
     * @param rxTask      执行任务
     * @param errorAction 出错的处理
     * @param <T>
     * @return
     */
    public static <T, R> Subscription executeAsyncTask(@NonNull RxAsyncTask<T, R> rxTask, @NonNull Action1<Throwable> errorAction) {
        return executeAsyncTask(null, rxTask, errorAction);
    }

    /**
     * 执行Rx通用任务 (IO线程中执行耗时操作 执行完成调用UI线程中的方法)
     *
     * @param executor    指定执行任务的线程池
     * @param rxTask      执行任务
     * @param errorAction 出错的处理
     * @param <T>
     * @return
     */
    public static <T, R> Subscription executeAsyncTask(final Executor executor, @NonNull RxAsyncTask<T, R> rxTask, @NonNull Action1<Throwable> errorAction) {
        RxTaskOnSubscribe<RxAsyncTask<T, R>> onSubscribe = getRxAsyncTaskOnSubscribe(rxTask);
        return Observable.create(onSubscribe, Emitter.BackpressureMode.LATEST)
                .compose(executor == null ? RxSchedulerUtils.<RxAsyncTask<T, R>>_io_main() : RxSchedulerUtils.<RxAsyncTask<T, R>>_io_main(executor))
                .subscribe(new Action1<RxAsyncTask<T, R>>() {
                    @Override
                    public void call(RxAsyncTask<T, R> rxAsyncTask) {
                        rxAsyncTask.doInUIThread(rxAsyncTask.getOutData());  //在UI线程工作
                    }
                }, errorAction);
    }

    @NonNull
    private static <T, R> RxTaskOnSubscribe<RxAsyncTask<T, R>> getRxAsyncTaskOnSubscribe(@NonNull final RxAsyncTask<T, R> rxTask) {
        return new RxTaskOnSubscribe<RxAsyncTask<T, R>>(rxTask) {
            @Override
            public void call(Emitter<RxAsyncTask<T, R>> rxAsyncTaskEmitter) {
                RxAsyncTask<T, R> task = getTask();
                task.setOutData(task.doInIOThread(task.getInData()));  //在io线程工作
                rxAsyncTaskEmitter.onNext(task);
                rxAsyncTaskEmitter.onCompleted();
            }
        };
    }

    /**
     * 执行异步任务（IO线程处理，UI线程显示）
     *
     * @param t     处理入参
     * @param func1 动作
     * @return
     */
    public static <T, R> Observable<R> executeAsyncTask(@NonNull T t, @NonNull Func1<T, R> func1) {
        return Observable.just(t)
                .map(func1)
                .compose(RxSchedulerUtils.<R>_io_main());
    }

    /**
     * 执行异步任务（IO线程处理，UI线程显示）
     *
     * @param t          处理入参
     * @param func1      动作
     * @param subscriber 订阅者
     * @return
     */
    public static <T, R> Subscription executeAsyncTask(@NonNull T t, @NonNull Func1<T, R> func1, @NonNull BaseSubscriber<R> subscriber) {
        return executeAsyncTask(t, func1).subscribe(subscriber);
    }


    /**
     * 执行异步任务（IO线程处理，UI线程显示）
     *
     * @param t           处理入参
     * @param transformer 转化器
     * @return
     */
    public static <T, R> Observable<R> executeAsyncTask(@NonNull T t, @NonNull Observable.Transformer<T, R> transformer) {
        return Observable.just(t)
                .compose(transformer)
                .compose(RxSchedulerUtils.<R>_io_main());
    }

    /**
     * 执行异步任务（IO线程处理，UI线程显示）
     *
     * @param t           处理入参
     * @param transformer 转化器
     * @param subscriber  订阅者
     * @return
     */
    public static <T, R> Subscription executeAsyncTask(@NonNull T t, @NonNull Observable.Transformer<T, R> transformer, @NonNull BaseSubscriber<R> subscriber) {
        return executeAsyncTask(t, transformer).subscribe(subscriber);
    }

    //=====================集合、数组遍历处理=========================//

    /**
     * 遍历集合进行处理（IO线程处理，UI线程显示）
     *
     * @param rxIteratorTask
     * @return
     */
    public static <T, R> Subscription executeRxIteratorTask(final RxIteratorTask<T, R> rxIteratorTask) {
        return executeRxIteratorTask(rxIteratorTask, new SimpleThrowableAction(TAG));
    }


    /**
     * 遍历集合进行处理（IO线程处理，UI线程显示）
     *
     * @param rxIteratorTask
     * @param errorAction    出错的处理
     * @return
     */
    public static <T, R> Subscription executeRxIteratorTask(final RxIteratorTask<T, R> rxIteratorTask, @NonNull Action1<Throwable> errorAction) {
        Observable<T> observable = rxIteratorTask.isArray() ? Observable.from(rxIteratorTask.getArray()) : Observable.from(rxIteratorTask.getIterable());
        return observable.map(new Func1<T, R>() {
            @Override
            public R call(T t) {
                return rxIteratorTask.doInIOThread(t);
            }
        }).compose(RxSchedulerUtils.<R>_io_main())
                .subscribe(new Action1<R>() {
                    @Override
                    public void call(R r) {
                        rxIteratorTask.doInUIThread(r);
                    }
                }, errorAction);
    }

    /**
     * 遍历数组进行处理（IO线程处理，UI线程显示）
     *
     * @param t          数组
     * @param func1      动作
     * @param subscriber 订阅者
     * @return
     */
    public static <T, R> Subscription foreach(@NonNull T[] t, @NonNull Func1<T, R> func1, @NonNull BaseSubscriber<R> subscriber) {
        return Observable.from(t)
                .map(func1)
                .compose(RxSchedulerUtils.<R>_io_main())
                .subscribe(subscriber);
    }


    /**
     * 遍历数组进行处理（IO线程处理，UI线程显示）
     *
     * @param t           数组
     * @param transformer 转化器
     * @param subscriber  订阅者
     * @return
     */
    public static <T, R> Subscription foreach(@NonNull T[] t, @NonNull Observable.Transformer<T, R> transformer, @NonNull BaseSubscriber<R> subscriber) {
        return Observable.from(t)
                .compose(transformer)
                .compose(RxSchedulerUtils.<R>_io_main())
                .subscribe(subscriber);
    }

    /**
     * 遍历集合进行处理（IO线程处理，UI线程显示）
     *
     * @param t          数组
     * @param func1      动作
     * @param subscriber 订阅者
     * @return
     */
    public static <T, R> Subscription foreach(@NonNull Iterable<T> t, @NonNull Func1<T, R> func1, @NonNull BaseSubscriber<R> subscriber) {
        return Observable.from(t)
                .map(func1)
                .compose(RxSchedulerUtils.<R>_io_main())
                .subscribe(subscriber);
    }


    /**
     * 遍历集合进行处理（IO线程处理，UI线程显示）
     *
     * @param t           数组
     * @param transformer 转化器
     * @param subscriber  订阅者
     * @return
     */
    public static <T, R> Subscription foreach(@NonNull Iterable<T> t, @NonNull Observable.Transformer<T, R> transformer, @NonNull BaseSubscriber<R> subscriber) {
        return Observable.from(t)
                .compose(transformer)
                .compose(RxSchedulerUtils.<R>_io_main())
                .subscribe(subscriber);
    }

}
