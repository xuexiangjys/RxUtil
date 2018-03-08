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

package com.xuexiang.rxutil.rxjava.task;

import com.xuexiang.rxutil.rxjava.impl.IRxIOTask;
import com.xuexiang.rxutil.rxjava.impl.IRxUITask;

/**
 * 通用的Rx执行任务，在io线程中进行数据处理，在ui线程中刷新ui
 *
 * @author xuexiang
 * @date 2018/3/8 下午2:31
 */
public abstract class CommonRxTask<T, R> implements IRxIOTask<T, R>, IRxUITask<R> {

    /**
     * IO执行任务的入参
     */
    private T InData;

    /**
     * IO执行任务的出参,UI执行任务的入参
     */
    private R OutData;

    public CommonRxTask(T inData) {
        InData = inData;
    }

    public T getInData() {
        return InData;
    }

    public CommonRxTask setInData(T inData) {
        InData = inData;
        return this;
    }

    public R getOutData() {
        return OutData;
    }

    public CommonRxTask setOutData(R outData) {
        OutData = outData;
        return this;
    }
}
