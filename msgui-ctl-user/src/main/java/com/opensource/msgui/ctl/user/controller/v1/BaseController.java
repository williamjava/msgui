package com.opensource.msgui.ctl.user.controller.v1;

import com.opensource.msgui.busi.base.service.api.BusiBaseService;
import com.opensource.msgui.busi.user.service.api.UserService;
import com.opensource.msgui.ctl.base.GlobalController;
import com.opensource.msgui.domain.base.Domain;
import com.opensource.msgui.domain.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import java.util.Map;

public class BaseController<M extends BusiBaseService, T extends Domain> extends GlobalController<M,T> {
    @Resource
    private UserService userService;

    @Override
    protected Domain getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userwrap=new User();
        Map principal = (Map) authentication.getPrincipal();
        Long userId = (Long) principal.get("userId");
        userwrap.setId(userId);
        User user = (User) userService.getDomain(userwrap);
        return user;
    }
}
