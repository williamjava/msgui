package com.opensource.msgui.auth.server.service.impl;

import com.google.common.collect.Lists;
import com.opensource.msgui.auth.server.service.UserAuthService;
import com.opensource.msgui.busi.user.service.api.UserService;
import com.opensource.msgui.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuthServiceImpl implements UserAuthService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User userwrap = new User();
        userwrap.setUsername(name);
        userwrap.setDeleted(Boolean.valueOf(false));
        User user = (User) this.userService.getDomain(userwrap);
        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("USER");
        grantedAuthorities.add(simpleGrantedAuthority);
        if (user != null) {
            return (UserDetails) new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
        }
        return null;
    }

    @Override
    public User getUserByusername(String name) {
        User userwrap = new User();
        userwrap.setUsername(name);
        userwrap.setDeleted(Boolean.valueOf(false));
        return (User) this.userService.getDomain(userwrap);
    }
}
