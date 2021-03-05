package com.example.car_app;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String Re_Password;

    public User(String firstName, String lastName, String email, String password, String re_Password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        Re_Password = re_Password;
    }
}
