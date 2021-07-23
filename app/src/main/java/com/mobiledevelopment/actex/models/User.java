package com.mobiledevelopment.actex.models;

import com.mobiledevelopment.actex.models.account.Account;
import com.mobiledevelopment.actex.models.network.Session;
import com.mobiledevelopment.actex.models.network.Token;

public class User {
    private Token RequestToken;
    private static String apiKey = "ef9122e6c53e6efc123cb9f1fdf478b8";
    private Session sessionToken;
    private static User user = new User();
    private String username;
    private String password;
    private Account account;
    public static enum LoginSuccess {
        FAILED, IN_PROGRESS, SUCCEED
    }
    private Boolean loggedIn = false;
    private LoginSuccess loginSuccess;
    public void setLoggedIn(Boolean loggedIn){
        this.loggedIn = loggedIn;
    }
    public Boolean getLoggedIn(){
        return loggedIn;
    }
    public LoginSuccess isLoginSucceed() {
        return loginSuccess;
    }

    public void setLoginSuccess(LoginSuccess loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public String getUsername() {
        return username;
    }

    public void setAuthDetails(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    private User() {
    }
    public void setAccount(Account account){
        this.account = account;
    }
    public Account getAccount(){
        return this.account;
    }
    public Token getRequestToken() {
        return RequestToken;
    }

    public void setRequestToken(Token requestToken) {
        RequestToken = requestToken;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static void setApiKey(String apiKey) {
        User.apiKey = apiKey;
    }

    public Session getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(Session sessionToken) {
        this.sessionToken = sessionToken;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        User.user = user;
    }

    public static User getInstance() {
        return user;
    }

}
