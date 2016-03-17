package com.example.pc.run.FriendTabs;

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

/**
 * Created by Joss on 14/03/2016.
 */
public class FriendReq_act extends Fragment {

    public ListView friendsReqList;
    public ArrayList<Profile> friendReqList;
    FriendRequestAdapter friendReqAdapter;
    String url = "http://t-simkus.com/run/getFriendRequests.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friendReqList = new ArrayList<>();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_friend_req_act, container, false);

        friendsReqList = (ListView) v.findViewById(R.id.friendReqList);

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
            }
        }

        friendReqAdapter = new FriendRequestAdapter(getActivity(), friendReqList);
        friendsReqList.setAdapter(friendReqAdapter);

        System.out.println("Sending data");

    }

    public void fullRefresh(){
        getFriendRequests();
    }
}
