package com.testone.coffee.testone.modle.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;

import com.testone.coffee.testone.modle.ScreenShotModle;
import com.testone.coffee.testone.utils.BitmapUtils;
import com.testone.coffee.testone.utils.DeviceUtils;
import com.testone.coffee.testone.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coffee on 2017/5/27.
 */

public class ScreenShotManager {
    private ContentResolver contentResolver;
    private ScreenShotProvider screenShotProvider;
    private static ScreenShotManager instance;
    private Context context;
    public static ScreenShotManager get(){
        if(instance == null){
            instance = new ScreenShotManager();
        }
        return instance;
    }
    public ScreenShotManager with(Context context){
        this.context = context;
        contentResolver = context.getContentResolver();
        return this;
    }
    public ScreenShotManager putData(Bitmap map,String name){
        if(map != null){
            ContentValues cv = new ContentValues();
            cv.put(IScreenShotInterface.ScreenTableName.screen_name,name);
            if(DeviceUtils.get().with(context).getImei() == null){
                cv.put(IScreenShotInterface.ScreenTableName.device_id, "null id");
            }else{
                cv.put(IScreenShotInterface.ScreenTableName.device_id, DeviceUtils.get().with(context).getImei());
            }

            cv.put(IScreenShotInterface.ScreenTableName.capture_time, TimeUtils.getCurrentDate());
            cv.put(IScreenShotInterface.ScreenTableName.capture_stream, BitmapUtils.getBlob(map));
            contentResolver.insert(IScreenShotInterface.ScreenTableName.uri,cv);
        }else{
            Log.i("prepared","BitMap is null");
        }

        return this;
    }
    public List<ScreenShotModle> findData(){
        List<ScreenShotModle> modleList = new ArrayList<>();
        Cursor cursor = contentResolver.query(IScreenShotInterface.ScreenTableName.uris,null,null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndexOrThrow(IScreenShotInterface.ScreenTableName.capture_id));
                String screenName = cursor.getString(cursor.getColumnIndexOrThrow(IScreenShotInterface.ScreenTableName.screen_name));
                String device_id = cursor.getString(cursor.getColumnIndexOrThrow(IScreenShotInterface.ScreenTableName.device_id));
                String capture_time = cursor.getString(cursor.getColumnIndexOrThrow(IScreenShotInterface.ScreenTableName.capture_time));
                byte[] capture_stream = cursor.getBlob(cursor.getColumnIndexOrThrow(IScreenShotInterface.ScreenTableName.capture_stream));
                modleList.add(new ScreenShotModle(id,screenName,device_id,capture_time,capture_stream));
            }
        }
        cursor.close();
        return modleList;
    }

}
