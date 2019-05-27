package com.djylrz.xzpt.utils;

import com.djylrz.xzpt.bean.ResumeTemplate;

import java.io.Serializable;

public class ResumeModelItem {
    private int resumeModel;

    private ResumeTemplate resumeTemplate;

    public String getCreateOrHistory() {
        return createOrHistory;
    }

    public void setCreateOrHistory(String createOrHistory) {
        this.createOrHistory = createOrHistory;
    }

    private String createOrHistory;

    public ResumeTemplate getResumeTemplate() {
        return resumeTemplate;
    }


    public void setResumeTemplate(ResumeTemplate resumeTemplate) {
        this.resumeTemplate = resumeTemplate;
    }

    public ResumeModelItem(int resumeModel) {
        this.resumeModel = resumeModel;
    }
    public ResumeModelItem(ResumeTemplate resumeTemplate) {
        this.resumeTemplate = resumeTemplate;
    }

    public void setResumeModel(int resumeModel) {
        this.resumeModel = resumeModel;
    }

    public int getResumeModel() {
        return resumeModel;
    }
}
