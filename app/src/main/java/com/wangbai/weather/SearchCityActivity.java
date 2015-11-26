package com.wangbai.weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.wangbai.weather.db.LocalCityDbManager;
import com.wangbai.weather.db.WeatherDbProviderManager;
import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.event.CityWeatherUpdateEvent;
import com.wangbai.weather.util.YaHooWeatherUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by binwang on 2015/11/10.
 */
public class SearchCityActivity extends BaseActivity {
    private EditText mEditTextView;
    private ListView mResultListView;
    private SearchResultListAdapter mAdapter;

    private GridView  mGridView;
    private CityGridAdapter mCityGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        mEditTextView = (EditText) findViewById(R.id.search_edit);
        mResultListView = (ListView) findViewById(R.id.result_list);
        mAdapter = new SearchResultListAdapter(this);
        mResultListView.setAdapter(mAdapter);

        mGridView = (GridView) findViewById(R.id.gridview);
        mCityGridAdapter = new CityGridAdapter(this);
        mGridView.setAdapter(mCityGridAdapter);

        mResultListView.setOnItemClickListener(mOnItemClickListener);
        mEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        URL url = YaHooWeatherUtils.getURL(YaHooWeatherUtils.getRequestSearchCitysUrl(charSequence.toString()));
                        if (url == null) {
                            return;
                        }
                        List<HashMap<String, String>> result = YaHooWeatherUtils.sendRequestAndParseResult(url, "");
                        if (result == null || result.isEmpty()) {
                            return;
                        }
                        List<SearResultData> datas = toSearResultDatas(result);
                        mAdapter.setDatas(datas);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                }.start();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        loadLocalCityData();
    }

    private void loadLocalCityData(){
        List<WeatherTable> weatherTables =   LocalCityDbManager.queryWorldCityTableByCityName(this, "");

        mCityGridAdapter.addTables(weatherTables);
        mCityGridAdapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(SearchCityActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            SearResultData searResultData = (SearResultData) mAdapter.getItem(position);
            WeatherTable weatherTable = new WeatherTable();
            weatherTable.cityName = searResultData.mName;
            weatherTable.countryName = searResultData.mCountry;
            weatherTable.cityWeid = searResultData.mWoeid;

            WeatherDbProviderManager.getInstance(SearchCityActivity.this).insertWeatherData(weatherTable);
            EventBus.getDefault().post(new CityWeatherUpdateEvent().setData(weatherTable));


        }
    };

    private List<SearResultData> toSearResultDatas(List<HashMap<String, String>> result) {
        List<SearResultData> datas = new ArrayList<>();
        for (HashMap<String, String> item : result) {
            SearResultData data = new SearResultData();
            data.mName = item.get("name");
            data.mCountry = item.get("country");
            data.mWoeid = item.get("woeid");

            data.mAdmin1 = item.get("admin1");
            data.mAdmin2 = item.get("admin2");
            data.mAdmin3 = item.get("admin3");
            datas.add(data);
        }
        return datas;

    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SearchCityActivity.class));
    }

}
