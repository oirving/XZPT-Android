package com.djylrz.xzpt.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Recruitment implements Serializable {

  private static final long serialVersionUID = 1;

  private long recruitmentId;
  private Date publishTime;
  private long validate;
  private String companyId;
  private String jobName;
  private String description;
  private String contact;
  private String location;
  private String deliveryRequest;
  private String salary;
  private String degree;
  private long workTime;
  private long industryLabel;
  private String stationLabel;
  private long jobType;
  private String companyName;

  public Recruitment(long recruitmentId, Timestamp publishTime, long validate, String companyId, String jobName, String description, String contact, String location, String deliveryRequest, String salary, String degree, long workTime, long industryLabel, String stationLabel, long jobType) {
    this.recruitmentId = recruitmentId;
    this.publishTime = publishTime;
    this.validate = validate;
    this.companyId = companyId;
    this.jobName = jobName;
    this.description = description;
    this.contact = contact;
    this.location = location;
    this.deliveryRequest = deliveryRequest;
    this.salary = salary;
    this.degree = degree;
    this.workTime = workTime;
    this.industryLabel = industryLabel;
    this.stationLabel = stationLabel;
    this.jobType = jobType;
  }

  public long getRecruitmentId() {
    return recruitmentId;
  }

  public void setRecruitmentId(long recruitmentId) {
    this.recruitmentId = recruitmentId;
  }


  public Date getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(java.sql.Timestamp publishTime) {
    this.publishTime = publishTime;
  }


  public long getValidate() {
    return validate;
  }

  public void setValidate(long validate) {
    this.validate = validate;
  }


  public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }


  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
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


  public String getSalary() {
    return salary;
  }

  public void setSalary(String salary) {
    this.salary = salary;
  }


  public String getDegree() {
    return degree;
  }

  public void setDegree(String degree) {
    this.degree = degree;
  }


  public long getWorkTime() {
    return workTime;
  }

  public void setWorkTime(long workTime) {
    this.workTime = workTime;
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


  public long getJobType() {
    return jobType;
  }

  public void setJobType(long jobType) {
    this.jobType = jobType;
  }
  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

}
