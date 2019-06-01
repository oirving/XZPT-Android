package com.djylrz.xzpt.utils;

import com.djylrz.xzpt.R;
import com.djylrz.xzpt.bean.ResumeDelivery;

public class DeliveryRecordItem {
    String state;
    String companyName;
    String jobName;
    String userName;
    String companyId;
    int delete;

    public ResumeDelivery getResumeDelivery() {
        return resumeDelivery;
    }

    public void setResumeDelivery(ResumeDelivery resumeDelivery) {
        this.resumeDelivery = resumeDelivery;
    }

    private ResumeDelivery resumeDelivery;


    public DeliveryRecordItem(ResumeDelivery resumeDelivery) {
        this.resumeDelivery = resumeDelivery;
        this.state=Constants.RESUME_STATE[(int)resumeDelivery.getDeliveryStatus()+1];
        this.companyName=resumeDelivery.getCompanyName();
        this.jobName=resumeDelivery.getRecruitmentName();
        this.userName=resumeDelivery.getUserName();
        this.delete=R.drawable.delete_resume;
        this.companyId = resumeDelivery.getCompanyId();
    }

    public DeliveryRecordItem(String state , String companyName , String jobName , String userName) {
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
