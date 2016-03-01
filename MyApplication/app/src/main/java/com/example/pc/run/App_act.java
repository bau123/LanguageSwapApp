package com.example.pc.run;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Gcm.Config;
import com.example.pc.run.Gcm.MyGcmPushReceiver;
import com.example.pc.run.Gcm.NotificationUtils;
import com.example.pc.run.Gcm.RegistrationIntentService;
import com.example.pc.run.Global.GlobalProfile;
import com.example.pc.run.LocationServices.CoordinatesToString;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Search.Profile_frag;
import com.example.pc.run.SharedPref.ApplicationSingleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App_act extends AppCompatActivity {

    private static String TAG = "In AppAct";
    private BroadcastReceiver regReceiver;
    private ViewPager viewPager;
    SearchView searchEngine;
    ProgressDialog progress;
    String url = "http://k1.esy.es/search-db.php";
    ArrayList<Fragment> frags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_act);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //Location
        setLocation();

        Map<String, String> tempParams = new HashMap<>();
        tempParams.put("info", "");
        tempParams.put("email", GlobalProfile.profileEmail);
        processParameters(tempParams);

        searchEngine = (SearchView) findViewById(R.id.searchView);
        searchEngine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                System.out.println("making params");
                Map<String, String> parameters = new HashMap<>();
                parameters.put("info", query);
                parameters.put("email", GlobalProfile.profileEmail);
                System.out.println("params made " + query);

                processParameters(parameters);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        //Setting up broadcast receiver
        regReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) { //TAKE OUT !!!!!!!!!!!
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    String token = intent.getStringExtra("token");
                    Toast.makeText(getApplicationContext(), "GCM registration token: " + token, Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL
                    Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    processPushNotification(intent);

                }
            }
        };

        //Checks if play service is available
        if (checkPlayService()) {
            //Register gcm
            Intent intent = new Intent(this, RegistrationIntentService.class);
            intent.putExtra("key", "register");
            startService(intent);
        }
    }

    private void processParameters(Map<String, String> parameters){
        progress = ProgressDialog.show(this, "Please wait..", "Loading profiles...", true);
        Requests jsObjRequest = new Requests(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    progress.dismiss();
                    processResult(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
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

    private void processResult(JSONObject input) throws JSONException, InterruptedException {
        JSONArray profileNames = input.getJSONArray("result");
        //Clear the array containing the profile fragments
        ArrayList<Fragment> tempFrags = new ArrayList<>();
        Log.d("PROFILE NAMES:", profileNames.toString());

        ArrayList<JSONObject> information = new ArrayList<>();

        for (int i = 0; i < profileNames.length(); i++) {
            JSONObject current = profileNames.getJSONObject(i);
            if (current.getString("passed").equals("true")) {
                information.add(current);
            } else {
                //Produce message !!!!!
            }
        }
        System.out.println("Sending data");
        // Make fragments for every user found, store in frag array.
        for (int i = 0; i < information.size(); i++) {
            JSONObject tempJson = new JSONObject(information.get(i).toString());
            tempFrags.add(Profile_frag.newInstance(tempJson));
        }
        frags = tempFrags;

        viewPager.removeAllViews();
        viewPager.invalidate();
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        System.out.println("refreshed pageAdapter");
    }

    class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
            if (fm.getFragments() != null) {
                fm.getFragments().clear();
                System.out.println("fragment manager wiped");
            }
        }

        public Fragment getItem(int pos) {
            return frags.get(pos);
        }

        public int getCount() {
            return frags.size();
        }
    }

    public void setLocation() {
        String locationUrl = "http://192.168.0.11/Run/updateLocation.php";
        try {
            CoordinatesToString cts = new CoordinatesToString(this);
            System.out.println("Current location " + cts.latitude + " " + cts.longitude);
            System.out.println("Current campus " + cts.campus);
            //The string of the campus name
            String campus = cts.campus;

            Map<String, String> parameters = new HashMap<>();
            parameters.put("campus", campus);
            parameters.put("latitude", Double.toString(cts.latitude));
            parameters.put("longitude", Double.toString(cts.longitude));
            parameters.put("email", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);

            Requests jsObjRequest = new Requests(Request.Method.POST, locationUrl, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError response) {
                    Log.d("Response: ", response.toString());
                }
            });
            ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Sorry we cant get the location", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onResume() {
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
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(regReceiver);
        super.onPause();
    }

}

