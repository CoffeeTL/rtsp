package com.testone.coffee.testone;

import android.app.Application;
import android.content.Context;



/**
 * Created by coffee on 2017/5/16.
 */

public class RtspApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
