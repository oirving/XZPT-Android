package com.djylrz.xzpt.bean;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User implements Serializable {

    private static final long serialVersionUID = 1;
    private String userId;//用户id
    private String telephone;//电话号码
    private String passwd;//密码
    private String userName;//姓名
    private String headUrl;//头像地址
    private String email;//邮箱
    private long sex;//性别
    private String school;//学校
    private String specialty;//专业
    private long highestEducation;//最高学历
    private java.sql.Date startTime;//开始时间
    private java.sql.Date endTime;//结束时间
    private long workTime;//工作时间制度
    private long jobType;//工作类型
    private String presentCity;//居住城市
    private String expectedCity;//期望城市
    private long industryLabel;//行业标签
    private String stationLabel;//岗位标签
    private String expectSalary;//期望薪资
    private String token;
    private long age;//年龄


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


    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
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


    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }


    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }


    public long getHighestEducation() {
        return highestEducation;
    }

    public void setHighestEducation(long highestEducation) {
        this.highestEducation = highestEducation;
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


    public long getWorkTime() {
        return workTime;
    }

    public void setWorkTime(long workTime) {
        this.workTime = workTime;
    }


    public long getJobType() {
        return jobType;
    }

    public void setJobType(long jobType) {
        this.jobType = jobType;
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


    public long getIndustryLabel() {
        return industryLabel;
    }

    public void setIndustryLabel(long industryLabel) {
        this.industryLabel = industryLabel;
    }


    public String getStationLabel() {
        return stationLabel;
    }

    public void setStationLabel(String stationLabel) {
        this.stationLabel = stationLabel;
    }


    public String getExpectSalary() {
        return expectSalary;
    }

    public void setExpectSalary(String expectSalary) {
        this.expectSalary = expectSalary;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

}