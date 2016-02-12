package com.example.pc.run;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pc.run.Network_Utils.AppController;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarException;

public class Register_act extends AppCompatActivity {

    private EditText pass, email;
    String url = "http://k1.esy.es/insert-db.php";
    String passEmail = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_act);
        pass = (EditText) findViewById(R.id.pass);
        email = (EditText) findViewById(R.id.email);
    }

    public void register(View view) {
        System.out.println("making params");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email.getText().toString());
        parameters.put("password", pass.getText().toString());
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
        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }

    //Determines whether input is valid.
    private void processResult(JSONObject input) {
        String result = "";
        try{
            result = input.getString("message");
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (result.equals("success")) {
            ApplicationSingleton.getInstance().getPrefManager().storeAuthentication(email.getText().toString(), pass.getText().toString());
            Intent intent = new Intent(Register_act.this, CreateProfile_Act.class);
            intent.putExtra("email", passEmail);
            startActivity(intent);
            finish();
        } else if (result.equals("failure")) {
            Toast.makeText(getApplicationContext(), "Sorry email is already taken", Toast.LENGTH_LONG).show();
        }

    }

}
