package com.djylrz.xzpt.bean;

public class TempResponseRecruitmentData<T>{
    private Integer currentPage;
    private Integer numOfPage;
    private Integer pageSize;
    private T contentList;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getNumOfPage() {
        return numOfPage;
    }

    public void setNumOfPage(Integer numOfPage) {
        this.numOfPage = numOfPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public T getContentList() {
        return contentList;
    }

    public void setContentList(T contentList) {
        this.contentList = contentList;
    }
}
