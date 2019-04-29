package com.djylrz.xzpt.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Company implements Serializable {

  private static final long serialVersionUID = 1;

  private String companyId;
  private String telephone;
  private String passwd;
  private String companyName;
  private String headUrl;
  private String email;
  private String description;
  private long status;
  private String token;


  public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
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


  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
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


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public long getStatus() {
    return status;
  }

  public void setStatus(long status) {
    this.status = status;
  }


  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
