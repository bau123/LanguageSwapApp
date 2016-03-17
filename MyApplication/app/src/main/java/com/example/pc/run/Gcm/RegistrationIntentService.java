package com.example.pc.run.Gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.R;

import com.example.pc.run.SharedPref.ApplicationSingleton;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "In RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    public static final String KEY = "key";
    public static final String TOPIC = "topic";
    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";

    @Override
    // Register with gcm and obtain gcm reg id
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = null;
        try{
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            //Send to database
            sendRegistrationToServer(token);
            //Boolean saved in shared pref indicating the token is generated
            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, true).apply();
            //Stores toke in shared pref
            ApplicationSingleton.getInstance().getPrefManager().storeToken(token);
            Log.i(TAG, "GCM Registration Token: " + token);
        }catch (Exception e){
            Log.d(TAG, "Failed to complete token refresh", e);
            //Boolean indicating the token failed
            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, false).apply();
            e.printStackTrace();
        }
        //Notify the ui that registration is complete
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // Send the registration token to our server
        // to keep it in MySQL
        String url = "http://t-simkus.com/run/updateGcm.php"; //REPLACE

        System.out.println("Making params for reg to server");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("gcm_registration_id", token);
        parameters.put("email", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);
        System.out.println("params made");

        Requests jsObjRequest = new Requests(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    processResult(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void processResult(JSONObject input) throws JSONException, InterruptedException {
        String result = "";
        try{
            result = input.getString("message");
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (result.equals("success")) {
            Intent registrationComplete = new Intent(Config.SENT_TOKEN_TO_SERVER);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
        } else if (result.equals("failure")) {
            Toast.makeText(getApplicationContext(), "Unable to send gcm reg code to server", Toast.LENGTH_SHORT).show();
        }

    }
}