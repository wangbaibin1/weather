package com.wangbai.weather.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.wangbai.weather.ui.MainActivity;
import com.wangbai.weather.R;
import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.ui.MainApplication;

/**
 * Created by binwang on 2015/11/12.
 */
public class WeatherNotification {
    public static final int PERMAINT_WEATHER_ID = 1;

    private static  WeatherNotification mInstance;
    private  NotificationManager mNotificationManager;
    private Context mContext;
    private WeatherNotification(Context context){
        mContext  = context.getApplicationContext();
        mNotificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public static WeatherNotification getInstance(Context context){
        if(mInstance == null){
            mInstance = new WeatherNotification(context);
        }
        return mInstance;
    }

    public void displayPermantNotification(WeatherTable weatherTable){
        int icon = R.mipmap.ic_launcher;
        CharSequence tickerText = MainApplication.getContext().getString(R.string.app_name);
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon,tickerText,when);
        WeatherRemoteViews remoteViews = new WeatherRemoteViews(mContext);

        remoteViews.updateWeatherInfo(weatherTable);
        notification.contentView = remoteViews;
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        PendingIntent contextIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        notification.contentIntent  = contextIntent;
        mNotificationManager.notify(PERMAINT_WEATHER_ID, notification);
    }

    public void canclePermantNotification(){
        mNotificationManager.cancel(PERMAINT_WEATHER_ID);
    }

}
