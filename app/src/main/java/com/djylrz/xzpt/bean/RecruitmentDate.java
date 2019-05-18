package com.djylrz.xzpt.bean;

/**
 * @Description: 招聘会实体
 * @Title: RecruitmentDate
 * @ProjectName XZPT-Android
 * @Author mingjun
 * @Date 2019/5/18上午 12:29
 */
public class RecruitmentDate {
    private String id;
    private String title;
    private String location;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minutes;
    private String url;

    public RecruitmentDate(String id, String title, String location, String year, String month, String day, String hour, String minutes, String url) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minutes = minutes;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
