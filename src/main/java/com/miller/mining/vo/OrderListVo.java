package com.miller.mining.vo;

public class OrderListVo {

    private String username;
    private String money;

    public OrderListVo() {
    }

    public OrderListVo(String username, String money) {
        this.username = username;
        this.money = money;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
