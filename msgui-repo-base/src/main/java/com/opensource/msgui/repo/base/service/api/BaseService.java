package com.opensource.msgui.repo.base.service.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.opensource.msgui.commons.data.mapper.Pages;
import com.opensource.msgui.commons.exception.MsguiDAOException;
import com.opensource.msgui.domain.base.Domain;

import java.io.Serializable;

public interface BaseService<T extends Domain> extends IService<T> {
    T saveDomain(T domain) throws MsguiDAOException;

    T getDomainById(Serializable domainId);

    T getDomainByWrapper(T domain);

    T updateDomain(T domain);

    T updateDomain(T domain, T queryDomain);

    Boolean deleteDomain(Long id);

    Pages<T> page(Integer pageNo, Integer pageSize, T domain, String keyword);

    int countDomain(T domain);
}
