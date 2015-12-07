package com.wangbai.weather.util;

import com.wangbai.weather.db.WeatherTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by binwang on 2015/11/10.
 */
public class YaHooWeatherUtils {
    private static final int HTTP_TIMEOUT = 1000 * 55;
    private static final long EXPIRE_TIME = 2 * 60 * 60 * 1000;

    // Json
    public static final String JSON_TAG_QUERY = "query";
    public static final String JSON_TAG_COUNT = "count";
    public static final String JSON_TAG_RESULTS = "results";
    public static final String JSON_TAG_PLACE = "place";
    public static final String JSON_TAG_WOEID = "woeid";
    public static final String JSON_TAG_NAME = "name";
    public static final String JSON_TAG_COUNTRY = "country";
    public static final String JSON_TAG_CONTENT = "content";
    public static final String JSON_TAG_ADMIN = "admin";

    public static URL getURL(String urlsString) {
        URL url = null;
        try {
            url = new URL(urlsString);
        } catch (MalformedURLException e1) {
        }
        return url;
    }

    public static String getRequestWeatherInfoUrl(String woeid, String temperUnit) {
        return "http://weather.yahooapis.com/forecastrss?w="
                + woeid + "&u=" + temperUnit;
    }

    public static String getRequestSearchCitysUrl(String cityName) {
        String city_query_woeid = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places%20where%20text%3D%22"
                + URLEncoder.encode(cityName)
                + "*%22and%20placeTypeName%3D%22Town%22%20&format=json";
        return city_query_woeid;
    }

    public static String getRequestWoeidUrl(String latitude, String longitude) {
        String location_city = "https://api.flickr.com/services/rest/?method=flickr.places.findByLatLon&api_key=78252f8c459d49c53d9f6ae6263d18f8&lat="
                + latitude + "&lon=" + longitude + "&accuracy=11&format=rest";
        return location_city;
    }

    public static List<HashMap<String, String>> sendRequestAndParseResult(URL url, final String woeid) {

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(HTTP_TIMEOUT);
            conn.setReadTimeout(HTTP_TIMEOUT);
            InputStream inputStream = conn.getInputStream();
            String resultFromServer = inputStream2StringResult(inputStream);
            int code = conn.getResponseCode();

            return parseJsonCitys(resultFromServer);
        } catch (Exception e) {

        } finally {
            if(conn != null){
                conn.disconnect();
                conn = null;
            }
        }
        return null;
    }

    public static String sendRequestAndParseResultLocation(URL url, final String woeid) {

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(HTTP_TIMEOUT);
            conn.setReadTimeout(HTTP_TIMEOUT);
            InputStream inputStream = conn.getInputStream();
            String resultFromServer = inputStream2StringResult(inputStream);
            int code = conn.getResponseCode();
            WeatherTable info =  parseString(resultFromServer, 2, woeid);
            return info.cityWeid;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null){
                conn.disconnect();
                conn = null;
            }
        }
        return "";
    }

    public static boolean isNeedUpdateWeather(WeatherTable weatherTable){
        if(System.currentTimeMillis() - weatherTable.mLastUpdateTime > EXPIRE_TIME){
            return true;
        }

        return false;
    }

    public static WeatherTable sendRequestAndParseResultWeather(URL url, final String woeid) {

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(HTTP_TIMEOUT);
            conn.setReadTimeout(HTTP_TIMEOUT);
            InputStream inputStream = conn.getInputStream();
            String resultFromServer = inputStream2StringResult(inputStream);
            int code = conn.getResponseCode();
            WeatherTable info =  parseString(resultFromServer,1,woeid);
            info.mLastUpdateTime = System.currentTimeMillis();
            return info;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null){
                conn.disconnect();
                conn = null;
            }
        }
        return null;
    }

    private static WeatherTable parseString(String result, int requestCode,String woeid) {
        WeatherTable weatherInfo = null;
        SAXParserFactory webspf = SAXParserFactory.newInstance();
        XMLReader xmlReader = null;
        try {
            SAXParser webParser = webspf.newSAXParser();
            xmlReader = webParser.getXMLReader();
            YahooPaser mHandler = new YahooPaser(requestCode,woeid);
            xmlReader.setContentHandler(mHandler);
        } catch (ParserConfigurationException e1) {
        } catch (SAXException e1) {
        }
        try {
            StringReader stringReader = new StringReader(result);
            InputSource inputSource = new InputSource(stringReader);
            try {
                xmlReader.parse(inputSource);
                weatherInfo = ((YahooPaser) xmlReader.getContentHandler()).getWeatherTable();
            } catch (IOException e) {
            }
        } catch (SAXException e) {
        }
        return weatherInfo;
    }


    public static List<HashMap<String, String>> parseJsonCitys(String jsonData) {
        List<HashMap<String, String>> mSearchCitys = new ArrayList<HashMap<String, String>>();
        ;
        try {
            int count = new JSONObject(jsonData).optJSONObject(JSON_TAG_QUERY)
                    .getInt(JSON_TAG_COUNT);
            if (count == 0) {
                mSearchCitys.clear();
            } else if (count == 1) {
                JSONObject cityObject = new JSONObject(jsonData)
                        .optJSONObject(JSON_TAG_QUERY)
                        .optJSONObject(JSON_TAG_RESULTS)
                        .optJSONObject(JSON_TAG_PLACE);
                HashMap<String, String> cityHashMap = new HashMap<String, String>();
                cityHashMap.put(JSON_TAG_WOEID,
                        cityObject.optString(JSON_TAG_WOEID));
                cityHashMap.put(JSON_TAG_NAME,
                        cityObject.optString(JSON_TAG_NAME));
                cityHashMap.put(
                        JSON_TAG_COUNTRY,
                        cityObject.optJSONObject(JSON_TAG_COUNTRY).optString(
                                JSON_TAG_CONTENT));
                for (int j = 1; j <= 3; j++) {
                    if (!cityObject.optString(JSON_TAG_ADMIN + j).equals("null")) {
                        cityHashMap.put(JSON_TAG_ADMIN + j,
                                cityObject.optJSONObject(JSON_TAG_ADMIN + j)
                                        .optString(JSON_TAG_CONTENT));
                    }
                }

                mSearchCitys.add(cityHashMap);
            } else {
                JSONArray citysArray = new JSONObject(jsonData)
                        .optJSONObject(JSON_TAG_QUERY)
                        .optJSONObject(JSON_TAG_RESULTS)
                        .optJSONArray(JSON_TAG_PLACE);
                for (int i = 0, len = citysArray.length(); i < len; i++) {
                    JSONObject cityObject = citysArray.optJSONObject(i);
                    HashMap<String, String> cityHashMap = new HashMap<String, String>();
                    cityHashMap.put(JSON_TAG_WOEID,
                            cityObject.optString(JSON_TAG_WOEID));
                    cityHashMap.put(JSON_TAG_NAME,
                            cityObject.optString(JSON_TAG_NAME));
                    cityHashMap.put(
                            JSON_TAG_COUNTRY,
                            cityObject.optJSONObject(JSON_TAG_COUNTRY).optString(
                                    JSON_TAG_CONTENT));
                    for (int j = 1; j <= 3; j++) {
                        if (!cityObject.optString(JSON_TAG_ADMIN + j).equals("null")) {
                            cityHashMap.put(JSON_TAG_ADMIN + j,
                                    cityObject.optJSONObject(JSON_TAG_ADMIN + j)
                                            .optString(JSON_TAG_CONTENT));
                        }
                    }
                    mSearchCitys.add(cityHashMap);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mSearchCitys;
    }

    public static String inputStream2StringResult(InputStream inputstream) {
        String resultData = "";
        InputStreamReader isr = new InputStreamReader(inputstream);
        BufferedReader buffer = new BufferedReader(isr);
        String inputLine = "";
        try {
            while (((inputLine = buffer.readLine()) != null)) {
                resultData += inputLine + "\n";
            }
            if (resultData != "") {
                return resultData;
            }
        } catch (IOException e) {
        }
        return resultData;
    }

}
