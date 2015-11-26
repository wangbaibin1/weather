package com.wangbai.weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wangbai.weather.db.WeatherDbProviderManager;
import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.event.CityWeatherUpdateEvent;
import com.wangbai.weather.event.CurrentCityChangeEvent;
import com.wangbai.weather.util.ShareConfigManager;

import de.greenrobot.event.EventBus;

/**
 * Created by binwang on 2015/11/16.
 */
public class CityManagerActivity extends BaseActivity{
    private ListView mListView;
    private CityManagerAdapter mAddedCityAdapter;
    private String mCurrentWoid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.added_city_activity);
        mListView = (ListView) findViewById(R.id.listview);
        mAddedCityAdapter = new CityManagerAdapter(this);
        mListView.setAdapter(mAddedCityAdapter);
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchCityActivity.startActivity(CityManagerActivity.this);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CityManagerActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                EventBus.getDefault().post(new CityWeatherUpdateEvent().setData((WeatherTable) mAddedCityAdapter.getItem(position)));
            }
        });


        loadData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(TextUtils.isEmpty(ShareConfigManager.getInstance(this).getCurrentCityWoid())
                || !ShareConfigManager.getInstance(this).getCurrentCityWoid().equals(mCurrentWoid)){
            EventBus.getDefault().post(new CurrentCityChangeEvent());
        }
    }

    private void loadData(){
        mCurrentWoid = ShareConfigManager.getInstance(this).getCurrentCityWoid();
        mAddedCityAdapter.addWeatherTables(WeatherDbProviderManager.getInstance(this).quaryWeatherData(true,""));
    }

    public static void startActivity(Context context){
        context.startActivity(new Intent(context,CityManagerActivity.class));
    }
}
