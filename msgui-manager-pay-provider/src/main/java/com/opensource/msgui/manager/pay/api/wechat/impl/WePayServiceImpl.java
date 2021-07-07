package com.opensource.msgui.manager.pay.api.wechat.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import com.opensource.msgui.commons.utils.MapperUtils;
import com.opensource.msgui.manager.pay.wechat.api.WePayService;
import com.opensource.msgui.manager.pay.wechat.sdk.WXPay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WePayServiceImpl implements WePayService {
    private static final Logger log = LoggerFactory.getLogger(WePayServiceImpl.class);

    @Resource
    private WXPay wxPay;

    @Value("${pay.we.notify}")
    private String notify;

    @Override
    public Map addPrePayOrder(String body, String orderNo, Long totalFee, String clientIp) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("body", body);
        map.put("out_trade_no", orderNo);
        map.put("total_fee", String.valueOf(totalFee));
        map.put("spbill_create_ip", clientIp);
        map.put("notify_url", this.notify);
        map.put("trade_type", "NATIVE");
        log.info("in params=>" + MapperUtils.mapToJson(map));
        Map<String, String> orderMap = this.wxPay.unifiedOrder(map);
        log.info("result=>" + MapperUtils.mapToJson(orderMap));
        return orderMap;
    }

    @Override
    public Map addH5PayOrder(String body, String orderNo, Long totalFee, String clientIp, String openid) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("body", body);
        map.put("out_trade_no", orderNo);
        map.put("total_fee", String.valueOf(totalFee));
        map.put("spbill_create_ip", clientIp);
        map.put("notify_url", this.notify);
        map.put("trade_type", "JSAPI");
        map.put("openid", openid);
        log.info("H5 in params=>" + MapperUtils.mapToJson(map));
        Map<String, String> orderMap = this.wxPay.unifiedOrder(map);
        log.info("H5 result=>" + MapperUtils.mapToJson(orderMap));
        return orderMap;
    }
}
