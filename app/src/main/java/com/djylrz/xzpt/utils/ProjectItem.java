package com.djylrz.xzpt.utils;


public class ProjectItem {
    private String projectName;
    private int next;

    /**
     *
     * @param projectName
     * @param next
     */
    public ProjectItem(String projectName, int next) {
        this.projectName = projectName;
        this.next = next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getNext() {
        return next;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }
}
