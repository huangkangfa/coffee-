package com.youxin.app.ui.welcome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.youxin.app.R;
import com.youxin.app.ui.main.MainActivity;

/**
 * Created by huangkangfa on 2018/9/29.
 */

 public class WelcomeActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, MainActivity.class));
    }
}
