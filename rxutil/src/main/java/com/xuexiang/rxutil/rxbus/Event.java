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

import java.util.Arrays;

/**
 * 万能的事件对象
 * @author xuexiang
 * @date 2018/3/26 下午11:01
 */
public class Event {
    /**
     * 事件key
     */
    private Object mKey;
    /**
     * 携带的数据
     */
    private Object mValue;

    public Event() {}

    public Event(Object key, Object value) {
        mKey = key;
        mValue = value;
    }

    public Object getKey() {
        return mKey;
    }

    public Event setKey(Object key) {
        mKey = key;
        return this;
    }

    public Object getValue() {
        return mValue;
    }

    public Event setValue(Object value) {
        mValue = value;
        return this;
    }

    @Override
    public String toString() {
        return "[Event] { \n" +
                "  mKey:" + Event.toString(mKey) + "\n" +
                "  mValue:" + Event.toString(mValue) + "\n" +
                "}";
    }

    /**
     * 判断是否是指定的事件
     * @param eventKey
     * @return
     */
    public boolean isEvent(Object eventKey) {
        return mKey.equals(eventKey);
    }


    /**
     * 将对象转化为String
     * @param object
     * @return
     */
    public static String toString(Object object) {
        if (object == null) {
            return "null";
        }
        if (!object.getClass().isArray()) {
            return object.toString();
        }
        if (object instanceof boolean[]) {
            return Arrays.toString((boolean[]) object);
        }
        if (object instanceof byte[]) {
            return Arrays.toString((byte[]) object);
        }
        if (object instanceof char[]) {
            return Arrays.toString((char[]) object);
        }
        if (object instanceof short[]) {
            return Arrays.toString((short[]) object);
        }
        if (object instanceof int[]) {
            return Arrays.toString((int[]) object);
        }
        if (object instanceof long[]) {
            return Arrays.toString((long[]) object);
        }
        if (object instanceof float[]) {
            return Arrays.toString((float[]) object);
        }
        if (object instanceof double[]) {
            return Arrays.toString((double[]) object);
        }
        if (object instanceof Object[]) {
            return Arrays.deepToString((Object[]) object);
        }
        return "Couldn't find a correct type for the object";
    }
}
