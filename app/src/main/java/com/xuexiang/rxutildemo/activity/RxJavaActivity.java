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

package com.xuexiang.rxutildemo.activity;

import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.xuexiang.rxutil.rxjava.task.CommonRxTask;
import com.xuexiang.rxutil.rxjava.RxJavaUtils;
import com.xuexiang.rxutil.rxjava.task.RxIOTask;
import com.xuexiang.rxutil.rxjava.task.RxUITask;
import com.xuexiang.rxutildemo.R;
import com.xuexiang.rxutildemo.base.BaseActivity;

import butterknife.OnClick;

/**
 * @author xuexiang
 * @date 2018/3/8 下午3:37
 */
public class RxJavaActivity extends BaseActivity {
    private final static String TAG = "RxJavaActivity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rxjava;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListener() {

    }


    @OnClick({R.id.btn_do_in_io, R.id.btn_do_in_ui, R.id.btn_do_in_io_ui})
    void OnClick(View v) {
        switch(v.getId()) {
            case R.id.btn_do_in_io:
                RxJavaUtils.doInIOThread(new RxIOTask<String>("我是入参123") {
                    @Override
                    public Void doInIOThread(String s) {
                        Log.e(TAG, "[doInIOThread]  " + getLooperStatus() + ", 入参:" + s);
                        return null;
                    }
                });
                break;
            case R.id.btn_do_in_ui:
                RxJavaUtils.doInUIThread(new RxUITask<String>("我是入参456") {
                    @Override
                    public void doInUIThread(String s) {
                        Log.e(TAG, "[doInUIThread]  " + getLooperStatus() + ", 入参:" + s);
                    }
                });
                break;
            case R.id.btn_do_in_io_ui:
                RxJavaUtils.executeRxTask(new CommonRxTask<String, Integer>("我是入参789") {
                    @Override
                    public Integer doInIOThread(String s) {
                        Log.e(TAG, "[doInIOThread]  " + getLooperStatus() + ", 入参:" + s);
                        return 12345;
                    }

                    @Override
                    public void doInUIThread(Integer integer) {
                        Log.e(TAG, "[doInUIThread]  " + getLooperStatus() + ", 入参:" + integer);
                    }
                });
                break;
            default:
                break;
        }

    }

    /**
     * 获取当前线程的状态
     * @return
     */
    public String getLooperStatus() {
        return "当前线程状态：" + (Looper.myLooper() == Looper.getMainLooper() ? "主线程" : "子线程");
    }
}
