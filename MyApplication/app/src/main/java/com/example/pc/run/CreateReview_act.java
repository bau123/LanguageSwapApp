package com.example.pc.run;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.FriendTabs.FriendsList_act;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateReview_act extends Activity {

    String email;
    String reviewerEmail;
    private static Integer checker;
    String description;
    String type;
    RatingBar rating;
    EditText comment;
    Spinner reviewType;
    String url = "http://t-simkus.com/run/createReview.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_act);

        reviewerEmail = getIntent().getStringExtra("myEmail");
        email = getIntent().getStringExtra("userEmail");
        Log.d("MAKING REVIEW BETWEEN:", email + " " + reviewerEmail);

        rating = (RatingBar)findViewById(R.id.ratingBar);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                checker = Math.round(Float.valueOf(ratingBar.getRating()));
            }
        });
        comment = (EditText)findViewById(R.id.commentEdit);
        reviewType = (Spinner)findViewById(R.id.typeSpinner);

    }


    public void submitReview(View v){
        description = comment.getText().toString();
        type = reviewType.getSelectedItem().toString();
        Log.d("TYPE:", type + " " + checker);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);parameters.put("rating", checker.toString());parameters.put("type", type);
        parameters.put("comment", description);parameters.put("reviewer", reviewerEmail);

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
            Intent intent = new Intent(CreateReview_act.this, FriendsList_act.class);
            startActivity(intent);
        } else if (result.equals("failure")) {
            Toast.makeText(getApplicationContext(), "Only one review is aloud per person...", Toast.LENGTH_LONG).show();
        }

    }
}
