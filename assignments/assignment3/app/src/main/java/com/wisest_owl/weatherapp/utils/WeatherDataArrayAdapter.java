package com.wisest_owl.weatherapp.utils;

import java.util.List;

import com.wisest_owl.weatherapp.R;
import com.wisest_owl.weatherapp.aidl.WeatherData;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Custom ArrayAdapter for the WeatherData class, which makes each row
 * of the ListView have a more complex layout than just a single
 * textview (which is the default for ListViews).
 */
public class WeatherDataArrayAdapter extends ArrayAdapter<WeatherData> {

    private final String TAG =
            this.getClass().getCanonicalName();

    /**
     * Construtor that declares which layout file is used as the
     * layout for each row.
     */
    public WeatherDataArrayAdapter(Context context) {
        super(context, R.layout.activity_main);
    }

    /**
     * Construtor that declares which layout file is used as the
     * layout for each row.
     */
    public WeatherDataArrayAdapter(Context context,
                                   List<WeatherData> objects) {
        super(context, R.layout.activity_main, objects);
    }

    /**
     * Method used by the ListView to "get" the "view" for each row of
     * data in the ListView.
     *
     * @param position
     *            The position of the item within the adapter's data set of the
     *            item whose view we want. convertView The old view to reuse, if
     *            possible. Note: You should check that this view is non-null
     *            and of an appropriate type before using. If it is not possible
     *            to convert this view to display the correct data, this method
     *            can create a new view. Heterogeneous lists can specify their
     *            number of view types, so that this View is always of the right
     *            type (see getViewTypeCount() and getItemViewType(int)).
     * @param parent
     *            The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position,
                        View convertView,
                        ViewGroup parent) {
        WeatherData data = getItem(position);

        Log.d(TAG, "It means we print data: " + data.getName());


        /*
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.Weather_data_row,
                    parent,
                    false);
        }



        TextView resultTV =
                (TextView) convertView.findViewById(R.id.name);
        TextView dbRefsTV =
                (TextView) convertView.findViewById(R.id.db_refs);
        TextView yearAddedTV =
                (TextView) convertView.findViewById(R.id.year_added_to_db);

        resultTV.setText(data.mLongForm);
        dbRefsTV.setText("" + data.mFreq);
        yearAddedTV.setText("" + data.mSince);
        */
        return convertView;
    }
}
