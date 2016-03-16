package com.example.pc.run.Objects;


import java.io.Serializable;

public class ChatRoom implements Serializable {

    private String id, name, lastMessage, timeStamp, image, email, otherUser;
    private int unreadCount;

    public ChatRoom() {
    }

    public ChatRoom(String id, String name, String email, String lastMessage, String timestamp, String otherUser, int unreadCount, String image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.lastMessage = lastMessage;
        this.timeStamp = timestamp;
        this.otherUser = otherUser;
        this.unreadCount = unreadCount;
        this.image = image;
    }

    public String getOtherUser() {return otherUser;}

    public void setOtherUser(String otherUser) {this.otherUser = otherUser;}

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {return email;}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getImage() {
        return image;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
