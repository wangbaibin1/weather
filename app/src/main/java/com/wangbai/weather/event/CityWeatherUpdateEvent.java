package com.wangbai.weather.event;

import com.wangbai.weather.db.WeatherTable;

/**
 * Created by binwang on 2015/11/16.
 */
public class CityWeatherUpdateEvent {
    public WeatherTable mWeatherTable;

    public CityWeatherUpdateEvent setData(WeatherTable data) {
        mWeatherTable = data;
        return this;
    }
}
