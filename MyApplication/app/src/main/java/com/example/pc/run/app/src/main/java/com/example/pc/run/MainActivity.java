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
import android.widget.Toast;

import com.example.pc.run.Gcm.Config;
import com.example.pc.run.Gcm.RegistrationIntentService;
import com.example.pc.run.LocationServices.CoordinatesToString;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class MainActivity extends AppCompatActivity {

    private static String TAG = "In mainAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view){
        Intent intent = new Intent(this, Login_act.class);
        startActivity(intent);
    }

    public void register(View view){
        Intent intent = new Intent(this, Register_act.class);
        startActivity(intent);
    }



}
