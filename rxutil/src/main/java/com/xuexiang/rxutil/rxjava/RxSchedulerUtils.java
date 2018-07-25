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

import java.util.concurrent.Executor;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 线程调度工具
 *
 * @author xuexiang
 * @since 2018/6/10 下午7:10
 */
public final class RxSchedulerUtils {

    private RxSchedulerUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 回到主线程
     *
     * @param observable 被观察者
     */
    public static <T> Observable<T> toMain(Observable<T> observable) {
        return observable.observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 回到io线程
     *
     * @param observable 被观察者
     */
    public static <T> Observable<T> toIo(Observable<T> observable) {
        return observable.observeOn(Schedulers.io());
    }

    //============获取时间转换器Transformer==============//

    /**
     * 订阅发生在主线程 （  ->  -> main)
     * 使用compose操作符
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> _main() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return RxSchedulerUtils.toMain(tObservable);
            }
        };
    }

    /**
     * 订阅发生在io线程 （  ->  -> io)
     * 使用compose操作符
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> _io() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return RxSchedulerUtils.toIo(tObservable);
            }
        };
    }

    /**
     * 处理在io线程，订阅发生在主线程（ -> io -> main)
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> _io_main() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 处理在io线程，订阅也发生在io线程（ -> io -> io)
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> _io_io() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
            }
        };
    }


    /**
     * 订阅发生在io线程 （  ->  -> io)
     * 使用compose操作符
     *
     * @param executor 线程池
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> _io(final Executor executor) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.observeOn(Schedulers.from(executor));
            }
        };
    }

    /**
     * 处理在io线程，订阅发生在主线程（ -> io -> main)
     *
     * @param executor 线程池
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> _io_main(final Executor executor) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.from(executor))
                        .unsubscribeOn(Schedulers.from(executor))
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 处理在io线程，订阅也发生在io线程（ -> io -> io)
     *
     * @param executor 线程池
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> _io_io(final Executor executor) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.from(executor))
                        .unsubscribeOn(Schedulers.from(executor))
                        .observeOn(Schedulers.from(executor));
            }
        };
    }


}
