package com.example.pc.run.Search;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.R;
import org.json.JSONObject;

public class Profile_frag extends Fragment {

    TextView name, languagesKnown, languagesLearning, interests;
    ImageView profileImage;
    private Profile profile;
    private String data;

    public Profile_frag(){
        profile =  new Profile();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        data = getArguments().getString("data");
        View v = inflater.inflate(R.layout.activity_profile_frag, container, false);


        name = (TextView) v.findViewById(R.id.nameField);
        languagesKnown = (TextView) v.findViewById(R.id.langKnownField);
        languagesLearning = (TextView) v.findViewById(R.id.langLearningField);
        interests = (TextView) v.findViewById(R.id.interestsField);
        profileImage = (ImageView)v.findViewById(R.id.profileImageSwipe);

        try{
            JSONObject obj = new JSONObject(data);
            profile = new Profile(obj.getString("name"), obj.getString("languagesKnown"), obj.getString("languagesLearning"), obj.getString("interests"));
            if(obj.getString("photo") != null){
                profile.setProfilePicture(obj.getString("photo"));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        name.setText("Name: " + profile.getName());
        languagesKnown.setText("Languages Known: " + profile.getLanguagesKnown());
        languagesLearning.setText("Languages Learning: " + profile.getLanguagesLearning());
        interests.setText("Interests: " + profile.getInterests());

        if(profile.getProfilePicture() != null){
            profileImage.setImageBitmap(profile.getProfilePicture());
        }

        System.out.println("new fragment made");
        return v;
    }

    public static Profile_frag newInstance(JSONObject input){
        Profile_frag fragment = new Profile_frag();
        Bundle data = new Bundle();
        try{
            data.putString("data", input.toString());
        }catch(Exception e ){
            e.printStackTrace();
        }

        fragment.setArguments(data);
        return fragment;
    }

}
