package com.testone.coffee.testone.modle.data;

import android.content.Context;
import android.text.TextUtils;

import com.testone.coffee.testone.modle.CameraModle;
import com.testone.coffee.testone.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coffee on 2017/5/22.
 */

public class CameraSPCache extends CameraCache {
    private Context context;
    public CameraSPCache(Context context){
        this.context = context;
    }
    @Override
    public void add(CameraModle modle) {
        int num = PreferenceUtils.getInt(context,"camera_num",0);
        num += 1;
        PreferenceUtils.putString(context,num+"name",modle.getCamera_name());
        PreferenceUtils.putString(context,num+"url",modle.getRtsp_url());
        PreferenceUtils.putInt(context,"camera_num",num);
    }

    @Override
    public List<CameraModle> findAll() {
        List<CameraModle> modleList = new ArrayList<>();
        int num = PreferenceUtils.getInt(context,"camera_num",0);
        if(num > 0){
            for(int i = 1 ;i<= num;i++){
                String name  = PreferenceUtils.getString(context,i+"name");
                String url = PreferenceUtils.getString(context,i+"url");
                modleList.add(new CameraModle(name,url));
            }
        }
        return modleList;
    }

    @Override
    public void deleteSingle(String url) {
        List<CameraModle> modleList = findAll();
        if(modleList != null && modleList.size() != 0){
            for (int i = 0; i < modleList.size(); i++) {
                if(TextUtils.equals(url,modleList.get(i).getRtsp_url())){
                    modleList.remove(i);
                }
            }
        }
        PreferenceUtils.putInt(context,"camera_num",modleList.size());
        for(int i =0 ; i < modleList.size() ; i++){
            int temp = i+1;
            PreferenceUtils.putString(context,temp+"name",modleList.get(i).getCamera_name());
            PreferenceUtils.putString(context,temp+"url",modleList.get(i).getRtsp_url());
        }
    }
}
