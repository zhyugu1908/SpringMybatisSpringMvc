package com.springmvc.common.util;

import java.util.List;

/**
 * Created by zhangyuguang on 2018/10/18   .
 */
public class Pager<T> {

    private List resultList;   //用于封装返回的结果
    private int currentPage = 1;  //默认第一页
    private int pages;

    private int pageSize = 20;   // 页面大小默认为20  最后实际大小根据页面来定
    private Long totalSize;      // 总记录条数
    private Long operate;        // 操作标志

    private List<T> pageList;

    private int sortField ;
    private int sortMethod;

    public Pager(int currentPage , int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Pager(List resultList, int currentPage, int pages, int pageSize, Long totalSize) {
        this.resultList = resultList;
        this.currentPage = currentPage;
        this.pages = pages;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
    }

    public List getResultList() {
        return resultList;
    }

    public void setResultList(List resultList) {
        this.resultList = resultList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Long getOperate() {
        return operate;
    }

    public void setOperate(Long operate) {
        this.operate = operate;
    }

    public int getSortField() {
        return sortField;
    }

    public void setSortField(int sortField) {
        this.sortField = sortField;
    }

    public int getSortMethod() {
        return sortMethod;
    }

    public void setSortMethod(int sortMethod) {
        this.sortMethod = sortMethod;
    }

    public List<T> getPageList() {
        return pageList;
    }

    public void setPageList(List<T> pageList) {
        this.pageList = pageList;
    }

    @Override
    public String toString() {
        return "Pager{" +
                "resultList=" + resultList +
                ", currentPage=" + currentPage +
                ", pages=" + pages +
                ", pageSize=" + pageSize +
                ", totalSize=" + totalSize +
                ", operate=" + operate +
                ", pageList=" + pageList +
                ", sortField=" + sortField +
                ", sortMethod=" + sortMethod +
                '}';
    }
}
