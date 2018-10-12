package com.youxin.app.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.youxin.app.model.User;

/**
 * Created by huangkangfa on 2018/9/28.
 */

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}
