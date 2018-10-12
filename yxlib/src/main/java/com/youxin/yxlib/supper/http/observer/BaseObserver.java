package com.youxin.yxlib.supper.http.observer;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by huangkangfa on 2018/9/21.
 */

public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onComplete() {

    }
}
