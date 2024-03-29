package com.i3developer.shayari;

public class User {
    private String name;
    private String email;
    private String dob;
    private String picUrl;
    private String fcmToken;
    private String referralUid;

    public User(){

    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, String email, String dob, String picUrl) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.picUrl = picUrl;
    }

    public User(String name, String email, String dob, String picUrl, String fcmToken, String referralUid) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.picUrl = picUrl;
        this.fcmToken = fcmToken;
        this.referralUid = referralUid;
    }

    public User(String name, String email, String dob, String picUrl, String fcmToken) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.picUrl = picUrl;
        this.fcmToken = fcmToken;
    }

    public User(String name, String picUrl) {
        this.name = name;
        this.picUrl = picUrl;
    }

    public User(String name, String email, String dob) {
        this.name = name;
        this.email = email;
        this.dob = dob;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getReferralUid() {
        return referralUid;
    }

    public void setReferralUid(String referralUid) {
        this.referralUid = referralUid;
    }
}
