package com.wangbai.weather.event;

import com.wangbai.weather.db.WeatherTable;

/**
 * Created by binwang on 2015/11/16.
 */
public class CityChangeEvent {
    public WeatherTable mWeatherTable;
    public boolean mIsExist;
    public CityChangeEvent setData(WeatherTable data,boolean isExist) {
        mWeatherTable = data;
        mIsExist = isExist;
        return this;
    }
}
