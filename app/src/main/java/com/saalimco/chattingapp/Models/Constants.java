package com.saalimco.chattingapp.Models;

import java.util.HashMap;

public class Constants {
    public static final String REMOTE_MESSAGE_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MESSAGE_CONTENT_TYPE="Content-Type";
    public static final String REMOTE_MSG_DATA="data";
    public static final String REMOTE_MSG_REGISTRATION_IDS="registration_ids";
    public static HashMap<String,String> remoteMsgHeaders=null;
    public static HashMap<String,String> getRemoteMsgHeaders(){
        if(remoteMsgHeaders==null){
            remoteMsgHeaders=new HashMap<>();
            remoteMsgHeaders.put(REMOTE_MESSAGE_AUTHORIZATION, "key=AAAACut3PUU:APA91bGMsJJsrt7EReeUMobMlyIeVQKZQFPCJE1LnkGxARTlaPwvfF9f_W2xkF9wq7XtbJgEtbA2xtkHgTy1uKU-lKp8F7X4ZcAjE9q957oFo2pfp-OrmVl3eVVFSbEC8UZ7bb9BR_vi");
            remoteMsgHeaders.put(REMOTE_MESSAGE_CONTENT_TYPE, "application/json");
        }
        return remoteMsgHeaders;
    }
}
