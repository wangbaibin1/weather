package com.wangbai.weather.event;

import com.wangbai.weather.db.WeatherTable;

/**
 * Created by binwang on 2015/12/7.
 */
public class LocationEvent {
    public WeatherTable mWeatherTable;
    public LocationEvent setData(WeatherTable data) {
        mWeatherTable = data;
        return this;
    }
}
