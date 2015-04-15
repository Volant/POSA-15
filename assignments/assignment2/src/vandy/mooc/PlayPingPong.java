package vandy.mooc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * @class PlayPingPong
 *
 * @brief This class uses elements of the Android HaMeR framework to
 *        create two Threads that alternately print "Ping" and "Pong",
 *        respectively, on the display.
 */
public class PlayPingPong implements Runnable {
    /**
     * Keep track of whether a Thread is printing "ping" or "pong".
     */
    private enum PingPong {
        PING, PONG
    };

    private String TAG = "PingPong";

    private boolean mDone = false;

    /**
     * Number of iterations to run the ping-pong algorithm.
     */
    private final int mMaxIterations;

    /**
     * The strategy for outputting strings to the display.
     */
    private final OutputStrategy mOutputStrategy;

    /**
     * Define a pair of Handlers used to send/handle Messages via the
     * HandlerThreads.
     */
    // @@ TO DO - you fill in here.
    Handler mPingHandler = new Handler();
    Handler mPongHandler = new Handler();

    /**
     * Define a CyclicBarrier synchronizer that ensures the
     * HandlerThreads are fully initialized before the ping-pong
     * algorithm begins.
     */
    // @@ TO DO - you fill in here.
    protected CyclicBarrier mBarrier;

    /**
     * Implements the concurrent ping/pong algorithm using a pair of
     * Android Handlers (which are defined as an array field in the
     * enclosing PlayPingPong class so they can be shared by the ping
     * and pong objects).  The class (1) extends the HandlerThread
     * superclass to enable it to run in the background and (2)
     * implements the Handler.Callback interface so its
     * handleMessage() method can be dispatched without requiring
     * additional subclassing.
     */
    class PingPongThread extends HandlerThread implements Handler.Callback {
        /**
         * Keeps track of whether this Thread handles "pings" or
         * "pongs".
         */
        private PingPong mMyType;

        /**
         * Number of iterations completed thus far.
         */
        private int mIterationsCompleted;

        /**
         * Constructor initializes the superclass and type field
         * (which is either PING or PONG).
         */
        public PingPongThread(PingPong myType) {
        	super(myType.toString());
            // @@ TO DO - you fill in here.
        	
        	mMyType = myType;
        	
        }

        /**
         * This hook method is dispatched after the HandlerThread has
         * been started.  It performs ping-pong initialization prior
         * to the HandlerThread running its event loop.
         */
        @Override    
        protected void onLooperPrepared() {
            // Create the Handler that will service this type of
            // Handler, i.e., either PING or PONG.
            // @@ TO DO - you fill in here.
        	if (mMyType == PingPong.PING) {
        		mPingHandler = new Handler(this);
        	} else {
        		mPongHandler = new Handler(this);
        	}

            try {
                // Wait for both Threads to initialize their Handlers.
                // @@ TO DO - you fill in here.
            	
        		mBarrier.await();
            	
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Start the PING_THREAD first by (1) creating a Message
            // where the PING Handler is the "target" and the PONG
            // Handler is the "obj" to use for the reply and (2)
            // sending the Message to the PING_THREAD's Handler.
            // @@ TO DO - you fill in here.
            
            if (mMyType == PingPong.PONG) {
            	Message mMsgPingPong = Message.obtain();
            	mMsgPingPong.obj = mPongHandler;
            	mMsgPingPong.setTarget(mPingHandler);
            	mMsgPingPong.sendToTarget();
            }

        }

        /**
         * Hook method called back by HandlerThread to perform the
         * ping-pong protocol concurrently.
         */
        @Override
        public boolean handleMessage(Message reqMsg) {
            // Print the appropriate string if this thread isn't done
            // with all its iterations yet.
            // @@ TO DO - you fill in here, replacing "true" with the
            // appropriate code.
            if (mIterationsCompleted < mMaxIterations) {
            	mIterationsCompleted++;
            	if (mMyType == PingPong.PING) {
            		mOutputStrategy.print("PING" + "(" + mIterationsCompleted + ")\n");
            	}
            	if (mMyType == PingPong.PONG) {
            		mOutputStrategy.print("PONG" + "(" + mIterationsCompleted + ")\n");
            	}
            } else {
                // Shutdown the HandlerThread to the main PingPong
                // thread can join with it.
                // @@ TO DO - you fill in here.
            	mDone = true;
        		try {
        			synchronized (mBarrier) {
        				mBarrier.notifyAll();
        				
        			}
        			join();
        		} catch (InterruptedException e) {
        			e.printStackTrace();
        		}
            }

            // Create a Message that contains the Handler as the
            // reqMsg "target" and our Handler as the "obj" to use for
            // the reply.
            // @@ TO DO - you fill in here.
            Message mPingPongMsg = Message.obtain();
            mPingPongMsg.obj = reqMsg.getTarget();
            mPingPongMsg.setTarget((Handler)reqMsg.obj);
            mPingPongMsg.sendToTarget();
            
            // Return control to the Handler in the other
            // HandlerThread, which is the "target" of the msg
            // parameter.
            // @@ TO DO - you fill in here.

            return true;
        }
    }

    /**
     * Constructor initializes the data members.
     */
    public PlayPingPong(int maxIterations,
                        OutputStrategy outputStrategy) {
        // Number of iterations to perform pings and pongs.
        mMaxIterations = maxIterations;

        // Strategy that controls how output is displayed to the user.
        mOutputStrategy = outputStrategy;
    }

    /**
     * Start running the ping/pong code, which can be called from a
     * main() method in a Java class, an Android Activity, etc.
     */
    public void run() {
        // Let the user know we're starting. 
        mOutputStrategy.print("Ready...Set...Go!");
       
        // Create the ping and pong threads.
        // @@ TO DO - you fill in here.
        PingPongThread mPingHT = new PingPongThread(PingPong.PING);
        PingPongThread mPongHT = new PingPongThread(PingPong.PONG);

        mBarrier = new CyclicBarrier(2);
        
        // Start ping and pong threads, which cause their Looper to
        // loop.
        // @@ TO DO - you fill in here.
        mPingHT.start();
        mPongHT.start();
        
        // Barrier synchronization to wait for all work to be done
        // before exiting play().
        // @@ TO DO - you fill in here.

		try {
			mPingHT.join();
			mPongHT.join();
		} catch (InterruptedException ex) {
			mPingHT.interrupt();
			mPongHT.interrupt();
			return;
    	}

        // Let the user know we're done.
        mOutputStrategy.print("Done!");
    }
}
