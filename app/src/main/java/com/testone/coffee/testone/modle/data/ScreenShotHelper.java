package com.testone.coffee.testone.modle.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by coffee on 2017/5/27.
 */

public class ScreenShotHelper extends SQLiteOpenHelper implements IScreenShotInterface {
    public ScreenShotHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        Log.i("prepared","screenShotHelper onCreate");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("prepared","TABLESQL");
        String TABLESQL = "CREATE TABLE IF NOT EXISTS "
                + ScreenTableName.TABLE + " ("
                + ScreenTableName.capture_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ScreenTableName.screen_name + " VARCHAR,"
                + ScreenTableName.device_id + " VARCHAR,"
                + ScreenTableName.capture_time + " VARCHAR,"
                + ScreenTableName.capture_stream + " BLOB)";

        db.execSQL(TABLESQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ScreenTableName.TABLE);
        onCreate(db);
    }
}
