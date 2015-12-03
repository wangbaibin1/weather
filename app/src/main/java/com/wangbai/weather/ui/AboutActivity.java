package com.wangbai.weather.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wangbai.weather.R;

/**
 * Created by binwang on 2015/11/17.
 */
public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }

    public void onClickBack(View view){
        finish();
    }
}
