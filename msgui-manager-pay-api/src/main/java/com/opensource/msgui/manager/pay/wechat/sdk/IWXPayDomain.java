package com.opensource.msgui.manager.pay.wechat.sdk;

public interface IWXPayDomain {
    void report(String paramString, long paramLong, Exception paramException);

    DomainInfo getDomain(WXPayConfig paramWXPayConfig);

    class DomainInfo {
        public String domain;

        public boolean primaryDomain;

        public DomainInfo(String domain, boolean primaryDomain) {
            this.domain = domain;
            this.primaryDomain = primaryDomain;
        }

        @Override
        public String toString() {
            return "DomainInfo{domain='" + this.domain + '\'' + ", primaryDomain=" + this.primaryDomain + '}';
        }
    }
}
