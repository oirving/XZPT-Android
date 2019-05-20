package com.djylrz.xzpt.bean;

public class SubRecruitmentData {
    //职位名称
    private String jobName;
    //岗位描述
    private String description;
    //联系人及联系方式
    private String contact;
    //工作地点
    private String location;
    //投递要求
    private String deliveryRequest;
    //薪资
    private String salary;
    //学历要求
    private String degree;
    //工作时间
    private String workTime;
    //行业标签
    private String industryLabel;
    //岗位标签
    private String stationLabel;
    //招聘、实习或者兼职
    private String jobType;
    //招聘人数
    private String headCount;

    public SubRecruitmentData(String jobName, String description, String contact, String location, String deliveryRequest, String salary, String degree, String workTime, String industryLabel, String stationLabel, String jobType, String headCount) {
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
        this.headCount = headCount;
    }
    public SubRecruitmentData() {
        super();
    }

    public String getHeadCount() {
        return headCount;
    }

    public void setHeadCount(String headCount) {
        this.headCount = headCount;
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

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getIndustryLabel() {
        return industryLabel;
    }

    public void setIndustryLabel(String industryLabel) {
        this.industryLabel = industryLabel;
    }

    public String getStationLabel() {
        return stationLabel;
    }

    public void setStationLabel(String stationLabel) {
        this.stationLabel = stationLabel;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    @Override
    public String toString() {
        return "SubRecruitmentData{" +
                "jobName='" + jobName + '\'' +
                ", description='" + description + '\'' +
                ", contact='" + contact + '\'' +
                ", location='" + location + '\'' +
                ", deliveryRequest='" + deliveryRequest + '\'' +
                ", salary='" + salary + '\'' +
                ", degree='" + degree + '\'' +
                ", workTime='" + workTime + '\'' +
                ", industryLabel='" + industryLabel + '\'' +
                ", stationLabel='" + stationLabel + '\'' +
                ", jobType='" + jobType + '\'' +
                ", headCount='" + headCount + '\'' +
                '}';
    }
}
