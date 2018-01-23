package com.didlink.rest.bean;

import java.io.Serializable;

public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private long uid;

    private byte status;
    private boolean online;
    private String username;
    private String nickname;
    private String avatar;

    public Contact() {

    }

    public Contact(long uid, String username, String nickname) {
        this.uid = uid;
        this.username = username;
        this.nickname = nickname;
    }

    public Contact(long uid, boolean online, String username, String nickname) {
        this.uid = uid;
        this.online = online;
        this.username = username;
        this.nickname = nickname;
    }

    public Contact(long uid, byte status, boolean online, String username, String nickname, String avatar) {
        this.uid = uid;
        this.status = status;
        this.online = online;
        this.username = username;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String toString() {
        return  "uid: " + uid + "\n" +
                "status: " + status + "\n" +
                "username: " + username;
    }

}
