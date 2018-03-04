# RxUtil
[![RxUtil][rxSvg]][rx]  [![api][apiSvg]][api]

一个实用的RxJava1工具类库
## 关于我
[![github](https://img.shields.io/badge/GitHub-xuexiangjys-blue.svg)](https://github.com/xuexiangjys)   [![csdn](https://img.shields.io/badge/CSDN-xuexiangjys-green.svg)](http://blog.csdn.net/xuexiangjys)

## 内容
- RxBus 支持多事件定义，支持数据携带，支持全局和局部的事件订阅和注销
- 订阅池管理
- 线程调度辅助工具
- RxBinding 使用工具类
- RxJava常用方法工具类

## 1、演示



## 2、如何使用
目前支持主流开发工具AndroidStudio的使用，直接配置build.gradle，增加依赖即可.

### 2.1、Android Studio导入方法，添加Gradle依赖

先在项目根目录的 build.gradle 的 repositories 添加:
```
allprojects {
     repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

然后在dependencies添加:

```
dependencies {
   ...
   implementation 'io.reactivex:rxjava:1.2.9'
   implementation 'io.reactivex:rxandroid:1.2.1'
   implementation 'com.github.xuexiangjys:RxUtil:1.0'
}
```
### 2.1、RxBus使用

#### 2.1.1、事件注册订阅

1.使用RxBusUtils.get().onMainThread方法注册事件，并指定订阅发生在主线程。

```
RxBusUtils.get().onMainThread(EventKey.EVENT_HAVE_DATA, new Action1<Event>() {
            @Override
            public void call(Event event) {
                showContent(EventKey.EVENT_HAVE_DATA, event.toString());
            }
        });
```
2.使用RxBusUtils.get().on方法注册事件，订阅所在线程为事件发生线程，也可指定订阅发生的线程。

```
RxBusUtils.get().onMainThread(EventKey.EVENT_HAVE_DATA, new Action1<Event>() {
            @Override
            public void call(Event event) {
                showContent(EventKey.EVENT_HAVE_DATA, event.toString());
            }
        });
```

#### 2.1.2、事件发送

1.使用RxBusUtils.get().post(Object eventName)发送不带数据的事件。
```
RxBusUtils.get().post(EventKey.EVENT_NO_DATA);
```

2.使用RxBusUtils.get().post(Object eventName, Object content)发送携带数据的事件。
```
RxBusUtils.get().post(EventKey.EVENT_HAVE_DATA, new Event(EventKey.EVENT_HAVE_DATA, "这里携带的是数据"));
RxBusUtils.get().post(EventKey.EVENT_HAVE_DATA, true);
```

#### 2.1.3、事件注销

1.使用RxBusUtils.get().unregisterAll(Object eventName)取消事件的所有订阅并注销事件。
```
RxBusUtils.get().unregisterAll(EventKey.EVENT_HAVE_DATA);
```

2.使用RxBusUtils.get().unregister(Object eventName, SubscribeInfo subscribeInfo)取消事件的某个指定订阅。
SubscribeInfo是事件注册订阅后返回的订阅信息。如果在取消该订阅后，该事件如无其他订阅，便自动注销该事件。
```
RxBusUtils.get().unregister(EventKey.EVENT_CLEAR, mSubscribeInfo);
```


[rxSvg]: https://img.shields.io/badge/XLog-v1.0.1-brightgreen.svg
[rx]: https://github.com/xuexiangjys/XLog
[apiSvg]: https://img.shields.io/badge/API-14+-brightgreen.svg
[api]: https://android-arsenal.com/api?level=14

