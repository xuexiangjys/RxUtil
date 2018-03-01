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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import android.support.annotation.NonNull;

/**
 * RxBus事件通知工具
 * @author xuexiang
 * @date 2018/3/1 上午10:30
 */
public class RxBus {
    private ConcurrentHashMap<Object, List<Subject>> maps = new ConcurrentHashMap<Object, List<Subject>>();
    private static RxBus sInstance;

    /**
     * 获取RxBus的实例
     * @return
     */
    public static RxBus get() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                if (sInstance == null) {
                    sInstance = new RxBus();
                }
            }
        }
        return sInstance;
    }

    /**
     * 简单以对象的类名注册
     *
     * @param tag 类对象
     * @return
     */
    public <T> Observable<T> simpleRegister(@NonNull Object tag) {
        return register(tag.getClass().getSimpleName());
    }

    /**
     * 注册订阅
     *
     * @param tag 注册标识
     * @return
     */
    public <T> Observable<T> register(@NonNull Object tag) {
        List<Subject> subjects = maps.get(tag);
        if (subjects == null) {
            subjects = new ArrayList<>();
            maps.put(tag, subjects);
        }
        Subject<T, T> subject = PublishSubject.<T>create();
        subjects.add(subject);
        return subject;
    }

    /**
     * 注销tag制定的订阅
     *
     * @param tag        注册标识
     * @param observable 被观察者
     */
    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjects = maps.get(tag);
        if (subjects != null) {
            subjects.remove(observable);
            if (subjects.isEmpty()) {
                maps.remove(tag);
            }
        }
    }

    /**
     * 注销tag所有的订阅
     *
     * @param tag 注册标识
     */
    public void unregisterAll(@NonNull Object tag) {
        List<Subject> subjects = maps.get(tag);
        if (subjects != null) {
            maps.remove(tag);
        }
    }

    /**
     * 发送指定tag的事件(不带内容)
     *
     * @param tag 注册标识
     */
    public void post(@NonNull Object tag) {
        post(tag, tag);
    }

    /**
     * 发送指定tag的事件
     *
     * @param tag 注册标识
     * @param o   发生内容
     */
    @SuppressWarnings("unchecked")
    public void post(@NonNull Object tag, @NonNull Object o) {
        List<Subject> subjects = maps.get(tag);
        if (subjects != null && !subjects.isEmpty()) {
            for (Subject s : subjects) {
                s.onNext(o);
            }
        }
    }
}
