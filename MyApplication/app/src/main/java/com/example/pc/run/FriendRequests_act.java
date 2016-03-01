package com.example.pc.run;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.pc.run.Adapters.FriendListAdapter;
import com.example.pc.run.Adapters.FriendRequestAdapter;
import com.example.pc.run.Objects.Profile;

import java.util.ArrayList;

public class FriendRequests_act extends AppCompatActivity {

    public ListView friendRequests;
    Button acceptButton;
    Button rejectButton;
    public static ArrayList<Profile> fqList;
    FriendRequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests_act);

        fqList = new ArrayList<>();
        friendRequests = (ListView)findViewById(R.id.fqList);

        Profile testProfile = new Profile("Jack M.", "jack.m@kcl.ac.uk");
        Profile testProfile2 = new Profile("Emily J.", "emily.j@kcl.ac.uk");

        fqList.add(testProfile); fqList.add(testProfile2);
        adapter = new FriendRequestAdapter(FriendRequests_act.this, fqList);

        friendRequests.setAdapter(adapter);
    }
}
