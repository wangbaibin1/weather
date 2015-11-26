package com.wangbai.weather.Notification;

import android.content.Context;
import android.os.Parcel;
import android.text.TextUtils;
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
        setImageViewResource(R.id.weather_icon, WeatherStatusUtil.getForecastIconResid(weatherTable.code));
        setTextViewText(R.id.temperture, weatherTable.temperature + weatherTable.temperUnit);
        setTextViewText(R.id.weather_info,mContext.getString(WeatherStatusUtil.CODE_TO_WEATHERINFO[weatherTable.code]));

        setTextViewText(R.id.city,weatherTable.cityName);
        if(!TextUtils.isEmpty(weatherTable.pubDate)){
            setTextViewText(R.id.pub_date,WeatherTable.getPubDate(weatherTable.pubDate));
        }

        return this;
    }

}
