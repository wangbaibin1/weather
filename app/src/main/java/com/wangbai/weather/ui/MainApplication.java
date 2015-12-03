package com.wangbai.weather.ui;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.wangbai.weather.db.LocalCityDbManager;

/**
 * Created by binwang on 2015/11/18.
 */
public class MainApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Intent intent = new Intent(this, MainService.class);
        startService(intent);

        LocalCityDbManager.writeToDatabase(this);
    }

    public static Context getContext(){
        return mContext;
    }
}
