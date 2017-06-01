package com.testone.coffee.testone.modle.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by coffee on 2017/5/27.
 */

public interface IScreenShotInterface {
    String AUTHORITY = "com.producemedia.camera";
    String DB_NAME = "screenShot.db";
    int VERSION = 3;
    interface ScreenTableName extends BaseColumns{
        String TABLE = "capture";
        Uri uri = Uri.parse("content://"+AUTHORITY+"/"+TABLE);
        Uri uris = Uri.parse("content://"+AUTHORITY+"/"+TABLE+"/*");

        String capture_id = "_id";
        String screen_name = "screen_name";
        String device_id = "device_id";
        String capture_time = "capture_time";
        String capture_stream = "capture_stream";

        String CONTENT_LIST = "vnd.android.cursor.dir/vnd.bookprovider.screenShot";
        String CONTENT_ITEM = "vnd.android.cursor.item/vnd.bookprovider.screenShot";
    }
}
