package com.pit.myim.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: deng
 * @Date: 2019/9/16 9:52
 * @Version 1.0
 */

public class PageFrom implements Serializable {

    private static final long serialVersionUID = 6617523307144124506L;
    //第几页
    private Integer pageNum = 1;
    //每页条数
    private Integer pageSize = 10;
    //排序字段
    private List<String> defaultOrderFields = new ArrayList<>();
    //是否升序
    private Boolean asc = true;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<String> getDefaultOrderFields() {
        return defaultOrderFields;
    }

    public Boolean getAsc() {
        return asc;
    }

    public void setAsc(Boolean asc) {
        this.asc = asc;
    }

    public void setDefaultOrderFields(List<String> defaultOrderFields) {

        if (null == defaultOrderFields) {
            this.defaultOrderFields = new ArrayList<>(0);
        } else {
            this.defaultOrderFields = defaultOrderFields;
        }
    }


}
