package com.wangbai.weather.db;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.wangbai.weather.ui.SearResultData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binwang on 2015/11/12.
 */
public class WeatherDbProviderManager {
    private static WeatherDbProviderManager mInstance;
    private Context mContext;

    public static WeatherDbProviderManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WeatherDbProviderManager(context);
        }

        return mInstance;
    }

    private WeatherDbProviderManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public void deleteWeatherData(WeatherTable weatherTable) {
        if (weatherTable == null) {
            return;
        }
        mContext.getContentResolver().delete(CityProvider.CONTENT_URI_FOR_WEATHER, WeatherTable.Columns.WEID + "=?", new String[]{weatherTable.cityWeid});
        mContext.getContentResolver().delete(CityProvider.CONTENT_URI_FOR_FORECAST, ForeCastTable.Columns.WEID + "=?", new String[]{weatherTable.cityWeid});

    }

    public void insertLocationWeatherData(WeatherTable weatherTable) {
        if (weatherTable == null) {
            return;
        }

        if(TextUtils.isEmpty(weatherTable.cityWeid)){
            mContext.getContentResolver().insert(CityProvider.CONTENT_URI_FOR_WEATHER, weatherTable.toContentValues());
        } else {
            mContext.getContentResolver().update(CityProvider.CONTENT_URI_FOR_WEATHER,
                    weatherTable.toContentValues(),
                    WeatherTable.Columns.LOCATION + "=?",
                    new String[]{String.valueOf(WeatherTable.LOCATION_SIGN)});
        }
        List<ForeCastTable> foreCastTables = weatherTable.getForecastList();
        if (foreCastTables == null || foreCastTables.size() <= 0) {
            return;
        }
        if(TextUtils.isEmpty(weatherTable.cityWeid)){
            return;
        }
        insertForeCastTable(foreCastTables,weatherTable.cityWeid);
    }

    public void insertWeatherData(WeatherTable weatherTable) {
        if (weatherTable == null) {
            return;
        }

        if (mContext.getContentResolver().update(CityProvider.CONTENT_URI_FOR_WEATHER, weatherTable.toContentValues(), WeatherTable.Columns.WEID + "=?", new String[]{weatherTable.cityWeid}) <= 0) {
            mContext.getContentResolver().insert(CityProvider.CONTENT_URI_FOR_WEATHER, weatherTable.toContentValues());
        }


        List<ForeCastTable> foreCastTables = weatherTable.getForecastList();
        if (foreCastTables == null || foreCastTables.size() <= 0) {
            return;
        }
        insertForeCastTable(foreCastTables,weatherTable.cityWeid);
    }

    public void insertForeCastTable(List<ForeCastTable> foreCastTables,String woid) {
        if (foreCastTables == null || foreCastTables.size() <= 0) {
            return;
        }

        mContext.getContentResolver().delete(CityProvider.CONTENT_URI_FOR_FORECAST, ForeCastTable.Columns.WEID + "=?", new String[]{woid});
        for (ForeCastTable foreCastTable : foreCastTables) {
            mContext.getContentResolver().insert(CityProvider.CONTENT_URI_FOR_FORECAST, foreCastTable.toContentValues());
        }
    }

    public List<WeatherTable> quaryWeatherData(boolean isNeedForeCast,String woid) {
        String selection = null;
        String[] selectionArgs = null;

        if(!TextUtils.isEmpty(woid)){
            selection = WeatherTable.Columns.WEID +" =? ";
            selectionArgs  =new String[]{woid};
        }
        List<WeatherTable> weatherTables = new ArrayList();
        Cursor cursor = mContext.getContentResolver().query(CityProvider.CONTENT_URI_FOR_WEATHER, null, selection, selectionArgs, null);
        if (cursor == null) {
            return null;
        }

        while (cursor.moveToNext()) {
            weatherTables.add(WeatherTable.fromCusor(cursor));
        }
        cursor.close();

        if (isNeedForeCast) {
            List<ForeCastTable> foreCastTableList = new ArrayList();
            Cursor forecastCursor = mContext.getContentResolver().query(CityProvider.CONTENT_URI_FOR_FORECAST, null, selection, selectionArgs, null);
            while (forecastCursor.moveToNext()) {
                foreCastTableList.add(ForeCastTable.fromCursor(forecastCursor));
            }

            for (WeatherTable weatherTable : weatherTables) {
                for (ForeCastTable foreCastTable : foreCastTableList) {
                    if (!TextUtils.isEmpty(weatherTable.cityWeid) && weatherTable.cityWeid.equals(foreCastTable.weid)) {
                        weatherTable.addForecast(foreCastTable);
                    }
                }
            }

            if (forecastCursor != null) {
                forecastCursor.close();
                forecastCursor = null;
            }
        }

        return weatherTables;
    }


}
