package com.example.pc.run.Search;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.R;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Profile_frag extends Fragment {

    TextView name, languagesKnown, languagesLearning, interests, campus;
    ImageView profileImage;
    private Profile profile;
    private String data;
    private final String url = "http://t-simkus.com/run/requestFriend.php";
    private FloatingActionButton addFriend;
    private String gcm;
    private String strCampus;

    public Profile_frag(){
        profile =  new Profile();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        data = getArguments().getString("data");
        View v = inflater.inflate(R.layout.activity_profile_frag, container, false);

        addFriend = (FloatingActionButton) v.findViewById(R.id.addFavBtn);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addFriend();
            }
        });

        name = (TextView) v.findViewById(R.id.nameField);
        languagesKnown = (TextView) v.findViewById(R.id.langKnownField);
        languagesLearning = (TextView) v.findViewById(R.id.langLearningField);
        interests = (TextView) v.findViewById(R.id.interestsField);
        profileImage = (ImageView)v.findViewById(R.id.profileImageSwipe);
        campus = (TextView) v.findViewById(R.id.lblCampus);

        //Pull each profile using the data retrive from the server
        try{
            JSONObject obj = new JSONObject(data);
            profile = new Profile(obj.getString("name"), obj.getString("languagesKnown"), obj.getString("languagesLearning"), obj.getString("interests"));
            strCampus = obj.getString("campus");
            profile.setEmail(obj.getString("email"));
            gcm = obj.getString("gcm_registration_id");
            if(obj.getString("photo") != null){
                profile.setProfilePicture(obj.getString("photo"));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        //Set textviews with the profile details
        name.append(" " + profile.getName());
        languagesKnown.append(" " + profile.getLanguagesKnown());
        languagesLearning.append(" " + profile.getLanguagesLearning());
        interests.append(" " + profile.getInterests());
        campus.append(" " + this.strCampus);

        if(profile.getProfilePicture() != null){
            profileImage.setImageBitmap(profile.getProfilePicture());
        }

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
    public void addFriend(){
        //new notification
        //Creating params needed to send to user friend request
        Map<String, String> params = new HashMap<String, String>();
        params.put("emailFrom", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);
        params.put("emailTo", profile.getEmail());
        params.put("gcmTo", gcm);
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

    private void processResult(JSONObject input) throws JSONException, InterruptedException {
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
        else if(result.equals("Insert failed")){
            System.out.println("Friend request: Insert failed");
        }
    }


}
