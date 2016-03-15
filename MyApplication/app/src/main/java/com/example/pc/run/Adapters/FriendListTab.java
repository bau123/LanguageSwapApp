package com.example.pc.run.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.pc.run.FriendTabs.FriendReq_act;
import com.example.pc.run.FriendTabs.FriendsList_act;

/**
 * Created by Joss on 14/03/2016.
 */
public class FriendListTab extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public FriendListTab(FragmentManager fm, int NumOfTabs){
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0:
                FriendsList_act friendList = new FriendsList_act();
                return friendList;
            case 1:
                FriendReq_act friendRequests = new FriendReq_act();
                return friendRequests;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
