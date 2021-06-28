package com.opensource.msgui.repo.base.service.api.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.Serializable;
import java.lang.reflect.Field;

import com.opensource.msgui.commons.data.mapper.Pages;
import com.opensource.msgui.commons.utils.LocalDateTimeUtil;
import com.opensource.msgui.domain.base.Domain;
import com.opensource.msgui.repo.base.service.api.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class BaseServiceImpl<M extends BaseMapper<T>, T extends Domain> extends ServiceImpl<M, T> implements BaseService<T> {
    @Autowired
    private M mapper;

    public BaseServiceImpl() {
    }

    @Override
    public T saveDomain(T domain) {
        boolean is_save = this.saveOrUpdate(domain);
        return is_save ? domain : null;
    }

    @Override
    public T getDomainById(Serializable domainId) {
        return this.getById(domainId);
    }

    @Override
    public T updateDomain(T domain) {
        domain.setUpdateTime(LocalDateTimeUtil.getDateNowSec());
        int i = this.mapper.updateById(domain);
        return i > 0 ? domain : null;
    }

    @Override
    public T updateDomain(T domain, T queryDomain) {
        UpdateWrapper<T> updateWrapper = new UpdateWrapper(queryDomain);
        int i = this.mapper.update(domain, updateWrapper);
        return i > 0 ? domain : null;
    }

    @Override
    public Boolean deleteDomain(Long id) {
        int i = this.mapper.deleteById(id);
        return i > 0;
    }

    @Override
    public T getDomainByWrapper(T domain) {
        QueryWrapper<T> wrapper = new QueryWrapper(domain);
        return this.getOne(wrapper);
    }

    @Override
    public Pages<T> page(Integer pageNo, Integer pageSize, T domain, String keyword) {
        QueryWrapper<T> wrapper = new QueryWrapper(domain);
        if (!StringUtils.isEmpty(keyword)) {
            Field[] var6 = domain.getClass().getDeclaredFields();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Field field = var6[var8];
                TableField annotation = field.getAnnotation(TableField.class);
                if (!ObjectUtil.isEmpty(annotation)) {
                    ((QueryWrapper)wrapper.or()).like(annotation.value(), keyword);
                }
            }
        }

        wrapper.orderByDesc("update_time");
        IPage<T> ipage = this.page(new Page((long)pageNo, (long)pageSize), wrapper);
        Pages of = Pages.of(ipage);
        return of;
    }

    @Override
    public int countDomain(T domain) {
        QueryWrapper<T> wrapper = new QueryWrapper(domain);
        int count = this.count(wrapper);
        return count;
    }
}
