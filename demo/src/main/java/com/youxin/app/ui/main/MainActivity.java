package com.youxin.app.ui.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.youxin.app.R;
import com.youxin.app.databinding.ActivityMainBinding;
import com.youxin.app.db.DBManager;
import com.youxin.app.model.User;
import com.youxin.yxlib.base.BaseActivity;
import com.youxin.yxlib.base.BaseFragment;
import com.youxin.yxlib.util.ThreadManager;
import com.youxin.yxlib.util.ToastUtil;

import java.util.List;

public class MainActivity extends BaseActivity {
    ThreadManager mThreadManager;

    public String search="查询";
    public String insert="插入";

    @Override
    public BaseFragment setRootFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding= DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setSearch(search);
        binding.setInsert(insert);
        binding.setMainActivity(this);
        mThreadManager=new ThreadManager(ThreadManager.Type.FixedThread,5);
    }


    public void searchData( ){
        mThreadManager.execute(new Runnable() {
            @Override
            public void run() {
                final List<User> data= DBManager.getDB().userDao().getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(data.size()>0){
                            ToastUtil.showShort(MainActivity.this,"数据库内数据数量："+data.size());
                        }else{
                            ToastUtil.showShort(MainActivity.this,"没有数据");
                        }
                    }
                });
            }
        });
    }

    public void insertData(){
        mThreadManager.execute(new Runnable() {
            @Override
            public void run() {
                User user=new User();
                user.setName("黄");
                user.setAccount("康发");
                DBManager.getDB().userDao().insertAll(user);
            }
        });
    }

}
