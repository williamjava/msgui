package com.opensource.msgui.busi.base.service.api;

import com.opensource.msgui.commons.data.mapper.Pages;
import com.opensource.msgui.domain.base.Domain;

import java.util.List;

public interface BusiBaseService<T extends Domain> {
  Domain addDomain(T paramT);
  
  Domain updateBusiDomain(T paramT);
  
  Domain getDomainById(Long paramLong);
  
  Pages<T> page(Integer paramInteger1, Integer paramInteger2, T paramT, String paramString);
  
  Boolean delDomain(Long paramLong);
  
  Domain getDomain(T paramT);

  Boolean addDomainBatch(List<T> paramList);
  
  int countByDomain(T paramT);
}
