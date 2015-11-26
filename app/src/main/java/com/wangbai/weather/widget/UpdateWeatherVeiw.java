package com.wangbai.weather.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangbai.weather.R;

/**
 * Created by binwang on 2015/11/23.
 */
public class UpdateWeatherVeiw extends LinearLayout {
    private ImageView mUpdateIcon;
    private TextView mUpdateTextView;

    public UpdateWeatherVeiw(Context context) {
        this(context, null);
    }

    public UpdateWeatherVeiw(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.update_weather_view, this);

        mUpdateIcon = (ImageView) findViewById(R.id.updateIcon);
        mUpdateTextView = (TextView) findViewById(R.id.updatetext);
    }

    public void show(boolean isUpdating,String updateText){
        if(isUpdating){
            mUpdateTextView.setText(updateText);
            mUpdateIcon.startAnimation(android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.update_weather_rotate));
        } else{
            mUpdateTextView.setText(updateText);
            mUpdateIcon.clearAnimation();
        }
    }
}
