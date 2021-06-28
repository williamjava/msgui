package com.opensource.msgui.repo.user.provider.impl;

import com.opensource.msgui.domain.user.User;
import com.opensource.msgui.repo.base.service.api.impl.BaseServiceImpl;
import com.opensource.msgui.repo.user.api.RepoUserService;
import com.opensource.msgui.repo.user.provider.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class RepoUserServiceImpl extends BaseServiceImpl<UserMapper, User> implements RepoUserService {

}