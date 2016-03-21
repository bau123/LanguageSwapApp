package com.example.pc.run.VideoChat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pc.run.R;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.video.VideoCallListener;

import java.util.List;

public class IncomingCallScreenActivity extends BaseActivity {

    private String mCallId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call_screen);

        Button answer = (Button) findViewById(R.id.acceptBtn);
        answer.setOnClickListener(mClickListener);
        Button decline = (Button) findViewById(R.id.declineBtn);
        decline.setOnClickListener(mClickListener);

        mCallId = getIntent().getStringExtra(VideoService.CALL_ID);
    }

    private void answerClicked() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, CallScreenActivity.class);
            intent.putExtra(VideoService.CALL_ID, mCallId);
            startActivity(intent);
        } else {
            finish();
        }
    }

    private void declineClicked() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    @Override
    protected void onServiceConnected() {
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            TextView remoteUser = (TextView) findViewById(R.id.remoteUser);
            remoteUser.setText(call.getRemoteUserId());

        } else {
            Log.e("IncomingCallScreen", "Started with invalid callId, aborting");
            finish();
        }
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.acceptBtn:
                    answerClicked();
                    break;
                case R.id.declineBtn:
                    declineClicked();
                    break;
            }
        }
    };
    private class SinchCallListener implements VideoCallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d("IncomingCallScreen", "Call ended, cause: " + cause.toString());
            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d("IncomingCallScreen", "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d("IncomingCallScreen", "Call progressing");
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
}
