package com.didlink.rest.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private long chid;
    private long uid;

    private byte type;
    private byte status;
    private String name;
    private String description;

    private Channel parent;
    private List<Channel> children;
    private List<Contact> contacts;
    private List<Topic> topics;

    public Channel() {
        this.children = new ArrayList<>();
        this.contacts = new ArrayList<>();
        this.topics = new ArrayList<>();
    }

    public long getChid() {
        return chid;
    }

    public void setChid(long chid) {
        this.chid = chid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Channel getParent() {
        return parent;
    }
    public void setParent(Channel parent) {
        this.parent = parent;
    }

    public void addChild(Channel child) {
        this.children.add(child);
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
    }

    public void addTopic(Topic topic) {
        this.topics.add(topic);
    }

    public String toString() {
        return  "chid: " + chid + "\n" +
                "uid: " + uid + "\n" +
                "type: " + type + "\n" +
                "status: " + status + "\n" +
                "name: " + name + "\n" +
                "description: " + description;
    }

}