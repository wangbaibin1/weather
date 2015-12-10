package com.wangbai.weather.info;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.widget.ForeCastView;

/**
 * Created by binwang on 2015/11/23.
 */
public class ForeCastItem extends BaseInfo {
    private WeatherTable mWeatherTable;

    public ForeCastItem(Context context, WeatherTable weatherTable) {
        mType = FORECAST_WEATHER;
        mWeatherTable = weatherTable;
        mContext = context;
    }

    public void updateWeatherTable(WeatherTable weatherTable) {
        mWeatherTable = weatherTable;
    }

    @Override
    public View getConvertView(View convertView, ViewGroup parent) {
        if (convertView == null || convertView instanceof ForeCastView) {
            convertView = new ForeCastView(mContext);
        }
        ((ForeCastView) convertView).updateForeCast(mWeatherTable);
        return convertView;
    }
}
