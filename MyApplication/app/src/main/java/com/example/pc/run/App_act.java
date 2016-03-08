package com.example.pc.run;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
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
import com.example.pc.run.LocationServices.SelectedCampus;
import com.example.pc.run.LocationServices.UserLocation;
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

public class App_act extends Fragment {

    private static String TAG = "In AppAct";
    private BroadcastReceiver regReceiver;
    private ArrayList<UserLocation> arrayUsers = new ArrayList<>();
    private ArrayList<String> campuses = new ArrayList<>();
    private ArrayList<SelectedCampus> selectedCampus = new ArrayList<>();
    private ViewPager viewPager;
    SearchView searchEngine;
    String searchInput;
    ProgressDialog progress;
    String url = "http://t-simkus.com/run/search-db.php";
    ArrayList<Fragment> frags = new ArrayList<>();
    private View masterView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Location
        setLocation();

        Map<String, String> tempParams = new HashMap<>();
        tempParams.put("info", "");
        tempParams.put("email", GlobalProfile.profileEmail);
        processParameters(tempParams);

        System.out.println("making params");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("info", searchInput);
        parameters.put("email", GlobalProfile.profileEmail);
        System.out.println("params made " + searchInput);

        processParameters(parameters);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_app_act, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        masterView = v;

        //setCheckHandlers();

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void processParameters(Map<String, String> parameters) {
        // progress = ProgressDialog.show(this, "Please wait..", "Loading profiles...", true);
        Requests jsObjRequest = new Requests(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    //progress.dismiss();
                    processResult(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
                //progress.dismiss();
            }
        });
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
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
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        System.out.println("refreshed pageAdapter");
        //progress.dismiss();
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
        String locationUrl = "http://t-simkus.com/run/updateLocation.php";
        try {
            CoordinatesToString cts = new CoordinatesToString(this.getContext());
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
            Toast.makeText(this.getContext().getApplicationContext(), "Sorry we cant get the location", Toast.LENGTH_LONG).show();
        }
    }

    public void setParams() {
        String url2 = "http://t-simkus.com/run/getLocations.php";
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("campus", "Not at any campus");

        Requests jsObjRequest = new Requests(Request.Method.POST, url2, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    processResultCampus(response);
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


    /*
    Process json result
     */
    private void processResultCampus(JSONObject input) throws InterruptedException {
        try {
            JSONArray r = input.getJSONArray("result");
            for(int i = 0; i < r.length();i++) {
                JSONObject j = (JSONObject) r.get(i);
                String e = j.get("email").toString();
                String c = j.get("campus").toString();
                this.arrayUsers.add(new UserLocation(e,c));
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

    }

//    public void setCheckHandlers() {
//        GridLayout grid = (GridLayout) findViewById(R.id.grid0);
//        for(int i = 0; i < grid.getChildCount();i++) {
//            grid.getChildAt(i).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    CheckBox checkBox = (CheckBox) findViewById(v.getId());
//                    String IdAsString = v.getResources().getResourceName(v.getId());
//                    IdAsString = IdAsString.substring(IdAsString.length()-1);
//                    if(checkBox.isChecked()) {
//                        selectedCampus.add(new SelectedCampus(Integer.valueOf(IdAsString),true));
//                    }
//                    else {
//                        for(int i = 0 ; i < selectedCampus.size();i++) {
//                            if(selectedCampus.get(i).campus==Integer.parseInt(IdAsString)) {
//                                System.out.println("made invalid");
//                                selectedCampus.get(i).valid = false;
//                            }
//                        }
//                    }
//                }
//            });
//        }
//    }

//    public void setButtonHandler() {
//        Button btnSearch = (Button) findViewById(R.id.btnSearch);
//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                campuses.clear();
//                translateCampus(selectedCampus);
//                getCampusPeople();
//                //ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0];
//            }
//        });
//    }


    public void translateCampus(ArrayList<SelectedCampus> c) {
        //Translate numbers to string for campuses
        if(c.size() == 0) {
            campuses.add("Not at any campus");
        }
        else {
            for (int i = 0; i < c.size(); i++) {

                if (c.get(i).campus == 0 && c.get(i).valid) {
                    campuses.add("Strand");
                } else if (c.get(i).campus == 1 && c.get(i).valid) {
                    campuses.add("Franklin-Wilkins");
                } else if (c.get(i).campus == 2 && c.get(i).valid) {
                    campuses.add("James Clerk Maxwell");
                } else if (c.get(i).campus == 3 && c.get(i).valid) {
                    campuses.add("Maughan Library");
                } else if (c.get(i).campus == 4 && c.get(i).valid) {
                    campuses.add("Durry Lane");
                } else if (c.get(i).campus == 5 && c.get(i).valid) {
                    campuses.add("Virginia Woolf");
                }

            }


        }
    }

    /*
    Get people from selected campuses
     */
    public void getCampusPeople() {
        System.out.println("People at selected campus:");
        for(String campus : campuses) {
            for(UserLocation user : arrayUsers) {
                if(user.campus.equals(campus)) {
                    System.out.println(user.email + " " + user.campus);
                }
            }
        }
    }

}

