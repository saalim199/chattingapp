package com.saalimco.chattingapp.Models;

public class ChatMessage {
    public String senderId, receiverId, message;
    Long dateTime;
    public String conversionId, conversionName, conversionImage;
    public ChatMessage(){

    }

    public ChatMessage(String senderId, String message) {
        this.senderId = senderId;
        this.message = message;
    }

    public ChatMessage(String senderId, String receiverId, String message, Long dateTime) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.dateTime = dateTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }
}
