package com.wisest_owl.weatherapp.activities;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wisest_owl.weatherapp.R;
import com.wisest_owl.weatherapp.operations.WeatherCache;
import com.wisest_owl.weatherapp.operations.WeatherOps;
import com.wisest_owl.weatherapp.operations.WeatherOpsImpl;
import com.wisest_owl.weatherapp.utils.Utils;


public class MainActivity extends LifecycleLoggingActivity {

    private WeatherOps mWeatherOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the WeatherOps object one time.
        mWeatherOps = new WeatherOpsImpl(this);

        // Initiate the service binding protocol.
        mWeatherOps.bindService();
    }

    @Override
    protected void onDestroy() {
        // Unbind from the Service.
        mWeatherOps.unbindService();

        // Always call super class for necessary operations when an
        // Activity is destroyed.
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getWeatherSync(View v) {
        mWeatherOps.getWeatherSync(v);
    }

    public void getWeatherAsync(View v) {
        mWeatherOps.getWeatherAsync(v);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mWeatherOps.onConfigurationChanged(newConfig);
    }

}
