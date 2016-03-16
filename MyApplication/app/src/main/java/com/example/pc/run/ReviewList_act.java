package com.example.pc.run;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Adapters.ReviewListAdapter;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Review;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewList_act extends AppCompatActivity {

    String email;
    String url = "http://t-simkus.com/run/pullReviews.php";
    ListView teachingReviews;
    ListView learningReviews;
    ArrayList<Review> teachingReviewList;
    ArrayList<Review> learningReviewList;
    ReviewListAdapter teachingAdapter;
    ReviewListAdapter learningAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list_act);

        email = getIntent().getStringExtra("email");
        Log.d("REVIEW EMAIL:", email);

        teachingReviews = (ListView)findViewById(R.id.reviewListTeaching);
        learningReviews = (ListView)findViewById(R.id.reviewListLearning);
        teachingReviewList = new ArrayList<>();
        learningReviewList = new ArrayList<>();
        getReviews();

    }

    public ReviewList_act(){

    }

    public ReviewList_act(String email){
        this.email = email;
    }

    public void getReviews(){

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

    private void processResult(JSONObject input) throws JSONException, InterruptedException {

        JSONArray reviews = input.getJSONArray("result");
        for (int i=0; i<reviews.length(); i++){
            JSONObject current = reviews.getJSONObject(i);

            Review review = new Review(current.getString("rating"), current.getString("review"),
                    current.getString("reviewer"), current.getString("type"));

            if(current.getString("type").equals("Teacher")){
                teachingReviewList.add(review);
            }
            if(current.getString("type").equals("Learner")){
                learningReviewList.add(review);
            }
        }

        teachingAdapter = new ReviewListAdapter(this, teachingReviewList);
        teachingReviews.setAdapter(teachingAdapter);
        learningAdapter = new ReviewListAdapter(this, learningReviewList);
        learningReviews.setAdapter(learningAdapter);

    }


}
