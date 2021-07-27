package com.opensource.msgui.auth.server.controller;

import com.google.common.collect.Maps;
import com.opensource.msgui.auth.server.service.UserAuthService;
import com.opensource.msgui.busi.user.service.api.UserService;
import com.opensource.msgui.commons.response.ResponseCode;
import com.opensource.msgui.commons.response.ResponseResult;
import com.opensource.msgui.commons.utils.MapperUtils;
import com.opensource.msgui.commons.utils.OkHttpClientUtil;
import com.opensource.msgui.domain.user.User;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;

@RestController
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Value("${server.port}")
    private String serverPort;

    @Value("${business.oauth2.grant_type}")
    public String oauth2GrantType;

    @Value("${business.oauth2.client_id}")
    public String oauth2ClientId;

    @Value("${business.oauth2.client_secret}")
    public String oauth2ClientSecret;

    @Resource
    public UserAuthService userAuthService;

    @Resource
    public PasswordEncoder passwordEncoder;

    @Resource
    public TokenStore tokenStore;

    @Resource
    private UserService userService;

    @PostMapping({"/api/encryptPwd"})
    public ResponseResult encryptPwd(String pwd){
        return ResponseResult.success(passwordEncoder.encode(pwd));
    }

    @PostMapping({"/api/sso/login"})
    public ResponseResult login(@RequestBody LoginParam loginParam){
        Map<String, Object> result = Maps.newHashMap();
        UserDetails userDetails = this.userAuthService.loadUserByUsername(loginParam.getUsername());
        if (userDetails == null) {
            return ResponseResult.failure(ResponseCode.USER_LOGIN_ERROR);
        }

        if (!this.passwordEncoder.matches(loginParam.getPassword(), userDetails.getPassword())) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            User user = this.userService.getUserByName(loginParam.getUsername());
            String encode = passwordEncoder.encode(loginParam.getPassword());
            user.setPassword(encode);
            this.userService.updateBusiDomain(user);
        }

        Map<String, String> params = Maps.newHashMap();
        params.put("username", loginParam.getUsername());
        params.put("password", loginParam.getPassword());
        params.put("grant_type", this.oauth2GrantType);
        params.put("client_id", this.oauth2ClientId);
        params.put("client_secret", this.oauth2ClientSecret);
        System.out.println(params.toString());
        try {
            String URL_OAUTH_TOKEN = String.format("http://%s:%s%s", new Object[]{"localhost", this.serverPort, "/oauth/token"});
            Response response = OkHttpClientUtil.getInstance().postData(URL_OAUTH_TOKEN, params);
            String jsonString = (Objects.<ResponseBody>requireNonNull(response.body())).string();
            Map<String, Object> jsonMap = MapperUtils.json2map(jsonString);
            System.out.println(jsonMap);
            String token = String.valueOf(jsonMap.get("access_token"));
            result.put("token", token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(ResponseCode.SUCCESS, result);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping({"/api/user/info"})
    public ResponseResult info(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userAuthService.getUserByusername(authentication.getName());
        if (user == null) {
            return ResponseResult.failure();
        }
        user.setPassword(null);
        return ResponseResult.success(user);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping({"/api/user/logout"})
    public ResponseResult logout(String token) {
        OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(token);
        this.tokenStore.removeAccessToken(oAuth2AccessToken);
        return ResponseResult.success("用户已注销!");
    }

    /**
     * 获取请求token
     * @param request
     * @return
     */
    private String extractHeaderToken(HttpServletRequest request) {
        Enumeration headers = request.getHeaders("Authorization");

        String value;
        do {
            if (!headers.hasMoreElements()) {
                return null;
            }

            value = (String)headers.nextElement();
        } while(!value.toLowerCase().startsWith("Bearer".toLowerCase()));

        String authHeaderValue = value.substring("Bearer".length()).trim();
        request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, value.substring(0, "Bearer".length()).trim());
        int commaIndex = authHeaderValue.indexOf(44);
        if (commaIndex > 0) {
            authHeaderValue = authHeaderValue.substring(0, commaIndex);
        }

        return authHeaderValue;
    }

    static class LoginParam {
        private String username;

        private String password;

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof LoginParam)) {
                return false;
            }
            LoginParam other = (LoginParam) o;
            if (!other.canEqual(this)) {
                return false;
            }
            Object this$username = getUsername(), other$username = other.getUsername();
            if ((this$username == null) ? (other$username != null) : !this$username.equals(other$username)) {
                return false;
            }
            Object this$password = getPassword(), other$password = other.getPassword();
            return !((this$password == null) ? (other$password != null) : !this$password.equals(other$password));
        }

        protected boolean canEqual(Object other) {
            return other instanceof LoginParam;
        }

        @Override
        public int hashCode() {
            int result = 1;
            Object $username = getUsername();
            result = result * 59 + (($username == null) ? 43 : $username.hashCode());
            Object $password = getPassword();
            return result * 59 + (($password == null) ? 43 : $password.hashCode());
        }

        @Override
        public String toString() {
            return "LoginController.LoginParam(username=" + getUsername() + ", password=" + getPassword() + ")";
        }

        public String getUsername() {
            return this.username;
        }

        public String getPassword() {
            return this.password;
        }
    }
}
