package com.wangbai.weather;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by binwang on 2015/11/19.
 */
public class BaseActivity extends Activity{
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

}
