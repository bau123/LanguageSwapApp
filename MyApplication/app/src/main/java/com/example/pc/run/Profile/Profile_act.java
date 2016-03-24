package com.example.pc.run.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.example.pc.run.Friends.BlockUser;
import com.example.pc.run.Global.GlobalMethds;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.R;
import com.example.pc.run.Friends.ReviewList_act;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile_act extends AppCompatActivity {

    private TextView name;
    private TextView interests;
    private TextView languagesKnown;
    private TextView languagesLearning;
    private ImageView profileImage;
    private Bitmap bitmap;
    private String email;
    private String url = "http://t-simkus.com/run/pullProfile.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_act);

        email = getIntent().getStringExtra("email");
        Log.d("PROFILE EMAIL:", email);

        //Initialising vaiables
        name = (TextView) findViewById(R.id.nameView);
        interests = (TextView) findViewById(R.id.interestsView);
        languagesKnown = (TextView) findViewById(R.id.languagesKnownView);
        languagesLearning = (TextView) findViewById(R.id.languagesLearningView);
        profileImage = (ImageView) findViewById(R.id.profileImageView);

        getProfileInfo();
    }

    /*
        Pull profile info with email as primary key
     */
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

    /*
        Processes database result and displays profile info
     */
    private void processResult(JSONObject input) throws JSONException {

        JSONArray userInfo = input.getJSONArray("result");
        JSONObject current = userInfo.getJSONObject(0);

        Log.d("NAME:", current.getString("name"));
        Log.d("Interests::", current.getString("interests"));
        Log.d("Languages Known:", current.getString("languagesKnown"));
        Log.d("Languages Learning:", current.getString("languagesLearning"));
        Log.d("BITMAP STRING:", current.getString("photo"));

        name.setText("Name: " + current.getString("name"));
        interests.setText("Interests:" + current.getString("interests"));
        languagesKnown.setText("Languages known:" + current.getString("languagesKnown"));
        languagesLearning.setText("Languages learning: " + current.getString("languagesLearning"));

        if(!current.getString("photo").equals("") && !current.getString("photo").equals("photo")) {
            System.out.println("photo changed");
            bitmap = GlobalMethds.stringToBitmap(current.getString("photo"));
            profileImage.setImageBitmap(bitmap);
        }

        if(current.getString("name") != null) {
            System.out.println("SUCCESSFUL");
        }else{
            System.out.println("UNSUCCESSFUL");
        }
    }

    /*
        Intent to ReviewList
     */
    public void seeReviews(View view){
        Intent intent = new Intent(Profile_act.this, ReviewList_act.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    /*
        Intent to BlockUser
     */
    public void reportUser(View view){
        Intent intent = new Intent(Profile_act.this, BlockUser.class);
        intent.putExtra("myEmail", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);
        intent.putExtra("userEmail", email);
        startActivity(intent);
    }

}
