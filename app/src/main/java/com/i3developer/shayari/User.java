package com.i3developer.shayari;

public class User {
    public String name,picUrl;

    public User(String name) {
        this.name = name;
    }

    public User(String name, String picUrl) {
        this.name = name;
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }
}
