package com.opensource.msgui.manager.pay.wechat.sdk;

import java.util.HashMap;
import java.util.Map;

public class WXPay {
    private WXPayConfig config;

    private WXPayConstants.SignType signType;

    private boolean autoReport;

    private boolean useSandbox;

    private String notifyUrl;

    private WXPayRequest wxPayRequest;

    public WXPay(WXPayConfig config) throws Exception {
        this(config, null, true, false);
    }

    public WXPay(WXPayConfig config, boolean autoReport) throws Exception {
        this(config, null, autoReport, false);
    }

    public WXPay(WXPayConfig config, boolean autoReport, boolean useSandbox) throws Exception {
        this(config, null, autoReport, useSandbox);
    }

    public WXPay(WXPayConfig config, String notifyUrl) throws Exception {
        this(config, notifyUrl, true, false);
    }

    public WXPay(WXPayConfig config, String notifyUrl, boolean autoReport) throws Exception {
        this(config, notifyUrl, autoReport, false);
    }

    public WXPay(WXPayConfig config, String notifyUrl, boolean autoReport, boolean useSandbox) throws Exception {
        this.config = config;
        this.notifyUrl = notifyUrl;
        this.autoReport = autoReport;
        this.useSandbox = useSandbox;
        this.signType = WXPayConstants.SignType.MD5;
        this.wxPayRequest = new WXPayRequest(config);
    }

    private void checkWXPayConfig() throws Exception {
        if (this.config == null) {
            throw new Exception("config is null");
        }
        if (this.config.getAppID() == null || this.config.getAppID().trim().length() == 0) {
            throw new Exception("appid in config is empty");
        }
        if (this.config.getMchID() == null || this.config.getMchID().trim().length() == 0) {
            throw new Exception("appid in config is empty");
        }
        if (this.config.getCertStream() == null) {
            throw new Exception("cert stream in config is empty");
        }
        if (this.config.getWXPayDomain() == null) {
            throw new Exception("config.getWXPayDomain() is null");
        }
        if (this.config.getHttpConnectTimeoutMs() < 10) {
            throw new Exception("http connect timeout is too small");
        }
        if (this.config.getHttpReadTimeoutMs() < 10) {
            throw new Exception("http read timeout is too small");
        }
    }

    public Map<String, String> fillRequestData(Map<String, String> reqData) throws Exception {
        reqData.put("appid", this.config.getAppID());
        reqData.put("mch_id", this.config.getMchID());
        reqData.put("nonce_str", WXPayUtil.generateNonceStr());
        if (WXPayConstants.SignType.MD5.equals(this.signType)) {
            reqData.put("sign_type", "MD5");
        } else if (WXPayConstants.SignType.HMACSHA256.equals(this.signType)) {
            reqData.put("sign_type", "HMAC-SHA256");
        }
        reqData.put("sign", WXPayUtil.generateSignature(reqData, this.config.getKey(), this.signType));
        return reqData;
    }

    public boolean isResponseSignatureValid(Map<String, String> reqData) throws Exception {
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), this.signType);
    }

    public boolean isPayResultNotifySignatureValid(Map<String, String> reqData) throws Exception {
        WXPayConstants.SignType signType;
        String signTypeInData = reqData.get("sign_type");
        if (signTypeInData == null) {
            signType = WXPayConstants.SignType.MD5;
        } else {
            signTypeInData = signTypeInData.trim();
            if (signTypeInData.length() == 0) {
                signType = WXPayConstants.SignType.MD5;
            } else if ("MD5".equals(signTypeInData)) {
                signType = WXPayConstants.SignType.MD5;
            } else if ("HMAC-SHA256".equals(signTypeInData)) {
                signType = WXPayConstants.SignType.HMACSHA256;
            } else {
                throw new Exception(String.format("Unsupported sign_type: %s", new Object[]{signTypeInData}));
            }
        }
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), signType);
    }

    public String requestWithoutCert(String urlSuffix, Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String msgUUID = reqData.get("nonce_str");
        String reqBody = WXPayUtil.mapToXml(reqData);
        String resp = this.wxPayRequest.requestWithoutCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, this.autoReport);
        return resp;
    }

    public String requestWithCert(String urlSuffix, Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String msgUUID = reqData.get("nonce_str");
        String reqBody = WXPayUtil.mapToXml(reqData);
        String resp = this.wxPayRequest.requestWithCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, this.autoReport);
        return resp;
    }

    public Map<String, String> processResponseXml(String xmlStr) throws Exception {
        String return_code, RETURN_CODE = "return_code";
        Map<String, String> respData = WXPayUtil.xmlToMap(xmlStr);
        if (respData.containsKey(RETURN_CODE)) {
            return_code = respData.get(RETURN_CODE);
        } else {
            throw new Exception(String.format("No `return_code` in XML: %s", new Object[]{xmlStr}));
        }
        if (return_code.equals("FAIL")) {
            return respData;
        }
        if (return_code.equals("SUCCESS")) {
            if (isResponseSignatureValid(respData)) {
                return respData;
            }
            throw new Exception(String.format("Invalid sign value in XML: %s", new Object[]{xmlStr}));
        }
        throw new Exception(String.format("return_code value %s is invalid in XML: %s", new Object[]{return_code, xmlStr}));
    }

    public Map<String, String> microPay(Map<String, String> reqData) throws Exception {
        return microPay(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> microPay(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "/sandboxnew/pay/micropay";
        } else {
            url = "/pay/micropay";
        }
        String respXml = requestWithoutCert(url, fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return processResponseXml(respXml);
    }

    public Map<String, String> microPayWithPos(Map<String, String> reqData) throws Exception {
        return microPayWithPos(reqData, this.config.getHttpConnectTimeoutMs());
    }

    public Map<String, String> microPayWithPos(Map<String, String> reqData, int connectTimeoutMs) throws Exception {
        int remainingTimeMs = 60000;
        long startTimestampMs = 0L;
        Map<String, String> lastResult = null;
        Exception lastException = null;
        while (true) {
            startTimestampMs = WXPayUtil.getCurrentTimestampMs();
            int readTimeoutMs = remainingTimeMs - connectTimeoutMs;
            if (readTimeoutMs > 1000) {
                try {
                    lastResult = microPay(reqData, connectTimeoutMs, readTimeoutMs);
                    String returnCode = lastResult.get("return_code");
                    if (returnCode.equals("SUCCESS")) {
                        String resultCode = lastResult.get("result_code");
                        String errCode = lastResult.get("err_code");
                        if (resultCode.equals("SUCCESS")) {
                            break;
                        }
                        if (errCode.equals("SYSTEMERROR") || errCode.equals("BANKERROR") || errCode.equals("USERPAYING")) {
                            remainingTimeMs -= (int) (WXPayUtil.getCurrentTimestampMs() - startTimestampMs);
                            if (remainingTimeMs <= 100) {
                                break;
                            }
                            WXPayUtil.getLogger().info("microPayWithPos: try micropay again");
                            if (remainingTimeMs > 5000) {
                                Thread.sleep(5000L);
                                continue;
                            }
                            Thread.sleep(1000L);
                            continue;
                        }
                    }
                    break;
                } catch (Exception ex) {
                    lastResult = null;
                    lastException = ex;
                    continue;
                }
            }
            break;
        }
        if (lastResult == null) {
            throw lastException;
        }
        return lastResult;
    }

    public Map<String, String> unifiedOrder(Map<String, String> reqData) throws Exception {
        return unifiedOrder(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> unifiedOrder(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "/sandboxnew/pay/unifiedorder";
        } else {
            url = "/pay/unifiedorder";
        }
        if (this.notifyUrl != null) {
            reqData.put("notify_url", this.notifyUrl);
        }
        String respXml = requestWithoutCert(url, fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return processResponseXml(respXml);
    }

    public Map<String, String> orderQuery(Map<String, String> reqData) throws Exception {
        return orderQuery(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> orderQuery(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "/sandboxnew/pay/orderquery";
        } else {
            url = "/pay/orderquery";
        }
        String respXml = requestWithoutCert(url, fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return processResponseXml(respXml);
    }

    public Map<String, String> reverse(Map<String, String> reqData) throws Exception {
        return reverse(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> reverse(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "/sandboxnew/secapi/pay/reverse";
        } else {
            url = "/secapi/pay/reverse";
        }
        String respXml = requestWithCert(url, fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return processResponseXml(respXml);
    }

    public Map<String, String> closeOrder(Map<String, String> reqData) throws Exception {
        return closeOrder(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> closeOrder(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "/sandboxnew/pay/closeorder";
        } else {
            url = "/pay/closeorder";
        }
        String respXml = requestWithoutCert(url, fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return processResponseXml(respXml);
    }

    public Map<String, String> refund(Map<String, String> reqData) throws Exception {
        return refund(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> refund(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "/sandboxnew/secapi/pay/refund";
        } else {
            url = "/secapi/pay/refund";
        }
        String respXml = requestWithCert(url, fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return processResponseXml(respXml);
    }

    public Map<String, String> refundQuery(Map<String, String> reqData) throws Exception {
        return refundQuery(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> refundQuery(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "/sandboxnew/pay/refundquery";
        } else {
            url = "/pay/refundquery";
        }
        String respXml = requestWithoutCert(url, fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return processResponseXml(respXml);
    }

    public Map<String, String> downloadBill(Map<String, String> reqData) throws Exception {
        return downloadBill(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> downloadBill(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        Map<String, String> ret;
        if (this.useSandbox) {
            url = "/sandboxnew/pay/downloadbill";
        } else {
            url = "/pay/downloadbill";
        }
        String respStr = requestWithoutCert(url, fillRequestData(reqData), connectTimeoutMs, readTimeoutMs).trim();
        if (respStr.indexOf("<") == 0) {
            ret = WXPayUtil.xmlToMap(respStr);
        } else {
            ret = new HashMap<>();
            ret.put("return_code", "SUCCESS");
            ret.put("return_msg", "ok");
            ret.put("data", respStr);
        }
        return ret;
    }

    public Map<String, String> report(Map<String, String> reqData) throws Exception {
        return report(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> report(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "/sandboxnew/payitil/report";
        } else {
            url = "/payitil/report";
        }
        String respXml = requestWithoutCert(url, fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return WXPayUtil.xmlToMap(respXml);
    }

    public Map<String, String> shortUrl(Map<String, String> reqData) throws Exception {
        return shortUrl(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> shortUrl(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "/sandboxnew/tools/shorturl";
        } else {
            url = "/tools/shorturl";
        }
        String respXml = requestWithoutCert(url, fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return processResponseXml(respXml);
    }

    public Map<String, String> authCodeToOpenid(Map<String, String> reqData) throws Exception {
        return authCodeToOpenid(reqData, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
    }

    public Map<String, String> authCodeToOpenid(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = "/sandboxnew/tools/authcodetoopenid";
        } else {
            url = "/tools/authcodetoopenid";
        }
        String respXml = requestWithoutCert(url, fillRequestData(reqData), connectTimeoutMs, readTimeoutMs);
        return processResponseXml(respXml);
    }

    public Map<String, String> oldFacePay(Map<String, String> reqData) throws Exception {
        String url = "/pay/facepay";
        String respXml = requestWithoutCert(url, fillRequestData(reqData), this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
        return processResponseXml(respXml);
    }
}
