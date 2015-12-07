package com.wangbai.weather.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.wangbai.weather.R;
import com.wangbai.weather.db.LocalCityDbManager;
import com.wangbai.weather.db.WeatherDbProviderManager;
import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.event.CityChangeEvent;
import com.wangbai.weather.event.LocationEvent;
import com.wangbai.weather.loader.CitySearchLoader;
import com.wangbai.weather.util.NetWorkUtil;
import com.wangbai.weather.util.ShareConfigManager;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by binwang on 2015/11/10.
 */
public class SearchCityActivity extends BaseActivity {
    private ImageView mSearchingImg;
    private EditText mEditTextView;
    private ListView mResultListView;
    private SearchResultListAdapter mAdapter;

    private GridView  mGridView;
    private CityGridAdapter mCityGridAdapter;
    private CitySearchLoader mCitySearchLoader;

    private List<WeatherTable> mHasAddedCitys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        mSearchingImg = (ImageView) findViewById(R.id.searching_icon);
        mEditTextView = (EditText) findViewById(R.id.search_edit);
        mResultListView = (ListView) findViewById(R.id.result_list);
        mAdapter = new SearchResultListAdapter(this);
        mResultListView.setAdapter(mAdapter);

        mGridView = (GridView) findViewById(R.id.gridview);
        mCityGridAdapter = new CityGridAdapter(this);
        mGridView.setAdapter(mCityGridAdapter);
        mGridView.setOnItemClickListener(mGridOnItemClickListener);

        mResultListView.setOnItemClickListener(mOnItemClickListener);
        mEditTextView.addTextChangedListener(mTextWatcher);

        loadHasAddedCity();
        loadLocalCityData();
    }

    private void loadHasAddedCity(){
        mHasAddedCitys = WeatherDbProviderManager.getInstance(this).quaryWeatherData(false, "");
    }

    private TextWatcher mTextWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
            if(TextUtils.isEmpty(charSequence)){
                mSearchingImg.clearAnimation();
                mSearchingImg.setVisibility(View.GONE);
                return;
            }

            if (!NetWorkUtil.isNetworkConnected()) {
                Toast.makeText(SearchCityActivity.this,R.string.no_network,Toast.LENGTH_SHORT).show();
                return;
            }

            mSearchingImg.setVisibility(View.VISIBLE);
            mSearchingImg.setAnimation(android.view.animation.AnimationUtils.loadAnimation(SearchCityActivity.this, R.anim.update_weather_rotate));

            if(mCitySearchLoader == null){
                mCitySearchLoader = new CitySearchLoader(mSearchResultListener);
            }

            mCitySearchLoader.startSearchCity(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    private CitySearchLoader.SearchResultListener mSearchResultListener = new CitySearchLoader.SearchResultListener(){

        @Override
        public void searchFinish(String key, final List<SearResultData> datas) {
            if(!key.equals(mEditTextView.getText().toString())){
                return;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSearchingImg.clearAnimation();
                    mSearchingImg.setVisibility(View.GONE);
                    mAdapter.setDatas(datas);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    };


    private void loadLocalCityData(){

        List<WeatherTable> weatherTables =   LocalCityDbManager.queryWorldCityTableByCityName(this, "");

        mCityGridAdapter.addTables(weatherTables);
        mCityGridAdapter.notifyDataSetChanged();
    }

    public boolean isHasAdded(String woeid){
        if(mHasAddedCitys == null || mHasAddedCitys.isEmpty() || TextUtils.isEmpty(woeid)){
            return false;
        }

       for(WeatherTable weatherTable : mHasAddedCitys){
           if(woeid.equals(weatherTable.cityWeid)){
               return true;
           }
       }

        return false;
    }
    private AdapterView.OnItemClickListener mGridOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final WeatherTable weatherTable = (WeatherTable) mCityGridAdapter.getItem(position);
            if(isHasAdded(weatherTable.cityWeid)){
                Toast.makeText(SearchCityActivity.this,R.string.have_added_tips,Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(SearchCityActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            if (weatherTable.isLocation()) {
                EventBus.getDefault().post(new LocationEvent().setData(weatherTable));
            } else {
                WeatherDbProviderManager.getInstance(SearchCityActivity.this).insertWeatherData(weatherTable);
                EventBus.getDefault().post(new CityChangeEvent().setData(weatherTable,false));
            }
        }
    };


    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SearResultData searResultData = (SearResultData) mAdapter.getItem(position);
            if(isHasAdded(searResultData.mWoeid)){
                Toast.makeText(SearchCityActivity.this,R.string.have_added_tips,Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(SearchCityActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);


            WeatherTable weatherTable = new WeatherTable();
            weatherTable.cityName = searResultData.mName;
            weatherTable.countryName = searResultData.mCountry;
            weatherTable.cityWeid = searResultData.mWoeid;

            WeatherDbProviderManager.getInstance(SearchCityActivity.this).insertWeatherData(weatherTable);
            EventBus.getDefault().post(new CityChangeEvent().setData(weatherTable,false));


        }
    };

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SearchCityActivity.class));
    }

    public void onClickBack(View view){
        if (TextUtils.isEmpty(ShareConfigManager.getInstance(this).getCurrentCityWoid())) {
            Toast.makeText(this,R.string.add_city_tips,Toast.LENGTH_SHORT).show();
            return;
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(ShareConfigManager.getInstance(this).getCurrentCityWoid())) {
            Toast.makeText(this,R.string.add_city_tips,Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }
}
