package com.youxin.yxlib.view.status;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.youxin.yxlib.R;

public class StatusDelegate {

    private Context mContext;
    private LinearLayout mParentView;
    private View mStatusBar;

    /**
     * 初始化状态栏
     *
     * @param root
     * @param fragment
     * @return
     */
    public View init(View root, Fragment fragment) {
        mContext = fragment.getContext();

        mParentView = new LinearLayout(mContext);
        mParentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mParentView.setOrientation(LinearLayout.VERTICAL);
        mParentView.addView(root);

        //添加状态栏
        mStatusBar = new View(mContext);
        mStatusBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, QMUIStatusBarHelper.getStatusbarHeight(mContext)));
        mStatusBar.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        mParentView.addView(mStatusBar, 0);

        return mParentView;
    }


    public void hide() {
        mStatusBar.setVisibility(View.GONE);
    }

    public void show() {
        mStatusBar.setVisibility(View.VISIBLE);
    }

    public void setColorFromResources(int id) {
        mStatusBar.setBackgroundColor(mContext.getResources().getColor(id));
    }
    public void setColor(int color) {
        mStatusBar.setBackgroundColor(color);
    }
    public void setColor(String color) {
        mStatusBar.setBackgroundColor(Color.parseColor(color));
    }
}
