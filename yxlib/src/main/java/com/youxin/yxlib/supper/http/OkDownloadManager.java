package com.youxin.yxlib.supper.http;

import android.os.Environment;
import android.text.TextUtils;

import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.request.base.Request;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.lzy.okserver.task.XExecutor;

import java.io.Serializable;
import java.util.Map;

/**
 * 下载管理器
 * Created by huangkangfa on 2018/9/21.
 */
public class OkDownloadManager {

    private String cachePath = Environment.getExternalStorageDirectory().getPath() + "/download/";   //下载目录
    private int downloadNum = 3;  //同时下载数量
    private OkDownload mOkDownload;

    private volatile static OkDownloadManager instance;

    private OkDownloadManager() {
    }

    public static OkDownloadManager getInstance() {
        if (instance == null) {
            synchronized (OkDownloadManager.class) {
                if (instance == null) {
                    instance = new OkDownloadManager();
                    instance.mOkDownload = OkDownload.getInstance();
                    instance.mOkDownload.setFolder(instance.cachePath);
                    instance.mOkDownload.getThreadPool().setCorePoolSize(instance.downloadNum);
                }
            }
        }
        return instance;
    }

    /**
     * 获取当前所有下载任务
     * @return
     */
    public Map<String, DownloadTask> getTaskMap(){
        return instance.mOkDownload.getTaskMap();
    }

    /**
     * 设置下载目录
     *
     * @param path
     */
    public void setCachePath(String path) {
        instance.cachePath = path;
        instance.mOkDownload.setFolder(path);
    }

    /**
     * 获取下载目录
     *
     * @return
     */
    public String getCachePath() {
        return instance.cachePath;
    }

    /**
     * 设置同时下载的数量
     *
     * @param num
     */
    public void setDownloadNum(int num) {
        instance.downloadNum = num;
        instance.mOkDownload.getThreadPool().setCorePoolSize(num);
    }

    /**
     * 获取同时下载的数量
     *
     * @return
     */
    public int getDownloadNum() {
        return instance.downloadNum;
    }

    /**
     * 添加下载任务，并返回本身
     *
     * @param flag     任务标识
     * @param request  请求
     * @param priority 优先级
     * @param path     子文件夹目录
     * @param fileName 文件名称
     * @param listener 下载进度监听
     * @param objs     额外数据，最多传三个
     * @return
     */
    public DownloadTask addDownloadTask(String flag, Request request, int priority, String path, String fileName, DownloadListener listener, Serializable... objs) {
        DownloadTask dt = OkDownload.request(flag, request).priority(priority);
        if (!TextUtils.isEmpty(path))
            dt.folder(path);
        if (!TextUtils.isEmpty(fileName))
            dt.fileName(fileName);
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

    public DownloadTask addDownloadTask(String flag, Request request) {
        return addDownloadTask(flag, request, null);
    }

    public DownloadTask addDownloadTask(String flag, Request request, DownloadListener listener) {
        return addDownloadTask(flag, request, null, listener);
    }

    public DownloadTask addDownloadTask(String flag, Request request, String fileName, DownloadListener listener) {
        return addDownloadTask(flag, request, null, fileName, listener);
    }

    public DownloadTask addDownloadTask(String flag, Request request, String path, String fileName, DownloadListener listener) {
        return addDownloadTask(flag, request, 0, path, fileName, listener);
    }

    /**
     * 获取特定标识的下载任务
     *
     * @param flag
     * @return
     */
    public DownloadTask getDownloadTask(String flag) {
        return instance.mOkDownload.getTask(flag);
    }

    /**
     * 移除特定标识的下载任务
     *
     * @param flag
     */
    public void removeDownloadTask(String flag) {
        instance.mOkDownload.removeTask(flag);
    }

    /**
     * 移除所有下载任务
     *
     * @param needDeleteLocal 是否删除已经下载的本地文件
     */
    public void removeAllDownloadTask(boolean needDeleteLocal) {
        instance.mOkDownload.removeAll(needDeleteLocal);
    }

    /**
     * 开始所有下载任务
     */
    public void startAllDownloadTask() {
        instance.mOkDownload.startAll();
    }

    /**
     * 暂停所有下载任务
     */
    public void pauseAllDownloadTask() {
        instance.mOkDownload.pauseAll();
    }

    /**
     * 是否有该标识的任务存在
     *
     * @param flag
     * @return
     */
    public boolean hasDownloadTask(String flag) {
        return instance.mOkDownload.hasTask(flag);
    }

    /**
     * 设置所有任务完成监听
     *
     * @param listener
     */
    public void setOnAllTaskEndListener(XExecutor.OnAllTaskEndListener listener) {
        instance.mOkDownload.addOnAllTaskEndListener(listener);
    }

    /**
     * 移除所有任务完成监听
     *
     * @param listener
     */
    public void removeOnAllTaskEndListener(XExecutor.OnAllTaskEndListener listener) {
        instance.mOkDownload.removeOnAllTaskEndListener(listener);
    }

    /**
     * 获取下载管理器的数据库
     * 可以进行增删改查操作
     * @return
     */
    public DownloadManager getDownloadManager(){
        return DownloadManager.getInstance();
    }

}





