package com.example.pc.run;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pc.run.Chat.ChatRooms;
import com.example.pc.run.Gcm.Config;
import com.example.pc.run.Gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class MainActivity extends AppCompatActivity {

    private static String TAG = "In mainAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view){
        Intent intent = new Intent(this, Login_act.class);
        startActivity(intent);
    }

<<<<<<< HEAD
    public void register(View view){
        Intent intent = new Intent(this, Register_act.class);
        startActivity(intent);
=======
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_search){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new App_act();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new FriendsList_act();
                title = getString(R.string.title_friends);
                break;
            case 2:
                fragment = new ChatRooms();
                title = getString(R.string.title_messages);
                break;


            default:fragment = new App_act();
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    public void processPushNotification(Intent intent){
        int type = intent.getIntExtra("type", -1);
        // If push is friend request
        if(type == Config.PUSH_TYPE_FRIEND){
            Toast.makeText(getApplicationContext(), "Someone has sent you a friend request", Toast.LENGTH_LONG).show();
            ///NEED TO CHOOSE WHAT TO DO HERE
        }
        //else !!!!!!!!!!!!!!!!!!!!!!!!!!!ADDDD LATERRR
        else{

        }
    }

    public boolean checkPlayService() {
        int queryResult = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (queryResult == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(queryResult)) {
            String errorString = GoogleApiAvailability.getInstance().getErrorString(queryResult);
            Log.d(TAG, "Problem with google play service : " + queryResult + " " + errorString);
            Toast.makeText(getApplicationContext(), "Device is not supported. Please install google play service.", Toast.LENGTH_LONG).show();
            finish();
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(regReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(regReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // Clear notification tray
        NotificationUtils.clearNotifications();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(regReceiver);
        super.onPause();
>>>>>>> c6bfc0c1a6b28c1e38690c3fdf8207698da9e203
    }

}
