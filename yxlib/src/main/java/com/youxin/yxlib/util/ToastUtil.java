package com.youxin.yxlib.util;

import android.content.Context;
import android.widget.Toast;


/**
 * Toast统一管理类
 */
public class ToastUtil {

    /*cannot be instantiated*/
    private ToastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }
}