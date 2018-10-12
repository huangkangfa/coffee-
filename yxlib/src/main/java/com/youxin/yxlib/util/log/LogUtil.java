package com.youxin.yxlib.util.log;

import android.util.Log;

/**
 * 日志打印管理
 * Created by Administrator on 2017/5/3 0003.
 */
public class LogUtil {
    public static boolean isDebug = true;  //是否打印日志
    public static String defaultStr = "LogUtil"; //默认标识

    /**
     * 若字符串参数只有一个，那么就是打印的参数
     * 若2个以上，那么第一个就是打印标识，第二个就是打印内容
     *
     * @param s
     */
    public static void e(String... s) {
        if (isDebug) {
            if (s.length > 1) {
                Log.e(s[0], s[1]);
            } else if (s.length == 1) {
                Log.e(defaultStr, s[0]);
            }
        }
    }

    /**
     * 若字符串参数只有一个，那么就是打印的参数
     * 若2个以上，那么第一个就是打印标识，第二个就是打印内容
     *
     * @param s
     */
    public static void v(String... s) {
        if (isDebug) {
            if (s.length > 1) {
                Log.v(s[0], s[1]);
            } else if (s.length == 1) {
                Log.v(defaultStr, s[0]);
            }
        }
    }

    /**
     * 若字符串参数只有一个，那么就是打印的参数
     * 若2个以上，那么第一个就是打印标识，第二个就是打印内容
     *
     * @param s
     */
    public static void d(String... s) {
        if (isDebug) {
            if (s.length > 1) {
                Log.d(s[0], s[1]);
            } else if (s.length == 1) {
                Log.d(defaultStr, s[0]);
            }
        }
    }

    /**
     * 若字符串参数只有一个，那么就是打印的参数
     * 若2个以上，那么第一个就是打印标识，第二个就是打印内容
     *
     * @param s
     */
    public static void i(String... s) {
        if (isDebug) {
            if (s.length > 1) {
                Log.i(s[0], s[1]);
            } else if (s.length == 1) {
                Log.i(defaultStr, s[0]);
            }
        }
    }

    /**
     * 若字符串参数只有一个，那么就是打印的参数
     * 若2个以上，那么第一个就是打印标识，第二个就是打印内容
     *
     * @param s
     */
    public static void w(String... s) {
        if (isDebug) {
            if (s.length > 1) {
                Log.w(s[0], s[1]);
            } else if (s.length == 1) {
                Log.w(defaultStr, s[0]);
            }
        }
    }

    /**
     * 若字符串参数只有一个，那么就是打印的参数
     * 若2个以上，那么第一个就是打印标识，第二个就是打印内容
     *
     * @param s
     */
    public static void wtf(String... s) {
        if (isDebug) {
            if (s.length > 1) {
                Log.wtf(s[0], s[1]);
            } else if (s.length == 1) {
                Log.wtf(defaultStr, s[0]);
            }
        }
    }
}
