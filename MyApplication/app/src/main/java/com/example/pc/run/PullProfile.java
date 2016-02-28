package com.example.pc.run;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Global.GlobalBitmap;
import com.example.pc.run.Global.GlobalProfile;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PullProfile {

    JSONArray userInfo;
    String email;
    String url = "http://k1.esy.es/pullProfile.php";

    public PullProfile(String email){
        this.email = email;
        pullInformation();
    }

    public void pullInformation() {
        System.out.println("Making params");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("email", email);
        System.out.println("params made");

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

            userInfo = input.getJSONArray("result");
            JSONObject current = userInfo.getJSONObject(0);

        Log.d("BITMAP STRING:", current.getString("name"));
        Log.d("BITMAP STRING:", current.getString("interests"));
        Log.d("BITMAP STRING:", current.getString("languagesKnown"));
        Log.d("BITMAP STRING:", current.getString("languagesLearning"));
        Log.d("BITMAP STRING:", current.getString("photo"));

        GlobalProfile.profileName= current.getString("name");
        GlobalProfile.profileInterests = current.getString("interests");
        GlobalProfile.languagesKnown = current.getString("languagesKnown");
        GlobalProfile.languagesLearning = current.getString("languagesLearning");
        GlobalProfile.bitmapString = current.getString("photo");

        if(current.getString("name") != null) {
            System.out.println("SUCCESSFUL");
        }else{
            System.out.println("UNSUCCESSFUL");
        }
    }
}
