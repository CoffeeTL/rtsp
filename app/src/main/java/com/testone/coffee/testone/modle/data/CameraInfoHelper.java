package com.testone.coffee.testone.modle.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by coffee on 2017/5/22.
 */

public class CameraInfoHelper extends SQLiteOpenHelper {

    public CameraInfoHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists info(id integer primary key autoincrement,name text,ip text,port text,backString text,username text,pwd text,url text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists info");
        onCreate(db);
    }
}
