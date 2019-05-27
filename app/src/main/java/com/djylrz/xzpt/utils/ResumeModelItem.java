package com.djylrz.xzpt.utils;

import com.djylrz.xzpt.bean.ResumeRecord;
import com.djylrz.xzpt.bean.ResumeTemplate;

import java.io.Serializable;

public class ResumeModelItem {
    private int resumeModel;

    private ResumeTemplate resumeTemplate;

    private ResumeRecord resumeRecord;
    private String createOrHistory;

    public ResumeModelItem(ResumeRecord resumeRecord) {
        this.resumeRecord = resumeRecord;
        this.resumeTemplate=new ResumeTemplate();
        this.resumeTemplate.setImgFileName(resumeRecord.getTemplateJpgUrl());
        this.resumeTemplate.setTemplateFileName(resumeRecord.getResumeUrl());
        this.createOrHistory = PostParameterName.INTENT_PUT_EXTRA_VALUE_RESUME_HISTORY;
    }

    public ResumeModelItem(int resumeModel) {
        this.resumeModel = resumeModel;
    }

    public ResumeModelItem(ResumeTemplate resumeTemplate) {
        this.resumeTemplate = resumeTemplate;
    }

    public ResumeRecord getResumeRecord() {
        return resumeRecord;
    }

    public void setResumeRecord(ResumeRecord resumeRecord) {
        this.resumeRecord = resumeRecord;
    }

    public String getCreateOrHistory() {
        return createOrHistory;
    }

    public void setCreateOrHistory(String createOrHistory) {
        this.createOrHistory = createOrHistory;
    }

    public ResumeTemplate getResumeTemplate() {
        return resumeTemplate;
    }

    public void setResumeTemplate(ResumeTemplate resumeTemplate) {
        this.resumeTemplate = resumeTemplate;
    }

    public int getResumeModel() {
        return resumeModel;
    }

    public void setResumeModel(int resumeModel) {
        this.resumeModel = resumeModel;
    }
}
