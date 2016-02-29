package com.example.pc.run;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateProfile_Act extends AppCompatActivity {

    EditText name;
    EditText interests;
    EditText languagesKnown;
    EditText languagesLearning;
    String email;
    String url = "http://192.168.0.11/Run/insert-profile-db.php";
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile_);

        email = getIntent().getStringExtra("email");

        name = (EditText)findViewById(R.id.nameEdit);
        interests = (EditText)findViewById(R.id.interestsEdit);
        languagesKnown = (EditText)findViewById(R.id.langKnownEdit);
        languagesLearning = (EditText)findViewById(R.id.langLearningEdit);
    }

    public void addProfileInfo(View view) {
        System.out.println("making params");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("name", name.getText().toString());
        parameters.put("languagesKnown", languagesKnown.getText().toString());
        parameters.put("languagesLearning", languagesLearning.getText().toString());
        parameters.put("interests", interests.getText().toString());
        System.out.println("params made");
        Log.d("Email Passed:", email);

        profile = new Profile(name.getText().toString(), languagesKnown.getText().toString(),
                languagesLearning.getText().toString(), interests.getText().toString());

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

    //Determines whether input is valid.
    private void processResult(JSONObject input) throws InterruptedException {
        String result = "";
        try{
            result = input.getString("message");
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (result.equals("success")) {
            ApplicationSingleton.getInstance().getPrefManager().storeProfile(profile); //STORE PROFILE WITH THIS
            Intent intent = new Intent(this, App_act.class);
            startActivity(intent);
        } else if (result.equals("failure")) {
            Toast.makeText(getApplicationContext(), "Adding Profile info failed", Toast.LENGTH_LONG).show();
        }

    }
}
