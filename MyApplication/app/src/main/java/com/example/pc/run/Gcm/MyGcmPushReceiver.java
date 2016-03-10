package com.example.pc.run.Gcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.pc.run.App_act;
import com.example.pc.run.Chat.ChatRoomActivity;
import com.example.pc.run.MainActivity;
import com.example.pc.run.Objects.Message;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.SharedPref.ApplicationSingleton;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyGcmPushReceiver extends GcmListenerService {


    //Gets the notification format
    //then sends it into the noticationUtils calls to display notification

    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();
    private NotificationUtils notificationUtils;

    @Override
    // Class is triggered whenever a push notification is sent
    public void onMessageReceived(String from, Bundle bundle) {
        String title = bundle.getString("title");
        Boolean isBackground = Boolean.valueOf(bundle.getString("is_background"));
        String flag = bundle.getString("flag");
        String data = bundle.getString("data");

        System.out.println(data);
        System.out.println(title);

        if (flag == null)
            return;

        Log.d(TAG, "title: " + title);
        Log.d(TAG, "isBackground: " + isBackground);
        Log.d(TAG, "flag: " + flag);
        Log.d(TAG, "data: " + data);

        if(ApplicationSingleton.getInstance().getPrefManager().getProfile() == null){
            // user is not logged in, skipping push notification
            Log.e(TAG, "user is not logged in, skipping push notification");
            return;
        }

        switch (Integer.parseInt(flag)) {
            case Config.PUSH_TYPE_USER:
                // push notification is specific to message from user
                System.out.println("Push type is user");
                processUserMessage(title, isBackground, data);
                break;
            case Config.PUSH_TYPE_FRIEND:
                // push notification is specific to friend request
                processFriendRequest(title, isBackground, data);
                break;
        }
    }

    /**
     * Processing Friend request
     * this message will be broadcasts to all the activities registered
     * */
    private void processFriendRequest(String title, boolean isBackground, String data) {
        if (!isBackground) {
            try {
                JSONObject datObj = new JSONObject(data);
                String emailFrom = datObj.getString("emailFrom");
                String nameFrom = datObj.getString("nameFrom");

                //Creates the message
                String message = "Friend request from " + nameFrom;  //CHANGE ME !!!!!!!

                // verifying whether the app is in background or foreground
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("type", Config.PUSH_TYPE_FRIEND);
                    pushNotification.putExtra("message", message);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                } else {
                    //Get current time to add with notification
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date());

                    // app is in background. show the message in notification try
                    Intent resultIntent = new Intent(getApplicationContext(), App_act.class);
                    System.out.println("Friend request: " + message);
                    showNotificationMessage(getApplicationContext(), title, message, currentDateandTime, resultIntent);
                }
            } catch (JSONException e) {
                Log.e(TAG, "json parsing error: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }


    private void processUserMessage(String title, boolean isBackground, String data) {
        if (!isBackground) {

            try {
                JSONObject datObj = new JSONObject(data);

                Message message = new Message();
                message.setMessage(datObj.getString("message"));
                message.setMessageId(datObj.getString("message_id"));
                message.setDateCreated(datObj.getString("created_at"));
                message.setName(datObj.getString("user_name"));
                message.setEmail(datObj.getString("email"));

                System.out.println("collected and created user message " + datObj.getString("created_at"));

                // verifying whether the app is in background or foreground
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    System.out.println("User message is sent to chat ");
                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("type", Config.PUSH_TYPE_USER);
                    System.out.println("Message while in chat !!!!!!!!!!!!!!!!!!");
                    pushNotification.putExtra("message", message);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                } else {
                    // app is in background. show the message in notification try
                    Intent resultIntent = new Intent(getApplicationContext(), ChatRoomActivity.class);  // FIXXXXXXXXXXX
                    showNotificationMessage(getApplicationContext(), title, message.getName() + " : " + message.getMessage(), message.getDateCreated(), resultIntent);
                }
            } catch (JSONException e) {
                Log.e(TAG, "json parsing error: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }


    /**
     * Showing notification with text only
     * */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }


}