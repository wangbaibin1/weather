package com.wangbai.weather.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.wangbai.weather.ui.MainApplication;
import com.wangbai.weather.R;

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
    public static final int LOCATION_SIGN = 1;

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
        String LOCATION = "location";
        String UPDATE_TIME = "update_time";
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
                + Columns.LOCATION + " INTEGER,"
                + Columns.UPDATE_TIME + " INTEGER" + ");");
    }

    public boolean isHaveSuccessUpdate(){
        return !TextUtils.isEmpty(pubDate);
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
        weatherTable.mLocation = cursor.getInt(cursor.getColumnIndex(Columns.LOCATION));
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
        contentValues.put(Columns.LOCATION, mLocation);
        contentValues.put(Columns.UPDATE_TIME, mLastUpdateTime);
        return contentValues;
    }

    public boolean isLocation(){
        return mLocation == LOCATION_SIGN;
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
    public int mLocation;
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
                return MainApplication.getContext().getString(R.string.publish_just_now);
            } else if ((System.currentTimeMillis() - time) / (1000 * 60 * 60) == 0) {
                return MainApplication.getContext().getString(R.string.publish_minutes_ago,(System.currentTimeMillis() - time) / (1000 * 60));
            } else {
                return MainApplication.getContext().getString(R.string.publish_hours_ago,(System.currentTimeMillis() - time) / (1000 * 60 * 60));
            }
        }

        return MainApplication.getContext().getString(R.string.publish_time,result);
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
