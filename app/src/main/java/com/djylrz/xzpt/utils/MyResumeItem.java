package com.djylrz.xzpt.utils;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.Resume;

public class MyResumeItem {
    private String jobName;
    private int next;
    private int delete;
    private Resume resume;

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public MyResumeItem(Resume resume){
        this.resume=resume;
        this.jobName=resume.getExpectWork();
        this.delete = R.id.delete;
        this.next = R.id.next;
    }

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
