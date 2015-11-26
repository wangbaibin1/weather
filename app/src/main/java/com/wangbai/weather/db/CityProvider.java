package com.wangbai.weather.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by binwang on 2015/11/12.
 */
public class  CityProvider extends ContentProvider {
    public static final String AUTHORITY = "com.wangbai.cityweather";
    public static final String TODAY_WEATHER = "today_weather";
    public static final String FORECAST = "forecast";
    public static final Uri CONTENT_URI_FOR_WEATHER = Uri.parse("content://" + AUTHORITY + "/" + TODAY_WEATHER);
    public static final Uri CONTENT_URI_FOR_FORECAST = Uri.parse("content://" + AUTHORITY + "/" + FORECAST);

    public static final int WEATHER_I = 1;
    public static final int FORECAST_I = 2;

    private static UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, TODAY_WEATHER, WEATHER_I);
        mUriMatcher.addURI(AUTHORITY, FORECAST, FORECAST_I);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Cursor query(Uri uri, String[] projections, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = CityOpenHelper.getInstance(getContext()).getReadableDatabase();
        Cursor cursor = null;
        switch (mUriMatcher.match(uri)) {
            case WEATHER_I:
                cursor = db.query(WeatherTable.TABLE_NAME, projections, selection, selectionArgs, null, null, sortOrder);
                break;
            case FORECAST_I:
                cursor = db.query(ForeCastTable.TABLE_NAME, projections, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = CityOpenHelper.getInstance(getContext()).getWritableDatabase();
        Uri insertUri = null;
        long rowId = 0;
        switch (mUriMatcher.match(uri)) {
            case WEATHER_I:
                rowId = db.insert(WeatherTable.TABLE_NAME, null, contentValues);
                if (rowId > 0) {
                    insertUri = ContentUris.withAppendedId(CONTENT_URI_FOR_WEATHER, rowId);
                }
                break;
            case FORECAST_I:
                rowId = db.insert(ForeCastTable.TABLE_NAME, null, contentValues);
                if (rowId > 0) {
                    insertUri = ContentUris.withAppendedId(CONTENT_URI_FOR_FORECAST, rowId);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
        if (insertUri != null) {
            getContext().getContentResolver().notifyChange(insertUri, null);
        }
        return insertUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = CityOpenHelper.getInstance(getContext()).getWritableDatabase();
        int id = 0;
        switch (mUriMatcher.match(uri)) {
            case WEATHER_I:
                id = db.delete(WeatherTable.TABLE_NAME, selection, selectionArgs);
                break;
            case FORECAST_I:
                id = db.delete(ForeCastTable.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = CityOpenHelper.getInstance(getContext()).getWritableDatabase();
        int id = 0;
        switch (mUriMatcher.match(uri)) {
            case WEATHER_I:
                id = db.update(WeatherTable.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case FORECAST_I:
                id = db.update(ForeCastTable.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }
}
