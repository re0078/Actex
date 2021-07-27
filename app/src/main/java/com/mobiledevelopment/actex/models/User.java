package com.mobiledevelopment.actex.models;

import com.mobiledevelopment.actex.models.account.Account;
import com.mobiledevelopment.actex.models.network.Session;
import com.mobiledevelopment.actex.models.network.Token;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Token RequestToken;

    @Getter
    @Setter
    private static String apiKey = "ef9122e6c53e6efc123cb9f1fdf478b8";
    private Session sessionToken;

    @Getter
    private static User user = new User();
    private String username;
    private String password;
    private Account account;
    private Boolean loggedIn = false;
    private LoginSuccess loginSuccess;

    public enum LoginSuccess {
        FAILED, IN_PROGRESS, SUCCEED
    }

    public void setAuthDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static User getInstance() {
        return user;
    }
}
