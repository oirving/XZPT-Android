package com.djylrz.xzpt.bean;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.List;

/**
 * @author Murphy
 * @date 2019/4/29 19:13
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PageData<T> implements Serializable {

    private static final long serialVersionUID = 1;

    // 当前请求的页数（1-n）
    private int currentPage;
    // 总共有多少页
    private int numOfPage;
    // 页大小
    private int pageSize;
    // 内容数组
    private List<T> contentList;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNumOfPage() {
        return numOfPage;
    }

    public void setNumOfPage(int numOfPage) {
        this.numOfPage = numOfPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getContentList() {
        return contentList;
    }

    public void setContentList(List<T> contentList) {
        this.contentList = contentList;
    }
}
