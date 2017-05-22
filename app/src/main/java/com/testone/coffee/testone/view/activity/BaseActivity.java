package com.testone.coffee.testone.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.testone.coffee.testone.modle.data.CameraManager;


/**
 * Created by coffee on 2017/5/16.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected int cacheId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        cacheId = CameraManager.DB_CACHE;
    }

    public abstract int getLayoutId() ;
}
