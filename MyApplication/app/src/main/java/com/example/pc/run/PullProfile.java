package com.example.pc.run;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.pc.run.Global.GlobalProfile;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class PullProfile {

    JSONArray userInfo;
    String email;
    String url = "http://t-simkus.com/run/pullProfile.php";

    public PullProfile() {
        this.email = ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0];
        ;
    }

    public void pullInformation() {

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("email", email);
/*
        // Execute Synchronous volley request
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, future, future) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", email);
                return parameters;
            }
        };
        ApplicationSingleton.getInstance().addToRequestQueue(request);

        try {
            JSONObject response = null;
            while (response == null) {
                try {
                    response = future.get(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            try {
                processResult(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("timeout in pull profile");
            e.printStackTrace();
        }
*/
    }

    private void processResult(JSONObject input) throws JSONException {

        userInfo = input.getJSONArray("result");
        JSONObject current = userInfo.getJSONObject(0);

        Log.d("NAME:", current.getString("name"));
        Log.d("Interests::", current.getString("interests"));
        Log.d("Languages Known:", current.getString("languagesKnown"));
        Log.d("Languages Learning:", current.getString("languagesLearning"));
        Log.d("BITMAP STRING:", current.getString("photo"));

        Profile profile = new Profile(current.getString("name"), current.getString("languagesKnown"),
                current.getString("languagesLearning"), current.getString("interests"));

        ApplicationSingleton.getInstance().getPrefManager().storeProfile(profile);
        ApplicationSingleton.getInstance().getPrefManager().storeProfileImage("photo");

        if (current.getString("name") != null) {
            System.out.println("SUCCESSFUL");
        } else {
            System.out.println("UNSUCCESSFUL");
        }
    }
}
