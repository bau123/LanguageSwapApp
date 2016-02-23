package com.example.pc.run;

import android.content.BroadcastReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.example.pc.run.LocationServices.CoordinatesToString;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Search.Profile_frag;
import com.example.pc.run.SharedPref.ApplicationSingleton;

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
    String url = "http://k1.esy.es/search-db.php";
    ArrayList<Fragment> frags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_act);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        //Location
        setLocation();

        //Set default fragment
        JSONObject tempJson = new JSONObject();
        try {
            tempJson.put("name", "test");
            tempJson.put("languagesKnown", "test");
            tempJson.put("languagesLearning", "test");
            tempJson.put("interests", "test");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Profile_frag temp = new Profile_frag().newInstance(tempJson);
        frags.add(temp);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

        searchEngine = (SearchView) findViewById(R.id.searchView);
        searchEngine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                System.out.println("making params");
                Map<String, String> parameters = new HashMap<>();
                parameters.put("info", query);
                System.out.println("params made " + query);

                Requests jsObjRequest = new Requests(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response.toString());
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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
                System.out.println("Added profile searched");
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
        String locationUrl = "http://k1.esy.es/updateLocation.php";
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

}

