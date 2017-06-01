package com.testone.coffee.testone;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.videolan.libvlc.BitmapCache;


/**
 * Created by coffee on 2017/5/16.
 */

public class RtspApplication extends Application {
    private static RtspApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * Called when the overall system is running low on memory
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        BitmapCache.getInstance().clear();
    }

    /**
     * @return the main context of the Application
     */
    public static Context getAppContext()
    {
        return instance;
    }

    /**
     * @return the main resources from the Application
     */
    public static Resources getAppResources()
    {
        if(instance == null) return null;
        return instance.getResources();
    }
}
