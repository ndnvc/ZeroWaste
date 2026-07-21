package com.example.uasproject_zerowaste;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {

    @PrimaryKey
    @NonNull
    private String userId;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;

    // Constructor untuk Room
    public User(@NonNull String userId, String name, String email, String password, String phoneNumber, String address) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    @Ignore
    public User() {
        this.userId = "";
    }

    @Ignore
    public User(String userId, String name, String email, String password, String phoneNumber) {
        this(userId, name, email, password, phoneNumber, "");
    }

    @NonNull
    public String getUserId() { return userId; }
    public void setUserId(@NonNull String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}