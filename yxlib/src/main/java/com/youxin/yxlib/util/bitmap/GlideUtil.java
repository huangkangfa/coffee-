package com.youxin.yxlib.util.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.youxin.yxlib.util.phone.SDCardUtil;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Glide 和 RecyclerView 结合使用出现卡顿   详见 https://blog.csdn.net/Sean_css/article/details/79963903
 * Created by huangkangfa on 2018/9/27.
 */

public class GlideUtil {

    /**
     * 加载图片到指定imageview
     *
     * @param context
     * @param url
     * @param imageView
     * @param options
     */
    public static void load(Context context, String url, ImageView imageView, RequestOptions options) {
        Glide.with(context).load(url).apply(options).into(imageView);
    }
    public static void load(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

    /**
     * 获取指定图片Drawable，然后自己回调处理
     *
     * @param context
     * @param url
     * @param simpleTarget
     * @param options
     */
    public static void load(Context context, String url, SimpleTarget<Drawable> simpleTarget, RequestOptions options) {
        Glide.with(context).load(url).apply(options).into(simpleTarget);
    }
    public static void load(Context context, String url, SimpleTarget<Drawable> simpleTarget) {
        Glide.with(context).load(url).into(simpleTarget);
    }

    /**
     * 预加载图片
     *
     * @param context
     * @param url
     */
    public static void preload(Context context, String url) {
        Glide.with(context).load(url).preload();
    }

    /**
     * 获取图片的下载文件信息
     * 此方法会堵塞直到下载文件完成后返回
     *
     * @param context
     * @param url
     * @return
     */
    public static File getFile(Context context, String url) {
        try {
            return Glide.with(context).asFile().load(url).submit().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Bitmap getBitmap(Context context, String url) {
        try {
            return Glide.with(context).asBitmap().load(url).submit().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Drawable getDrawable(Context context, String url) {
        try {
            return Glide.with(context).asDrawable().load(url).submit().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String CENTERCROP = "CenterCrop";
    public static final String FITCENTER = "FitCenter";
    public static final String CIRCLECROP = "CircleCrop";

    /**
     * 获取RequestOptions
     *
     * @param type
     * @return
     */
    public static RequestOptions getRequestOptions(String type) {
        RequestOptions options = new RequestOptions();
        switch (type) {
            case CENTERCROP:
                options.centerCrop();
                break;
            case FITCENTER:
                options.fitCenter();
                break;
            case CIRCLECROP:
                options.circleCrop();
                break;
        }
        return options;
    }

    /**
     * 清空缓存
     * @param context
     */
    public static void clear(Context context){
        BitmapModel.clearMemoryCache(context);
    }

    /**
     * 获取glide缓存目录
     *
     * @param context context
     * @return 缓存目录
     */
    public static File getGlideDiskCacheDir(Context context) {
        String path;
        if (SDCardUtil.isSDCardEnable()) {
            path = context.getExternalCacheDir() + BitmapModel.GLIDE_DISCACHE_DIR;
        } else {
            path = context.getCacheDir() + BitmapModel.GLIDE_DISCACHE_DIR;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsoluteFile();
    }


    /**
     * 获取Glide缓存大小
     *
     * @param context context
     * @return 缓存大小
     */
    public static String getGlidecacheFileSizeStr(Context context) {
        long fileSize = getGlidecacheFileSizeNum(context);
        return Formatter.formatFileSize(context, fileSize);
    }

    public static long getGlidecacheFileSizeNum(Context context) {
        long fileSize = 0;
        File file = getGlideDiskCacheDir(context);
        for (File childFile : file.listFiles()) {
            fileSize += childFile.length();
        }
        return fileSize;
    }

}
