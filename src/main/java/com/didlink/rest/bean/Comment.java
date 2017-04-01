package com.didlink.rest.bean;

import java.io.Serializable;

public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private String author;
    private String text;

    public Comment() {
    }

    public Comment(final String author, final String text) {
        this.author = author;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public void setAuthor(String author) { this.author = author; }

    public void setText(String text) { this.text = text; }
}
