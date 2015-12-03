package com.wangbai.weather.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wangbai.weather.R;
import com.wangbai.weather.db.WeatherDbProviderManager;
import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.event.CityWeatherUpdateEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by binwang on 2015/11/24.
 */
public class CityGridAdapter extends BaseAdapter{
    private List<WeatherTable> mWeatherTable = new ArrayList();
    private Context mContext;
    public CityGridAdapter(Context context){
        mContext = context;
    }

    public void addTables(List<WeatherTable> tables){
        if(tables == null || tables.isEmpty()){
            return;
        }
        mWeatherTable.addAll(tables);
    }

    @Override
    public int getCount() {
        return mWeatherTable.size();
    }

    @Override
    public Object getItem(int position) {
        return mWeatherTable.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.city_grid_item,parent,false);
        }
        final WeatherTable weatherTable = (WeatherTable) getItem(position);
        TextView cityName = (TextView) convertView.findViewById(R.id.city_name);
        cityName.setText(weatherTable.cityName);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intent);


                WeatherDbProviderManager.getInstance(mContext).insertWeatherData(weatherTable);
                EventBus.getDefault().post(new CityWeatherUpdateEvent().setData(weatherTable));
            }
        });


        return convertView;
    }
}
