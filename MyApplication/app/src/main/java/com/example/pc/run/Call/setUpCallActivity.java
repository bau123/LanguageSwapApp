package com.example.pc.run.Call;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;


import com.example.pc.run.R;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;

public class setUpCallActivity extends BaseActivity implements CallAPIServices.StartFailedListener {

    /*
    use mSpinner to display a loading
     */
    private ProgressDialog mSpinner;
    /*
    UserID will be the ID of the caller
     */
    private String userID;
    /*
    otherUserId will be the ID of the receiver of the call
     */
    private String otherUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_call);
    }


    @Override
    public void onStarted() {
        /*

         */
    }

    @Override
    public void onStartFailed(SinchError error){
        if (mSpinner != null) {
            mSpinner.dismiss();
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

    private void callUserButton(){
        String userName = userID;
        if (userName == null){
            //THAT USER DOES NOT EXIST
        } if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
           // showSpinner();
        }
        Call call = getSinchServiceInterface().callUserVideo(userName);
        String callID = call.getCallId();
        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(CallAPIServices.CALL_ID, callID);
        startActivity(callScreen);
    }



    private void showSpinner() {
        mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Looking For User");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();
    }

    private void startVideoStream() {
        Intent videoStreamActivity = new Intent(this, VideoStreaming.class);
        startActivity(videoStreamActivity);
    }
}
