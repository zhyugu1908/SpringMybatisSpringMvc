package com.springmvc.common.util;
import java.util.List;

/**
 * Created by zhangyuguang on 2018/10/18.
 */
public class Pages<T> {
    private Integer  countPage;
    private Integer  currentPage =1;
    private Integer  pageSize =3;
    private Integer  startIndex;
    private Integer  countRecord;
    private List<T>  entityList;
    private Integer  endIndex;

    private String   name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  Pages(){

    }

    public Pages(Integer countPage, Integer pageSize) {
        this.countPage = countPage;
        this.pageSize = pageSize;
        this.startIndex = (currentPage -1)*pageSize;
    }

    public Pages(Integer countPage, Integer pageSize, Integer startIndex) {
        this.countPage = countPage;
        this.pageSize = pageSize;
        this.startIndex = startIndex;
        this.endIndex = startIndex + pageSize;
    }

    public Integer getCountPage() {
        countPage =  (countRecord%pageSize) !=0 ? countRecord/pageSize+1 : countRecord/pageSize;

        return countPage;
    }

    public void setCountPage(Integer countPage) {
        this.countPage = countPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer curreentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getCountRecord() {
        return countRecord;
    }

    public void setCountRecord(Integer countRecord) {
        this.countRecord = countRecord;
    }

    public List<T> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<T> entityList) {
        this.entityList = entityList;
    }


    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }


    @Override
    public String toString() {
        return "Pages{" +
                "countPage=" + countPage +
                ", currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", startIndex=" + startIndex +
                ", countRecord=" + countRecord +
                ", entityList=" + entityList +
                ", endIndex=" + endIndex +
                ", name='" + name + '\'' +
                '}';
    }
}
