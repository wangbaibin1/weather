package com.wangbai.weather.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by binwang on 2015/11/12.
 */
public class WeatherTable {
    public static final String TABLE_NAME = "forecast";

    public WeatherTable() {

    }

    public interface Columns extends BaseColumns {
        String CITY = "city";
        String COUNTRY = "country";
        String WEID = "weid";
        String T_U = "t_u";
        String CODE = "code";
        String MAX_T = "max_t";
        String MIN_T = "min_t";
        String T = "t";
        String PUB_D = "pub_d";
        String UPDATE_TIME = "update_time";
    }

    public WeatherTable initData()
    {
        WeatherTable weatherTable = new WeatherTable();

        weatherTable._id= 0;
        weatherTable.cityName = "Baibin" ;
        weatherTable.countryName = "CHINA";
        weatherTable.cityWeid= "HEHE";
        weatherTable.temperUnit= "HEHE";
        weatherTable.code = 0;
        weatherTable.temperature = "28";
        weatherTable.mLastUpdateTime = 0 ;
        weatherTable.pubDate = "OK";
        weatherTable.maxTemper = "0";
        weatherTable.minTemper = "21";
        weatherTable.mForeCastTableList = null;

        return  weatherTable;

    }



    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + Columns._ID + " INTEGER PRIMARY KEY,"
                + Columns.CITY + " TEXT,"
                + Columns.COUNTRY + " TEXT,"
                + Columns.WEID + " TEXT,"
                + Columns.T_U + " TEXT,"
                + Columns.CODE + " INTEGER,"
                + Columns.T + " TEXT,"
                + Columns.MAX_T + " TEXT,"
                + Columns.MIN_T + " TEXT,"
                + Columns.PUB_D + " TEXT,"
                + Columns.UPDATE_TIME + " INTEGER" + ");");
    }

    public static WeatherTable fromCusor(Cursor cursor) {
        WeatherTable weatherTable = new WeatherTable();
        weatherTable.cityName = cursor.getString(cursor.getColumnIndex(Columns.CITY));
        weatherTable.countryName = cursor.getString(cursor.getColumnIndex(Columns.COUNTRY));
        weatherTable.cityWeid = cursor.getString(cursor.getColumnIndex(Columns.WEID));
        weatherTable.temperUnit = cursor.getString(cursor.getColumnIndex(Columns.T_U));
        weatherTable.code = cursor.getInt(cursor.getColumnIndex(Columns.CODE));
        weatherTable.temperature = cursor.getString(cursor.getColumnIndex(Columns.T));
        weatherTable.maxTemper = cursor.getString(cursor.getColumnIndex(Columns.MAX_T));
        weatherTable.minTemper = cursor.getString(cursor.getColumnIndex(Columns.MIN_T));
        weatherTable.pubDate = cursor.getString(cursor.getColumnIndex(Columns.PUB_D));
        weatherTable.mLastUpdateTime = cursor.getLong(cursor.getColumnIndex(Columns.UPDATE_TIME));
        return weatherTable;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.CITY, cityName);
        contentValues.put(Columns.COUNTRY, countryName);
        contentValues.put(Columns.WEID, cityWeid);
        contentValues.put(Columns.T_U, temperUnit);
        contentValues.put(Columns.CODE, code);
        contentValues.put(Columns.T, temperature);
        contentValues.put(Columns.MAX_T, maxTemper);
        contentValues.put(Columns.MIN_T, minTemper);
        contentValues.put(Columns.PUB_D, pubDate);
        contentValues.put(Columns.UPDATE_TIME, mLastUpdateTime);
        return contentValues;
    }

    public int _id;
    public String cityName;
    public String countryName;
    public String cityWeid;
    public String temperUnit;
    public int code = 0;
    public String temperature;
    public long mLastUpdateTime;
    public String pubDate;
    public String maxTemper;
    public String minTemper;
    public ArrayList<ForeCastTable> mForeCastTableList;

    public void addForecast(ForeCastTable weather) {
        if (mForeCastTableList == null) {
            mForeCastTableList = new ArrayList();
        }
        mForeCastTableList.add(weather);
    }

    public List<ForeCastTable> getForecastList() {
        return mForeCastTableList;
    }

    public int getForecastSize() {
        return mForeCastTableList == null ? 0 : mForeCastTableList.size();
    }

    public static String getPubDate(String pubData) {
        pubData = pubData.replace("\n", "");
        String result = null;
        long time = toWeatherTimeStamp(pubData);

        Date date = new Date(time);
        result = formatDate("MM-dd", date);

        if (result.equals(formatDate("MM-dd", new Date(System.currentTimeMillis())))) {
            if ((System.currentTimeMillis() - time) / (1000 * 60) == 0) {
                return "just now publish";
            } else if ((System.currentTimeMillis() - time) / (1000 * 60 * 60) == 0) {
                return (System.currentTimeMillis() - time) / (1000 * 60) + " minutes ago publish";
            } else {
                return (System.currentTimeMillis() - time) / (1000 * 60 * 60) + " hours ago publish";
            }
        }

        return result + " publish";
    }

    private static long toWeatherTimeStamp(String pubData) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm aa", Locale.US);

        try {
            Date date = format.parse(pubData);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatDate(String pattern, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
        return format.format(date);
    }


}
