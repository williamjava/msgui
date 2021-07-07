package com.opensource.msgui.manager.pay.api.wechat.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.opensource.msgui.manager.pay.wechat.sdk.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeConfig {
    @Value("${pay.we.app_id}")
    private String APP_ID = null;

    @Value("${pay.we.mcn_id}")
    private String MCN_ID = null;

    @Value("${pay.we.key}")
    private String KEY = null;

    @Value("${alipay.app_id}")
    private String ALI_APP_ID;

    @Value("${alipay.app_id}")
    private String appid;

    @Value("${alipay.rsa_pri_key}")
    private String rsa_pri_key;

    @Value("${alipay.rsa_pub_key}")
    private String alipay_public_key;

    @Bean
    public WXPay wxPay() throws Exception {
        WePayConfig config = new WePayConfig(this.APP_ID, this.MCN_ID, this.KEY);
        WeDomain weDomain = new WeDomain();
        config.setWXPayDomain(weDomain);
        WXPay wxPay = new WXPay(config);
        return wxPay;
    }

    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", this.appid, this.rsa_pri_key, "json", "UTF-8", this.alipay_public_key, "RSA2");
    }
}
