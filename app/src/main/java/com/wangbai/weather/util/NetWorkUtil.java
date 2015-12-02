package com.wangbai.weather.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.wangbai.weather.MainApplication;

/**
 * Created by binwang on 2015/12/2.
 */
public class NetWorkUtil {
    public static boolean isNetworkConnected() {
        Context context = MainApplication.getContext();
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }
        return false;
    }
}
