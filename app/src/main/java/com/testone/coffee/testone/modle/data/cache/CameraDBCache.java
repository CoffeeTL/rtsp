package com.testone.coffee.testone.modle.data.cache;

import android.content.Context;

import com.testone.coffee.testone.modle.CameraModle;
import com.testone.coffee.testone.modle.data.CameraInfoIml;
import com.testone.coffee.testone.modle.data.cache.CameraCache;

import java.util.List;

/**
 * Created by coffee on 2017/5/22.
 */

public class CameraDBCache extends CameraCache {
    private CameraInfoIml cameraInfoIml;
    public CameraDBCache(Context context){
        cameraInfoIml = new CameraInfoIml(context);
    }
    @Override
    public void add(CameraModle modle) {
        cameraInfoIml.add(modle);
    }

    @Override
    public List<CameraModle> findAll() {
        List<CameraModle> modleList = cameraInfoIml.findAll();
        return modleList;
    }

    @Override
    public void deleteSingle(String url) {
        cameraInfoIml.deleteByUrl(url);
    }
}
