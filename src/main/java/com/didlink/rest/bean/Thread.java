package com.didlink.rest.bean;

import java.io.Serializable;

public class Thread implements Serializable {
    private static final long serialVersionUID = 1L;

    private long thid;
    private long tid;
    private long uid;

    private byte status;
    private String content;
    private long lastupdate;
    private long createtime;

    public long getThid() {
        return thid;
    }
    public void setThid(long thid) {
        this.thid = thid;
    }

    public long getTid() {
        return tid;
    }
    public void setTid(long tid) {
        this.tid = tid;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {
        return  "thid: " + thid + "\n" +
                "tid: " + tid + "\n" +
                "uid: " + uid + "\n" +
                "status: " + status + "\n" +
                "content: " + content;
    }

}
