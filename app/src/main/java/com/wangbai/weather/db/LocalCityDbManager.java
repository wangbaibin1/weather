package com.wangbai.weather.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.wangbai.weather.R;
import com.wangbai.weather.ui.MainApplication;
import com.wangbai.weather.util.ShareConfigManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by binwang on 2015/11/24.
 */
public class LocalCityDbManager {
    public static final String WORLD_CITY_TABLE_NAME = "world_city_table";
    private static String databasepath = "/data/data/%s/database";
    private static final String LOCAL_CITY_NAME = "city_database.db";

    public static void writeToDatabase(Context context) {
        if (ShareConfigManager.getInstance(context).isCopyAssets()) {
            return;
        }

        File pathFile = new File(getDatabaseFilepath(context));
        File file = new File(getDatabaseFile(context, LOCAL_CITY_NAME));
        if (!file.exists()) {
            pathFile.mkdirs();
        }

        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open(LOCAL_CITY_NAME);
            String writePath = getDatabaseFile(context, LOCAL_CITY_NAME);

            FileOutputStream fos = new FileOutputStream(writePath);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.flush();
            fos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ShareConfigManager.getInstance(context).setCopyAssets(true);
    }

    private static String getDatabaseFilepath(Context context) {
        return String.format(databasepath, context.getApplicationInfo().packageName);
    }

    private static String getDatabaseFile(Context context, String dbfile) {
        return getDatabaseFilepath(context) + "/" + dbfile;
    }


    public static SQLiteDatabase openDatabase(Context context) {
        return SQLiteDatabase.openDatabase(getDatabaseFile(context, LOCAL_CITY_NAME), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);

    }

    private static void addLocationTable(List<WeatherTable> cityInfoList){
        WeatherTable table = new WeatherTable();
        table.cityName = MainApplication.getContext().getString(R.string.location);
        table.cityWeid = WeatherTable.LOCATION_WOID;

        cityInfoList.add(table);
    }

    public static List<WeatherTable> queryWorldCityTableByCityName(Context context, String cityName) {
        Cursor cursor = null;
        List<WeatherTable> cityInfoList = new ArrayList();
        addLocationTable(cityInfoList);

        SQLiteDatabase mSQLiteDatabase = openDatabase(context);
        try {
            if (TextUtils.isEmpty(cityName)) {
                cursor = mSQLiteDatabase.rawQuery("select * from " + WORLD_CITY_TABLE_NAME, null);
            } else {
                cursor = mSQLiteDatabase.rawQuery("select _id, city_name, yahoo_wid from " + WORLD_CITY_TABLE_NAME +
                        " where city_name = ?", new String[]{cityName});
            }

            if (cursor == null) {
                return null;
            }

            WeatherTable cityInfo = null;
            while (cursor.moveToNext()) {
                cityInfo = new WeatherTable();
//                cityInfo._id = cursor.getString(0);
                cityInfo.cityName = cursor.getString(1);
                cityInfo.cityWeid = cursor.getString(2);
                cityInfoList.add(cityInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return cityInfoList;
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return cityInfoList;
    }
}
