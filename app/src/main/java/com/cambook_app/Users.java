package com.cambook_app;

public class Users {
    String fname;
    String sname;
    String username;
    String dob;
    String gender;
    String email ;
    String password;

    public Users(String fname, String sname, String username, String dob, String gender, String email, String password) {
        this.fname = fname;
        this.sname = sname;
        this.username = username;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public String getSname() {
        return sname;
    }

    public String getUsername() {
        return username;
    }

    public String getDob() {
        return dob;
    }
    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
