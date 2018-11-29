package com.picode.gopoh.Model;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;
import java.util.HashMap;

public class Chat implements IMessage {
    private String id, roomId, message, senderId, senderName;
    private Date createdAt;

    public Chat(String id, String roomId, String message, String senderId, String senderName, Date createdAt) {
        this.id = id;
        this.roomId = roomId;
        this.message = message;
        this.senderId = senderId;
        this.senderName = senderName;
        this.createdAt = createdAt;
    }

    public Chat(String id, HashMap<String, Object> hashMap) {
        this(
                id,
                (String) hashMap.get("roomId"),
                (String) hashMap.get("message"),
                (String) hashMap.get("senderId"),
                (String) hashMap.get("senderName"),
                (Date) hashMap.get("time")
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getText() {
        return message;
    }

    @Override
    public IUser getUser() {
        return new User(senderId, senderName);
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
