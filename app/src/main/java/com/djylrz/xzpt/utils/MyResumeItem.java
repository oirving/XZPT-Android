package com.djylrz.xzpt.utils;

public class MyResumeItem {
    private String jobName;
    private String resumeState;
    private String companyName;
    private int next;
    private int delete;

    public MyResumeItem(String jobName,String resumeState,String companyName,int next,int delete) {
        this.companyName = companyName;
        this.delete = delete;
        this.jobName = jobName;
        this.resumeState = resumeState;
        this.next = next;
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

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setDelete(int delete) {
        this.delete = delete;
    }

    public int getDelete() {
        return delete;
    }

    public void setResumeState(String resumeState) {
        this.resumeState = resumeState;
    }

    public String getResumeState() {
        return resumeState;
    }
}
