package com.wangbai.weather.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wangbai.weather.R;
import com.wangbai.weather.db.WeatherTable;

import java.util.ArrayList;
import java.util.List;

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

        if(weatherTable.isLocation()){
            convertView.findViewById(R.id.location_icon).setVisibility(View.VISIBLE);
        } else{
            convertView.findViewById(R.id.location_icon).setVisibility(View.GONE);
        }

        return convertView;
    }


}
