package com.djylrz.xzpt.utils;

import com.djylrz.xzpt.R;

public class DeliveryRecordItem {
    String state;
    String companyName;
    String jobName;
    String userName;
    int delete;

    public DeliveryRecordItem(String state ,String companyName ,String jobName ,String userName) {
        this.companyName = companyName;
        this.jobName = jobName;
        this.state = state;
        this.userName = userName;
        this.delete = R.drawable.delete_resume;
    }

    public String getJobName() {
        return jobName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getUserName() {
        return userName;
    }

    public String getState() {
        return state;
    }

    public int getDelete() {
        return delete;
    }
}
