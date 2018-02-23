package com.miller.mining.vo;

public class UserPermitionVo {

    private String username;
    private String phoneID;

    public UserPermitionVo() {
    }

    public UserPermitionVo(String username, String phoneID) {
        this.username = username;
        this.phoneID = phoneID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneID() {
        return phoneID;
    }

    public void setPhoneID(String phoneID) {
        this.phoneID = phoneID;
    }
}
