package com.youxin.yxlib.util.bitmap;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.youxin.yxlib.util.phone.SDCardUtil;

@GlideModule
public class BitmapModel extends AppGlideModule {
    public final static String GLIDE_DISCACHE_DIR = "/glide_cache_dir";

    private static final int M = 1024 * 1024;
    private static final int MAX_DISK_CACHE_SIZE = 256 * M;

    /**
     * ************************ Memory Cache ************************
     */

    public static void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * ************************ GlideModule override ************************
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

        if (SDCardUtil.isSDCardEnable()) {
            builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, GLIDE_DISCACHE_DIR, MAX_DISK_CACHE_SIZE));
        } else {
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context, GLIDE_DISCACHE_DIR, MAX_DISK_CACHE_SIZE));
        }

    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}
