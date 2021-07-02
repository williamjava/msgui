package com.opensource.msgui.ctl.user.controller.v1;

import com.opensource.msgui.busi.user.service.api.UserService;
import com.opensource.msgui.domain.user.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whj
 *
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/user/v1")
public class UserController extends BaseController<UserService, User> {

}

    
