package com.example.pc.run.SharedPref;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.tv.TvContract;
import android.util.Log;

import com.example.pc.run.Objects.Profile;

public class SharedPrefManager {

    public static final String TAG = "In SharedPref";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "Run";

    // All Shared Preferences Keys
    private static final Profile KEY_PROFILE = new Profile();
    private static final String KEY_PROFILE_Name = "profile_name";
    private static final String KEY_PROFILE_languagesKnown = "profile_languagesKnown";
    private static final String KEY_PROFILE_languagesLearning = "profile_languagesLearning";
    private static final String KEY_PROFILE_interests = "profile_interests";
    private static final String KEY_email = "email";
    private static final String KEY_password = "password";
    private static final String KEY_NOTIFICATIONS = "notifications";

    public SharedPrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    //Returns profile from shared pref
    public Profile getProfile(){
        String name, languagesKnown, languagesLearning, interests;
        name = pref.getString(KEY_PROFILE_Name, null);
        languagesKnown = pref.getString(KEY_PROFILE_languagesKnown, null);
        languagesLearning = pref.getString(KEY_PROFILE_languagesLearning, null);
        interests = pref.getString(KEY_PROFILE_interests, null);
        Profile temp = new Profile();
        return temp;
    }

    //Store the profile object
    public void storeProfile(Profile profile){
        editor.putString(KEY_PROFILE_Name, profile.getName());
        editor.putString(KEY_PROFILE_languagesKnown, profile.getLanguagesKnown());
        editor.putString(KEY_PROFILE_languagesLearning, profile.getLanguagesLearning());
        editor.putString(KEY_PROFILE_interests, profile.getInterests());
        editor.commit();
        Log.e(TAG, "User is stored in shared preferences. " + profile.getName());
    }

    //Store the password and email of user
    public void storeAuthentication(String email, String pass){
        editor.putString(KEY_email, email);
        editor.putString(KEY_password, pass);
        editor.commit();
    }

    //Returns an array of password and email
    public String[] getAuthentication(){
        String[] auth = {pref.getString(KEY_email, null), pref.getString(KEY_password, null)};
        return auth;
    }

}
