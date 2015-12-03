package com.wangbai.weather.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wangbai.weather.R;
import com.wangbai.weather.db.WeatherDbProviderManager;
import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.util.ShareConfigManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binwang on 2015/11/16.
 */
public class CityManagerAdapter extends BaseAdapter {
    private List<WeatherTable> mWeatherTableList = new ArrayList();
    private Context mContext;
    public CityManagerAdapter(Context context){
        mContext = context;
    }

    public void addWeatherTables(List<WeatherTable> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        mWeatherTableList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mWeatherTableList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWeatherTableList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.added_city_item_view,parent,false);
        }
       final WeatherTable weatherTable = (WeatherTable) getItem(position);
        ((TextView) convertView.findViewById(R.id.city_name)).setText(weatherTable.cityName);
        ((TextView) convertView.findViewById(R.id.tempreture)).setText(weatherTable.minTemper +"~"+weatherTable.maxTemper);
        convertView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWeatherTableList.remove(weatherTable);
                notifyDataSetChanged();
                WeatherDbProviderManager.getInstance(mContext).deleteWeatherData(weatherTable);
                if (mWeatherTableList != null && !mWeatherTableList.isEmpty()) {
                    ShareConfigManager.getInstance(mContext).setCurrentCityWoid(mWeatherTableList.get(0).cityWeid);
                } else {
                    ShareConfigManager.getInstance(mContext).setCurrentCityWoid("");
                }


            }
        });
        return convertView;
    }
}
