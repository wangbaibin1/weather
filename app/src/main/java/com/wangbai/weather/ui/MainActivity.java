package com.wangbai.weather.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.fb.FeedbackAgent;
import com.wangbai.weather.Notification.WeatherNotification;
import com.wangbai.weather.R;
import com.wangbai.weather.db.WeatherDbProviderManager;
import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.event.CityChangeEvent;
import com.wangbai.weather.event.CurrentCityChangeEvent;
import com.wangbai.weather.event.LocationEvent;
import com.wangbai.weather.util.LocationUtil;
import com.wangbai.weather.util.ShareConfigManager;
import com.wangbai.weather.util.WeatherStatusUtil;
import com.wangbai.weather.util.YaHooWeatherUtils;
import com.wangbai.weather.widget.UpdateWeatherVeiw;
import com.wangbai.weather.widget.WeatherListView;

import java.net.URL;

import de.greenrobot.event.EventBus;


public class MainActivity extends BaseActivity {
    private TextView mCityTitle;
    private ImageView mSetting;
    private ImageView mWeatherBg;

    private DrawerLayout mDrawerLayout;
    private View mLeftDrawer;
    private WeatherListView mListView;
    private UpdateWeatherVeiw mUpdateWeatherVeiw;
    private MainWeatherAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initLeftMenu();

        initData();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {

        String woid = ShareConfigManager.getInstance(this).getCurrentCityWoid();
        if (!TextUtils.isEmpty(woid)) {
            WeatherTable weatherTable = WeatherDbProviderManager.getInstance(this).getCurrentCityWeather(true,woid);
            if (weatherTable != null) {
                cityOrWeatherUpdateUI(weatherTable);
                if(weatherTable.isLocationWeid()){
                    startLocation();
                    return;
                }

                if (YaHooWeatherUtils.isNeedUpdateWeather(weatherTable)) {
                    updateWeatherInfo(weatherTable);
                    return;
                }
            }
        }

        SearchCityActivity.startActivity(this);

    }

    private void initLeftMenu() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mLeftDrawer = findViewById(R.id.left_drawer);
        findViewById(R.id.notification_icon).setSelected(ShareConfigManager.getInstance(this).isNotificationOpen());
        findViewById(R.id.city_setting).setOnClickListener(mLeftMenuClickListener);
        findViewById(R.id.notification_setting).setOnClickListener(mLeftMenuClickListener);
        findViewById(R.id.feedback_setting).setOnClickListener(mLeftMenuClickListener);
        findViewById(R.id.help_setting).setOnClickListener(mLeftMenuClickListener);
    }

    private WeatherListView.ScrollListener mScrollListener = new WeatherListView.ScrollListener() {

        @Override
        public void startUpdate() {
            if (mListView.mIsUpdating || isDataEmpty()) {
                return;
            }
            if(mWeatherTable == null){
                return;
            }
            updateWeatherInfo(mWeatherTable);
        }

        @Override
        public void canUpdate(boolean isCan) {
            if (mListView.mIsUpdating || isDataEmpty()) {
                return;
            }
            if (isCan) {
                mUpdateWeatherVeiw.show(false, getString(R.string.handle_up_update));
            } else {
                if (mWeatherTable == null || TextUtils.isEmpty(mWeatherTable.pubDate)) {
                    mUpdateWeatherVeiw.show(false, getString(R.string.no_weather));
                } else {
                    mUpdateWeatherVeiw.show(false, WeatherTable.getPubDate(mWeatherTable.pubDate));
                }

            }
        }

        @Override
        public boolean isDataEmpty() {
            return mWeatherTable == null;
        }
    };

    private void initView() {
        mCityTitle = (TextView) findViewById(R.id.city_title);
        mSetting = (ImageView) findViewById(R.id.setting);
        mWeatherBg = (ImageView) findViewById(R.id.weather_bg);

        mListView = (WeatherListView) findViewById(R.id.listview);
        mListView.setScrollListener(mScrollListener);

        addHeaderOrFooter();

        mAdapter = new MainWeatherAdapter(this);
        mListView.setAdapter(mAdapter);

        mCityTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CityManagerActivity.startActivity(MainActivity.this);
            }
        });

        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
                    mDrawerLayout.closeDrawer(mLeftDrawer);
                } else {
                    mDrawerLayout.openDrawer(mLeftDrawer);
                }
            }
        });

    }

    private void addHeaderOrFooter() {
        mUpdateWeatherVeiw = new UpdateWeatherVeiw(this);
        mListView.addHeaderView(mUpdateWeatherVeiw);
    }


    private View.OnClickListener mLeftMenuClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.city_setting:
                    CityManagerActivity.startActivity(MainActivity.this);
                    break;
                case R.id.notification_setting:
                    boolean isOpen = !ShareConfigManager.getInstance(MainActivity.this).isNotificationOpen();
                    view.findViewById(R.id.notification_icon).setSelected(isOpen);
                    ShareConfigManager.getInstance(MainActivity.this).setNotificationOpen(isOpen);

                    String currentCityWoid = ShareConfigManager.getInstance(MainActivity.this).getCurrentCityWoid();
                    if (isOpen && !TextUtils.isEmpty(currentCityWoid)) {
                        WeatherTable weatherTable = WeatherDbProviderManager.getInstance(MainActivity.this).getCurrentCityWeather(false, currentCityWoid);
                        if (weatherTable != null) {
                            WeatherNotification.getInstance(MainActivity.this).displayPermantNotification(weatherTable);
                        }
                    } else {
                        WeatherNotification.getInstance(MainActivity.this).canclePermantNotification();
                    }
                    break;
                case R.id.feedback_setting:
                    FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
                    agent.startFeedbackActivity();
                    break;
                case R.id.help_setting:
                    AboutActivity.startActivity(MainActivity.this);
                    break;
            }
            if (mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
                mDrawerLayout.closeDrawer(mLeftDrawer);
            }
        }
    };

    public void onEventMainThread(Object event) {
        if (event instanceof CityChangeEvent) {
            onHandleCityWeatherUpdateEvent((CityChangeEvent) event);
        } else if (event instanceof CurrentCityChangeEvent) {
            initData();
        } else if(event instanceof LocationEvent){
            onHandleLocationEvent((LocationEvent) event);
        }

    }

    private void startLocation(){
        mUpdateWeatherVeiw.show(true, getString(R.string.location_going));
        mListView.startUpdating();
        LocationUtil locationUtil = new LocationUtil(this);
        locationUtil.startLocationAndUpdateUI(new LocationUtil.LocationListenter() {

            @Override
            public void weatherFinish(final WeatherTable weatherTable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mUpdateWeatherVeiw.show(false, "");
                        mListView.backToBegin();
                        if (weatherTable == null || TextUtils.isEmpty(weatherTable.cityWeid)) {
                            Toast.makeText(MainActivity.this, R.string.location_fail, Toast.LENGTH_SHORT).show();
                        } else {
                            weatherTable.mLocation = WeatherTable.LOCATION_SIGN;
                            Toast.makeText(MainActivity.this, R.string.location_success, Toast.LENGTH_SHORT).show();
                            cityOrWeatherUpdateUI(weatherTable);
                            ShareConfigManager.getInstance(MainActivity.this).setCurrentCityWoid(weatherTable.cityWeid);
                            WeatherDbProviderManager.getInstance(MainActivity.this).insertLocationWeatherData(weatherTable);
                        }
                    }
                });
            }

            @Override
            public void fail() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mUpdateWeatherVeiw.show(false, "");
                        mListView.backToBegin();
                        Toast.makeText(MainActivity.this, R.string.location_fail, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void onHandleLocationEvent(LocationEvent event) {
        WeatherDbProviderManager.getInstance(MainActivity.this).insertLocationWeatherData(event.mWeatherTable);
        ShareConfigManager.getInstance(this).setCurrentCityWoid(event.mWeatherTable.cityWeid);
        mCityTitle.setText(event.mWeatherTable.cityName);
        startLocation();
    }

    private WeatherTable mWeatherTable;

    public void cityOrWeatherUpdateUI(WeatherTable weatherTable) {
        if (weatherTable == null) {
            return;
        }
        mWeatherTable = weatherTable;
        mAdapter.addTodayInfo(weatherTable);
        mAdapter.addForeCastInfo(weatherTable);

        mAdapter.notifyDataSetChanged();
        mCityTitle.setText(weatherTable.cityName);

        int resId = WeatherStatusUtil.getWeatherBgResid(weatherTable.code);
        if (resId != -1) {
            mWeatherBg.setImageResource(resId);
        }

    }

    private void updateWeatherInfo(final WeatherTable weatherTable) {
        mUpdateWeatherVeiw.show(true, getString(R.string.weather_updating));
        mListView.startUpdating();
        final URL weatherurl = YaHooWeatherUtils.getURL(YaHooWeatherUtils.getRequestWeatherInfoUrl(weatherTable.cityWeid, ShareConfigManager.getInstance(this).getTempertureUnit()));
        new Thread() {
            @Override
            public void run() {
                super.run();

                final WeatherTable info = YaHooWeatherUtils.sendRequestAndParseResultWeather(weatherurl, weatherTable.cityWeid);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mUpdateWeatherVeiw.show(false, "");
                        cityOrWeatherUpdateUI(info);
                        mListView.backToBegin();
                    }
                });

                if (info == null) {
                    return;
                }
                if (ShareConfigManager.getInstance(MainActivity.this).isNotificationOpen()) {
                    WeatherNotification.getInstance(MainActivity.this).displayPermantNotification(info);
                }

                WeatherDbProviderManager.getInstance(MainActivity.this).insertWeatherData(info);
            }
        }.start();
    }

    private void onHandleCityWeatherUpdateEvent(final CityChangeEvent event) {
        ShareConfigManager.getInstance(this).setCurrentCityWoid(event.mWeatherTable.cityWeid);
        if(!event.mIsExist){
            WeatherDbProviderManager.getInstance(MainActivity.this).insertWeatherData(event.mWeatherTable);
        }
        cityOrWeatherUpdateUI(event.mWeatherTable);

        if (ShareConfigManager.getInstance(MainActivity.this).isNotificationOpen()) {
            WeatherNotification.getInstance(MainActivity.this).displayPermantNotification(event.mWeatherTable);
        }

        if (!YaHooWeatherUtils.isNeedUpdateWeather(event.mWeatherTable)) {
            return;
        }

        updateWeatherInfo(event.mWeatherTable);

    }

    long mLastTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (System.currentTimeMillis() - mLastTime > 2000) {
                Toast.makeText(this, R.string.exit_tips, Toast.LENGTH_SHORT).show();
                mLastTime = System.currentTimeMillis();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
