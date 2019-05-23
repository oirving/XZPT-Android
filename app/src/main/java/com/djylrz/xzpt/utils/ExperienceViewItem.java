package com.djylrz.xzpt.utils;

public class ExperienceViewItem {
    private String companyName;
    private String position;
    private int next;

    /**
     *
     * @param companyName
     * @param position
     * @param next
     */
    public ExperienceViewItem(String companyName,String position,int next) {
        this.companyName = companyName;
        this.next = next;
        this.position = position;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getNext() {
        return next;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }
}
