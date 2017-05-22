package com.testone.coffee.testone.modle.data;

import android.content.Context;
import android.text.TextUtils;

import com.testone.coffee.testone.modle.CameraInfoModle;
import com.testone.coffee.testone.modle.CameraModle;
import com.testone.coffee.testone.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coffee on 2017/5/22.
 */

public class CameraManager {
    public static int DB_CACHE = 1;
    public static int SP_CACHE = 2;
    private Context context;
    private static CameraManager instance = null;
    private CameraCache cameraCache;
    public static CameraManager getInstance(){
        if(instance == null){
            instance = new CameraManager();
        }
        return instance;
    }
    public CameraManager with(Context context){
        this.context = context;
        return this;
    }
    public CameraManager using(int cache){
        switch(cache){
            case 1:
                cameraCache = new CameraDBCache(context);
                break;
            case 2:
                cameraCache = new CameraSPCache(context);
                break;
        }
        return this;
    }
    public List<CameraModle> getDatas(){
       return cameraCache.findAll();
    }
    public void addDatas(CameraModle infoModle){
        cameraCache.add(infoModle);
    }

    public void deleteOne(String url) {
        cameraCache.deleteSingle(url);
    }
}
