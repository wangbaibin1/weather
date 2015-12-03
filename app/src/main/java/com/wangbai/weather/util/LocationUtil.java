package com.wangbai.weather.util;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.wangbai.weather.db.WeatherTable;

import java.net.URL;

/**
 * Created by binwang on 2015/11/10.
 */
public class LocationUtil {
    private LocationManager mLocationManager;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private static final int TEN_SECONDS = 10000;
    private static final int TEN_METERS = 10;
    private Context mContext;

    public interface LocationListenter {
        void weatherFinish(WeatherTable weatherTable);

        void fail();
    }

    public LocationUtil(Context context) {
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startLocationAndUpdateUI(LocationListenter locationListenter) {
        Location betterLocation = startLocation();
        if (betterLocation != null) {
            accwuqireWeather(betterLocation,locationListenter);
        } else{
            locationListenter.fail();
        }
    }

    private final LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private Location requestUpdatesFromProvider(final String provider) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            mLocationManager.requestLocationUpdates(provider, TEN_SECONDS, TEN_METERS, listener);
            location = mLocationManager.getLastKnownLocation(provider);
        }

        return location;
    }

    public Location startLocation() {
        Location gpsLocation = null;
        Location networkLocation = null;
        Location passiveLocation = null;

        gpsLocation = requestUpdatesFromProvider(LocationManager.GPS_PROVIDER);
        networkLocation = requestUpdatesFromProvider(LocationManager.NETWORK_PROVIDER);
        passiveLocation = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        // If both providers return last known locations, compare the two and use the better
        // one to update the UI.  If only one provider returns a location, use it.
        return getBetterLocation(gpsLocation, networkLocation, passiveLocation);
    }

    private Location getBetterLocation(Location gpsLocation, Location networkLocation, Location passiveLocation) {
        Location betterLocation = null;

        if (gpsLocation != null && networkLocation != null) {
            betterLocation = judgeBetterLocation(gpsLocation, networkLocation);
        } else if (gpsLocation != null) {
            betterLocation = gpsLocation;
        } else if (networkLocation != null) {
            betterLocation = networkLocation;
        } else if (passiveLocation != null) {
            betterLocation = passiveLocation;
        }

        return betterLocation;
    }

    protected Location judgeBetterLocation(Location newLocation, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return newLocation;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved.
        if (isSignificantlyNewer) {
            return newLocation;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return currentBestLocation;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return newLocation;
        } else if (isNewer && !isLessAccurate) {
            return newLocation;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return newLocation;
        }
        return currentBestLocation;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void accwuqireWeather(final Location location,final LocationListenter locationListenter) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = YaHooWeatherUtils.getURL(YaHooWeatherUtils.getRequestWoeidUrl(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
                if (url == null) {
                    locationListenter.fail();
                    return;
                }

                String cityCode = YaHooWeatherUtils.sendRequestAndParseResultLocation(url, "");
                if(TextUtils.isEmpty(cityCode)){
                    locationListenter.fail();
                    return;
                }

                URL weatherurl = YaHooWeatherUtils.getURL(YaHooWeatherUtils.getRequestWeatherInfoUrl(cityCode, ShareConfigManager.getInstance(mContext).getTempertureUnit()));
                WeatherTable info = YaHooWeatherUtils.sendRequestAndParseResultWeather(weatherurl, "");
                locationListenter.weatherFinish(info);
            }
        }).start();
    }

}
