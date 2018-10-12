package com.youxin.yxlib.supper.http.callback;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.base.Request;
import com.youxin.yxlib.supper.http.convert.JsonConvert;

import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * Created by huangkangfa on 2018/9/21.
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    public JsonCallback() {
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        //这里做请求前的操作，比如设置头部token、加密等
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {
        //详细自定义的原理和文档，看这里： https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        JsonConvert<T> convert = new JsonConvert<>(type,clazz);
        return convert.convertResponse(response);
    }
}








