package com.wangbai.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by binwang on 2015/11/12.
 */
public class CityOpenHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "city_db";
    private static int DB_VERSON = 1;

    private static CityOpenHelper mInstance;

    public static synchronized CityOpenHelper getInstance(Context context){
        if(mInstance == null){
            mInstance = new CityOpenHelper(context);
        }
        return mInstance;
    }


    private CityOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSON);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        WeatherTable.createTable(sqLiteDatabase);
        ForeCastTable.createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
