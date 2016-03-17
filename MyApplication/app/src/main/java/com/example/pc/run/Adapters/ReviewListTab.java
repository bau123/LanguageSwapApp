package com.example.pc.run.Adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.pc.run.FriendTabs.Review_learner;
import com.example.pc.run.FriendTabs.Review_teacher;

/**
 * Created by Joss on 14/03/2016.
 */
public class ReviewListTab extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    String email;

    public ReviewListTab(FragmentManager fm, int NumOfTabs, String email){
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.email = email;
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0:
                Review_teacher teacherReviews = new Review_teacher();
                Bundle x = new Bundle();
                x.putString("email", email);
                teacherReviews.setArguments(x);
                return teacherReviews;
            case 1:
                Review_learner learnerReviews = new Review_learner();
                Bundle y = new Bundle();
                y.putString("email", email);
                learnerReviews.setArguments(y);
                return learnerReviews;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}