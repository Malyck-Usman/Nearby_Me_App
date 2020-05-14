package com.example.nearbyme.Model;

public class User {
    private String user_name;
    private String phone_no;
    private String e_mail;
    private String password;

    public User() {
    }

    public User(String user_name, String phone_no, String e_mail, String password) {
        this.user_name = user_name;
        this.phone_no = phone_no;
        this.e_mail = e_mail;
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }


    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
