package com.wisest_owl.weatherapp.jsonweather;

import java.util.ArrayList;
import java.util.List;

/**
 * This "Plain Ol' Java Object" (POJO) class represents data of
 * interest downloaded in Json from the Acronym Service.  We don't
 * care about all the data, just the fields defined in this class.
 */
public class JsonWeather {
    /**
     * Various tags corresponding to data downloaded in Json from the
     * Weather Service.
     */
    final public static String weather_JSON = "weather";
        final public static String name_JSON = "main";

    final public static String wind_JSON = "wind";
        final public static String speed_JSON = "speed";
        final public static String deg_JSON = "deg";

    final public static String main_JSON = "main";
        final public static String temp_JSON = "temp";
        final public static String hum_JSON = "humidity";

    final public static String sys_JSON = "sys";
        final public static String rise_JSON = "sunrise";
        final public static String set_JSON = "sunset";



    /**
     * Various fields corresponding to data downloaded in Json from
     * the Weather Service.
     */
    private String mName;
    private double mSpeed;
    private double mDeg;
    private double mTemp;
    private long mHumidity;
    private long mSunrise;
    private long mSunset;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getSpeed () {
        return mSpeed;
    }

    public void setSpeed (double mSpeed) {
        this.mSpeed = mSpeed;
    }

    public double getDeg () {
        return mDeg;
    }

    public void setDeg (double mDeg) {
        this.mDeg = mDeg;
    }

    public double getTemp () {
        return mTemp;
    }

    public void setTemp (double mTemp) {
        this.mTemp = mTemp;
    }

    public long getHumidity () {
        return mHumidity;
    }

    public void setHumidity (long mHumidity) {
        this.mHumidity = mHumidity;
    }

    public long getSunrise () {
        return mSunrise;
    }

    public void setSunrise (long mSunrise) {
        this.mSunrise = mSunrise;
    }

    public long getSunset () {
        return mSunset;
    }

    public void setSunset (long mSunset) {
        this.mSunset = mSunset;
    }

    /**
     * Constructor that initializes all the fields of interest.
     */


    public JsonWeather(String name,
                       double speed,
                       double deg,
                       double temp,
                       long humidity,
                       long sunrise,
                       long sunset
    ) {
        mName = name;
        mSpeed = speed;
        mDeg = deg;
        mTemp = temp;
        mHumidity = humidity;
        mSunrise = sunrise;
        mSunset = sunset;
    }

    /**
     * No-op constructor.
     */
    public JsonWeather() {
    }

}
