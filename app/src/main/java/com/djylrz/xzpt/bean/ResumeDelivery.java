package com.djylrz.xzpt.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ResumeDelivery implements Serializable {

    private static final long serialVersionUID = 1;

    private long resumeDeliveryId;
    private long recruitmentId;
    private String userId;
    private long resumeId;
    private long deliveryStatus;
    private String remark;

    private String userName;
    private String recruitmentName;
    private String school;
    private String speciality;
    private String companyName;
    private String companyId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getResumeDeliveryId() {
        return resumeDeliveryId;
    }

    public void setResumeDeliveryId(long resumeDeliveryId) {
        this.resumeDeliveryId = resumeDeliveryId;
    }

    public long getRecruitmentId() {
        return recruitmentId;
    }

    public void setRecruitmentId(long recruitmentId) {
        this.recruitmentId = recruitmentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getResumeId() {
        return resumeId;
    }

    public void setResumeId(long resumeId) {
        this.resumeId = resumeId;
    }

    public long getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(long deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRecruitmentName() {
        return recruitmentName;
    }

    public void setRecruitmentName(String recruitmentName) {
        this.recruitmentName = recruitmentName;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
