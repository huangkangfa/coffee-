package com.youxin.app;

import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.youxin.app.config.NimSdkConfig;
import com.youxin.app.config.YXCache;
import com.youxin.app.db.DBManager;
import com.youxin.app.sdk_supper.push.handler.YxMixPushMessageHandler;
import com.youxin.yxlib.BaseApplication;

/**
 * Created by huangkangfa on 2018/9/28.
 */

public class YXApplication extends BaseApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        //数据库初始化
        DBManager.newInstance(this);
        //初始化易云sdk
        initNimSdk();
    }

    private void initNimSdk(){
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, getLoginInfo(), NimSdkConfig.getSDKOptions());

        if (NIMUtil.isMainProcess(this)) {
            // 注意：以下操作必须在主进程中进行
            // 注册自定义推送消息处理，这个是可选项
            NIMPushClient.registerMixPushMessageHandler(new YxMixPushMessageHandler());
        }
    }

    private LoginInfo getLoginInfo() {
        String account = YXCache.getAccount();
        String token = YXCache.getToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }
}
