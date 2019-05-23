package com.djylrz.xzpt.utils;

public class JobIntentViewItem {
    private String jobName;
    private String basicSalary;
    private String topSalary;
    private String jobLocation;
    private String jobIndustry;
    private int next;

    /**
     *
     * @param jobName
     * @param basicSalary
     * @param topSalary
     * @param jobLocation
     * @param jobIndustry
     * @param next
     */
    public  JobIntentViewItem(String jobName,String basicSalary,String topSalary,String jobLocation,String jobIndustry,int next) {
        this.jobName = jobName;
        this.basicSalary = basicSalary;
        this.topSalary = topSalary;
        this.jobLocation = jobLocation;
        this.jobIndustry = jobIndustry;
        this.next = next;
    }
    public void setJobIndustry(String jobIndustry) {
        this.jobIndustry = jobIndustry;
    }

    public String getJobIndustry() {
        return jobIndustry;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getNext() {
        return next;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setBasicSalary(String basicSalary) {
        this.basicSalary = basicSalary;
    }

    public String getBasicSalary() {
        return basicSalary;
    }

    public void setTopSalary(String topSalary) {
        this.topSalary = topSalary;
    }

    public String getTopSalary() {
        return topSalary;
    }
}
