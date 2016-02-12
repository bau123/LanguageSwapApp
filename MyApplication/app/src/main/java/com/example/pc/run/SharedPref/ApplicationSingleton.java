package com.example.pc.run.SharedPref;

import android.app.Application;

public class ApplicationSingleton extends Application {

    private static ApplicationSingleton mInstance;

    private SharedPrefManager pref;

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

}
