package com.example.pc.run.Gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class TokenListener  extends InstanceIDListenerService {

    private static final String TAG = TokenListener.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

}
