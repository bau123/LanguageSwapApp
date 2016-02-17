package com.example.pc.run.Objects;


import java.io.Serializable;

public class Message implements Serializable {

    String email, message, messageId, dateCreated;
    Profile user;

    public Message(){
    }

    public Message(String email, String message,String messageId, String dateCreated, Profile user){
        this.email = email;
        this.message = message;
        this.dateCreated = dateCreated;
        this.messageId = messageId;
        this.user = user;
    }

    public Profile getUser(){return user;};
    public String getMessageId() {return messageId;}

    public String getEmail() {return email;}

    public String getMessage() {return message;}

    public String getDateCreated() {return dateCreated;}

    public void setEmail(String email) {this.email = email;}

    public void setMessage(String message) {this.message = message;}

    public void setMessageId(String messageId) {this.messageId = messageId;}

    public void setDateCreated(String dateCreated) {this.dateCreated = dateCreated;}

    public void setUser(Profile user){this.user = user;}

}
