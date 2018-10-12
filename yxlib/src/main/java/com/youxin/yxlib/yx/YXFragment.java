package com.youxin.yxlib.yx;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youxin.yxlib.base.BaseFragment;
import com.youxin.yxlib.view.status.StatusDelegate;

public abstract class YXFragment extends BaseFragment {
    public final StatusDelegate _status = new StatusDelegate();
    public abstract void init(ViewDataBinding binding);

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ViewDataBinding binding = DataBindingUtil.bind(rootView);
        rootView=_status.init(binding.getRoot(),this);
        init(binding);
        return rootView;
    }
}
