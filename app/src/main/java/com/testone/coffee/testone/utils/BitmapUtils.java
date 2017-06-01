package com.testone.coffee.testone.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by coffee on 2017/5/27.
 */

public class BitmapUtils {

    public static byte[] getBlob(Bitmap bitmap){
        int size = bitmap.getWidth() * bitmap.getHeight()*4;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(size);
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}
