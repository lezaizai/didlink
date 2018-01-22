package com.didlink.rest.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Topic implements Serializable {
    private static final long serialVersionUID = 1L;

    private long tid;
    private long chid;
    private long uid;

    private byte status;
    private String subject;
    private long lastupdate;
    private long createtime;
    private List<Thread> threads;

    public Topic() {
        threads = new ArrayList<>();
    }

    public long getTid() {
        return tid;
    }
    public void setTid(long tid) {
        this.tid = tid;
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

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void addThread(Thread thread) {
        this.threads.add(thread);
    }
    public List<Thread> getThreads() {
        return threads;
    }

    public String toString() {
        return  "tid: " + tid + "\n" +
                "chid: " + chid + "\n" +
                "uid: " + uid + "\n" +
                "subject: " + subject;
    }

}
