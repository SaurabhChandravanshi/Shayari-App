package com.i3developer.shayari;

public class User {
    private String name;
    private String email;
    private String dob;
    private String picUrl;

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

    public User(String name, String picUrl) {
        this.name = name;
        this.picUrl = picUrl;
    }

    public User(String name, String email, String dob) {
        this.name = name;
        this.email = email;
        this.dob = dob;
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
}
