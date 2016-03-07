package com.example.pc.run.Call;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.example.pc.run.R;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;

public class setUpCallActivity extends BaseActivity implements CallAPIServices.StartFailedListener {
    ProgressDialog waitSpinner;

    private Button startCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_call);
        startCall = (Button) findViewById(R.id.callButton);
        startCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUserButton("make it return the name of the user you want to call(from a list of users?)");
            }
        });
    }
    /*
    This method will take in the string and starts the api client
     */
    private void startVideoClient(String loginID){
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(loginID);
        }
    }

    @Override
    public void onStarted() {
    }

    @Override
    public void onStartFailed(SinchError error){
        if (waitSpinner != null) {
            waitSpinner.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        super.onDestroy();
    }
    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (waitSpinner != null) {
            waitSpinner.dismiss();
        }
        super.onPause();
    }

    /*
    Once the call button is clicked, it will take in the username of the data and call the other user
    based on their name on the database
     */
    private void callUserButton(String callReceive) {
        String callReceiver = callReceive;

            Call call = getSinchServiceInterface().callUserVideo(callReceiver);
            String callID = call.getCallId();

            Intent callScreen = new Intent(this, CallScreenActivity.class);
            callScreen.putExtra(CallAPIServices.CALL_ID, callID);
            startActivity(callScreen);
    }

    private void startVideoStream() {
        Intent videoStreamActivity = new Intent(this, CallScreenActivity.class);
        startActivity(videoStreamActivity);
    }


    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.callButton:
                    startVideoStream();
                    break;

                case R.id.stopButton:
                    stopButtonClicked();
                    break;

            }
        }
    };

    private void showSpinner() {
        waitSpinner = new ProgressDialog(this);
        waitSpinner.setTitle("Connecting Call");
        waitSpinner.setMessage("Please wait...");
        waitSpinner.show();
    }
}
