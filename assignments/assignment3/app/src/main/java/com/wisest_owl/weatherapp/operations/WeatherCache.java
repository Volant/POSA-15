package com.wisest_owl.weatherapp.operations;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherCache extends SQLiteOpenHelper {


    public WeatherCache(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table weather_last ("
                + "id integer primary key autoincrement,"
                + "city varchar(200), "
                + "name varchar(200), "
                + "speed float, "
                + "deg float, "
                + "temp float, "
                + "humidity integer, "
                + "sunrise bigint, "
                + "sunset bigint,"
                + "time_changed bigint"
                + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
