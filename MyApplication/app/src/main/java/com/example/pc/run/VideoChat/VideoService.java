package com.example.pc.run.VideoChat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

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

public class VideoService extends Service{

    private static final String appKey = "e772e953-e32f-472d-b2bf-857fe305bd6c";
    private static final String appSecret = "mFTbOmXcZUC9UFkgHhHRGw==";
    private static final String enviro = "sandbox.sinch.com";
    private SinchClient videoClient;
    private SinchServiceInterface mSinchServiceInterface = new SinchServiceInterface();
    public static final String CALL_ID = "CALL_ID";
    String usernameA;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void startVideo(String username){
        android.content.Context context = this.getApplicationContext();
        usernameA = username;
        videoClient = Sinch.getSinchClientBuilder().context(context)
                .applicationKey(appKey)
                .applicationSecret(appSecret)
                .environmentHost(enviro)
                .userId(username)
                .build();
        videoClient.setSupportCalling(true);
        videoClient.startListeningOnActiveConnection();

        videoClient.getCallClient().addCallClientListener(new SinchCallClientListener());
        videoClient.addSinchClientListener(new SinchClientListener() {
            @Override
            public void onClientStarted(SinchClient sinchClient) {
                Log.d("VideoService", "Video Client has started");
            }

            @Override
            public void onClientStopped(SinchClient sinchClient) {
                Log.d("VideoService", "Video Client has stopped");
            }

            @Override
            public void onClientFailed(SinchClient sinchClient, SinchError sinchError) {
                Log.d("VideoService", "Video Client has failed");
            }

            @Override
            public void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration) {

            }

            @Override
            public void onLogMessage(int i, String s, String s1) {

            }
        });
        videoClient.start();

    }

    private boolean isStarted() {
        return (videoClient != null && videoClient.isStarted());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSinchServiceInterface;
    }

    public class SinchServiceInterface extends Binder {

        public Call callUserVideo(String userId) {
            return videoClient.getCallClient().callUserVideo(userId);
        }

        public String getUserName() {
            return usernameA;
        }

        public void startClient(String userName) {
            startVideo(userName);
        }

        public void stopClient() {
            endCall();
        }

        public Call getCall(String callId) {
            return videoClient.getCallClient().getCall(callId);
        }
        public VideoController getVideoController(){
            if (!isStarted()) {
                return null;
            }
            return videoClient.getVideoController();
        }
        public AudioController getAudioController() {
            if (!isStarted()) {
                return null;
            }
            return videoClient.getAudioController();
        }
    }

    private void endCall(){
        videoClient.stopListeningOnActiveConnection();
        videoClient.terminate();
    }

    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {
            Log.d("VideoService", "Incoming call");
            Intent intent = new Intent(VideoService.this, IncomingCallScreenActivity.class);
            intent.putExtra(CALL_ID, call.getCallId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            VideoService.this.startActivity(intent);
        }
    }
}
