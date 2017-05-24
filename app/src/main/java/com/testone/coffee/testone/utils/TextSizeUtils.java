package com.testone.coffee.testone.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.testone.coffee.testone.view.activity.CameraAddActivity;

/**
 * Created by coffee on 2017/5/24.
 */

public class TextSizeUtils {
    public static int DEFAULT_MAX_SIZE = 6;
    public static int DEFAULT_MEDIUM_SIZE = 5;
    public static int DEFAULT_MIN_SIZE = 4;
    public static void calculateTextSizeByDimension(Context context,TextView tv, int type){
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        int textSize = (int) (dm.density * type);
        int textSize = 4*type;
        tv.setTextSize(textSize);
    }
    public static void calculateTextSizeByDimension(Context context, EditText et, int type){
//        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        int textSize = (int) (dm.density * type);
        int textSize = 4*type;
        et.setTextSize(textSize);
    }


}
