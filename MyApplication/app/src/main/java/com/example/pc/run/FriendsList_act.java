package com.example.pc.run;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Adapters.FriendListAdapter;
import com.example.pc.run.Adapters.FriendRequestAdapter;
import com.example.pc.run.Global.GlobalProfile;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendsList_act extends Fragment {

    public ListView friendsList;
    public ListView friendsReqList;
    public ArrayList<Profile> friendList;
    public ArrayList<Profile> friendReqList;
    FriendListAdapter friendListAdapter;
    FriendRequestAdapter friendReqAdapter;
    String url = "http://t-simkus.com/run/getFriendRequests.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friendList = new ArrayList<>();
        friendReqList = new ArrayList<>();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_friends_list_act, container, false);

        friendsReqList = (ListView) v.findViewById(R.id.friendReqList);
        friendsList = (ListView) v.findViewById(R.id.friendsList);

        getFriendRequests();

        return v;
    }

    public void getFriendRequests(){
        System.out.println("Making params");
        Map<String, String> parameters = new HashMap<String, String>();
        Log.d("EMAIL:", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);
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
        JSONArray otherFriends = input.getJSONArray("result2");
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
            } else {
                friendList.add(profile);
            }
        }

        if(otherFriends.length() != 0){
            for(int i=0; i<otherFriends.length(); i++){
                JSONObject current = otherFriends.getJSONObject(i);
                Log.d("OTHER DETAILS:", current.getString("email") + " " + current.getString("boolean")
                        + " " + current.getString("name"));

                Profile profile = new Profile(current.getString("email"),current.getString("name"), current.getString("languagesKnown"),
                        current.getString("languagesLearning"), current.getString("interests"));

                friendList.add(profile);

            }
        }
        friendReqAdapter = new FriendRequestAdapter(getActivity(), friendReqList);
        friendsReqList.setAdapter(friendReqAdapter);
        friendListAdapter = new FriendListAdapter(getActivity(), friendList);
        friendsList.setAdapter(friendListAdapter);

        System.out.println("Sending data");

    }

    public void fullRefresh(){
        getFriendRequests();
    }

}
