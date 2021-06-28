package com.opensource.msgui.busi.base.service.api.impl;

import java.util.List;

import com.opensource.msgui.busi.base.service.api.BusiBaseService;
import com.opensource.msgui.commons.data.mapper.Pages;
import com.opensource.msgui.commons.exception.MsguiDAOException;
import com.opensource.msgui.domain.base.Domain;
import com.opensource.msgui.repo.base.service.api.BaseService;
import org.springframework.beans.factory.annotation.Autowired;

public class BusiBaseServiceImpl<M extends BaseService, T extends Domain> implements BusiBaseService<T> {
  @Autowired
  protected M baseServie;
  
  @Override
  public Domain addDomain(T domain) {
    try {
      return this.baseServie.saveDomain((Domain)domain);
    } catch (MsguiDAOException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  @Override
  public Boolean delDomain(Long id) {
    return this.baseServie.deleteDomain(id);
  }
  
  @Override
  public Domain getDomain(T domain) {
    return this.baseServie.getDomainByWrapper((Domain)domain);
  }
  
  @Override
  public Boolean addDomainBatch(List<T> listDomain) {
    return Boolean.valueOf(this.baseServie.saveOrUpdateBatch(listDomain));
  }
  
  @Override
  public int countByDomain(T domain) {
    int i = this.baseServie.countDomain((Domain)domain);
    return i;
  }
  
  @Override
  public Pages<T> page(Integer pageNo, Integer pageSize, T domain, String keyword) {
    return this.baseServie.page(pageNo, pageSize, (Domain)domain, keyword);
  }
  
  @Override
  public Domain updateBusiDomain(T domain) {
    return this.baseServie.updateDomain((Domain)domain);
  }
  
  @Override
  public Domain getDomainById(Long id) {
    return this.baseServie.getDomainById(id);
  }
}
