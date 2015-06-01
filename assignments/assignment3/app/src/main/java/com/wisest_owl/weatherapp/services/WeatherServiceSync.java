package com.wisest_owl.weatherapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.wisest_owl.weatherapp.aidl.WeatherCall;
import com.wisest_owl.weatherapp.aidl.WeatherData;
import com.wisest_owl.weatherapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class WeatherServiceSync extends LifecycleLoggingService {

    @Override
    public IBinder onBind(Intent intent) {
        return mWeatherCallImpl;
    }

    private final WeatherCall.Stub mWeatherCallImpl = new WeatherCall.Stub() {

        @Override
        public List<WeatherData> getCurrentWeather(String weather)
                throws RemoteException {

            // Call the Weather Web service to get the weather data
            // of the designated country/city.
            List<WeatherData> weatherResults =
                    Utils.getResults(weather);

            if (weatherResults != null) {
                Log.d(TAG, "" + weatherResults.size() + " results for weather: " + weather);

                return weatherResults;
            } else {
                weatherResults = new ArrayList<WeatherData>();
                return weatherResults;
            }
        }
    };

    public static Intent makeIntent(Context context) {
        return new Intent(context,
                WeatherServiceSync.class);
    }
}
