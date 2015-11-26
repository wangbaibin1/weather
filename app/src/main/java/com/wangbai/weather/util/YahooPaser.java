package com.wangbai.weather.util;

import com.wangbai.weather.db.ForeCastTable;
import com.wangbai.weather.db.WeatherTable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by binwang on 2015/11/11.
 */
public class YahooPaser extends DefaultHandler {

    private int mRequestCode;
    private static final String TAG_LOCATION="location";
    private static final String UNIT_TAG = "units";
    private static final String PUB_DATE = "pubDate";
    private static final String CONDITION = "condition";
    private static final String FORECAST = "forecast";
    private static final String PLACE ="place";

    private static final String ATTR_CITY="city";
    private static final String ATTR_COUNTRY="country";
    private static final String ATTR_TEMPER_UNIT = "temperature";
    private static final String ATTR_CODE = "code";
    private static final String ATTR_TEMPER = "temp";
    private static final String ATTR_DAY = "day";
    private static final String ATTR_DATE = "date";
    private static final String ATTR_LOW = "low";
    private static final String ATTR_HIGH = "high";
    private static final String ATTR_WOEID="woeid";

    private String mWoeid;
    private WeatherTable mWeatherTable;
    protected StringBuffer sb = new StringBuffer();
    public YahooPaser(int code,String woeid) {
        mRequestCode = code;
        mWeatherTable = new WeatherTable();
        mWeatherTable.cityWeid = woeid;
        mWoeid = woeid;
    }

    public WeatherTable getWeatherTable() {
        return mWeatherTable;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        sb.append(ch, start, length);
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(localName.equals(PUB_DATE)) {
            mWeatherTable.pubDate = sb.toString();
        }
        sb.setLength(0);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (mRequestCode == 1) {
            if(localName.equals(TAG_LOCATION)) {
                mWeatherTable.cityName = attributes.getValue(ATTR_CITY);
                mWeatherTable.countryName = attributes.getValue(ATTR_COUNTRY);
            }
            if (localName.equals(UNIT_TAG)) {
                mWeatherTable.temperUnit = attributes.getValue(ATTR_TEMPER_UNIT);
            }

            if (localName.equals(CONDITION)) {
                mWeatherTable.temperature = attributes.getValue(ATTR_TEMPER);
                mWeatherTable.code = Integer.parseInt(attributes.getValue(ATTR_CODE));
            }

            if(localName.equals(FORECAST)) {
                ForeCastTable forcast = new ForeCastTable();
                forcast.weid = mWoeid;
                forcast.day = attributes.getValue(ATTR_DAY);
                forcast.date = attributes.getValue(ATTR_DATE);
                forcast.low = attributes.getValue(ATTR_LOW);
                forcast.high = attributes.getValue(ATTR_HIGH);
                forcast.code = Integer.parseInt(attributes.getValue(ATTR_CODE));
                if(mWeatherTable.getForecastSize() == 0) {
                    mWeatherTable.maxTemper = forcast.high;
                    mWeatherTable.minTemper = forcast.low;
                }
                mWeatherTable.addForecast(forcast);
            }

        } else if (mRequestCode == 2) {
            if(localName.equals(PLACE)) {
                String woeid = attributes.getValue(ATTR_WOEID);
                mWeatherTable.cityWeid = woeid;
            }
        }
    }
}
