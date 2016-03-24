package com.example.pc.run.SharedPref;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApplicationSingleton extends Application {

    public static final String TAG = "In ApplicationSingleton";

    private static ApplicationSingleton mInstance;
    private RequestQueue mRequestQueue;
    private SharedPrefManager pref;

    /*
        Sets instance state
     */
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized ApplicationSingleton getInstance() {
        return mInstance;
    }

    public SharedPrefManager getPrefManager() {
        if (pref == null) {
            pref = new SharedPrefManager(this);
        }

        return pref;
    }

    /*
        Returns requestqueue for manip
     */

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /*
      Adds request to request queue with tag
   */

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /*
      Adds request to request queure
   */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /*
        Cancels a pending request
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
