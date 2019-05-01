package com.djylrz.xzpt.bean;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class User implements Serializable {

    private static final long serialVersionUID = 1;
    private String userId;
    private String telephone;
    private String passwd;
    private String userName;
    private String headUrl;
    private String email;
    private long sex;
    private String school;
    private String specialty;
    private long highestEducation;
    private java.sql.Date startTime;
    private java.sql.Date endTime;
    private long workTime;
    private long jobType;
    private String presentCity;
    private String expectedCity;
    private long industryLabel;
    private String stationLabel;
    private String expectSalary;
    private String token;
    private long age;


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