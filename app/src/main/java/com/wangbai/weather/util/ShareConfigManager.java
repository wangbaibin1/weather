package com.wangbai.weather.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by binwang on 2015/11/17.
 */
public class ShareConfigManager {

    private static final String NORMAL_CONFIG = "normal_config";

    private static final String CURRENT_WOID = "current_woid";
    private static final String TEMPERTURE_UNIT = "tempreture_unit";
    private static final String NOTIFICATION_OPNE = "notification_open";

    private static final String COPY_ASSETS = "copy_assets";

    private static ShareConfigManager mInstance;
    private SharedPreferences mSharedPreferences;

    public static synchronized ShareConfigManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ShareConfigManager(context);
        }
        return mInstance;
    }

    private ShareConfigManager(Context context) {
        mSharedPreferences = context.getApplicationContext().getSharedPreferences(NORMAL_CONFIG, Context.MODE_PRIVATE);
    }

    public void setIntValue(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public void setBooleanValue(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public int getIntValue(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public String getStringValue(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public boolean getBooleanValue(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public String getTempertureUnit() {
        return getStringValue(TEMPERTURE_UNIT, "c");
    }

    public void setTempertureUnit(String unit) {
        setStringValue(TEMPERTURE_UNIT, unit);
    }

    public String getCurrentCityWoid() {
        return getStringValue(CURRENT_WOID, "");
    }

    public void setCurrentCityWoid(String woid) {
        setStringValue(CURRENT_WOID, woid);
    }

    public boolean isNotificationOpen() {
        return getBooleanValue(NOTIFICATION_OPNE, true);
    }

    public void setNotificationOpen(boolean isOpen) {
        setBooleanValue(NOTIFICATION_OPNE, isOpen);
    }

    public boolean isCopyAssets() {
        return getBooleanValue(COPY_ASSETS, false);
    }

    public void setCopyAssets(boolean isCopyAssets){
        setBooleanValue(COPY_ASSETS,isCopyAssets);
    }
}
