package com.wangbai.weather.info;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by binwang on 2015/11/23.
 */
public abstract class BaseInfo {
    public static final int TOADY_WEATHER = 0;
    public static final int FORECAST_WEATHER = 1;
    public int mType;
    protected Context mContext;

    public View getConvertView(View convertView,ViewGroup parent){
        return convertView;
    }
}
