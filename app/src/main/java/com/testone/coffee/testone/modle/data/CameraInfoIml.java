package com.testone.coffee.testone.modle.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.testone.coffee.testone.modle.CameraModle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coffee on 2017/5/22.
 */

public class CameraInfoIml {
    private static String TABLE_NAME = "camera.db";
    private static int VERSION_CODE = 1;
    private Context context;
    private CameraInfoHelper helper;
    private SQLiteDatabase db;
    public CameraInfoIml(Context context){
        this.context = context;

    }
    public void add(CameraModle modle){
        helper = new CameraInfoHelper(context,TABLE_NAME,null,VERSION_CODE);
        db = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name",modle.getCamera_name());
        cv.put("url",modle.getRtsp_url());
        db.insert("info",null,cv);
        db.close();
        cv.clear();
    }
    public List<CameraModle> findAll(){
        List<CameraModle> mlist = new ArrayList<>();
        helper = new CameraInfoHelper(context,TABLE_NAME,null,VERSION_CODE);
        if(helper != null){
            db = helper.getReadableDatabase();
            Cursor cursor = db.query("info",null,null,null,null,null,null);
            if(cursor != null){
                while (cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String url = cursor.getString(cursor.getColumnIndexOrThrow("url"));
                    mlist.add(new CameraModle(name,url));
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
