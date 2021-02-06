package com.i3developer.shayari;

public class User {
    public String name;
    public String email;
    public String dob;

    public User(){

    }

    public User(String name) {
        this.name = name;
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
}
