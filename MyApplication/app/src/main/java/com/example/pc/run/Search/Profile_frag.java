package com.example.pc.run.Search;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.App_act;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Message;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.PullProfile;
import com.example.pc.run.R;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Profile_frag extends Fragment {

    TextView name, languagesKnown, languagesLearning, interests;
    private Profile profile;
    private String data;
    private final String url = "http://k1.esy.es/requestFriend.php";


    public Profile_frag(){
        profile =  new Profile();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        data = getArguments().getString("data");
        View v = inflater.inflate(R.layout.activity_profile_frag, container, false);


        name = (TextView) v.findViewById(R.id.nameField);
        languagesKnown = (TextView) v.findViewById(R.id.langKnownField);
        languagesLearning = (TextView) v.findViewById(R.id.langLearningField);
        interests = (TextView) v.findViewById(R.id.interestsField);

        //Create the profile using the data retrive from the server
        try{
            JSONObject obj = new JSONObject(data);
            profile = new Profile(obj.getString("name"), obj.getString("languagesKnown"), obj.getString("languagesLearning"), obj.getString("interests"));
            profile.setEmail(obj.getString("email"));
        }catch(Exception e) {
            e.printStackTrace();
        }

        //Set textviews with the profile details
        name.setText("Name: " + profile.getName());
        languagesKnown.setText("Languages Known: " + profile.getLanguagesKnown());
        languagesLearning.setText("Languages Learning: " + profile.getLanguagesLearning());
        interests.setText("Interests: " + profile.getInterests());

        System.out.println("new fragment made");
        return v;
    }

    //Used to parse the data to this frag
    public static Profile_frag newInstance(JSONObject input){
        Profile_frag fragment = new Profile_frag();
        Bundle data = new Bundle();
        try{
            data.putString("data", input.toString());
        }catch(Exception e ){
            e.printStackTrace();
        }

        fragment.setArguments(data);
        return fragment;
    }

    //Sends a friend request to the user using a notification
    public void addFav(View view){
        //new notification
        //Creating params needed to send to user friend request
        Map<String, String> params = new HashMap<String, String>();
        params.put("emailFrom", ApplicationSingleton.getInstance().getPrefManager().getProfile().getEmail());
        params.put("emailTo", profile.getEmail());
        params.put("gcmTo", ApplicationSingleton.getInstance().getPrefManager().getToken());
        params.put("nameFrom", ApplicationSingleton.getInstance().getPrefManager().getProfile().getName());

        //Send message to database and then notify the user
        Requests jsObjRequest = new Requests(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
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

    }

    private void processResult(JSONObject input) throws InterruptedException {
        String result ="";
        try{
            result = input.getString("message");
        }catch (Exception e){
            e.printStackTrace();
        }
        if (result.equals("success")) {
            //Message telling user that friend request is sent
            Toast.makeText(getActivity().getBaseContext(), "Friend request is sent", Toast.LENGTH_LONG).show();
        } else if (result.equals("failure")) {
            Toast.makeText(getActivity().getBaseContext(), "Sorry request could not be sent", Toast.LENGTH_LONG).show();
        }
    }


}
