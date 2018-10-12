package com.youxin.app.config;

import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.youxin.yxlib.util.cache.SPUtil;

/**
 * Created by huangkangfa on 2018/9/29.
 */

public class YXCache {

    private final static String ACCOUNT="account";
    private final static String TOKEN="token";
    private final static String YX_ACCOUNT="yx_account";
    private final static String YX_TOKEN="yx_token";

    private static StatusBarNotificationConfig notificationConfig;

    public static void setNotificationConfig(StatusBarNotificationConfig notificationConfig) {
        YXCache.notificationConfig = notificationConfig;
    }

    public static StatusBarNotificationConfig getNotificationConfig() {
        return notificationConfig;
    }

    public static String getAccount() {
        return SPUtil.getString(ACCOUNT,null);
    }

    public static void setAccount(String account) {
        SPUtil.putString(ACCOUNT,account);
    }

    public static String getToken() {
        return SPUtil.getString(TOKEN,null);
    }

    public static void setToken(String token) {
        SPUtil.putString(TOKEN,token);
    }

    public static String getYxAccount() {
        return SPUtil.getString(YX_ACCOUNT,null);
    }

    public static void setYxAccount(String account) {
        SPUtil.putString(YX_ACCOUNT,account);
    }

    public static String getYxToken() {
        return SPUtil.getString(YX_TOKEN,null);
    }

    public static void setYxToken(String token) {
        SPUtil.putString(YX_TOKEN,token);
    }
}
