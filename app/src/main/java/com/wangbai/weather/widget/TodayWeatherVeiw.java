package com.wangbai.weather.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangbai.weather.R;
import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.util.DenstyUtil;
import com.wangbai.weather.util.WeatherStatusUtil;

/**
 * Created by binwang on 2015/11/11.
 */
public class TodayWeatherVeiw extends LinearLayout {
    private TextView mweather_info;
    private TextView mtigan_tm;

    private ImageView mimg_tem;

    public TodayWeatherVeiw(Context context) {
        this(context, null);
    }

    public TodayWeatherVeiw(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.today_weather_layout, this);
        mweather_info = (TextView) findViewById(R.id.weather_info);
        mtigan_tm = (TextView) findViewById(R.id.tigan_tm);

        mimg_tem = (ImageView) findViewById(R.id.img_tem);
    }

    public void updateUI(WeatherTable info) {
        if(info == null){
            return;
        }
        mweather_info.setText(WeatherStatusUtil.CODE_TO_WEATHERINFO[info.code]);
        if(TextUtils.isEmpty(info.temperature)){
            mtigan_tm.setText("~");
        } else{
            mtigan_tm.setText(getContext().getString(R.string.show_temper , info.temperature));
        }

        WeatherStatusUtil.setForecastIcon(mimg_tem, info.code);

    }
}
