package vandy.mooc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.lang.Runnable;
import java.lang.ref.WeakReference;

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
    
    public final static Integer DOWNLOAD_OK = -1;
    public final static Integer DOWNLOAD_FAIL = 1;
    
    private static dHandler handler;
    
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

    	handler = new dHandler(DownloadImageActivity.this);
    	Thread downloadThread = new Thread( new Runnable() {

			@Override
			public void run() {
				try {
			    	DownloadUtils DU = new DownloadUtils();
			    	
			    	Uri localUri = DU.downloadImage(getApplicationContext(), Uri.parse(URL));

                    Message msgObj = handler.obtainMessage();
                    Bundle bundle = new Bundle();
			    	if (null != localUri) {
	                    bundle.putString("message", localUri.toString());
	                    msgObj.setData(bundle);
	                    msgObj.what = DOWNLOAD_OK;
	                    handler.sendMessage(msgObj);
			    	} else {
			    		msgObj.what = DOWNLOAD_FAIL;
			    	}
				} catch (Throwable t) {
					Log.e(TAG, "Thread exception" + t);
				}
			}
    	});

        // @@ TO DO -- you fill in here using the Android "HaMeR"
        // concurrency framework.  Note that the finish() method
        // should be called in the UI thread, whereas the other
        // methods should be called in the background thread.  See
        // http://stackoverflow.com/questions/20412871/is-it-safe-to-finish-an-android-activity-from-a-background-thread
        // for more discussion about this topic.

    	downloadThread.start();
    }
}

class dHandler extends Handler{
    	
	private WeakReference<Activity> activityRef;
	
	public dHandler(Activity activity) {
		activityRef = new WeakReference<Activity>(activity); 
	}
	
	public void handleMessage(Message msg) {
		
		Activity activity = activityRef.get();
		
		if (null == activity) return;
		
		String response = msg.getData().getString("message");
		Integer status = msg.what;
		
		if (status == DownloadImageActivity.DOWNLOAD_OK) {
	    	Intent resultIntent = new Intent ();
	    	resultIntent.putExtra("localURI", response);
	    	activity.setResult(DownloadImageActivity.DOWNLOAD_OK, resultIntent);
		} else {
			activity.setResult(DownloadImageActivity.DOWNLOAD_FAIL);		
		}
		activity.finish();
	}
}
