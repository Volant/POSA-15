package com.wisest_owl.weatherapp.operations;

import java.lang.ref.WeakReference;
import java.util.List;

import com.wisest_owl.weatherapp.R;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.wisest_owl.weatherapp.activities.MainActivity;

import com.wisest_owl.weatherapp.aidl.WeatherData;
import com.wisest_owl.weatherapp.aidl.WeatherCall;
import com.wisest_owl.weatherapp.aidl.WeatherRequest;

import com.wisest_owl.weatherapp.aidl.WeatherResults;
import com.wisest_owl.weatherapp.services.WeatherServiceSync;

import com.wisest_owl.weatherapp.utils.Utils;
import com.wisest_owl.weatherapp.utils.WeatherDataArrayAdapter;
import com.wisest_owl.weatherapp.utils.GenericServiceConnection;

/**
 * This class implements all the Weather-related operations defined in
 * the WeatherOps interface.
 */
public class WeatherOpsImpl implements WeatherOps {
    /**
     * Debugging tag used by the Android logger.
     */
    protected final String TAG = getClass().getSimpleName();

    /**
     * Used to enable garbage collection.
     */
    protected WeakReference<MainActivity> mActivity;

    /**
     * The ListView that will display the results to the user.
     */
    protected WeakReference<ListView> mListView;

    /**
     * Weather entered by the user.
     */
    protected WeakReference<EditText> mEditText;

    /**
     * List of results to display (if any).
     */
    protected List<WeatherData> mResults;

    /**
     * A custom ArrayAdapter used to display the list of WeatherData
     * objects.
     */
    protected WeakReference<WeatherDataArrayAdapter> mAdapter;

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the WeatherServiceSync Service using bindService().
     */
    private GenericServiceConnection<WeatherCall> mServiceConnectionSync;

    /**
     * This GenericServiceConnection is used to receive results after
     * binding to the WeatherServiceAsync Service using bindService().
     */
    private GenericServiceConnection<WeatherRequest> mServiceConnectionAsync;

    /**
     * Constructor initializes the fields.
     */
    public WeatherOpsImpl(MainActivity activity) {
        // Initialize the WeakReference.
        mActivity = new WeakReference<>(activity);

        // Finish the initialization steps.
        initializeViewFields();
        initializeNonViewFields();
    }

    /**
     * Initialize the View fields, which are all stored as
     * WeakReferences to enable garbage collection.
     */
    private void initializeViewFields() {
        // Get references to the UI components.
        mActivity.get().setContentView(R.layout.activity_main);

        // Store the EditText that holds the urls entered by the user
        // (if any).
        mEditText = new WeakReference<>
                ((EditText) mActivity.get().findViewById(R.id.editText));

        // Store the ListView for displaying the results entered.
        mListView = new WeakReference<>
                ((ListView) mActivity.get().findViewById(R.id.listView));

        // Create a local instance of our custom Adapter for our
        // ListView.
        mAdapter = new WeakReference<>
                (new WeatherDataArrayAdapter(mActivity.get()));

        // Set the adapter to the ListView.
        mListView.get().setAdapter(mAdapter.get());
    }

    /**
     * (Re)initialize the non-view fields (e.g.,
     * GenericServiceConnection objects).
     */
    private void initializeNonViewFields() {
        mServiceConnectionSync =
                new GenericServiceConnection<WeatherCall>(WeatherCall.class);

        mServiceConnectionAsync =
                new GenericServiceConnection<WeatherRequest>(WeatherRequest.class);

        // Display results if any (due to runtime configuration change).
        if (mResults != null)
            displayResults(mResults);
    }

    /**
     * Called after a runtime configuration change occurs.
     */
    public void onConfigurationChanged(Configuration newConfig) {
        // Checks the orientation of the screen.
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            Log.d(TAG, "Now running in landscape mode");
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
            Log.d(TAG,
                    "Now running in portrait mode");
    }

    /**
     * Initiate the service binding protocol.
     */
    @Override
    public void bindService() {
        Log.d(TAG, "calling bindService()");

        // Launch the Weather Bound Services if they aren't already
        // running via a call to bindService(), which binds this
        // activity to the WeatherService* if they aren't already
        // bound.
        if (mServiceConnectionSync.getInterface() == null)
            mActivity.get().bindService
                    (WeatherServiceSync.makeIntent(mActivity.get()),
                            mServiceConnectionSync,
                            Context.BIND_AUTO_CREATE);
//
//        if (mServiceConnectionAsync.getInterface() == null)
//            mActivity.get().bindService
//                    (WeatherServiceAsync.makeIntent(mActivity.get()),
//                            mServiceConnectionAsync,
//                            Context.BIND_AUTO_CREATE);
    }

    /**
     * Initiate the service unbinding protocol.
     */
    @Override
    public void unbindService() {
        Log.d(TAG, "calling unbindService()");

        // Unbind the Async Service if it is connected.
        if (mServiceConnectionAsync.getInterface() != null)
            mActivity.get().unbindService
                    (mServiceConnectionAsync);

        // Unbind the Sync Service if it is connected.
        if (mServiceConnectionSync.getInterface() != null)
            mActivity.get().unbindService
                    (mServiceConnectionSync);
    }

    /*
     * Initiate the asynchronous Weather lookup when the user presses
     * the "Look Up Async" button.
     */
    public void getWeatherAsync(View v) {
        WeatherRequest WeatherRequest =
                mServiceConnectionAsync.getInterface();

        if (WeatherRequest != null) {
            // Get the Weather entered by the user.
            final String Weather =
                    mEditText.get().getText().toString();

            resetDisplay();

            try {
                // Invoke a one-way AIDL call, which does not block
                // the client.  The results are returned via the
                // sendResults() method of the mWeatherResults
                // callback object, which runs in a Thread from the
                // Thread pool managed by the Binder framework.
                WeatherRequest.getCurrentWeather(Weather, mWeatherResults);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException:" + e.getMessage());
            }
        } else {
            Log.d(TAG, "WeatherRequest was null.");
        }
    }

    /*
     * Initiate the synchronous Weather lookup when the user presses
     * the "Look Up Sync" button.
     */
    public void getWeatherSync(View v) {
        final WeatherCall WeatherCall =
                mServiceConnectionSync.getInterface();

        if (WeatherCall != null) {
            // Get the Weather entered by the user.
            final String Weather =
                    mEditText.get().getText().toString();

            resetDisplay();

            // Use an anonymous AsyncTask to download the Weather data
            // in a separate thread and then display any results in
            // the UI thread.
            new AsyncTask<String, Void, List<WeatherData>> () {
                /**
                 * Weather we're trying to expand.
                 */
                private String mWeather;

                /**
                 * Retrieve the expanded Weather results via a
                 * synchronous two-way method call, which runs in a
                 * background thread to avoid blocking the UI thread.
                 */
                protected List<WeatherData> doInBackground(String... Weathers) {
                    try {
                        mWeather = Weathers[0];
                        return WeatherCall.getCurrentWeather(mWeather);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                /**
                 * Display the results in the UI Thread.
                 */
                protected void onPostExecute(List<WeatherData> WeatherDataList) {
                    if (WeatherDataList.size() > 0) {
                        Log.d(TAG, "Showing results for weather");
//                        TextView textView = (TextView) mActivity.get().findViewById(R.id.textView);
//                        textView.setText(WeatherDataList.get(0).getName());
                        displayResults(WeatherDataList);
                    } else
                        Utils.showToast(mActivity.get(), "no expansions for " + mWeather + " found");
                }
                // Execute the AsyncTask to expand the Weather without
                // blocking the caller.
            }.execute(Weather);
        } else {
            Log.d(TAG, "mWeatherCall was null.");
        }
    }

    /**
     * The implementation of the WeatherResults AIDL Interface, which
     * will be passed to the Weather Web service using the
     * WeatherRequest.expandWeather() method.
     *
     * This implementation of WeatherResults.Stub plays the role of
     * Invoker in the Broker Pattern since it dispatches the upcall to
     * sendResults().
     */
    private WeatherResults.Stub mWeatherResults = new WeatherResults.Stub() {
        /**
         * This method is invoked by the WeatherServiceAsync to
         * return the results back to the WeatherActivity.
         */
        @Override
        public void sendResults(final List<WeatherData> WeatherDataList)
                throws RemoteException {
            // Since the Android Binder framework dispatches this
            // method in a background Thread we need to explicitly
            // post a runnable containing the results to the UI
            // Thread, where it's displayed.
            mActivity.get().runOnUiThread(new Runnable() {
                public void run() {
                    displayResults(WeatherDataList);
                }
            });
        }

        /**
         * This method is invoked by the WeatherServiceAsync to
         * return error results back to the WeatherActivity.
         */
        //@Override
        public void sendError(final String reason)
                throws RemoteException {
            // Since the Android Binder framework dispatches this
            // method in a background Thread we need to explicitly
            // post a runnable containing the results to the UI
            // Thread, where it's displayed.
            mActivity.get().runOnUiThread(new Runnable() {
                public void run() {
                    Utils.showToast(mActivity.get(),
                            reason);
                }
            });
        }
    };

    /**
     * Display the results to the screen.
     *
     * @param results
     *            List of Results to be displayed.
     */
    private void displayResults(List<WeatherData> results) {
        mResults = results;

        // Set/change data set.
        mAdapter.get().clear();
        mAdapter.get().addAll(mResults);
        mAdapter.get().notifyDataSetChanged();
    }

    /**
     * Reset the display prior to attempting to expand a new Weather.
     */
    private void resetDisplay() {
        Utils.hideKeyboard(mActivity.get(),
                mEditText.get().getWindowToken());

        mResults = null;
        mAdapter.get().clear();
        mAdapter.get().notifyDataSetChanged();
    }
}
