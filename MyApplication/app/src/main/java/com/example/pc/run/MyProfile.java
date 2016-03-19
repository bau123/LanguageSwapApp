package com.example.pc.run;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyProfile extends Fragment {

    TextView name;
    TextView interests;
    TextView languagesKnown;
    TextView languagesLearning;
    ImageView profileImage;
    Bitmap bitmap;
    String email;
    Button seeReviews;
    Button editProfile;
    String url = "http://t-simkus.com/run/pullProfile.php";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.myprofile_frag, container, false);
        setHasOptionsMenu(true);

        email = ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0];
        Log.d("PROFILE EMAIL:", email);

        name = (TextView) v.findViewById(R.id.nameView);
        interests = (TextView) v.findViewById(R.id.interestsView);
        languagesKnown = (TextView) v.findViewById(R.id.languagesKnownView);
        languagesLearning = (TextView) v.findViewById(R.id.languagesLearningView);
        profileImage = (ImageView) v.findViewById(R.id.profileImageView);
        seeReviews = (Button)v.findViewById(R.id.reviewButton);
        editProfile = (Button) v.findViewById(R.id.editProfile);
        seeReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewList_act.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getProfileInfo();

        return v;
    }

    public void getProfileInfo(){

        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);

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
    }

    private void processResult(JSONObject input) throws JSONException {

        JSONArray userInfo = input.getJSONArray("result");
        JSONObject current = userInfo.getJSONObject(0);

        Log.d("NAME:", current.getString("name"));
        Log.d("Interests::", current.getString("interests"));
        Log.d("Languages Known:", current.getString("languagesKnown"));
        Log.d("Languages Learning:", current.getString("languagesLearning"));
        Log.d("BITMAP STRING:", current.getString("photo"));

        name.setText("Name: " + current.getString("name"));
        interests.setText("Interests: " + current.getString("interests"));
        languagesKnown.setText("Languages Known: " +current.getString("languagesKnown"));
        languagesLearning.setText("Languages learning: " +current.getString("languagesLearning"));

        if(current.getString("photo") != null) {
            Log.d("PROFILE BITMAP:", current.getString("photo"));
            byte[] decodedByte = Base64.decode(current.getString("photo"), 0);
            bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
            profileImage.setImageBitmap(bitmap);
        }

        if(current.getString("name") != null) {
            System.out.println("SUCCESSFUL");
        }else{
            System.out.println("UNSUCCESSFUL");
        }
    }
}
