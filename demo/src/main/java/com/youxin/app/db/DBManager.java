package com.youxin.app.db;

import android.arch.persistence.room.Room;
import android.content.Context;

/**
 * Created by huangkangfa on 2018/9/28.
 */

public class DBManager {
    private final static String DB_NAME = "db_name";
    private volatile static DBManager mDBManager;
    private static AppDatabase db;

    private DBManager() {
    }

    private DBManager(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
//        db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).addMigrations(MIGRATION_1_2).build();
    }

    public static DBManager newInstance(Context context) {
        if (mDBManager == null) {
            synchronized (DBManager.class) {
                if (mDBManager == null) {
                    mDBManager = new DBManager(context);
                }
            }
        }
        return mDBManager;
    }

    public static AppDatabase getDB() {
        if (mDBManager == null || db == null) {
            throw new RuntimeException(" please  newInstance the DBManager first");
        }
        return db;
    }

    /******数据库版本更替******/

//    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("CREATE TABLE IF NOT EXISTS `User`(`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , `first_name` TEXT , `last_name` TEXT )");
//        }
//    };

}