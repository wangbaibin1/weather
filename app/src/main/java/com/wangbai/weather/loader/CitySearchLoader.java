package com.wangbai.weather.loader;

import android.os.Handler;
import android.os.HandlerThread;

import com.wangbai.weather.ui.SearResultData;
import com.wangbai.weather.util.YaHooWeatherUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by binwang on 2015/12/2.
 */
public class CitySearchLoader extends HandlerThread {
    private Handler mThreadHandler;
    private MyRunnable mMyRunnable;
    private SearchResultListener mSearchResultL;

    public CitySearchLoader(SearchResultListener l) {
        super("Search City Loader");
        mSearchResultL = l;
        start();
        mThreadHandler = new Handler(getLooper());
    }


    @Override
    public void run() {
        super.run();
    }

    class MyRunnable implements Runnable {
        public String mKey;
        public boolean isCancle = false;

        public MyRunnable(String key) {
            mKey = key;
        }

        @Override
        public void run() {
            URL url = YaHooWeatherUtils.getURL(YaHooWeatherUtils.getRequestSearchCitysUrl(mKey));
            if (url == null) {
                return;
            }

            if(isCancle){
                return;
            }
            List<HashMap<String, String>> result = YaHooWeatherUtils.sendRequestAndParseResult(url, "");

            if(isCancle){
                return;
            }

            if (result == null || result.isEmpty()) {
                mSearchResultL.searchFinish(mKey,null);
                return;
            }
            List<SearResultData> datas = toSearResultDatas(result);
            mSearchResultL.searchFinish(mKey, datas);
        }
    }


    public void startSearchCity(String key) {
        if (mMyRunnable != null) {
            mMyRunnable.isCancle = true;
            mThreadHandler.removeCallbacks(mMyRunnable);
        }

        mMyRunnable = new MyRunnable(key);
        mThreadHandler.post(mMyRunnable);
    }

    public interface SearchResultListener {
        void searchFinish(String key, List<SearResultData> datas);
    }

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
}
