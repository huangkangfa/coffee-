package com.youxin.yxlib.base;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;


/**
 * PopupWindow基类
 * @author huangkangfa 2015年8月18日 下午4:57:38
 */
public class BasePopupWindow extends PopupWindow {
    /**
     * Special value for the height or width requested by a View.
     * MATCH_PARENT means that the view wants to be as big as its parent,
     * minus the parent's padding, if any. Introduced in API Level 8.
     */
    public static final int MATCH_PARENT = -1;

    /**
     * Special value for the height or width requested by a View.
     * WRAP_CONTENT means that the view wants to be just large enough to fit
     * its own internal content, taking its own padding into account.
     */
    public static final int WRAP_CONTENT = -2;

    public Activity mActivity;
    public LayoutInflater mInflater;
    private View contentView;

    public BasePopupWindow(Activity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
        this.mInflater = mActivity.getLayoutInflater();
    }

    public void setContentView(int resource) {
        setContentView(resource, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
    }

    /**
     * 设置contentView
     * @param resource viewID
     * @param width
     * @param height
     * @param focusable 如果是false的话PopUpWindow只是一个浮现在当前界面上的view而已，不影响当前界面的任何操作。一般情况下设置true
     */
    public void setContentView(int resource, int width, int height, boolean focusable) {
        contentView = mInflater.inflate(resource, null);
        setContentView(contentView);
        setWidth(width);
        setHeight(height);
        setFocusable(focusable);

        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable(mActivity.getResources(), (Bitmap) null));
    }

    public View findViewById(int id) {
        return contentView.findViewById(id);
    }
}
