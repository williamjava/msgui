package com.opensource.msgui.commons.pay.sdk.we;

import static com.opensource.msgui.commons.pay.sdk.we.WXPayConstants.DOMAIN_API;

public class WeDomain implements IWXPayDomain {
    @Override
    public void report(String domain, long elapsedTimeMillis, Exception ex) {

    }

    @Override
    public DomainInfo getDomain(WXPayConfig config) {
        return new DomainInfo(DOMAIN_API,true);
    }
}
