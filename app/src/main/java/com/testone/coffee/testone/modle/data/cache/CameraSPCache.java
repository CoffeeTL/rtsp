package com.testone.coffee.testone.modle.data.cache;

import android.content.Context;
import android.text.TextUtils;

import com.testone.coffee.testone.modle.CameraInfoModle;
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
    public void add(CameraInfoModle modle) {
        int num = PreferenceUtils.getInt(context,"camera_num",0);
        num += 1;
        PreferenceUtils.putString(context,num+"name",modle.getName());
        PreferenceUtils.putString(context,num+"ip",modle.getIPAddress());
        PreferenceUtils.putString(context,num+"port",modle.getPort());
        PreferenceUtils.putString(context,num+"backString",modle.getBackString());
        PreferenceUtils.putString(context,num+"url",modle.turnIntoUrl());
        PreferenceUtils.putString(context,num+"username",modle.getUsername());
        PreferenceUtils.putString(context,num+"pwd",modle.getPwd());
        PreferenceUtils.putInt(context,"camera_num",num);
    }

    @Override
    public List<CameraInfoModle> findAll() {
        List<CameraInfoModle> modleList = new ArrayList<>();
        int num = PreferenceUtils.getInt(context,"camera_num",0);
        if(num > 0){
            for(int i = 1 ;i<= num;i++){
                String name = PreferenceUtils.getString(context,i+"name");
                String ip = PreferenceUtils.getString(context,i+"ip");
                String port = PreferenceUtils.getString(context,i+"port");
                String backString = PreferenceUtils.getString(context,i+"backString");
                String username = PreferenceUtils.getString(context,i+"username");
                String pwd = PreferenceUtils.getString(context,i+"pwd");
                modleList.add(new CameraInfoModle(name,ip,port,backString,username,pwd));
            }
        }
        return modleList;
    }

    @Override
    public void deleteSingle(String url) {
        List<CameraInfoModle> modleList = findAll();
        if(modleList != null && modleList.size() != 0){
            for (int i = 0; i < modleList.size(); i++) {
                if(TextUtils.equals(url,modleList.get(i).turnIntoUrl())){
                    modleList.remove(i);
                }
            }
        }
        PreferenceUtils.putInt(context,"camera_num",modleList.size());
        for(int i =0 ; i < modleList.size() ; i++){
            int temp = i+1;
            PreferenceUtils.putString(context,temp+"name",modleList.get(i).getName());
            PreferenceUtils.putString(context,temp+"ip",modleList.get(i).getIPAddress());
            PreferenceUtils.putString(context,temp+"port",modleList.get(i).getPort());
            PreferenceUtils.putString(context,temp+"backString",modleList.get(i).getBackString());
            PreferenceUtils.putString(context,temp+"username",modleList.get(i).getUsername());
            PreferenceUtils.putString(context,temp+"pwd",modleList.get(i).getPwd());
            PreferenceUtils.putString(context,temp+"url",modleList.get(i).turnIntoUrl());
        }
    }
}
