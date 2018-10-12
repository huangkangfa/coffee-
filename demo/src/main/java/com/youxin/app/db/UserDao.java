package com.youxin.app.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.youxin.app.model.User;

import java.util.List;

/**
 * Created by huangkangfa on 2018/9/28.
 */

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE name = :name")
    List<User> findByName(String name);

    @Query("SELECT * FROM user WHERE account = :account")
    List<User> findByAccount(String account);

    @Query("SELECT * FROM user WHERE name LIKE :name")
    List<User> findByNameFuzzy(String name);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}
