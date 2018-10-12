package com.netease.nim.uikit.common.util.sys;

import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by huangkangfa on 2018/10/10.
 */

public class ViewLockUtils {
    /**
     * 锁定内容View以防止跳闪
     */
    public static void lockContentView(View contentView) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) contentView.getLayoutParams();
        layoutParams.height = contentView.getHeight();
        layoutParams.width = contentView.getWidth();
        layoutParams.weight = 0;
    }

    /**
     * 释放锁定的内容View
     */
    public static void unlockContentView(View contentView) {
        ((LinearLayout.LayoutParams) contentView.getLayoutParams()).weight = 1;
    }
}
