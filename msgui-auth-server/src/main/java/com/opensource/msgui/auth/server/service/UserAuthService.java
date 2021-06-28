package com.opensource.msgui.auth.server.service;

import com.opensource.msgui.domain.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAuthService extends UserDetailsService {
  User getUserByusername(String paramString);
}
