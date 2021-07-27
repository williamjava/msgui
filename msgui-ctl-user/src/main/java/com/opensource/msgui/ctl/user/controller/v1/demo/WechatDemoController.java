package com.opensource.msgui.ctl.user.controller.v1.demo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opensource.msgui.commons.utils.MapperUtils;
import com.opensource.msgui.manager.redis.api.RedisService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author whj
 *
 * 微信相关Demo控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/wechat/v1")
public class WechatDemoController {
    @Resource
    private RedisService redisService;

    /**
     * 获取openid
     * @param code
     * @return
     * @throws Exception
     */
    private Map<String, String> getOpenid(String code) throws Exception {
        Map<String, String> result = new HashMap<>();
        String appid = "appid";
        String secret = "secret";
        RestTemplate rest = new RestTemplate();
        String grant_type = "authorization_code";

        String params = String.format("appid=%s&secret=%s&js_code=%s&grant_type=%s", new Object[] { appid, secret, code, grant_type});
        String codeUrl = "https://api.weixin.qq.com/sns/jscode2session?" + params;
        String res = rest.getForObject(codeUrl, String.class);
        Map<String, Object> map = MapperUtils.json2map(res);
        String openid = (String) map.get("openid");
        String sessionKey = (String) map.get("session_key");
        result.put("openid", openid);
        //存储sessionKey, 获取用户手机号使用
        if (StringUtils.isNotBlank(openid)){
            redisService.setIfAbsent("WECHAT:PHONE:" + openid, sessionKey, 3, TimeUnit.MINUTES);
        }
        return result;
    }

    /**
     * 解密手机号
     * @param encryptedData 加密数据
     * @param iv 偏移量
     * @return
     */
    private String getWechatAppletPhoneNumber(String encryptedData, String iv, String openid){
        String sessionKey = redisService.get("WECHAT:PHONE:" + openid);
        if (StringUtils.isBlank(sessionKey)) {
            return null;
        }
        try {
            String result = WechatAppletAesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            JsonParser jsonParser = new JsonParser();
            JsonObject userInfoJson = jsonParser.parse(result).getAsJsonObject();
            return userInfoJson.get("phoneNumber").getAsString();
        } catch (Exception e) {
            log.error("************获取微信小程序用户信息错误**************");
            log.error("encryptedData：" + encryptedData);
            log.error("sessionKey：" + sessionKey);
            log.error("iv：" + iv);
            log.error("************获取微信小程序用户信息错误**************");
            e.printStackTrace();
            return null;
        }
    }

    @Data
    @ApiModel(description = "获取微信手机号参数")
    static class WechatPhoneParam {
        @ApiModelProperty(value = "openid")
        @NotBlank(message = "openid不能为空")
        private String openid;

        @ApiModelProperty(value = "加密数据")
        @NotBlank(message = "加密数据不能为空")
        private String encryptedData;

        @ApiModelProperty(value = "偏移量")
        @NotBlank(message = "偏移量不能为空")
        private String iv;
    }
}