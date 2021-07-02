package com.opensource.msgui.ctl.user.controller.v1.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.opensource.msgui.busi.user.service.api.UserService;
import com.opensource.msgui.commons.response.ResponseResult;
import com.opensource.msgui.commons.utils.ObjectMappingUtils;
import com.opensource.msgui.ctl.user.controller.vo.UserVo;
import com.opensource.msgui.domain.user.User;
import com.opensource.msgui.manager.redis.api.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author whj
 *
 * Reids实践控制器
 */
@RestController
@RequestMapping("/api/redis/v1")
public class RedisController {
    @Resource
    private UserService userService;

    @Resource
    private RedisService redisService;

    @PostMapping("cacheUserInfo")
    public ResponseResult cacheUserInfo(String username) {
        User user = userService.getUserByName(username);
        redisService.set(username, JSONObject.toJSONString(user), 24 * 60 * 60);
        return ResponseResult.success("缓存用户信息成功");
    }

    @GetMapping("getUserByUsername/{username}")
    public ResponseResult getUserByUsername(@PathVariable String username) {
        String userJsonString = redisService.get(username);
        if (StringUtils.isNotBlank(userJsonString)) {
            return ResponseResult.success(ObjectMappingUtils.map(JSON.parseObject(userJsonString, User.class), UserVo.class));
        }

        return ResponseResult.success("未获取到任何用户信息");
    }

    @DeleteMapping("getUserByUsername/{username}")
    public ResponseResult delUser(@PathVariable String username) {
        redisService.del(username);

        return ResponseResult.success("从缓存中移除用户信息成功");
    }
}

    
