package com.saalimco.chattingapp.Models;


import java.io.Serializable;

public class Users implements Serializable {
    String username, password, fcmtoken, email, userid, profilepic, lastmessage;
    Integer availability;
    public Users(){

    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public Users(String username, String email, String profilepic, String fcmtoken, String userid){

    }
    public Users(String username, String password, String email, String profilepic) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profilepic=profilepic;
    }

    public Users(String username, String password, String fcmtoken, String email, String userid, String profilepic, String lastmessage) {
        this.username = username;
        this.password = password;
        this.fcmtoken = fcmtoken;
        this.email = email;
        this.userid = userid;
        this.profilepic = profilepic;
        this.lastmessage = lastmessage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFcmtoken() {
        return fcmtoken;
    }

    public void setFcmtoken(String fcmtoken) {
        this.fcmtoken = fcmtoken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }
}
