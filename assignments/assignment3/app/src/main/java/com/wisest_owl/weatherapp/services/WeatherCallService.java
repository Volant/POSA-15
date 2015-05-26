package com.wisest_owl.weatherapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.wisest_owl.weatherapp.aidl.WeatherCall;
import com.wisest_owl.weatherapp.aidl.WeatherData;

import java.util.List;

public class WeatherCallService extends Service {
    public WeatherCallService () {
    }

    @Override
    public IBinder onBind (Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public IBinder getBinder () {
        return mBinder;
    }

    private final WeatherCall.Stub mBinder = new WeatherCall.Stub() {

        @Override
        public List<WeatherData> getCurrentWeather (String Weather) throws RemoteException {
            return null;
        }
    }
}
