package com.testone.coffee.testone.modle.data.cache;

import com.testone.coffee.testone.modle.CameraInfoModle;

import java.util.List;

/**
 * Created by coffee on 2017/5/22.
 */

public abstract class CameraCache {
    public abstract void add(CameraInfoModle modle);
    public abstract List<CameraInfoModle> findAll();
    public abstract void deleteSingle(String url);
}
