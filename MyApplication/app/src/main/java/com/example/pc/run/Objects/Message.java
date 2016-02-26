package com.example.pc.run.Objects;


import java.io.Serializable;

public class Message implements Serializable {

    String email, message, messageId, dateCreated, name;

    public Message(){
    }

    public Message(String email, String message,String messageId, String dateCreated, String name){
        this.email = email;
        this.message = message;
        this.dateCreated = dateCreated;
        this.messageId = messageId;
        this.name = name;
    }

    public String getName(){return name;}

    public String getMessageId() {return messageId;}

    public String getEmail() {return email;}

    public String getMessage() {return message;}

    public String getDateCreated() {return dateCreated;}

    public void setEmail(String email) {this.email = email;}

    public void setMessage(String message) {this.message = message;}

    public void setMessageId(String messageId) {this.messageId = messageId;}

    public void setDateCreated(String dateCreated) {this.dateCreated = dateCreated;}

    public void setName(String name){this.name = name;}

}
