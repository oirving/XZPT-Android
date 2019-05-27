package com.djylrz.xzpt.utils;


import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Auto-generator
 *
 * @author Murphy
 */

public class InterviewSkill implements Serializable {

    private Long id;
    private String title;
    private String content;
    private java.sql.Timestamp time;
    private String author;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
