package com.opensource.msgui.manager.pay.wechat.sdk;

public class WeDomain implements IWXPayDomain {
    @Override
    public void report(String domain, long elapsedTimeMillis, Exception ex) {
    }

    @Override
    public IWXPayDomain.DomainInfo getDomain(WXPayConfig config) {
        return new IWXPayDomain.DomainInfo("api.mch.weixin.qq.com", true);
    }
}
