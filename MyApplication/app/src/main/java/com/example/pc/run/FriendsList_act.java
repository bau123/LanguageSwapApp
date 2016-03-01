package com.example.pc.run;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.pc.run.Adapters.FriendListAdapter;
import com.example.pc.run.Objects.Profile;

import java.util.ArrayList;

public class FriendsList_act extends AppCompatActivity {

    public ListView friendsList;
    public static ArrayList<Profile> list;
    FriendListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list_act);

        list = new ArrayList<>();
        friendsList = (ListView)findViewById(R.id.friendsList);

        /*
            TODO: LOAD FRIEND REQUEST LIST PROFILES AND ADD TO list
         */
        Profile testProfile = new Profile("Jack M.", "jack.m@kcl.ac.uk");
        Profile testProfile2 = new Profile("Emily J.", "emily.j@kcl.ac.uk");

        list.add(testProfile);list.add(testProfile2);
        adapter = new FriendListAdapter(FriendsList_act.this, list);
        friendsList.setAdapter(adapter);

    }

    public void basicRefresh(){
        adapter.notifyDataSetChanged();
    }
}
