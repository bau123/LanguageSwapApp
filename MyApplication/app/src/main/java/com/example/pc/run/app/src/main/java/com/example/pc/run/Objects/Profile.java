package com.example.pc.run.Objects;


import java.io.Serializable;

public class Profile implements Serializable{

    private String name;
    private String email;
    private String languagesKnown;
    private String languagesLearning;
    private String interests;

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

    public void updateName(String name){
        this.name = name;
    }

    public void updateLanguagesKnown(String languagesKnown){this.languagesKnown = languagesKnown;}

    public void updateLanguagesLearning(String languagesLearning){this.languagesLearning = languagesLearning;}

    public void updateInterests(String interests){
        this.interests = interests;
    }

    public void setEmail(String email) {this.email = email;}

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


}
