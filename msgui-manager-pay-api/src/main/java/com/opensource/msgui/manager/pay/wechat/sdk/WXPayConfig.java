package com.opensource.msgui.manager.pay.wechat.sdk;

import java.io.InputStream;
import java.io.Serializable;

public abstract class WXPayConfig implements Serializable {
    abstract String getAppID();

    abstract String getMchID();

    abstract String getKey();

    abstract InputStream getCertStream();

    public int getHttpConnectTimeoutMs() {
        return 6000;
    }

    public int getHttpReadTimeoutMs() {
        return 8000;
    }

    abstract IWXPayDomain getWXPayDomain();

    public boolean shouldAutoReport() {
        return true;
    }

    public int getReportWorkerNum() {
        return 6;
    }

    public int getReportQueueMaxSize() {
        return 10000;
    }

    public int getReportBatchSize() {
        return 10;
    }
}
