package com.youxin.app;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.youxin.app.viewmodel.VMRecentContact;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkangfa on 2018/9/29.
 */

public class YxObserverManager {

    private volatile static YxObserverManager mYxObserverManager;
    private Map<String,Observer> cache=new HashMap<>();

    //联系人、会话
    public final static String OBSERVER_RECENTCONTACT="observerRecentContact";

    private YxObserverManager() {
    }

    public static YxObserverManager getInstance() {
        if (mYxObserverManager == null) {
            synchronized (YxObserverManager.class) {
                if (mYxObserverManager == null) {
                    mYxObserverManager = new YxObserverManager();
                }
            }
        }
        return mYxObserverManager;
    }

    /**
     * 订阅联系人、会话变化
     * @param activity
     */
    public void registObserverRecentContact(final FragmentActivity activity){
        Observer observerRecentContact = new Observer<List<RecentContact>>() {
            @Override
            public void onEvent(List<RecentContact> messages) {
                ViewModelProviders.of(activity).get(VMRecentContact.class).getRecentContact().setValue(messages);
            }
        };
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(observerRecentContact, true);
        cache.put(OBSERVER_RECENTCONTACT,observerRecentContact);
    }


    /**
     * 移除订阅
     * @param flag
     */
    public void unRegistObserver(String... flag){
        if(flag.length>0){
            //逐条移除
            for(int i=0;i<flag.length;i++){
                NIMClient.getService(MsgServiceObserve.class).observeRecentContact(cache.get(flag[i]), false);
            }
        }else{
            //全部移除
            for(String key:cache.keySet()){
                NIMClient.getService(MsgServiceObserve.class).observeRecentContact(cache.get(key), false);
            }
        }

    }

}






