package com.youxin.yxlib.util.cache;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by huangkangfa on 2017/10/12.
 */

public class SPUtil {
    private final static String NAME="shared_prefs";

    private static SharedPreferences.Editor editor;
    private static SharedPreferences pref;

    private SPUtil(){}

    /**
     * 注意
     * @param context  application的context
     */
    public static void init(Context context){
        if(pref==null){
            synchronized (SPUtil.class){
                if(pref==null){
                    pref=context.getSharedPreferences(NAME,MODE_PRIVATE);
                    editor= pref.edit();
                }
            }
        }
    }

    private static void checkInit(){
        if(pref==null){
            throw new RuntimeException("please init SPUtil first");
        }
    }

    public static boolean putString(String key, String data){
        checkInit();
        editor.putString(key,data);
        return editor.commit();
    }

    public static String getString(String key, String defaultString){
        checkInit();
        return pref.getString(key,defaultString);
    }


    public static boolean putBoolean(String key, boolean data){
        checkInit();
        editor.putBoolean(key,data);
        return editor.commit();
    }

    public static boolean getBoolean(String key, boolean defaultBoolean){
        checkInit();
        return pref.getBoolean(key,defaultBoolean);
    }


    public static boolean putLong(String key, long data){
        checkInit();
        editor.putLong(key,data);
        return editor.commit();
    }

    public static long getLong(String key, long defaultLong){
        checkInit();
        return pref.getLong(key,defaultLong);
    }


    public static boolean putFloat(String key, float data){
        checkInit();
        editor.putFloat(key,data);
        return editor.commit();
    }

    public static float getFloat(String key, float defaultFloat){
        checkInit();
        return pref.getFloat(key,defaultFloat);
    }


    public static boolean putInt(String key, int data){
        checkInit();
        editor.putInt(key,data);
        return editor.commit();
    }

    public static int getInt(String key, int defaultInt){
        checkInit();
        return pref.getInt(key,defaultInt);
    }

    public static boolean clear(){
        return editor.clear().commit();
    }
}
