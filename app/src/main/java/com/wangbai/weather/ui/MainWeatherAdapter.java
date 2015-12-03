package com.wangbai.weather.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.info.BaseInfo;
import com.wangbai.weather.info.ForeCastItem;
import com.wangbai.weather.info.TodayInfoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binwang on 2015/11/23.
 */
public class MainWeatherAdapter extends BaseAdapter {
    private List<BaseInfo> mInfoList = new ArrayList();
    private Context mContext;

    public MainWeatherAdapter(Context context) {
        mContext = context;
    }

    private BaseInfo getBaseInfoByType(int type) {
        for (BaseInfo info : mInfoList) {
            if (info.mType == type) {
                return info;
            }
        }
        return null;
    }

    public void addTodayInfo(WeatherTable weatherTable) {
        BaseInfo info = getBaseInfoByType(BaseInfo.TOADY_WEATHER);
        if (info == null) {
            addBaseIfo(new TodayInfoItem(mContext, weatherTable));
        } else {
            TodayInfoItem todayInfoItem = (TodayInfoItem) info;
            todayInfoItem.updateWeatherTable(weatherTable);
        }
    }

    public void addForeCastInfo(WeatherTable weatherTable) {
        BaseInfo info = getBaseInfoByType(BaseInfo.FORECAST_WEATHER);
        if (info == null) {
            addBaseIfo(new ForeCastItem(mContext, weatherTable));
        } else {
            ForeCastItem foreCastItem = (ForeCastItem) info;
            foreCastItem.updateWeatherTable(weatherTable);
        }
    }

    public void addBaseIfo(BaseInfo baseInfo) {
        mInfoList.add(baseInfo);
    }

    @Override
    public int getCount() {
        return mInfoList.size();
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        return mInfoList.get(position).mType;
    }

    @Override
    public Object getItem(int position) {
        return mInfoList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseInfo baseInfo = (BaseInfo) getItem(position);

        return baseInfo.getConvertView(convertView, parent);
    }
}
