package com.example.pc.run.Objects;


import java.io.Serializable;

public class Message implements Serializable {

    String email, message, dateCreated;
    Profile profile;

    public Message(){
    }

    public Message(String email, String message, String dateCreated, Profile profile){
        this.email = email;
        this.message = message;
        this.dateCreated = dateCreated;
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
