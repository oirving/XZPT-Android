package com.djylrz.xzpt.bean;

import java.io.Serializable;

public class PostResult implements Serializable {
    private String resultCode;
    private String resultMsg;
    private String resultObject;

    public String getResultCode() {
        return resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public String getResultObject() {
        return resultObject;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public void setResultObject(String resultObject) {
        this.resultObject = resultObject;
    }
}
