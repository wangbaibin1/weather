package com.wangbai.weather.info;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.widget.TodayWeatherVeiw;

/**
 * Created by binwang on 2015/11/23.
 */
public class TodayInfoItem extends BaseInfo{
    private WeatherTable mWeatherTable;
    public TodayInfoItem(Context context,WeatherTable weatherTable){
        mType = TOADY_WEATHER;
        mWeatherTable = weatherTable;
        mContext = context;
    }

    public void updateWeatherTable(WeatherTable weatherTable){
        mWeatherTable = weatherTable;
    }

    @Override
    public View getConvertView(View convertView, ViewGroup parent) {
        if(convertView == null ||!( convertView instanceof TodayWeatherVeiw)){
            convertView = new TodayWeatherVeiw(mContext);

        }
        ((TodayWeatherVeiw) convertView).updateUI(mWeatherTable);
        return convertView;
    }
}
