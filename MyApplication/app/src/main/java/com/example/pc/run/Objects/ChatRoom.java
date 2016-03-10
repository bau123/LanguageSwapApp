package com.example.pc.run.Objects;


import java.io.Serializable;

public class ChatRoom implements Serializable {

    String id, name, lastMessage, timeStamp, image;
    int unreadCount;

    public ChatRoom() {
    }

    public ChatRoom(String id, String name, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timeStamp = timestamp;
        this.unreadCount = unreadCount;
    }

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
