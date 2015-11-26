package com.wangbai.weather;

import android.app.Application;
import android.content.Intent;

import com.wangbai.weather.db.LocalCityDbManager;

/**
 * Created by binwang on 2015/11/18.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Intent intent = new Intent(this, MainService.class);
        startService(intent);

        LocalCityDbManager.writeToDatabase(this);
    }
}
