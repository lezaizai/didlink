package com.didlink.rest.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private long chid;

    private byte type;
    private byte status;
    private String name;
    private String description;
    private int contacts_num;

    private Contact owner;
    private Channel parent;
    private List<Channel> children;
    private List<Contact> contacts;
    private List<Topic> topics;


    public Channel() {
        this.children = new ArrayList<>();
        this.contacts = new ArrayList<>();
        this.topics = new ArrayList<>();
        this.contacts_num = 0;
    }

    public long getChid() {
        return chid;
    }

    public void setChid(long chid) {
        this.chid = chid;
    }

    public Contact getOwner() {
        return owner;
    }

    public void setOwner(Contact owner) {
        this.owner = owner;
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

    public int getContacts_num() {
        return contacts_num;
    }

    public void setContacts_num(int contacts_num) {
        this.contacts_num = contacts_num;
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

    public List<Contact> getContacts() {
        return contacts;
    }

    public void addContact(Contact contact) {
        this.contacts_num++;
        this.contacts.add(contact);
    }

    public void addTopic(Topic topic) {
        this.topics.add(topic);
    }

    public String toString() {
        String contactsStr = "";
        for (int i = 0; i < this.contacts_num; i++) {
            contactsStr += "contact id: " + getContacts().get(i).getUid() + "\n" +
                    "contact nickname: " + getContacts().get(i).getNickname();
        }

        return  "chid: " + chid + "\n" +
                "uid: " + owner.getUid() + "\n" +
                "type: " + type + "\n" +
                "status: " + status + "\n" +
                "name: " + name + "\n" +
                "description: " + description + "\n" +
                contactsStr;
    }

}
