package com.testone.coffee.testone.modle.data.cache;

import com.testone.coffee.testone.modle.CameraModle;

import java.util.List;

/**
 * Created by coffee on 2017/5/22.
 */

public abstract class CameraCache {
    public abstract void add(CameraModle modle);
    public abstract List<CameraModle> findAll();
    public abstract void deleteSingle(String url);
}
