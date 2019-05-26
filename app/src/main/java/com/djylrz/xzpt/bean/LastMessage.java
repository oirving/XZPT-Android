package com.djylrz.xzpt.bean;

public class LastMessage {
    private String fromUuid;
    private String fromAccount;
    private String payload;///消息体需base64解码
    private String sequence;//sequence主要用来做消息的排序和去重，全局唯一
    private String bizType;//可用于表示消息类型扩展字段（可选）

    public String getFromUuid() {
        return fromUuid;
    }

    public void setFromUuid(String fromUuid) {
        this.fromUuid = fromUuid;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

}
