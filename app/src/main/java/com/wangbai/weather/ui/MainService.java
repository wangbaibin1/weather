package com.wangbai.weather.ui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.wangbai.weather.Notification.WeatherNotification;
import com.wangbai.weather.db.WeatherDbProviderManager;
import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.util.ShareConfigManager;

import java.util.List;

/**
 * Created by binwang on 2015/11/18.
 */
public class MainService extends Service{
    @Override
    public void onCreate() {
        super.onCreate();
        String currentWoid = ShareConfigManager.getInstance(this).getCurrentCityWoid();
       if(ShareConfigManager.getInstance(this).isNotificationOpen() && !TextUtils.isEmpty(currentWoid)){
           WeatherTable weatherTable = WeatherDbProviderManager.getInstance(this).getCurrentCityWeather(false,currentWoid);
           if(weatherTable != null){
               WeatherNotification.getInstance(this).displayPermantNotification(weatherTable);
           }
       }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
