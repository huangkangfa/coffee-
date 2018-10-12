package com.youxin.app.config.skin;


import com.youxin.app.R;
import com.youxin.yxlib.skin.BaseSkin;

/**
 * Created by huangkangfa on 2018/9/30.
 */

public class NiceSkinConfig extends BaseSkin {
    private static volatile NiceSkinConfig mSkinConfig;

    private NiceSkinConfig(){}

    public static NiceSkinConfig newInstance(){
        if(mSkinConfig==null){
            synchronized (NiceSkinConfig.class){
                if(mSkinConfig==null){
                    mSkinConfig=new NiceSkinConfig();
                }
            }
        }
        return mSkinConfig;
    }

    /******参数设置******/

    private int statusBarBg= R.color.qmui_config_color_transparent;
    private int statusBarTextColor=R.color.qmui_config_color_white;
    private int topBarBg= R.color.colorPrimary;
    private int topBarTextColor= R.color.qmui_config_color_white;
    private int contentBg=R.color.qmui_config_color_white;
    private int contentTextColor=R.color.qmui_config_color_black;

    public int getTopBarBg() {
        return topBarBg;
    }

    public int getStatusBarBg() {
        return statusBarBg;
    }

    public int getStatusBarTextColor() {
        return statusBarTextColor;
    }

    public int getTopBarTextColor() {
        return topBarTextColor;
    }

    public int getContentBg() {
        return contentBg;
    }

    public int getContentTextColor() {
        return contentTextColor;
    }

    public NiceSkinConfig setTopBarBg(int topBarBg) {
        this.topBarBg = topBarBg;
        return mSkinConfig;
    }

    public NiceSkinConfig setStatusBarBg(int statusBarBg) {
        this.statusBarBg = statusBarBg;
        return mSkinConfig;
    }

    public NiceSkinConfig setStatusBarTextColor(int statusBarTextColor) {
        this.statusBarTextColor = statusBarTextColor;
        return mSkinConfig;
    }

    public NiceSkinConfig setTopBarTextColor(int topBarTextColor) {
        this.topBarTextColor = topBarTextColor;
        return mSkinConfig;
    }

    public NiceSkinConfig setContentBg(int contentBg) {
        this.contentBg = contentBg;
        return mSkinConfig;
    }

    public NiceSkinConfig setContentTextColor(int contentTextColor) {
        this.contentTextColor = contentTextColor;
        return mSkinConfig;
    }
}
