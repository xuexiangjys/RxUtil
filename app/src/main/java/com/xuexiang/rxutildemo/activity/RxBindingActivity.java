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

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.xuexiang.rxutil.RxBindingUtils;
import com.xuexiang.rxutil.rxjava.SubscriptionPool;
import com.xuexiang.rxutil.subsciber.SimpleThrowableAction;
import com.xuexiang.rxutildemo.R;
import com.xuexiang.rxutildemo.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

/**
 * @author xuexiang
 * @date 2018/3/11 下午11:39
 */
public class RxBindingActivity extends BaseActivity {

    @BindView(R.id.btn_click)
    Button mBtnClick;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_rxbinding;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {

    }

    /**
     * 初始化监听
     */
    @Override
    protected void initListener() {
        RxBindingUtils.setViewClicks(mBtnClick, 5, TimeUnit.SECONDS, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                toast("触发点击");
            }
        });

        SubscriptionPool.get().add(RxBindingUtils.textChanges(mEtInput, 1, TimeUnit.SECONDS, new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                toast("输入内容:" + charSequence);
            }
        }), "textChanges");


        SubscriptionPool.get().add(Observable.combineLatest(RxBindingUtils.textChanges(mEtUsername), RxBindingUtils.textChanges(mEtPassword), new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2) {
                return !TextUtils.isEmpty(mEtUsername.getText()) && !TextUtils.isEmpty(mEtPassword.getText());
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                mBtnLogin.setEnabled(aBoolean);
            }
        }, new SimpleThrowableAction("RxBindingActivity")), "combineLatest");

    }

    @Override
    protected void onDestroy() {
        SubscriptionPool.get().remove("textChanges");
        SubscriptionPool.get().remove("combineLatest");
        super.onDestroy();
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        toast("登录");
    }
}
