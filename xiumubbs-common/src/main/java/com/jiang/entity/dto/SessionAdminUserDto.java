package com.jiang.entity.dto;

import java.io.Serializable;

public class SessionAdminUserDto implements Serializable {
    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
