package com.youxin.yxlib.util;

import android.os.Handler;
import android.os.Message;

/**
 * Created by huangkangfa on 2016/11/16 0016.
 */
public class MessageUtil {

    public static void sendMessage(Handler handler, int what, Object obj){
        Message msg=handler.obtainMessage();
        msg.what=what;
        msg.obj=obj;
        handler.sendMessage(msg);
    }

    public static void sendMessage(Handler handler, int what){
        Message msg=handler.obtainMessage();
        msg.what=what;
        handler.sendMessage(msg);
    }
}
