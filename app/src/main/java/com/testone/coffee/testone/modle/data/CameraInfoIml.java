package com.testone.coffee.testone.modle.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.testone.coffee.testone.modle.CameraInfoModle;
import com.testone.coffee.testone.modle.CameraModle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coffee on 2017/5/22.
 */

public class CameraInfoIml {
    private static String TABLE_NAME = "camera.db";
    private static int VERSION_CODE = 2;
    private Context context;
    private CameraInfoHelper helper;
    private SQLiteDatabase db;
    public CameraInfoIml(Context context){
        this.context = context;
    }
    public void add(CameraInfoModle modle){
        helper = new CameraInfoHelper(context,TABLE_NAME,null,VERSION_CODE);
        db = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",modle.getName());
        cv.put("ip",modle.getIPAddress());
        cv.put("port",modle.getPort());
        cv.put("backString",modle.getBackString());
        cv.put("url",modle.turnIntoUrl());
        db.insert("info",null,cv);
        db.close();
        cv.clear();
    }
    public List<CameraInfoModle> findAll(){
        List<CameraInfoModle> mlist = new ArrayList<>();
        helper = new CameraInfoHelper(context,TABLE_NAME,null,VERSION_CODE);
        if(helper != null){
            db = helper.getReadableDatabase();
            Cursor cursor = db.query("info",null,null,null,null,null,null);
            if(cursor != null){
                while (cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String ip = cursor.getString(cursor.getColumnIndexOrThrow("ip"));
                    String port = cursor.getString(cursor.getColumnIndexOrThrow("port"));
                    String backString = cursor.getString(cursor.getColumnIndexOrThrow("backString"));
                    mlist.add(new CameraInfoModle(name,ip,port,backString));
                }
            }
            db.close();
            cursor.close();
        }


        return mlist;
    }
    public void deleteByUrl(String url){
        helper = new CameraInfoHelper(context,TABLE_NAME,null,VERSION_CODE);
        db = helper.getReadableDatabase();
        db.delete("info","url = ?",new String[]{url});
        db.close();
    }
}
