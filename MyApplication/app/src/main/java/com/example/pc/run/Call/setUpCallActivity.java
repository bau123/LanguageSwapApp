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

     /*
     Here set the email to be who is makingh the call and who the call is going out to
     */
    private String userID;
    private String otherUserID;

    private Button startCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_call);
        startCall = (Button) findViewById(R.id.callButton);
        startCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callUserButton();
            }
        });
    }


    @Override
    public void onStarted() {
        /*
        i think we can leave this empty for now
         */
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

    private void callUserButton() {
        String userName = userID;
        String callReceiver = otherUserID;
        if (callReceiver == null) {
            Log.d("callReceiver", "Cannot Call User");
        }
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {

            Call call = getSinchServiceInterface().callUserVideo(callReceiver);
            String callID = call.getCallId();

            Intent callScreen = new Intent(this, CallScreenActivity.class);
            callScreen.putExtra(CallAPIServices.CALL_ID, callID);
            startActivity(callScreen);
        }
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
