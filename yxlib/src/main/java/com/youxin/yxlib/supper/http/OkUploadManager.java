package com.youxin.yxlib.supper.http;

import com.lzy.okgo.db.UploadManager;
import com.lzy.okgo.request.base.Request;
import com.lzy.okserver.OkUpload;
import com.lzy.okserver.task.XExecutor;
import com.lzy.okserver.upload.UploadListener;
import com.lzy.okserver.upload.UploadTask;

import java.io.Serializable;
import java.util.Map;

/**
 * 上传管理器
 * Created by huangkangfa on 2018/9/21.
 */
public class OkUploadManager {

    private volatile static OkUploadManager instance;

    private int uploadNum = 3;  //同时上传数量
    private OkUpload mOkUpload;

    private OkUploadManager() {
    }

    public static OkUploadManager getInstance() {
        if (instance == null) {
            synchronized (OkUploadManager.class) {
                if (instance == null) {
                    instance = new OkUploadManager();
                    instance.mOkUpload=OkUpload.getInstance();
                    instance.mOkUpload.getThreadPool().setCorePoolSize(instance.uploadNum);
                }
            }
        }
        return instance;
    }

    /**
     * 获取当前所有上传任务
     * @return
     */
    public Map<String, UploadTask<?>> getTaskMap(){
        return instance.mOkUpload.getTaskMap();
    }

    /**
     * 设置所有任务完成监听
     *
     * @param listener
     */
    public void setOnAllTaskEndListener(XExecutor.OnAllTaskEndListener listener) {
        instance.mOkUpload.addOnAllTaskEndListener(listener);
    }

    /**
     * 移除所有任务完成监听
     *
     * @param listener
     */
    public void removeOnAllTaskEndListener(XExecutor.OnAllTaskEndListener listener) {
        instance.mOkUpload.removeOnAllTaskEndListener(listener);
    }

    /**
     * 设置同时上传的数量
     *
     * @param num
     */
    public void setUploadNum(int num) {
        instance.uploadNum = num;
        instance.mOkUpload.getThreadPool().setCorePoolSize(num);
    }

    /**
     * 获取同时上传的数量
     *
     * @return
     */
    public int getUploadNum() {
        return instance.uploadNum;
    }

    /**
     * 添加上传任务，并返回本身
     *
     * @param flag     任务标识
     * @param request  请求
     * @param priority 优先级
     * @param listener 上传进度监听
     * @param objs     额外数据，最多传三个
     * @return
     */
    public UploadTask addUploadTask(String flag, Request request, int priority, UploadListener listener, Serializable... objs) {
        UploadTask dt = OkUpload.request(flag, request).priority(priority);
        if (objs.length > 0) {
            if (objs.length >= 1) {
                dt.extra1(objs[0]);
                if (objs.length >= 2) {
                    dt.extra2(objs[1]);
                    if (objs.length >= 3) {
                        dt.extra3(objs[2]);
                    }
                }
            }
        }
        dt.save();
        if (listener != null)
            dt.register(listener);
        return dt;
    }

    public UploadTask addUploadTask(String flag, Request request) {
        return addUploadTask(flag, request, null);
    }

    public UploadTask addUploadTask(String flag, Request request, UploadListener listener) {
        return addUploadTask(flag, request, 0, listener);
    }

    /**
     * 获取特定标识的上传任务
     *
     * @param flag
     * @return
     */
    public UploadTask getUploadTask(String flag) {
        return instance.mOkUpload.getTask(flag);
    }

    /**
     * 移除特定标识的上传任务
     *
     * @param flag
     */
    public void removeUploadTask(String flag) {
        instance.mOkUpload.removeTask(flag);
    }

    /**
     * 移除所有上传任务
     *
     */
    public void removeAllUploadTask() {
        instance.mOkUpload.removeAll();
    }

    /**
     * 开始所有上传任务
     */
    public void startAllUploadTask() {
        instance.mOkUpload.startAll();
    }

    /**
     * 暂停所有上传任务
     */
    public void pauseAllUploadTask() {
        instance.mOkUpload.pauseAll();
    }

    /**
     * 是否有该标识的任务存在
     *
     * @param flag
     * @return
     */
    public boolean hasUploadTask(String flag) {
        return instance.mOkUpload.hasTask(flag);
    }

    /**
     * 获取上传管理器的数据库
     * 可以进行增删改查操作
     * @return
     */
    public UploadManager getUploadManager(){
        return UploadManager.getInstance();
    }

}
