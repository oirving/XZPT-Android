package com.djylrz.xzpt.utils;

import com.djylrz.xzpt.R;

public class MyResumeItem {
    private String jobName;
    private int next;
    private int delete;

    public MyResumeItem(String jobName) {
        this.delete = R.id.delete;
        this.jobName = jobName;
        this.next = R.id.next;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }
}
