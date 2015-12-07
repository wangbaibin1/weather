package com.wangbai.weather.Notification;

import android.content.Context;
import android.os.Parcel;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.wangbai.weather.R;
import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.util.WeatherStatusUtil;

/**
 * Created by binwang on 2015/11/18.
 */
public class WeatherRemoteViews extends RemoteViews{
    private Context mContext;
    public WeatherRemoteViews(Context context) {
        super(context.getPackageName(), R.layout.wheather_notification_layout);
        mContext = context;
    }

    public WeatherRemoteViews updateWeatherInfo(WeatherTable weatherTable){
        if(weatherTable.isHaveSuccessUpdate()){
            setViewVisibility(R.id.weather_layout, View.VISIBLE);
            setViewVisibility(R.id.no_weather_layout, View.GONE);
            setImageViewResource(R.id.weather_icon, WeatherStatusUtil.getForecastIconResid(weatherTable.code));
            setTextViewText(R.id.temperture, mContext.getString(R.string.show_temper , weatherTable.temperature));
            setTextViewText(R.id.weather_info,mContext.getString(WeatherStatusUtil.CODE_TO_WEATHERINFO[weatherTable.code]));

            setTextViewText(R.id.city,weatherTable.cityName);
            if(!TextUtils.isEmpty(weatherTable.pubDate)){
                setTextViewText(R.id.pub_date,WeatherTable.getPubDate(weatherTable.pubDate));
            }
        } else{
            setViewVisibility(R.id.weather_layout, View.GONE);
            setViewVisibility(R.id.no_weather_layout, View.VISIBLE);
            setTextViewText(R.id.no_weather_city, weatherTable.cityName);
        }



        return this;
    }

}
