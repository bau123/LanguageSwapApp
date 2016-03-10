package com.example.pc.run.VideoChat;

import android.app.Application;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.app.Service;
import android.os.Binder;

import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.video.VideoController;

public class CallAPIServices extends Application{

    

    private SinchClient userClient;
    private static final String TAG = CallAPIServices.class.getSimpleName();
    public static final String CALL_ID = "CALL_ID";


    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void start(String userName) {
        if (userClient == null) {
            userClient = Sinch.getSinchClientBuilder().context(getApplicationContext())
                    .applicationKey("e772e953-e32f-472d-b2bf-857fe305bd6c")
                    .applicationSecret("mFTbOmXcZUC9UFkgHhHRGw==")
                    .environmentHost("sandbox.sinch.com")
                    .userId(userName)
                    .build();

            userClient.setSupportCalling(true);
            userClient.startListeningOnActiveConnection();
            userClient.addSinchClientListener(new MySinchClientListener());

            userClient.getCallClient().addCallClientListener(new SinchCallClientListener());
            userClient.start();
        }
    }


    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {
            Log.d(TAG, "Incoming call");
            //Intent intent = new Intent(CallAPIServices.this, IncomingCallScreenActivity.class);
            //intent.putExtra(CALL_ID, call.getCallId());
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //CallAPIServices.this.startActivity(intent);
        }
    }

    private boolean isStarted() {
        return (userClient != null && userClient.isStarted());
    }

    private class MySinchClientListener implements SinchClientListener {

        @Override
        public void onClientFailed(SinchClient client, SinchError error) {
            userClient.terminate();
            userClient = null;
        }

        @Override
        public void onClientStarted(SinchClient client) {
            Log.d(TAG, "SinchClient started");
        }

        @Override
        public void onClientStopped(SinchClient client) {
            Log.d(TAG, "SinchClient stopped");
        }

        @Override
        public void onLogMessage(int level, String area, String message) {
            switch (level) {
                case Log.DEBUG:
                    Log.d(area, message);
                    break;
                case Log.ERROR:
                    Log.e(area, message);
                    break;
                case Log.INFO:
                    Log.i(area, message);
                    break;
                case Log.VERBOSE:
                    Log.v(area, message);
                    break;
                case Log.WARN:
                    Log.w(area, message);
                    break;
            }
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient client,
                                                      ClientRegistration clientRegistration) {
        }
    }

}
