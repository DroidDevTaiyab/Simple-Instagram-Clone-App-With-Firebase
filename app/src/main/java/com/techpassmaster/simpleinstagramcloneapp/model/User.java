package com.techpassmaster.simpleinstagramcloneapp.model;

/**
 * Created by Techpass Master on 8-April-21.
 * Website - https://techpassmaster.com/
 * Email id - hello@techpassmaster.com
 */

public class User {

    private String id;
    private String userName;
    private String fullName;
    private String profileImageUrl;

    public User() {
    }

    public User(String id, String userName, String fullName, String profileImageUrl) {
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
        this.profileImageUrl = profileImageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
