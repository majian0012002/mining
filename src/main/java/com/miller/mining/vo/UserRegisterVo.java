package com.miller.mining.vo;

public class UserRegisterVo {

    private String username;
    private String password;
    private String phoneid;

    public UserRegisterVo() {
    }

    public UserRegisterVo(String username, String password, String phoneid) {
        this.username = username;
        this.password = password;
        this.phoneid = phoneid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneid() {
        return phoneid;
    }

    public void setPhoneid(String phoneid) {
        this.phoneid = phoneid;
    }
}
