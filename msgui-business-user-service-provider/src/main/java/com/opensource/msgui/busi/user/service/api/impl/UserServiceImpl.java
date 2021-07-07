package com.opensource.msgui.busi.user.service.api.impl;

import com.opensource.msgui.busi.base.service.api.impl.BusiBaseServiceImpl;
import com.opensource.msgui.busi.user.service.api.UserService;
import com.opensource.msgui.domain.user.User;
import com.opensource.msgui.repo.user.api.RepoUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BusiBaseServiceImpl<RepoUserService, User>
        implements UserService {
    @Override
    public User getUserByName(String username) {
        User user = new User();
        user.setUsername(username);
        return (User) this.getDomain(user);
    }
}
