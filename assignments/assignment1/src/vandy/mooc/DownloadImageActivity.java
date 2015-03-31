package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * An Activity that downloads an image, stores it in a local file on
 * the local device, and returns a Uri to the image file.
 */
public class DownloadImageActivity extends Activity {
    /**
     * Debugging tag used by the Android logger.
     */
    private final String TAG = getClass().getSimpleName();

    private String URL;
    
    /**
     * Hook method called when a new instance of Activity is created.
     * One time initialization code goes here, e.g., UI layout and
     * some class scope variable initialization.
     *
     * @param savedInstanceState object that contains saved state information.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        // @@ TO DO -- you fill in here.
    	super.onCreate(savedInstanceState);

        // Get the URL associated with the Intent data.
        // @@ TO DO -- you fill in here.
    	Intent toDownload = getIntent();
    	URL = toDownload.getDataString();
    	
    	Log.e(TAG, String.format("URL: %s", URL));
    	
    	if (null == URL) {
    		setResult(RESULT_CANCELED);
    		finish();
    		return;
    	}
    	
        // Download the image in the background, create an Intent that
        // contains the path to the image file, and set this as the
        // result of the Activity.

    	DownloadUtils DU = new DownloadUtils();
    	
    	Uri localUri = DU.downloadImage(getApplicationContext(), Uri.parse(URL));
    	
    	Intent resultIntent = new Intent ();
    	resultIntent.putExtra("localURI", localUri.toString());
    	setResult(RESULT_OK, resultIntent);
    	
    	finish();
    	
        // @@ TODO -- you fill in here using the Android "HaMeR"
        // concurrency framework.  Note that the finish() method
        // should be called in the UI thread, whereas the other
        // methods should be called in the background thread.  See
        // http://stackoverflow.com/questions/20412871/is-it-safe-to-finish-an-android-activity-from-a-background-thread
        // for more discussion about this topic.
    }
}
