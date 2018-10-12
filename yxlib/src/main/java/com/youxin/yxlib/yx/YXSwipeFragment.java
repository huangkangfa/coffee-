package com.youxin.yxlib.yx;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.youxin.yxlib.base.BaseSwipeFragment;
import com.youxin.yxlib.view.status.StatusDelegate;

public abstract class YXSwipeFragment extends BaseSwipeFragment {
    public abstract void init(ViewDataBinding binding);
    public final StatusDelegate _status = new StatusDelegate();

    @Override
    public View getRootView(View rootView) {
        super.getRootView(rootView);
        ViewDataBinding binding = DataBindingUtil.bind(rootView);
        rootView=_status.init(binding.getRoot(),this);
        init(binding);
        return rootView;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }
}
