package com.youxin.app.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.netease.nimlib.sdk.uinfo.model.UserInfo;

/**
 * Created by huangkangfa on 2018/9/28.
 */

@Entity(tableName = "User")
public class User implements UserInfo{
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "account")
    private String account;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "avatar")
    private String avatar;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
