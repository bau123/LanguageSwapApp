package com.example.pc.run.VideoChat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.run.R;
import com.example.pc.run.SharedPref.ApplicationSingleton;
import com.sinch.android.rtc.calling.Call;


public class StartCall extends BaseActivity{

    private String receiverEmail;
    private String callerName;
    private Button callButton;
    private TextView callName;
    static final String CLASSNAME = StartCall.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callerName = getIntent().getStringExtra("callerEmail");
        receiverEmail = getIntent().getStringExtra("receiverEmail");
        callUser(receiverEmail);
    }


    /*
    Returns the receivers email as a string
     */
    private String getDisplayName(){
        String displayName = receiverEmail;
        displayName.replace("@kcl.ac.uk", "");
        String displayFirsName [] = displayName.split(".");
        Log.d(CLASSNAME, displayFirsName[0]);
        return displayFirsName[0];
    }

    /*
    Takes in the call receivers email and starts a call to them
    This will open a new intent which takes them to the callscreen activity
     */
    public void callUser(String email) {
        if (email == null) {
            Toast.makeText(this, "Cannot Call User", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = getSinchServiceInterface().callUserVideo(email);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(VideoService.CALL_ID, callId);
        startActivity(callScreen);
    }

    /*
    Stops the videoCalling service and returns the user to the friendslist intent
     */
    private void stopCall(){
        getSinchServiceInterface().stopClient();
        Toast.makeText(this, "Ending Call", Toast.LENGTH_LONG).show();
        /*
        add an intent to return the user back to the friend list adapter/list
         */

    }
}
