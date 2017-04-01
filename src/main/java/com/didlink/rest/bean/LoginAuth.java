package com.didlink.rest.bean;

import java.io.Serializable;

public class LoginAuth  implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean status;
    private String username;
    private String password;
    private String token;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toString() {
        return "status: " + status + "\n" +
                "username: " + username + "\n" +
                "password: " + password + "\n" +
                "token: " + token;
    }

}
