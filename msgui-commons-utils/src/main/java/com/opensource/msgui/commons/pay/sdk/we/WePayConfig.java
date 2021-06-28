package com.opensource.msgui.commons.pay.sdk.we;

import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@NoArgsConstructor
public class WePayConfig extends WXPayConfig {

    private String appID;
    private String mchID;
    private String key;
    private InputStream certStream;
    private IWXPayDomain WXPayDomain;

    public WePayConfig(String appID, String mchID, String key) {
        this.appID = appID;
        this.mchID = mchID;
        this.key = key;
    }

    @Override
    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    @Override
    public String getMchID() {
        return mchID;
    }

    public void setMchID(String mchID) {
        this.mchID = mchID;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public InputStream getCertStream() {
        return certStream;
    }

    public void setCertStream(InputStream certStream) {
        this.certStream = certStream;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return WXPayDomain;
    }

    public void setWXPayDomain(IWXPayDomain WXPayDomain) {
        this.WXPayDomain = WXPayDomain;
    }
    public static WePayConfig getWeConfig(String appID, String mchID, String key,String certurl){
        URL url = null; //声明url对象
        try {
            url = new URL(certurl);
            URLConnection connection = url.openConnection(); //打开连接
            InputStream in = connection.getInputStream();
            return new WePayConfig(appID, mchID, key,in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public WePayConfig(String appID, String mchID, String key, InputStream certStream) {
        this.appID = appID;
        this.mchID = mchID;
        this.key = key;
        this.certStream = certStream;
    }
}
