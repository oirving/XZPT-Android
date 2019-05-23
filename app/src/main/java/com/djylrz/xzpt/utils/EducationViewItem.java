package com.djylrz.xzpt.utils;

public class EducationViewItem {
    private String schoolName;
    private String degree;
    private String startTime;
    private String endTime;
    private int next;

    /**
     *
     * @param schoolName
     * @param degree
     * @param startTime
     * @param endTime
     * @param next
     */
    public EducationViewItem(String schoolName,String degree,String startTime,String endTime,int next) {
        this.schoolName = schoolName;
        this.degree = degree;
        this.startTime = startTime;
        this.endTime = endTime;
        this.next = next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getNext() {
        return next;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDegree() {
        return degree;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }
}
