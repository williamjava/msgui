package com.opensource.msgui.ctl.base;

import com.opensource.msgui.busi.base.service.api.BusiBaseService;
import com.opensource.msgui.commons.data.mapper.Pages;
import com.opensource.msgui.commons.response.ResponseCode;
import com.opensource.msgui.commons.response.ResponseResult;
import com.opensource.msgui.domain.base.Domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

public abstract class GlobalController<M extends BusiBaseService, T extends Domain> {
  @Autowired
  protected M busiService;
  
  @PostMapping({"/save"})
  public ResponseResult add(@RequestBody T domain, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return ResponseResult.failure((Objects.requireNonNull(bindingResult.getFieldError())).getDefaultMessage());
    }
    Domain user = getUser();
    domain.setCreateBy(user.getId());
    Domain dr = this.busiService.addDomain(domain);
    if (dr != null) {
      return ResponseResult.success("数据保存成功", dr);
    }
    return ResponseResult.failure("数据保存失败");
  }
  
  @GetMapping({"/page"})
  public ResponseResult page(T domain, Integer pageNo, Integer pageSize, String keyword) {
    Pages<T> page = this.busiService.page(pageNo, pageSize, domain, keyword);
    System.out.println(ResponseResult.success(page));
    return ResponseResult.success(page);
  }
  
  @PutMapping({"/edit"})
  public ResponseResult update(@RequestBody T domain) {
    Domain user = getUser();
    domain.setUpdateBy(user.getId());
    Domain dr = this.busiService.updateBusiDomain(domain);
    return ResponseResult.success(dr);
  }
  
  @GetMapping({"/get"})
  public ResponseResult get(Long domainId) {
    Domain dr = this.busiService.getDomainById(domainId);
    return ResponseResult.success(dr);
  }
  
  @PutMapping({"/del"})
  public ResponseResult delete(Long domainId) {
    Boolean isDelSucc = this.busiService.delDomain(domainId);
    return isDelSucc.booleanValue() ? ResponseResult.success(ResponseCode.SUCCESS) : ResponseResult.failure("删除失败");
  }
  
  @GetMapping({"/getdomain"})
  public ResponseResult get(T domain) {
    Domain dr = this.busiService.getDomain(domain);
    return ResponseResult.success(dr);
  }
  
  protected abstract Domain getUser();
}
