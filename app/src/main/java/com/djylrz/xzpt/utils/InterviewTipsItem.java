package com.djylrz.xzpt.utils;

public class InterviewTipsItem {
    String title;
    String url;
    public InterviewTipsItem(String title,String url) {
        this.title = title;
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }
}
