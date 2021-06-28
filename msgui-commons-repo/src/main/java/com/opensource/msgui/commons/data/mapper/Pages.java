package com.opensource.msgui.commons.data.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.io.Serializable;
import java.util.List;

public class Pages<T> implements Serializable {
  private static final long serialVersionUID = -9022759906389169928L;
  
  private Long total;
  
  private Long pageSize;
  
  private Long currPage;
  
  private List<T> data;
  
  public Long getTotal() {
    return this.total;
  }
  
  public void setTotal(Long total) {
    this.total = total;
  }
  
  public Long getPageSize() {
    return this.pageSize;
  }
  
  public void setPageSize(Long pageSize) {
    this.pageSize = pageSize;
  }
  
  public Long getCurrPage() {
    return this.currPage;
  }
  
  public void setCurrPage(Long currPage) {
    this.currPage = currPage;
  }
  
  public List<T> getData() {
    return this.data;
  }
  
  public void setData(List<T> data) {
    this.data = data;
  }
  
  public Pages(Long total, Long pageSize, Long currPage, List<T> data) {
    this.total = total;
    this.pageSize = pageSize;
    this.currPage = currPage;
    this.data = data;
  }
  
  public Pages() {}
  
  public static Pages of(IPage ipage) {
    Pages pages = new Pages();
    pages.setCurrPage(Long.valueOf(ipage.getCurrent()));
    pages.setPageSize(Long.valueOf(ipage.getSize()));
    pages.setData(ipage.getRecords());
    pages.setTotal(Long.valueOf(ipage.getTotal()));
    return pages;
  }
}
