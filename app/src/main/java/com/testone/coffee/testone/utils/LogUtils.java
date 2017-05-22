package com.testone.coffee.testone.utils;

import android.util.Log;

/**
 * Created by coffee on 2017/5/17.
 */

public class LogUtils {
    private static String TAG = "rtsp";
    public static void send(String logContent){
        Log.i(TAG,logContent);
    }
}
