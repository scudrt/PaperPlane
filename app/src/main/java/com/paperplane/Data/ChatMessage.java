package com.paperplane.Data;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class ChatMessage{
    public ChatMessage(){
        this.sendTime = new Date().toString();
    }
    public ChatMessage(String message){
        this.sendTime = new Date().toString();
        this.message = message;
    }
    public ChatMessage(JSONObject json){
        this.sendTime = json.getString("sendTime");
        this.senderID = json.getString("senderID");
        this.receiverID = json.getString("receiverID");
        this.message = json.getString("message");
    }
    
    /**
     * PUBLIC
     * normal getters and setters
     */
    public void setMessage(String message){
        this.message = message;
    }
    public void setSenderID(String senderID){
        this.senderID = senderID;
    }
    public void setReceiverID(String receiverID){
        this.receiverID = receiverID;
    }
    public void setSendTime(String sendTime){
        this.sendTime = sendTime;
    }

    public String getMessage(){
        return this.message;
    }
    public String getReceiverID(){
        return this.receiverID;
    }
    public String getSenderID(){
        return this.senderID;
    }
    public String getSendTime(){
        return this.sendTime;
    }

    /**
     * PRIVATE
     */
    private String message;
    private String senderID;
    private String receiverID;
    private String sendTime;
}
