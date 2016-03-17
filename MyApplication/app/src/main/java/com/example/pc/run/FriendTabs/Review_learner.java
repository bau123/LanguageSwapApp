package com.example.pc.run.FriendTabs;

/**
 * Created by Joss on 17/03/2016.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Adapters.ReviewListAdapter;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Review;
import com.example.pc.run.R;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joss on 17/03/2016.
 */
public class Review_learner extends Fragment {


    String email;
    String url = "http://t-simkus.com/run/pullReviews.php";
    ListView learningReviews;
    ArrayList<Review> learningReviewList;
    ReviewListAdapter learningAdapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.review_learner_frag, container, false);

        email = getArguments().getString("email");
        Log.d("REVIEW EMAIL:", email);

        learningReviews = (ListView)v.findViewById(R.id.learnerList);
        learningReviewList = new ArrayList<>();
        getReviews();

        return v;

    }

    public Review_learner(){

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
                    current.getString("revieweremail"), current.getString("type"));

            if(current.getString("type").equals("Learner")){
                learningReviewList.add(review);
            }
        }
        learningAdapter = new ReviewListAdapter(getActivity(), learningReviewList);
        learningReviews.setAdapter(learningAdapter);

    }
}
