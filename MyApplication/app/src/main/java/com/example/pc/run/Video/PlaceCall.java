package com.example.pc.run.Video;


import android.content.Intent;
import android.os.Bundle;

import com.sinch.android.rtc.calling.Call;

public class PlaceCall extends  BaseActivity{

    private String otherUser;

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

    public void callUser() {

        Call call = getSinchServiceInterface().callUserVideo(otherUser);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, VideoCall.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }

}
