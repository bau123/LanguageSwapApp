package com.example.pc.run;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Adapters.FriendListTab;
import com.example.pc.run.Adapters.ReviewListAdapter;
import com.example.pc.run.Adapters.ReviewListTab;
import com.example.pc.run.FriendTabs.Review_learner;
import com.example.pc.run.FriendTabs.Review_teacher;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list_act);

        email = getIntent().getStringExtra("email");

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_layoutr);
        tabLayout.addTab(tabLayout.newTab().setText("Teaching Reviews"));
        tabLayout.addTab(tabLayout.newTab().setText("Learning Reviews"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.rpager);
        final ReviewListTab adapter = new ReviewListTab
                (this.getSupportFragmentManager(), tabLayout.getTabCount(), email);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public ReviewList_act(){

    }

    public ReviewList_act(String email){
        this.email = email;
    }


}
