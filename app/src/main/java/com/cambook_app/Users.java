package com.cambook_app;


public class Users {
    String fname;
    String username;
    String province;
    String city;
    String barangay;
    String dob;
    String gender;
    String email ;
    String password;

    public Users(){}

    public Users(String fname, String username,String province, String city, String barangay, String dob, String gender, String email, String password) {
        this.fname = fname;
        this.username = username;
        this.province = province;
        this.city = city;
        this.barangay = barangay;
        this.dob = dob;
        this.gender = gender;
        this.email = email;
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public String getUsername() {
        return username;
    }
    public String getProvince(){return province;}
    public String getCity(){return city;}
    public String getBarangay(){return barangay;}

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