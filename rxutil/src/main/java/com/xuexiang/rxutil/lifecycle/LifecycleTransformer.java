package com.xuexiang.rxutil.lifecycle;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * 生命周期转化器
 *
 * @author xuexiang
 * @since 2018/6/11 上午12:50
 */
public class LifecycleTransformer<T> implements Observable.Transformer<T, T> {
    private Observable<ActivityLifecycle> mLifecycleObservable;
    private ActivityLifecycle mActivityLifecycle;

    LifecycleTransformer(Observable<ActivityLifecycle> lifecycleObservable) {
        mLifecycleObservable = lifecycleObservable.share();
    }

    LifecycleTransformer(Observable<ActivityLifecycle> lifecycleObservable, ActivityLifecycle activityLifecycle) {
        mLifecycleObservable = lifecycleObservable;
        mActivityLifecycle = activityLifecycle;
    }

    @Override
    public Observable<T> call(Observable<T> sourceObservable) {
        return sourceObservable.takeUntil(getLifecycleObservable());
    }

    @NonNull
    private Observable<?> getLifecycleObservable() {
        if (mActivityLifecycle != null) {
            return mLifecycleObservable.takeFirst(new Func1<ActivityLifecycle, Boolean>() {
                @Override
                public Boolean call(ActivityLifecycle event) {
                    return mActivityLifecycle == event;
                }
            });
        }

        return Observable.combineLatest(mLifecycleObservable.first().map(ACTIVITY_LIFECYCLE),
                mLifecycleObservable.skip(1), new Func2<ActivityLifecycle, ActivityLifecycle, Boolean>() {
                    @Override
                    public Boolean call(ActivityLifecycle activityLifecycle, ActivityLifecycle event) {
                        return activityLifecycle == event;
                    }
                })
                .takeFirst(new Func1<Boolean, Boolean>() {
                    @Override
                    public Boolean call(Boolean aBoolean) {
                        return aBoolean;
                    }
                });
    }


    // Figures out which corresponding next lifecycle event in which to unsubscribe, for Activities
    private static final Func1<ActivityLifecycle, ActivityLifecycle> ACTIVITY_LIFECYCLE =
            new Func1<ActivityLifecycle, ActivityLifecycle>() {
                @Override
                public ActivityLifecycle call(ActivityLifecycle lastEvent) {
                    switch (lastEvent) {
                        case onCreate:
                            return ActivityLifecycle.onDestroy;
                        case onStart:
                            return ActivityLifecycle.onStop;
                        case onResume:
                            return ActivityLifecycle.onPause;
                        case onPause:
                            return ActivityLifecycle.onStop;
                        case onStop:
                            return ActivityLifecycle.onDestroy;
                        case onDestroy:
                            throw new IllegalStateException("Cannot injectRxLifecycle to Activity lifecycle when outside of it.");
                        default:
                            throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
                    }
                }
            };
}
