package com.example.pc.run;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Searchengine_act extends AppCompatActivity {

    SearchView searchEngine;
    TextView searchResults;
    String url = "http://k1.esy.es/search-db.php";
    JSONArray profileNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchengine_act);

        searchResults = (TextView)findViewById(R.id.dataView);
        searchEngine = (SearchView)findViewById(R.id.searchView);
        searchEngine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                System.out.println("making params");
                Map<String, String> parameters = new HashMap<>();
                parameters.put("info", query);
                System.out.println("params made " + query);

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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }



    private void processResult(JSONObject input) throws JSONException, InterruptedException {
        profileNames = input.getJSONArray("result");
        Log.d("PROFILE NAMES:", profileNames.toString());

        ArrayList<String> information = new ArrayList<>();

        for(int i=0; i<profileNames.length(); i++) {
            JSONObject current = profileNames.getJSONObject(i);
            if(current.getString("passed").equals("true")){

                information.add(i, "Name:" + current.getString("name") + " Languages Known: " + current.getString("languagesKnown")
                        + "\n" + "Languages Learning:" + current.getString("languagesLearning") + " Interests: " + current.getString("interests"));
            }
        }
        searchResults.setText("");
        for(int i=0; i<information.size(); i++){
            searchResults.append(information.get(i) + "\n");
        }

    }
}
