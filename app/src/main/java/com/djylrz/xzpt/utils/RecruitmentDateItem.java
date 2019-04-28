package com.djylrz.xzpt.utils;



public class RecruitmentDateItem {
    private String address;
    private String title;
    private String dateTime;

    /**
     *
     * @param address 招聘会地址
     * @param title 招聘会标题
     * @param dateTime 招聘会时间
     */
    public RecruitmentDateItem(String address,String title,String dateTime) {
        this.address = address;
        this.dateTime = dateTime;
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getTitle() {
        return title;
    }
}
