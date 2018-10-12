package com.youxin.yxlib.supper.http.convert;

import android.support.annotation.NonNull;

import com.lzy.okgo.model.Response;
import com.youxin.yxlib.supper.http.response.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huangkangfa on 2018/9/21.
 */

public class Transformer {

    public static <T> ObservableTransformer<Response<BaseResponse<T>>, Response<BaseResponse<T>>> switchSchedulers(final Consumer<Disposable> mConsumer) {
        return new ObservableTransformer<Response<BaseResponse<T>>, Response<BaseResponse<T>>>() {
            @Override
            public ObservableSource<Response<BaseResponse<T>>> apply(@NonNull Observable<Response<BaseResponse<T>>> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(mConsumer)
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<Response<BaseResponse<T>>, Response<BaseResponse<T>>> switchSchedulers() {
        return new ObservableTransformer<Response<BaseResponse<T>>, Response<BaseResponse<T>>>() {
            @Override
            public ObservableSource<Response<BaseResponse<T>>> apply(@NonNull Observable<Response<BaseResponse<T>>> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
