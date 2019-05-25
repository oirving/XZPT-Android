package com.djylrz.xzpt.utils;

/*
{
        "id": 4,        "image": "/media/images/app/home/2018/03/09/Big-data-free-courses-768x528.jpg"
    },
        "title": "\u6d4b\u8bd52",
        "date": "2018-03-09 17:42:37",

 */
public class ImageInfo {
    private int id;
    private String title;
    private String date;
    private String image;
    private String url;

    public ImageInfo() {
    }

    public ImageInfo(int id, String title, String date, String image, String url) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.image = image;
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }
}
