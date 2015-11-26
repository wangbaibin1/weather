package com.wangbai.weather.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.Date;

/**
 * Created by binwang on 2015/11/12.
 */
public class ForeCastTable {
    public static final String TABLE_NAME = "weather";
    public interface Columns extends BaseColumns {
        String WEID = "weid";
        String DATE = "date";
        String DAY = "day";
        String CODE = "code";
        String MAX_T = "max_t";
        String MIN_T = "min_t";
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + Columns._ID + " INTEGER PRIMARY KEY,"
                + Columns.WEID + " TEXT,"
                + Columns.DATE + " TEXT,"
                + Columns.DAY + " TEXT,"
                + Columns.CODE + " INTEGER,"
                + Columns.MAX_T + " TEXT,"
                + Columns.MIN_T + " TEXT" + ");");
    }

    public ContentValues toContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.WEID ,weid);
        contentValues.put(Columns.DATE , date);
        contentValues.put(Columns.DAY ,day);
        contentValues.put(Columns.CODE ,code);
        contentValues.put(Columns.MAX_T ,high);
        contentValues.put(Columns.MIN_T, low);
        return contentValues;
    }

    public static ForeCastTable fromCursor(Cursor cusor){
        ForeCastTable foreCastTable = new ForeCastTable();
        foreCastTable.weid = cusor.getString(cusor.getColumnIndex(Columns.WEID));
        foreCastTable.day = cusor.getString(cusor.getColumnIndex(Columns.DAY));
        foreCastTable.low = cusor.getString(cusor.getColumnIndex(Columns.MIN_T));
        foreCastTable.high = cusor.getString(cusor.getColumnIndex(Columns.MAX_T));
        foreCastTable.code = cusor.getInt(cusor.getColumnIndex(Columns.CODE));
        foreCastTable.date = cusor.getString(cusor.getColumnIndex(Columns.DATE));

        return foreCastTable;
    }

    public String day;
    public String date;
    public String low;
    public String high;
    public String weid;
    public int code;

}
