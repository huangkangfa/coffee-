package com.youxin.yxlib.util.phone;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * 术语和概念
 屏幕尺寸
 按屏幕对角测量的实际物理尺寸。
 为简便起见，Android 将所有实际屏幕尺寸分组为四种通用尺寸：小、 正常、大和超大。

 屏幕密度 dpi
 屏幕物理区域中的像素量；通常称为 dpi（每英寸 点数）。例如， 与“正常”或“高”密度屏幕相比，“低”密度屏幕在给定物理区域的像素较少。
 为简便起见，Android 将所有屏幕密度分组为六种通用密度： 低、中、高、超高、超超高和超超超高。

 方向
 从用户视角看屏幕的方向，即横屏还是 竖屏，分别表示屏幕的纵横比是宽还是高。请注意， 不仅不同的设备默认以不同的方向操作，而且 方向在运行时可随着用户旋转设备而改变。

 分辨率
 屏幕上物理像素的总数。添加对多种屏幕的支持时， 应用不会直接使用分辨率；而只应关注通用尺寸和密度组指定的屏幕 尺寸及密度。

 密度无关像素 (dp)
 在定义 UI 布局时应使用的虚拟像素单位，用于以密度无关方式表示布局维度 或位置。
 密度无关像素等于 160 dpi 屏幕上的一个物理像素，这是 系统为“中”密度屏幕假设的基线密度。在运行时，系统 根据使用中屏幕的实际密度按需要以透明方式处理 dp 单位的任何缩放 。dp 单位转换为屏幕像素很简单： px = dp * (dpi / 160)。 例如，在 240 dpi 屏幕上，1 dp 等于 1.5 物理像素。在定义应用的 UI 时应始终使用 dp 单位 ，以确保在不同密度的屏幕上正常显示 UI。
 dp和px的转换方程式  px = dp * (dpi / 160)



 注解:根据今日头条公布的适配方法完善的方法
 * px = dp * (dpi / 160)
 * Created by huangkangfa on 2018/9/10.


 如何使用?
 在初始化一次init()方法 之后再每一个使用的actvity或者是fragment中 调用setAutoDensityByWidth() 或者setAutoDensityByHeight()方法 注意每个activity中调用以最后一次为准
 例如我们公司的模板是1334*750 4.7英寸 所以
 AutoUtil.init(1337,750,4.7);
 AutoUtil.setAutoDensityByWidth(this,getApplication());


 */

public class AutoUtil {
    public static float commonScaleDensity;
    private static int targetWidth;
    private static int targetHeight;
    private static float size;
    private static int targetDpi;

    /**
     * 初始化 工具类 只需要初始化一次就可以了
     * @param targetWidth 适配模板的宽(px)
     * @param targetHeight 适配模板的高(px)
     * @param size 适配模板的尺寸(英寸),手机的对角线
     */
    public static void init(@NonNull int targetWidth, @NonNull int targetHeight, @NonNull float size){
        AutoUtil.targetWidth = targetWidth;
        AutoUtil.targetHeight = targetHeight;
        AutoUtil.size = size;
        //计算模板的dpi
        targetDpi = (int) (Math.sqrt(targetHeight^2+targetWidth^2)/size);
    }

    private static void setAutoDestentry(@NonNull Activity activity, final Application application, boolean isWidth) {
        if (targetDpi==0){
            throw new NullPointerException("你还没设置适配的模板,目标模板不能为空,请先初始化init方法");
        }
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (commonScaleDensity == 0) {
            commonScaleDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration configuration) {
                    if (configuration != null && configuration.fontScale > 0) {
                        commonScaleDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }
                @Override
                public void onLowMemory() {

                }
            });
        }
        float targetDensity;
        if (isWidth){
            targetDensity = (targetDpi/160)*appDisplayMetrics.widthPixels/targetWidth;
        }else {
            targetDensity = (targetDpi/160)*appDisplayMetrics.heightPixels/targetHeight;
        }
        int targetDensityDip = (int) (targetDensity * 160);
        float targetScaledDensity =targetDensity* commonScaleDensity / appDisplayMetrics.density;

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.densityDpi = targetDensityDip;
        appDisplayMetrics.scaledDensity =targetScaledDensity;

        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.densityDpi = targetDensityDip;

    }

    /**
     *以模板的高为标准适配
     * @param activity
     * @param application
     */
    public static void setAutoDensityByHeight(Activity activity, Application application){
        setAutoDestentry(activity,application,false);
    }

    /**
     * 以模板的宽为标准适配
     * @param activity
     * @param application
     */
    public static void setAutoDensityByWidth(Activity activity, Application application){
        setAutoDestentry(activity,application,true);
    }

}































