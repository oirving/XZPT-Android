package com.djylrz.xzpt.utils;

public class InterviewTipsItem {
    private String title;
    private String url;

    private InterviewSkill interviewSkill;

    public InterviewTipsItem(InterviewSkill interviewSkill) {
        this.interviewSkill = interviewSkill;

        this.title = interviewSkill.getTitle();
        this.url = PostParameterName.GET_URL_INTERVIEW_SKILL_ARTICLE+interviewSkill.getId();
    }

    public InterviewTipsItem(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public InterviewSkill getInterviewSkill() {
        return interviewSkill;
    }

    public void setInterviewSkill(InterviewSkill interviewSkill) {
        this.interviewSkill = interviewSkill;
    }
}
