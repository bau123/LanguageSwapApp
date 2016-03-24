package com.example.pc.run.Video;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


import com.example.pc.run.R;

import java.util.List;
/*
Incoming call activity class
 */
public class IncomingCall extends BaseActivity {
    /*
    Gets the name of the class
     */
    static final String TAG = IncomingCall.class.getSimpleName();
    /*
    Name of the caller ID
     */
    private String mCallId;
    /*
    Gets the AudioPlayer Class
     */
    private AudioPlayer mAudioPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        Button answer = (Button) findViewById(R.id.answerButton);
        answer.setOnClickListener(mClickListener);
        Button decline = (Button) findViewById(R.id.declineButton);
        decline.setOnClickListener(mClickListener);

        mAudioPlayer = new AudioPlayer(this);
        mAudioPlayer.playRingtone();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
    }
    /*
    Checks if the videoC client has connection and display who is calling
     */
    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            TextView remoteUser = (TextView) findViewById(R.id.remoteUser);
            String name = call.getRemoteUserId();
            String splitName [] = name.toString().split("\\.");
            String Fname = splitName[0].substring(0,1).toUpperCase() + splitName[0].substring(1);
            remoteUser.setText(Fname);

        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }
    /*
    Stops the audio for the ringtone and starts the VideoCall class
     */
    private void answerClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, VideoCall.class);
            intent.putExtra(SinchService.CALL_ID, mCallId);
            startActivity(intent);
        } else {
            finish();
        }
    }
    /*
    Stops the audio for the ringtone and ends the call
     */
    private void declineClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements VideoCallListener {
        /*
        Stops the ringtone and displays information of why the call ended
         */
        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended, cause: " + cause.toString());
            mAudioPlayer.stopRingtone();
            finish();
        }
        /*
        Logs a message when the call is established
         */
        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
        }
        /*
        Logs a message of ongoing calls
         */
        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }

        @Override
        public void onVideoTrackAdded(Call call) {
            // Display some kind of icon showing it's a video call
        }
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.answerButton:
                    answerClicked();
                    break;
                case R.id.declineButton:
                    declineClicked();
                    break;
            }
        }
    };
}
