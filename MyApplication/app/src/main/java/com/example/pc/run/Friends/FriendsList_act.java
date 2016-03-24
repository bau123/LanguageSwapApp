package com.example.pc.run.Friends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Adapters.FriendListAdapter;
import com.example.pc.run.Adapters.FriendRequestAdapter;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.R;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendsList_act extends AppCompatActivity{

    public ListView friendsList;
    public ListView friendsReqList;
    public ArrayList<Profile> friendList;
    public ArrayList<Profile> friendReqList;
    FriendListAdapter friendListAdapter;
    FriendRequestAdapter friendReqAdapter;
    String url = "http://t-simkus.com/run/getFriendRequests.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list_act);

        friendList = new ArrayList<>();
        friendReqList = new ArrayList<>();
        friendsReqList = (ListView)findViewById(R.id.friendReqList);
        friendsList = (ListView)findViewById(R.id.friendsList);

        getFriendRequests();

    }

    public void getFriendRequests(){
        System.out.println("Making params");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("email", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);
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
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void processResult(JSONObject input) throws JSONException, InterruptedException {

        JSONArray profileNames = input.getJSONArray("result");
        Log.d("PROFILE NAMES:", profileNames.toString());

        for (int i = 0; i < profileNames.length(); i++) {
            JSONObject current = profileNames.getJSONObject(i);
            Log.d("PROFILE DETAILS:", current.getString("email") + " " + current.getString("boolean")
                    + " " + current.getString("name"));

            Profile profile = new Profile(current.getString("email"),current.getString("name"), current.getString("languagesKnown"),
                    current.getString("languagesLearning"), current.getString("interests"));

            if (current.getString("photo") != null) {
                profile.setProfilePicture(current.getString("photo"));
            }

            if (current.getString("boolean").equals("false")) {
                friendReqList.add(profile);
                friendReqAdapter = new FriendRequestAdapter(FriendsList_act.this, friendReqList);
                friendsReqList.setAdapter(friendReqAdapter);
            } else {
                friendList.add(profile);
                friendListAdapter = new FriendListAdapter(FriendsList_act.this, friendList);
                friendsList.setAdapter(friendListAdapter);
            }
        }

        System.out.println("Sending data");

    }

    public void basicRefresh(){
        friendListAdapter.notifyDataSetChanged();
        friendReqAdapter.notifyDataSetChanged();
    }

    public void fullRefresh(){
        getFriendRequests();
    }

}
