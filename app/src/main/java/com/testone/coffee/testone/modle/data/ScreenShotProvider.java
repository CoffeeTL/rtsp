package com.testone.coffee.testone.modle.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by coffee on 2017/5/27.
 */

public class ScreenShotProvider extends ContentProvider {
    private static UriMatcher uriMatcher;
    private static final int screenShit = 1;
    private static final int screenShots = 2;
    private ScreenShotHelper helper;
    private SQLiteDatabase db;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(IScreenShotInterface.AUTHORITY, IScreenShotInterface.ScreenTableName.TABLE,screenShit);
        uriMatcher.addURI(IScreenShotInterface.AUTHORITY, IScreenShotInterface.ScreenTableName.TABLE+"/*",screenShots);
    }

    @Override
    public boolean onCreate() {
        helper = new ScreenShotHelper(getContext());
        return (helper == null) ? false : true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        db = helper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case screenShots:
                return db.query(IScreenShotInterface.ScreenTableName.TABLE,
                        projection, selection, selectionArgs, null, null,
                        sortOrder);
            case screenShit:
                long id = ContentUris.parseId(uri);
                String where = "_id=" + id;
                if (selection != null && !"".equals(selection)) {
                    where = selection + " and " + where;
                }
                return db.query(IScreenShotInterface.ScreenTableName.TABLE,
                        projection, where, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("This is a unKnow Uri"
                        + uri.toString());
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case screenShots:
                return IScreenShotInterface.ScreenTableName.CONTENT_LIST;
            case screenShit:
                return IScreenShotInterface.ScreenTableName.CONTENT_ITEM;
            default:
                throw new IllegalArgumentException("This is a unKnow Uri"
                        + uri.toString());
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case screenShots:
            case screenShit:
                db = helper.getWritableDatabase();
                long rowId = db.insert(
                        IScreenShotInterface.ScreenTableName.TABLE,
                        IScreenShotInterface.ScreenTableName.capture_id,
                        values);
                Uri insertUri = Uri.withAppendedPath(uri, "/" + rowId);
                Log.i("prepared", "insertUri:" + insertUri.toString());
                getContext().getContentResolver().notifyChange(uri, null);
                return insertUri;
            default:
                throw new IllegalArgumentException("This is a unKnow Uri"
                        + uri.toString());
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
