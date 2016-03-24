package com.example.pc.run;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RequestModification {

    public String requestEmail;
    public String toEmail;
    public String decision;
    public String url = "http://t-simkus.com/run/friendReqResult.php";

    public RequestModification(String requestEmail, String toEmail, String decision){

        this.requestEmail = requestEmail;
        this.toEmail = toEmail;
        this.decision = decision;

        makeRequest();
    }

    public void makeRequest(){

        Log.d("Making Parameters", toEmail + " " + requestEmail + " " + decision);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("requestEmail", requestEmail);
        parameters.put("toEmail", toEmail);
        parameters.put("decision", decision);

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

    private void processResult(JSONObject input) throws InterruptedException {
        String result ="";
        try{
            result = input.getString("message");
        }catch (Exception e){
            e.printStackTrace();
        }
        if (result.equals("success")) {
            System.out.println("SUCCESS");

        } else if (result.equals("failure")) {
            System.out.println("FAILURE");

        }
    }
}
