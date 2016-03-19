package com.example.pc.run;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Adapters.MultiSelectionSpinner;


import com.example.pc.run.Gcm.RegistrationIntentService;
import com.example.pc.run.Global.GlobalProfile;
import com.example.pc.run.LocationServices.CoordinatesToString;
import com.example.pc.run.LocationServices.SelectedCampus;
import com.example.pc.run.LocationServices.UserLocation;
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

public class App_act extends Fragment {

    private static String TAG = "In AppAct";

    public ArrayList<String> campuses = new ArrayList<>();
    public ArrayList<SelectedCampus> selectedCampus = new ArrayList<>();
    public ArrayList<UserLocation> arrayUsers = new ArrayList<>();
    private ViewPager viewPager;
    SearchView searchEngine;
    String searchInput;
    ProgressDialog progress;
    String url = "http://t-simkus.com/run/search-db.php";
    ArrayList<Fragment> frags = new ArrayList<>();
    Boolean makingRequest = false;
    private View masterView;
    private JSONObject niput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Update location table
        setLocation();

        //Get location table
        setParams();

        if(!makingRequest){
            getAll();
        }

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_app_act, container, false);
        setHasOptionsMenu(true);

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        masterView = v;
        MultiSelectionSpinner spinner = (MultiSelectionSpinner) v.findViewById(R.id.spinner);
        buildSpinner(spinner);

        return v;
    }


    public void buildSpinner(MultiSelectionSpinner spinner) {

        //Add the items
        String[] items = new String[]{
                "Strand",
                "Franklin-Wilkins",
                "James Clerk Maxwell",
                "Maughan Library",
                "Durry Lane",
                "Virginia Woolf"
        };

        spinner.setItems(items);

        //Set the listener
        //Set the listener
        spinner.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
            @Override
            public void selectedIndices(List<Integer> indices) {
                campuses.clear();
                TextView t = (TextView) getView().findViewById(R.id.textView3);
                t.setText("");
                selectedCampus.clear();
                for (int i = 0; i < indices.size(); i++) {
                    selectedCampus.add(new SelectedCampus(indices.get(i), true));
                }
                translateCampus(selectedCampus);
                getCampusPeople();
                try {
                    processResult(niput, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void selectedStrings(List<String> strings) {

                //
            }
        });

    }

    public void externalQuery(String query){
        makingRequest = true;
        Map<String, String> parameters = new HashMap<>();
        parameters.put("info", query);
        parameters.put("email", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);
        processParameters(parameters);
    }

    public void getAll(){
        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);
        parameters.put("info", "");
        System.out.println(ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);
        System.out.println("params made for search " + searchInput);
        processParameters(parameters);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void processParameters(Map<String, String> parameters) {
        // progress = ProgressDialog.show(this, "Please wait..", "Loading profiles...", true);
        System.out.println("In processParameters");
        Requests jsObjRequest = new Requests(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    //progress.dismiss();
                    niput = response;
                    processResult(response,false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                System.out.println("Error in processParameters");
                Log.d("Response: ", response.toString());
                //progress.dismiss();
            }
        });
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void processResult(JSONObject input, Boolean queried) throws JSONException, InterruptedException {

        JSONArray profileNames = input.getJSONArray("result");
        //Clear the array containing the profile fragments
        ArrayList<Fragment> tempFrags = new ArrayList<>();
        Log.d("PROFILE NAMES:", profileNames.toString());

        ArrayList<JSONObject> information = new ArrayList<>();

        for (int i = 0; i < profileNames.length(); i++) {
            JSONObject current = profileNames.getJSONObject(i);
            if(queried) {
                for(int b = 0; b < selectedEmails.size();b++) {
                    if (current.getString("passed").equals("true") && current.getString("email").equals(selectedEmails.get(b))) {
                        information.add(current);
                    } else {
                        //Produce message !!!!!
                    }
                }
            }
            else {

                if (current.getString("passed").equals("true") ) {
                    information.add(current);
                } else {
                    //Produce message !!!!!
                }

            }


        }
        System.out.println("Sending data");
        // Make fragments for every user found, store in frag array.
        for (int i = 0; i < information.size(); i++) {
            JSONObject tempJson = new JSONObject(information.get(i).toString());


            for(int b = 0; b < arrayUsers.size();b++) {
                // t.setText(t.getText() + "\n " +arrayUsers.get(b).email);
                if(tempJson.getString("email").equals(arrayUsers.get(b).email)) {
                    tempJson.put("campus",arrayUsers.get(b).campus);
                }
            }
            tempFrags.add(Profile_frag.newInstance(tempJson));


        }

        if(information.isEmpty()) {
            Toast.makeText(getActivity(),"Search returned 0 results",Toast.LENGTH_SHORT).show();

        }

        frags = tempFrags;

        viewPager.removeAllViews();
        viewPager.invalidate();
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        System.out.println("refreshed pageAdapter");
        //progress.dismiss();
        TextView t = (TextView) getView().findViewById(R.id.textView3);
        t.setText("Search By Campus");
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
                        System.out.println("In getLocation");
                        Log.d(TAG, "response error in setLocation method");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError response) {
                    Log.d(TAG, response.toString() + "In setLocation");
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
    public ArrayList<String> selectedEmails = new ArrayList<>();
    public void getCampusPeople() {
        selectedEmails.clear();
        //Over every selected campus
        for(String campus : campuses) {
            //Over every user
            for(UserLocation user : arrayUsers) {
                //If user is in the location of the currently iterating campus
                if (user.campus.equals(campus)) {
                    //Add his email to the list
                    selectedEmails.add(user.email);
                }
            }
        }
    }


}

