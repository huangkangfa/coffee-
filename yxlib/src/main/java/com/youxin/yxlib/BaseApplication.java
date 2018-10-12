package com.youxin.yxlib;

import android.app.Application;
import android.content.Context;

import com.youxin.yxlib.supper.http.OkGoRxManager;
import com.youxin.yxlib.util.cache.SPUtil;
import com.youxin.yxlib.util.phone.AutoUtil;
import com.youxin.yxlib.util.phone.ScreenUtil;

/**
 * Created by huangkangfa on 2018/9/28.
 */

public class BaseApplication extends Application{
    private static Context context;

    //默认设计图设置
    private static final int targetWidth = 1337;  //设计图宽度
    private static final int targetHeight = 750;  //设计图高度
    private static final float size = 4.7f;       //对角线英寸

    @Override
    public void onCreate() {
        super.onCreate();
        this.context=this;
        //屏幕工具初始化
        ScreenUtil.init(this);
        //今日头条屏幕适配
        AutoUtil.init(targetWidth, targetHeight, size);
        //sharedpreferences缓存初始化
        SPUtil.init(context);
        //okgo初始化
        OkGoRxManager.init(this);
    }

    public static Context getContext(){
        return context;
    }
}
