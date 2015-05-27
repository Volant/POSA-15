package com.wisest_owl.weatherapp.jsonweather;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

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
            return parseAcronymServiceResults(reader);
        }
    }

    /**
     * Parse a Json stream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseAcronymServiceResults(JsonReader reader)
        throws IOException {

        reader.beginArray();
        try {
            // If the acronym wasn't expanded return null;
            if (reader.peek() == JsonToken.END_ARRAY)
                return null;

            // Create a JsonWeather object for each element in the
            // Json array.
            return parseAcronymMessage(reader);
        } finally {
            reader.endArray();
        }
    }

    public List<JsonWeather> parseAcronymMessage(JsonReader reader)
        throws IOException {

        List<JsonWeather> acronyms = null;
        reader.beginObject();

        try {
            outerloop:
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                case JsonWeather.sf_JSON:
                    // Log.d(TAG, "reading sf field");
                    reader.nextString();
                    break;
                case JsonWeather.lfs_JSON:
                    // Log.d(TAG, "reading lfs field");
                    if (reader.peek() == JsonToken.BEGIN_ARRAY)
                        acronyms = parseAcronymLongFormArray(reader);
                    break outerloop;
                default:
		    reader.skipValue();
                    // Log.d(TAG, "weird problem with " + name + " field");
                    break;
                }
            }
        } finally {
                reader.endObject();
        }
        return acronyms;
    }

    /**
     * Parse a Json stream and convert it into a List of JsonWeather
     * objects.
     */
    public List<JsonWeather> parseAcronymLongFormArray(JsonReader reader)
        throws IOException {

        // Log.d(TAG, "reading lfs elements");

        reader.beginArray();

        try {
            List<JsonWeather> acronyms = new ArrayList<JsonWeather>();

            while (reader.hasNext()) 
                acronyms.add(parseAcronym(reader));
            
            return acronyms;
        } finally {
            reader.endArray();
        }
    }

    /**
     * Parse a Json stream and return a JsonWeather object.
     */
    public JsonWeather parseAcronym(JsonReader reader)
        throws IOException {

        reader.beginObject();

        JsonWeather weather = new JsonWeather();
        try {
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                case JsonWeather.lf_JSON:
                    weather.setName(reader.nextString());
                    // Log.d(TAG, "reading lf " + acronym.getLongForm());
                    break;
                case JsonWeather.freq_JSON:
                    weather.setDeg(reader.nextInt());
                    // Log.d(TAG, "reading freq " + acronym.getFreq());
                    break;
                case JsonWeather.since_JSON:
                    weather.setTemp(reader.nextInt());
                    // Log.d(TAG, "reading since " + acronym.getSince());
                    break;
                default:
                    reader.skipValue();
                    // Log.d(TAG, "ignoring " + name);
                    break;
                }
            } 
        } finally {
                reader.endObject();
        }
        return weather;
    }
}
