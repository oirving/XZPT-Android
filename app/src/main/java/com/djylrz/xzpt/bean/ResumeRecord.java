package com.djylrz.xzpt.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Murphy
 * @date 2019/5/315:26
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResumeRecord implements Serializable {

    private Long recordId;
    private String resumeUrl;
    private String templateJpgUrl;
    private String userId;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public String getTemplateJpgUrl() {
        return templateJpgUrl;
    }

    public void setTemplateJpgUrl(String templateJpgUrl) {
        this.templateJpgUrl = templateJpgUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
