package com.djylrz.xzpt.bean;

import java.sql.Timestamp;

/**
 * @program: XZPT-Android
 * @description: 招聘岗位实体类
 * @author: mingjun
 * @create: 2019-04-28 00:54
 */
public class Recruitment {
    private java.sql.Timestamp publishTime;
    private String companyId;
    private String description;
    private long recruitmentId;
    private String contact;
    private String qualifications;
    private String location;
    private String deliveryRequest;
    private String duty;
    private String salary;
    private long type;
    private long validate;

    public Recruitment(Timestamp publishTime, String companyId, String description, long recruitmentId, String contact, String qualifications, String location, String deliveryRequest, String duty, String salary, long type, long validate) {
        this.publishTime = publishTime;
        this.companyId = companyId;
        this.description = description;
        this.recruitmentId = recruitmentId;
        this.contact = contact;
        this.qualifications = qualifications;
        this.location = location;
        this.deliveryRequest = deliveryRequest;
        this.duty = duty;
        this.salary = salary;
        this.type = type;
        this.validate = validate;
    }

    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getRecruitmentId() {
        return recruitmentId;
    }

    public void setRecruitmentId(long recruitmentId) {
        this.recruitmentId = recruitmentId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDeliveryRequest() {
        return deliveryRequest;
    }

    public void setDeliveryRequest(String deliveryRequest) {
        this.deliveryRequest = deliveryRequest;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getValidate() {
        return validate;
    }

    public void setValidate(long validate) {
        this.validate = validate;
    }
}