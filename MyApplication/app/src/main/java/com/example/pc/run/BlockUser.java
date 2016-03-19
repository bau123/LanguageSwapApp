package com.example.pc.run;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.FriendTabs.*;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BlockUser extends AppCompatActivity {

    String email;
    String reportEmail;
    String description;
    String type;
    EditText comment;
    Spinner reportType;
    CheckBox block;
    Boolean blocked;
    String url = "http://t-simkus.com/run/reportUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_user);

        email = getIntent().getStringExtra("myEmail");
        reportEmail = getIntent().getStringExtra("userEmail");
        comment = (EditText)findViewById(R.id.commentThis);
        reportType = (Spinner)findViewById(R.id.reasonSpinner);
        block = (CheckBox)findViewById(R.id.blockCheckbox);

    }

    public void submitReport(View v){
        description = comment.getText().toString();
        type = reportType.getSelectedItem().toString();
        blocked = block.isChecked();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);parameters.put("block", blocked.toString());parameters.put("type", type);
        parameters.put("comment", description);parameters.put("reportEmail", reportEmail);

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
        String result = "";
        try{
            result = input.getString("message");
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (result.equals("success")) {
            Intent intent = new Intent(BlockUser.this, com.example.pc.run.FriendTabs.FriendsList_act.class);
            startActivity(intent);
        } else if (result.equals("failure")) {
            Toast.makeText(getApplicationContext(), "You have already reported this user...", Toast.LENGTH_LONG).show();
        }

    }
}
