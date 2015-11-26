package com.wangbai.weather.util;

import android.util.SparseIntArray;
import android.widget.ImageView;

import com.wangbai.weather.R;

/**
 * Created by binwang on 2015/11/11.
 */
public class WeatherStatusUtil {
    public static final int WEATHER_UNKNOW = -1;
    /**
     * ∑Á
     */
    public static final int WEATHER_WIND = 0;
    /**
     * ¿◊’Û”Í
     */
    public static final int WEATHER_THUNDERSTORMS = 1;
    /**
     * ”Íº–—©
     */
    public static final int WEATHER_RAIN_AND_SNOW = 2;
    /**
     * ∂≥”Í
     */
    public static final int WEATHER_FREEZING_RAIN = 3;
    /**
     * –°”Í
     */
    public static final int WEATHER_DRIZZLE = 4;
    /**
     * ’Û”Í
     */
    public static final int WEATHER_SHOWERS = 5;
    /**
     * –°—©
     */
    public static final int WEATHER_SNOW = 6;
    /**
     * ’Û—©
     */
    public static final int WEATHER_SNOW_SHOWERS = 7;
    /**
     * ”Íº–±˘±¢
     */
    public static final int WEATHER_HAIL = 8;
    /**
     * ŒÌˆ≤
     */
    public static final int WEATHER_DUST = 9;
    /**
     * ŒÌ
     */
    public static final int WEATHER_FOGGY = 10;
    /**
     * “ıÃÏ
     */
    public static final int WEATHER_COLD = 11;
    /**
     * ∂‡‘∆
     */
    public static final int WEATHER_CLOUDY = 12;
    /**
     * «Á
     */
    public static final int WEATHER_SUNNY = 13;
    /**
     * ¥Û—©
     */
    public static final int WEATHER_HEAVY_SNOW = 14;
    /**
     * ¥Û”Í
     */
    public static final int WEATHER_HEAVY_RAIN = 15;
    private static SparseIntArray sCodeTypeArray;

    private static SparseIntArray sCodeType2Overcast;
    public static final int[] CODE_TO_WEATHERINFO = {
            R.string.weather_0,
            R.string.weather_1,
            R.string.weather_2,
            R.string.weather_3,
            R.string.weather_4,
            R.string.weather_5,
            R.string.weather_6,
            R.string.weather_7,
            R.string.weather_8,
            R.string.weather_9,
            R.string.weather_10,
            R.string.weather_11,
            R.string.weather_12,
            R.string.weather_13,
            R.string.weather_14,
            R.string.weather_15,
            R.string.weather_16,
            R.string.weather_17,
            R.string.weather_18,
            R.string.weather_19,
            R.string.weather_20,
            R.string.weather_21,
            R.string.weather_22,
            R.string.weather_23,
            R.string.weather_24,
            R.string.weather_25,
            R.string.weather_26,
            R.string.weather_27,
            R.string.weather_28,
            R.string.weather_29,
            R.string.weather_30,
            R.string.weather_31,
            R.string.weather_32,
            R.string.weather_33,
            R.string.weather_34,
            R.string.weather_35,
            R.string.weather_36,
            R.string.weather_37,
            R.string.weather_38,
            R.string.weather_39,
            R.string.weather_40,
            R.string.weather_41,
            R.string.weather_42,
            R.string.weather_43,
            R.string.weather_44,
            R.string.weather_45,
            R.string.weather_46,
            R.string.weather_47,
    };

    static {
        sCodeTypeArray = new SparseIntArray();
        sCodeTypeArray.append(0, WEATHER_WIND);
        sCodeTypeArray.append(1, WEATHER_WIND);
        sCodeTypeArray.append(2, WEATHER_WIND);
        sCodeTypeArray.append(3, WEATHER_HEAVY_RAIN);
        sCodeTypeArray.append(4, WEATHER_THUNDERSTORMS);
        sCodeTypeArray.append(5, WEATHER_RAIN_AND_SNOW);
        sCodeTypeArray.append(6, WEATHER_RAIN_AND_SNOW);
        sCodeTypeArray.append(7, WEATHER_RAIN_AND_SNOW);
        sCodeTypeArray.append(8, WEATHER_FREEZING_RAIN);
        sCodeTypeArray.append(9, WEATHER_DRIZZLE);
        sCodeTypeArray.append(10, WEATHER_FREEZING_RAIN);
        sCodeTypeArray.append(11, WEATHER_SHOWERS);
        sCodeTypeArray.append(12, WEATHER_SHOWERS);
        sCodeTypeArray.append(13, WEATHER_SNOW);
        sCodeTypeArray.append(14, WEATHER_SNOW_SHOWERS);
        sCodeTypeArray.append(15, WEATHER_SNOW);
        sCodeTypeArray.append(16, WEATHER_SNOW);
        sCodeTypeArray.append(17, WEATHER_HAIL);
        sCodeTypeArray.append(18, WEATHER_RAIN_AND_SNOW);
        sCodeTypeArray.append(19, WEATHER_DUST);
        sCodeTypeArray.append(20, WEATHER_FOGGY);
        sCodeTypeArray.append(21, WEATHER_DUST);
        sCodeTypeArray.append(22, WEATHER_FOGGY);
        sCodeTypeArray.append(23, WEATHER_WIND);
        sCodeTypeArray.append(24, WEATHER_WIND);
        sCodeTypeArray.append(25, WEATHER_COLD);
        sCodeTypeArray.append(26, WEATHER_CLOUDY);
        sCodeTypeArray.append(27, WEATHER_CLOUDY);
        sCodeTypeArray.append(28, WEATHER_CLOUDY);
        sCodeTypeArray.append(29, WEATHER_CLOUDY);
        sCodeTypeArray.append(30, WEATHER_CLOUDY);
        sCodeTypeArray.append(31, WEATHER_SUNNY);
        sCodeTypeArray.append(32, WEATHER_SUNNY);
        sCodeTypeArray.append(33, WEATHER_SUNNY);
        sCodeTypeArray.append(34, WEATHER_SUNNY);
        sCodeTypeArray.append(35, WEATHER_HAIL);
        sCodeTypeArray.append(36, WEATHER_SUNNY);
        sCodeTypeArray.append(37, WEATHER_THUNDERSTORMS);
        sCodeTypeArray.append(38, WEATHER_THUNDERSTORMS);
        sCodeTypeArray.append(39, WEATHER_THUNDERSTORMS);
        sCodeTypeArray.append(40, WEATHER_SHOWERS);
        sCodeTypeArray.append(41, WEATHER_HEAVY_SNOW);
        sCodeTypeArray.append(42, WEATHER_SNOW_SHOWERS);
        sCodeTypeArray.append(43, WEATHER_HEAVY_SNOW);
        sCodeTypeArray.append(44, WEATHER_CLOUDY);
        sCodeTypeArray.append(45, WEATHER_THUNDERSTORMS);
        sCodeTypeArray.append(46, WEATHER_SNOW_SHOWERS);
        sCodeTypeArray.append(47, WEATHER_THUNDERSTORMS);


        sCodeType2Overcast = new SparseIntArray();
        sCodeType2Overcast.append(3, WEATHER_HEAVY_RAIN);
        sCodeType2Overcast.append(4, WEATHER_THUNDERSTORMS);
        sCodeType2Overcast.append(37, WEATHER_THUNDERSTORMS);
        sCodeType2Overcast.append(38, WEATHER_THUNDERSTORMS);
        sCodeType2Overcast.append(39, WEATHER_THUNDERSTORMS);
        sCodeType2Overcast.append(45, WEATHER_THUNDERSTORMS);
        sCodeType2Overcast.append(47, WEATHER_THUNDERSTORMS);
        sCodeType2Overcast.append(5, WEATHER_RAIN_AND_SNOW);
        sCodeType2Overcast.append(6, WEATHER_RAIN_AND_SNOW);
        sCodeType2Overcast.append(7, WEATHER_RAIN_AND_SNOW);
        sCodeType2Overcast.append(8, WEATHER_FREEZING_RAIN);
        sCodeType2Overcast.append(9, WEATHER_DRIZZLE);
        sCodeType2Overcast.append(10, WEATHER_FREEZING_RAIN);
        sCodeType2Overcast.append(9, WEATHER_DRIZZLE);
        sCodeType2Overcast.append(13, WEATHER_SNOW);
        sCodeType2Overcast.append(15, WEATHER_SNOW);
        sCodeType2Overcast.append(16, WEATHER_SNOW);
        sCodeType2Overcast.append(17, WEATHER_HAIL);
        sCodeType2Overcast.append(18, WEATHER_RAIN_AND_SNOW);
        sCodeType2Overcast.append(19, WEATHER_DUST);
        sCodeType2Overcast.append(21, WEATHER_DUST);
        sCodeType2Overcast.append(25, WEATHER_COLD);
        sCodeType2Overcast.append(35, WEATHER_HAIL);
        sCodeType2Overcast.append(41, WEATHER_HEAVY_SNOW);
        sCodeType2Overcast.append(43, WEATHER_HEAVY_SNOW);
    }

    public static int getWeather(int code) {
        return sCodeTypeArray.get(code);
    }

    public static int getWeatherBgResid(int code) {
        int type = getWeather(code);
        switch (type) {
            case WEATHER_CLOUDY:
            case WEATHER_COLD:
                return R.drawable.cloudy;

            case WEATHER_DUST:
                return R.drawable.dust;

            case WEATHER_FOGGY:
                return R.drawable.fog;

            case WEATHER_RAIN_AND_SNOW:
                return R.drawable.icyrain;


            case WEATHER_HEAVY_SNOW:
            case WEATHER_SNOW:
            case WEATHER_SNOW_SHOWERS:
                return R.drawable.snow;


            case WEATHER_SUNNY:
                return R.drawable.sunny;


            case WEATHER_WIND:
            case WEATHER_FREEZING_RAIN:
            case WEATHER_HAIL:
            case WEATHER_THUNDERSTORMS:
            case WEATHER_HEAVY_RAIN:
            case WEATHER_SHOWERS:
            case WEATHER_DRIZZLE:
                return R.drawable.rain;
            default:
                break;
        }

        return -1;
    }


    public static void setForecastIcon(ImageView view, int code) {
        int resId = getForecastIconResid(code);
        view.setImageResource(resId);
    }


    public static int getForecastIconResid(int code) {
        int type = getWeather(code);
        switch (type) {
            case WEATHER_CLOUDY:
                return R.drawable.weather_cloudy;
            case WEATHER_COLD:
                return R.drawable.weather_mostlycloudy;
            case WEATHER_DRIZZLE:
                return R.drawable.weather_rain;
            case WEATHER_DUST:
                return R.drawable.weather_dust;

            case WEATHER_FOGGY:
                return R.drawable.weather_fog;

            case WEATHER_FREEZING_RAIN:
                return R.drawable.weather_rain;

            case WEATHER_HAIL:
                return R.drawable.weather_snow;

            case WEATHER_HEAVY_SNOW:
                return R.drawable.weather_snow;

            case WEATHER_RAIN_AND_SNOW:
                return R.drawable.weather_icyrain;

            case WEATHER_SHOWERS:
                return R.drawable.weather_chancerain;

            case WEATHER_SNOW:
                return R.drawable.weather_snow;

            case WEATHER_SNOW_SHOWERS:
                return R.drawable.weather_chancerain;

            case WEATHER_SUNNY:
                return R.drawable.weather_sunny;

            case WEATHER_THUNDERSTORMS:
                return R.drawable.weather_chancestorm;

            case WEATHER_WIND:
                return R.drawable.weather_cloudy;

            case WEATHER_HEAVY_RAIN:
                return R.drawable.weather_rain;

            default:
                break;
        }
        return WEATHER_UNKNOW;
    }
}
