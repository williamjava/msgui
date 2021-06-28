package com.opensource.msgui.busi.user.service.api;

import com.opensource.msgui.busi.base.service.api.BusiBaseService;
import com.opensource.msgui.domain.user.User;

public interface UserService extends BusiBaseService<User> {
    User getUserByName(String username);
}
