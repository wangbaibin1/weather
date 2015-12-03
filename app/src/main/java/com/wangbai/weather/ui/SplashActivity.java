package com.wangbai.weather.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;

import com.wangbai.weather.R;

/**
 * Created by binwang on 2015/12/3.
 */
public class SplashActivity extends BaseActivity{

    public static void startActivity(Context context){
        context.startActivity(new Intent(context, SplashActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1500);

    }


}
