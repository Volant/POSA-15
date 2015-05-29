package com.wisest_owl.weatherapp.jsonweather;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import static android.util.JsonToken.BEGIN_OBJECT;
import static android.util.JsonToken.END_OBJECT;

/**
 * Parses the Json acronym data returned from the Acronym Services API
 * and returns a List of JsonWeather objects that contain this data.
 */
public class WeatherJSONParser {
    /**
     * Used for logging purposes.
     */
    private final String TAG =
        this.getClass().getCanonicalName();

    private List<JsonWeather> weathers = new ArrayList<>();

    /**
     * Parse the @a inputStream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseJsonStream(InputStream inputStream)
        throws IOException {

        // Create a JsonReader for the inputStream.
        try (JsonReader reader =
             new JsonReader(new InputStreamReader(inputStream,
                                                  "UTF-8"))) {
            // Log.d(TAG, "Parsing the results returned as an array");

            // Handle the array returned from the Acronym Service.
            parseWeatherServiceResults(reader);
        }

        return weathers;
    }

    /**
     * Parse a Json stream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseWeatherServiceResults(JsonReader reader)
        throws IOException {

        reader.beginObject();
        try {
            // If the acronym wasn't expanded return null;
            if (reader.peek() == JsonToken.END_OBJECT)
                return null;

            // Create a JsonWeather object for each element in the
            // Json array.
            return parseWeatherMessage(reader);
        } finally {

        }
    }

    public List<JsonWeather> parseWeatherMessage(JsonReader reader) throws IOException {

        weathers.add(new JsonWeather());

        if (reader.peek() == JsonToken.BEGIN_OBJECT) reader.beginObject();

        while (reader.hasNext()) {
            if (reader.peek() == JsonToken.BEGIN_OBJECT)
                continue;

            String name = reader.nextName();
            switch (name) {
                case JsonWeather.weather_JSON:
                    parseWeather_weather(reader);
                    break;
                case JsonWeather.wind_JSON:
                    parseWeather_wind(reader);
                    break;
                case JsonWeather.main_JSON:
                    parseWeather_main(reader);
                    break;
                case JsonWeather.sys_JSON:
                    parseWeather_sys(reader);
                    break;
                default:
                    reader.skipValue();
            }
        }

        if (reader.peek() == JsonToken.END_OBJECT) reader.endObject();

        return weathers;
    }

    public Void parseWeather_weather(JsonReader reader) throws IOException {

        JsonWeather mWeather = weathers.get(0);

        reader.beginArray();

        if (reader.peek() == BEGIN_OBJECT) reader.beginObject();

        while (reader.peek() != END_OBJECT) {
            String name = reader.nextName();
            switch (name) {
                case JsonWeather.name_JSON:
                    mWeather.setName(reader.nextString());
                    break;
                default:
                    reader.skipValue();
            }
        }

        reader.endObject();
        reader.endArray();

        return null;

    }

    public Void parseWeather_wind(JsonReader reader) throws IOException {

        JsonWeather mWeather = weathers.get(0);

        if (reader.peek() == BEGIN_OBJECT) reader.beginObject();

        while (reader.peek() != END_OBJECT) {
            String name = reader.nextName();
            switch (name) {
                case JsonWeather.speed_JSON:
                    mWeather.setSpeed(reader.nextDouble());
                    break;
                case JsonWeather.deg_JSON:
                    mWeather.setDeg(reader.nextDouble());
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        return null;
    }

    public Void parseWeather_main(JsonReader reader) throws IOException {

        JsonWeather mWeather = weathers.get(0);

        if (reader.peek() == BEGIN_OBJECT) reader.beginObject();

        while (reader.peek() != END_OBJECT) {
            String name = reader.nextName();
            switch (name) {
                case JsonWeather.temp_JSON:
                    mWeather.setTemp(reader.nextDouble());
                    break;
                case JsonWeather.hum_JSON:
                    mWeather.setHumidity(reader.nextLong());
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        return null;
    }

    public Void parseWeather_sys(JsonReader reader) throws IOException {
        JsonWeather mWeather = weathers.get(0);

        if (reader.peek() == BEGIN_OBJECT) reader.beginObject();

        while (reader.peek() != END_OBJECT) {
            String name = reader.nextName();
            switch (name) {
                case JsonWeather.rise_JSON:
                    mWeather.setSunrise(reader.nextLong());
                    break;
                case JsonWeather.set_JSON:
                    mWeather.setSunset(reader.nextLong());
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        return null;
    }

}
