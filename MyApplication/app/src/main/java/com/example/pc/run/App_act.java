package com.example.pc.run;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pc.run.Gcm.Config;
import com.example.pc.run.Gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class App_act extends AppCompatActivity {

    Button chatBtn;
    private static String TAG = "In AppAct";
    private BroadcastReceiver regReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_act);

        chatBtn = (Button) findViewById(R.id.enterChat);

        regReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    String token = intent.getStringExtra("token");
                    Toast.makeText(getApplicationContext(), "GCM registration token: " + token, Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL
                    Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    Toast.makeText(getApplicationContext(), "Push notification is received!", Toast.LENGTH_LONG).show();
                }
            }
        };

        if (checkPlayService()) {
            //Register gcm
            Intent intent = new Intent(this, RegistrationIntentService.class);
            intent.putExtra("key", "register");
            startService(intent);
        }

    }

    public boolean checkPlayService() {
        int queryResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (queryResult == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(queryResult)) {
            String errorString = GoogleApiAvailability.getInstance().getErrorString(queryResult);
            Log.d(TAG, "Problem with google play service : " + queryResult + " " + errorString);
            Toast.makeText(getApplicationContext(), "Device is not supported. Please install google play service.", Toast.LENGTH_LONG).show();
            finish();
        }
        return false;
    }

    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(regReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(regReceiver);
        super.onPause();
    }


}
