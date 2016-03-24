package com.example.pc.run.Objects;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.pc.run.Global.GlobalMethds;

import java.io.Serializable;

public class Profile implements Serializable{

    private String name;
    private String email;
    private String languagesKnown;
    private String languagesLearning;
    private String interests;
    private Bitmap profilePicture;

    public Profile(){

    }

    public Profile(String name, String email){
        this.name = name;
        this.email = email;
    }

    public Profile(String name, String languagesKnown, String languagesLearning, String interests){
        this.name = name;
        this.languagesKnown = languagesKnown;
        this.languagesLearning = languagesLearning;
        this.interests = interests;
    }
    public Profile(String email, String name, String languagesKnown, String languagesLearning, String interests){
        this.email = email;
        this.name = name;
        this.languagesKnown = languagesKnown;
        this.languagesLearning = languagesLearning;
        this.interests = interests;
    }


    public Profile(String name, String languagesKnown, String languagesLearning, String interests, Bitmap bitmap){
        this.name = name;
        this.languagesKnown = languagesKnown;
        this.languagesLearning = languagesLearning;
        this.interests = interests;
        this.profilePicture = bitmap;
    }


    public void updateName(String name){
        this.name = name;
    }

    public void updateLanguagesKnown(String languagesKnown){this.languagesKnown = languagesKnown;}

    public void updateLanguagesLearning(String languagesLearning){this.languagesLearning = languagesLearning;}

    public void updateInterests(String interests){
        this.interests = interests;
    }

    public void setEmail(String email) {this.email = email;}

    public void setProfilePicture(Bitmap profilePicture){
        this.profilePicture = profilePicture;
    }

    public void setProfilePicture(String bitmapString){
        profilePicture = GlobalMethds.stringToBitmap(bitmapString);
    }

    public String getName() {
        return name;
    }

    public String getLanguagesKnown() {
        return languagesKnown;
    }

    public String getLanguagesLearning() {
        return languagesLearning;
    }

    public String getInterests() {
        return interests;
    }

    public String getEmail(){return email;}

    public Bitmap getProfilePicture(){
        return profilePicture;
    }


}
