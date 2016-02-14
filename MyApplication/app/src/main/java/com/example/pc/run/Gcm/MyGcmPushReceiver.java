package com.example.pc.run.Gcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.pc.run.MainActivity;
import com.example.pc.run.Objects.Message;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.SharedPref.ApplicationSingleton;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

public class MyGcmPushReceiver extends GcmListenerService {


    //Gets the notification format
    //then sends it into the noticationUtils calls to display notification

    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();
    private NotificationUtils notificationUtils;


    //Class is used to trigger method whenever device receives push notification.
    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        String title = bundle.getString("title");
        String message = bundle.getString("message");
        String messageId = bundle.getString("message_id");
        String timestamp = bundle.getString("created_at");

        Log.e(TAG, "From: " + from);
        Log.e(TAG, "Title: " + title);
        Log.e(TAG, "message: " + message);
        Log.e(TAG, "timestamp: " + timestamp);

        //Checks if the user is logged in
        if (ApplicationSingleton.getInstance().getPrefManager().getProfile() != null) {
            if (!Boolean.valueOf(bundle.getString("is_background"))) {
                Message msg = new Message(from, message, messageId, timestamp);
                //Checks if the app is in the background
                if (NotificationUtils.isAppIsInBackground(getApplicationContext())) { //CHANGE THIS!!!!!!!!!
                    Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                    resultIntent.putExtra("message", message);
                    notificationUtils = new NotificationUtils(getApplicationContext());
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    notificationUtils.showNotificationMessage(title, message, timestamp, resultIntent);

                } else {
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", message);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                }
            }
        }
    }


}