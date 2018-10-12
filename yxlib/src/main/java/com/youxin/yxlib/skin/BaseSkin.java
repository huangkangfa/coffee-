package com.youxin.yxlib.skin;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;


/**
 * Created by huangkangfa on 2018/9/30.
 */

public class BaseSkin {
    public static final String TAG="Skin";

    /*****提交与监听*****/

    public void commit(){
        LiveDataEventBus.with(BaseSkin.TAG).setValue(this);
    }

    public void registerChangeListener(@NonNull LifecycleOwner owner, @NonNull Observer<? extends BaseSkin> observer){
        LiveDataEventBus.with(BaseSkin.TAG, BaseSkin.class).observe(owner, (Observer<BaseSkin>) observer);
    }

    public void unRegisterChangeListener(@NonNull Observer<? extends BaseSkin> observer){
        LiveDataEventBus.with(BaseSkin.TAG,BaseSkin.class).removeObserver((Observer<BaseSkin>) observer);
    }
}
