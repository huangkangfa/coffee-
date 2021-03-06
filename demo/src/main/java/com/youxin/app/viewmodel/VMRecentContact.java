package com.youxin.app.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.List;

public class VMRecentContact extends ViewModel {
    private MutableLiveData<List<RecentContact>> mLiveRecentContact=new MutableLiveData<>();

    public MutableLiveData<List<RecentContact>> getRecentContact() {
        return mLiveRecentContact;
    }

    public void loadRecentContact() {
        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable e) {
                        // recents参数即为最近联系人列表（最近会话列表）
                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                            return;
                        }
                        mLiveRecentContact.setValue(recents);
                    }
                });
    }
}