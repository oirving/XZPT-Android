package com.djylrz.xzpt.utils;

import com.djylrz.xzpt.bean.ResumeTemplate;

import java.io.Serializable;

public class ResumeModelItem {
    private int resumeModel;

    private ResumeTemplate resumeTemplate;

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
