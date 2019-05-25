package com.djylrz.xzpt.bean;



import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Resume implements Serializable {

    private static final long serialVersionUID = 1;

    private long resumeId;//简历id
    private String userId;//用户id
    private String telephone;//电话
    private String userName;//姓名
    private String headUrl;//头像URL
    private String email;//邮箱
    private long sex;//性别
    private String presentCity;//现居城市
    private String expectedCity;//期望工作城市
    private String school;//学校
    private String speciality;//专业
    private java.sql.Date startTime;//教育开始时间
    private java.sql.Date endTime;//教育结束时间
    private long highestEducation;//学位
    private String certificate;//获奖证书
    private String projectExperience;//项目经历
    private String practicalExperience;//实习经历
    private String expectWork;//期望职位

    public String getExpectWork() {
        return expectWork;
    }

    public void setExpectWork(String expectWork) {
        this.expectWork = expectWork;
    }

    private long resumeStatus;


    public long getResumeId() {
        return resumeId;
    }

    public void setResumeId(long resumeId) {
        this.resumeId = resumeId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public long getSex() {
        return sex;
    }

    public void setSex(long sex) {
        this.sex = sex;
    }


    public String getPresentCity() {
        return presentCity;
    }

    public void setPresentCity(String presentCity) {
        this.presentCity = presentCity;
    }


    public String getExpectedCity() {
        return expectedCity;
    }

    public void setExpectedCity(String expectedCity) {
        this.expectedCity = expectedCity;
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


    public java.sql.Date getStartTime() {
        return startTime;
    }

    public void setStartTime(java.sql.Date startTime) {
        this.startTime = startTime;
    }


    public java.sql.Date getEndTime() {
        return endTime;
    }

    public void setEndTime(java.sql.Date endTime) {
        this.endTime = endTime;
    }


    public long getHighestEducation() {
        return highestEducation;
    }

    public void setHighestEducation(long highestEducation) {
        this.highestEducation = highestEducation;
    }


    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }


    public String getProjectExperience() {
        return projectExperience;
    }

    public void setProjectExperience(String projectExperience) {
        this.projectExperience = projectExperience;
    }


    public String getPracticalExperience() {
        return practicalExperience;
    }

    public void setPracticalExperience(String practicalExperience) {
        this.practicalExperience = practicalExperience;
    }


    public long getResumeStatus() {
        return resumeStatus;
    }

    public void setResumeStatus(long resumeStatus) {
        this.resumeStatus = resumeStatus;
    }

}
