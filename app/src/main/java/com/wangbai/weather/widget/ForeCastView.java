package com.wangbai.weather.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wangbai.weather.R;
import com.wangbai.weather.db.ForeCastTable;
import com.wangbai.weather.db.WeatherTable;
import com.wangbai.weather.util.DenstyUtil;
import com.wangbai.weather.util.WeatherStatusUtil;

import java.util.List;

/**
 * Created by binwang on 2015/11/16.
 */
public class ForeCastView extends LinearLayout {
    private Context mContext;

    public ForeCastView(Context context) {
        this(context, null);
    }

    public ForeCastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        // 定义背景的效果
        setOrientation(VERTICAL);
        setBackgroundColor(0x4c333333);
        mContext = context;
    }

    public void updateForeCast(WeatherTable weatherTable) {
        removeAllViews();
        if (weatherTable == null) {
            return;
        }
        List<ForeCastTable> foreCastTables = weatherTable.getForecastList();
        if (foreCastTables == null || foreCastTables.size() == 0) {
            return;
        }

        for (ForeCastTable table : foreCastTables) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.forecast_item_view, null);
            if (getChildCount() == 0) {
                view.findViewById(R.id.split).setVisibility(GONE);
            } else {
                view.findViewById(R.id.split).setVisibility(VISIBLE);
            }


            // 定义天气View的的效果
            ((TextView) view.findViewById(R.id.day)).setText(table.day);
            ((TextView) view.findViewById(R.id.day)).setPadding(10,0,0,0);

            ((TextView) view.findViewById(R.id.tempreture)).setText(table.low + "~" + table.high);

            WeatherStatusUtil.setForecastIcon(((ImageView) view.findViewById(R.id.weather_icon)), table.code);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, DenstyUtil.dip2px(mContext, 60));
            addView(view, lp);
        }

    }
}
