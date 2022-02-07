package com.i3developer.shayari;

public class VerifiedUsers {
    private String name,email,photoUrl,fcmToken;

    public VerifiedUsers() {
    }

    public VerifiedUsers(String name, String email, String photoUrl, String fcmToken) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.fcmToken = fcmToken;
    }

    public VerifiedUsers(String name, String email, String photoUrl) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
