package com.example.pc.run.Video;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sinch.android.rtc.calling.Call;

public class PlaceCall extends  BaseActivity{
    /*
    Name of the call receiver
    */
    private String otherUser;
    static final String TAG = PlaceCall.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        otherUser = intent.getStringExtra("userEmail");
    }

    @Override
    protected void onServiceConnected() {
       callUser();
    }


    /*
    Starts a call with the other user and runs the VideoCall Class
     */
    public void callUser() {
        Log.d(TAG, "Attempt to call user");
        Call call = getSinchServiceInterface().callUserVideo(otherUser);
        String callId = call.getCallId();
        Intent callScreen = new Intent(this, VideoCall.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }

}
