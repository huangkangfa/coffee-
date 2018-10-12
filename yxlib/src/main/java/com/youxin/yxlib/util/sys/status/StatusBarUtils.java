package com.youxin.mylib.util.status;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.youxin.mylib.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 状态栏及虚拟底部栏设置工具类
 * <p>
 * Created by huangkangfa on 2017/10/12.
 */

public class StatusBarUtils {
    private static final String TAG_FAKE_STATUS_BAR_VIEW = "TAG_FAKE_STATUS_BAR_VIEW";
    private static final String TAG_MARGIN_ADDED = "TAG_MARGIN_ADDED";

    /**
     * 是否隐藏顶部状态栏
     * @param activity
     * @param enable
     */
    public static void hideStatusBar(Activity activity, boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(lp);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = activity.getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(attr);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 修改状态栏颜色
     *
     * @param activity
     * @param statusColor
     */
    public static void setBackgroundColor(Activity activity, int statusColor, boolean isNeedStatusBarHeight) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上处理方式
            Window window = activity.getWindow();
            //取消状态栏透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(statusColor);
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            //让view不根据系统窗口来调整自己的布局
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, isNeedStatusBarHeight);
                ViewCompat.requestApplyInsets(mChildView);
            }
        } else {
            //4.4~5.0处理方式
            Window window = activity.getWindow();
            //设置Window为全透明
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            //获取父布局
            View mContentChild = mContentView.getChildAt(0);
            //获取状态栏高度
            int statusBarHeight = getStatusBarHeight();

            //如果已经存在假状态栏则移除，防止重复添加
            removeFakeStatusBarViewIfExist(activity);
            //添加一个View来作为状态栏的填充
            addFakeStatusBarView(activity, statusColor, statusBarHeight);
            //设置子控件到状态栏的间距
            addMarginTopToContentChild(mContentChild, statusBarHeight);
            //不预留系统栏位置
            if (mContentChild != null) {
                ViewCompat.setFitsSystemWindows(mContentChild, false);
            }
            //如果在Activity中使用了ActionBar则需要再将布局与状态栏的高度跳高一个ActionBar的高度，否则内容会被ActionBar遮挡
            int action_bar_id = activity.getResources().getIdentifier("action_bar", "id", activity.getPackageName());
            View view = activity.findViewById(action_bar_id);
            if (view != null) {
                TypedValue typedValue = new TypedValue();
                if (activity.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
                    int actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, activity.getResources().getDisplayMetrics());
                    setContentTopPadding(activity, actionBarHeight);
                }
            }
        }
    }


    /**
     * 状态栏透明
     *
     * @param activity
     * @param isNeedStatusBarHeight         是否保留状态栏所占的空间
     * @param hideStatusBarBackground 是否是全透明  全透明、半透明
     */
    public static void setBackgroundTranslucent(Activity activity, boolean isNeedStatusBarHeight, boolean... hideStatusBarBackground) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上处理方式
            Window window = activity.getWindow();
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (hideStatusBarBackground != null && hideStatusBarBackground.length > 0 && hideStatusBarBackground[0]) {
                //如果为全透明模式，取消设置Window半透明的Flag
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //设置状态栏为透明
                window.setStatusBarColor(Color.TRANSPARENT);
                //设置window的状态栏不可见
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                //如果为半透明模式，添加设置Window半透明的Flag
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //设置系统状态栏处于可见状态
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            //view不根据系统窗口来调整自己的布局
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, isNeedStatusBarHeight);
                ViewCompat.requestApplyInsets(mChildView);
            }
        } else {
            //4.4~5.0处理方式
            Window window = activity.getWindow();
            //设置Window为透明
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            View mContentChild = mContentView.getChildAt(0);

            //移除已经存在假状态栏则,并且取消它的Margin间距
            removeFakeStatusBarViewIfExist(activity);
            removeMarginTopOfContentChild(mContentChild, getStatusBarHeight());
            if (mContentChild != null) {
                //fitsSystemWindow 为 false, 不预留系统栏位置.
                ViewCompat.setFitsSystemWindows(mContentChild, false);
            }
        }
    }

    /**
     * 使用CollapsingToolbarLayout使ToolBar具有折叠效果
     *
     * @param activity
     * @param appBarLayout
     * @param collapsingToolbarLayout
     * @param toolbar
     * @param statusColor
     */
    public static void setStatusBarColorForCollapsingToolbar(final Activity activity, final AppBarLayout appBarLayout, final CollapsingToolbarLayout collapsingToolbarLayout, Toolbar toolbar, final int statusColor) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上处理方式
            final Window window = activity.getWindow();
            //取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ////添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏为透明
            window.setStatusBarColor(Color.TRANSPARENT);
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            //通过OnApplyWindowInsetsListener()使Layout在绘制过程中将View向下偏移了,使collapsingToolbarLayout可以占据状态栏
            ViewCompat.setOnApplyWindowInsetsListener(collapsingToolbarLayout, new OnApplyWindowInsetsListener() {
                @Override
                public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                    return insets;
                }
            });

            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            //view不根据系统窗口来调整自己的布局
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, false);
                ViewCompat.requestApplyInsets(mChildView);
            }

            ((View) appBarLayout.getParent()).setFitsSystemWindows(false);
            appBarLayout.setFitsSystemWindows(false);
            collapsingToolbarLayout.setFitsSystemWindows(false);
            collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);
            //设置状态栏的颜色
            collapsingToolbarLayout.setStatusBarScrimColor(statusColor);
            toolbar.setFitsSystemWindows(false);
            //为Toolbar添加一个状态栏的高度, 同时为Toolbar添加paddingTop,使Toolbar覆盖状态栏，ToolBar的title可以正常显示.
            if (toolbar.getTag() == null) {
                CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
                int statusBarHeight = getStatusBarHeight();
                lp.height += statusBarHeight;
                toolbar.setLayoutParams(lp);
                toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
                toolbar.setTag(true);
            }

            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                private final static int EXPANDED = 0;
                private final static int COLLAPSED = 1;
                private int appBarLayoutState;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    //toolbar被折叠时显示状态栏
                    if (Math.abs(verticalOffset) > collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                        if (appBarLayoutState != COLLAPSED) {
                            appBarLayoutState = COLLAPSED;//修改状态标记为折叠
                            setBackgroundColor(activity, statusColor,true);
                        }
                    } else {
                        //toolbar显示时同时显示状态栏
                        if (appBarLayoutState != EXPANDED) {
                            appBarLayoutState = EXPANDED;//修改状态标记为展开
                            setBackgroundTranslucent(activity, false);
                        }
                    }
                }
            });
        } else {
            //4.4~5.0处理方式
            Window window = activity.getWindow();
            //设置Window为全透明
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);

            //AppBarLayout,CollapsingToolbarLayout,ToolBar,ImageView的fitsSystemWindow统一改为false, 不预留系统栏位置.
            View mContentChild = mContentView.getChildAt(0);
            mContentChild.setFitsSystemWindows(false);
            ((View) appBarLayout.getParent()).setFitsSystemWindows(false);
            appBarLayout.setFitsSystemWindows(false);
            collapsingToolbarLayout.setFitsSystemWindows(false);
            collapsingToolbarLayout.getChildAt(0).setFitsSystemWindows(false);

            toolbar.setFitsSystemWindows(false);
            //为Toolbar添加一个状态栏的高度, 同时为Toolbar添加paddingTop,使Toolbar覆盖状态栏，ToolBar的title可以正常显示.
            if (toolbar.getTag() == null) {
                CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
                int statusBarHeight = getStatusBarHeight();
                lp.height += statusBarHeight;
                toolbar.setLayoutParams(lp);
                toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());
                toolbar.setTag(true);
            }
            //移除已经存在假状态栏则,并且取消它的Margin间距
            int statusBarHeight = getStatusBarHeight();
            removeFakeStatusBarViewIfExist(activity);
            removeMarginTopOfContentChild(mContentChild, statusBarHeight);
            //添加一个View来作为状态栏的填充
            final View statusView = addFakeStatusBarView(activity, statusColor, statusBarHeight);

            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
            if (behavior != null && behavior instanceof AppBarLayout.Behavior) {
                int verticalOffset = ((AppBarLayout.Behavior) behavior).getTopAndBottomOffset();
                if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                    statusView.setAlpha(1f);
                } else {
                    statusView.setAlpha(0f);
                }
            } else {
                statusView.setAlpha(0f);
            }
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (Math.abs(verticalOffset) > appBarLayout.getHeight() - collapsingToolbarLayout.getScrimVisibleHeightTrigger()) {
                        //toolbar被折叠时显示状态栏
                        if (statusView.getAlpha() == 0) {
                            statusView.animate().cancel();
                            statusView.animate().alpha(1f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                        }
                    } else {
                        //toolbar展开时显示状态栏
                        if (statusView.getAlpha() == 1) {
                            statusView.animate().cancel();
                            statusView.animate().alpha(0f).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
                        }
                    }
                }
            });
        }
    }

    /**
     * 修改状态栏字体颜色
     * @param activity
     * @param dark
     */
    public static void setStatusBarTextColorMode(Activity activity,boolean dark){
        if(OSUtils.isMIUI()){
            //小米
            setMIUISetStatusBarLightMode(activity,dark);
        }else if(OSUtils.isFlymeOS()){
            //魅族
            setFlymeLightStatusBar(activity,dark);
        }else{
            //其他
            setAndroidNativeLightStatusBar(activity,dark);
        }
    }

    /**
     * 谷歌原生修改状态栏字体颜色
     * @param activity
     * @param dark
     *
     * 这里有个注意点：
     * 一旦用谷歌原生设置状态栏文字颜色的方法进行设置的话，因为一直会携带SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN这个flag，那么默认界面会变成全屏模式，需要在根布局中设置FitsSystemWindows属性为true，所以我在基类的 process方法中加入如下的代码。
     *
     * @Override protected void process(Bundle savedInstanceState) {
     *      // 华为,OPPO机型在StatusBarUtil.setLightStatusBar后布局被顶到状态栏上去了
     *      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
     *          View content = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
     *          if (content != null && !isUseFullScreenMode()) {
     *              content.setFitsSystemWindows(true);
     *          }
     *      }
     * }
     *
     * 或者在xml文件的根布局中去添加如下代码：
     * android:fitsSystemWindows="true"
     *
     */
    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /**
     * 小米系统下修改状态栏字体颜色
     * @param activity
     * @param dark
     * @return
     */
    private static boolean setMIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && OSUtils.isMIUI6Later()) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    }
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置魅族手机状态栏字体颜色
     * @param activity
     * @param dark
     * @return
     */
    private static boolean setFlymeLightStatusBar(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    private static void removeMarginTopOfContentChild(View v, int height) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
        lp.setMargins(lp.leftMargin, 0, lp.rightMargin, lp.bottomMargin);
        v.setLayoutParams(lp);
    }

    private static void removeFakeStatusBarViewIfExist(Activity activity) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();

        View fakeView = mDecorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        if (fakeView != null) {
            mDecorView.removeView(fakeView);
        }
    }

    private static View addFakeStatusBarView(Activity activity, int statusBarColor, int statusBarHeight) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();

        View mStatusBarView = new View(activity);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        layoutParams.gravity = Gravity.TOP;
        mStatusBarView.setLayoutParams(layoutParams);
        mStatusBarView.setBackgroundColor(statusBarColor);
        mStatusBarView.setTag(TAG_FAKE_STATUS_BAR_VIEW);

        mDecorView.addView(mStatusBarView);
        return mStatusBarView;
    }

    private static void addMarginTopToContentChild(View mContentChild, int statusBarHeight) {
        if (mContentChild == null) {
            return;
        }
        if (!TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
            lp.topMargin += statusBarHeight;
            mContentChild.setLayoutParams(lp);
            mContentChild.setTag(TAG_MARGIN_ADDED);
        }
    }

    private static void setContentTopPadding(Activity activity, int padding) {
        ViewGroup mContentView = (ViewGroup) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.setPadding(0, padding, 0, 0);
    }

    private static int getStatusBarHeight() {
        int result = 0;
        try {
            int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = Resources.getSystem().getDimensionPixelSize(resourceId);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

}
