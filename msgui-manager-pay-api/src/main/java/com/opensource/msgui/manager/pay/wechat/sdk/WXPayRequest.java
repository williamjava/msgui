package com.opensource.msgui.manager.pay.wechat.sdk;

import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class WXPayRequest {
    private WXPayConfig config;

    public WXPayRequest(WXPayConfig config) throws Exception {
        this.config = config;
    }

    private String requestOnce(String domain, String urlSuffix, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean useCert) throws Exception {
        BasicHttpClientConnectionManager connManager;
        if (useCert) {
            char[] password = this.config.getMchID().toCharArray();
            InputStream certStream = this.config.getCertStream();
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certStream, password);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1"}, null, (HostnameVerifier) new DefaultHostnameVerifier());
            connManager = new BasicHttpClientConnectionManager((Lookup) RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslConnectionSocketFactory).build(), null, null, null);
        } else {
            connManager = new BasicHttpClientConnectionManager((Lookup) RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build(), null, null, null);
        }
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().setConnectionManager((HttpClientConnectionManager) connManager).build();
        String url = "https://" + domain + urlSuffix;
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
        httpPost.setConfig(requestConfig);
        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", WXPayConstants.USER_AGENT + " " + this.config.getMchID());
        httpPost.setEntity((HttpEntity) postEntity);
        HttpResponse httpResponse = closeableHttpClient.execute((HttpUriRequest) httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");
    }

    private String request(String urlSuffix, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean useCert, boolean autoReport) throws Exception {
        Exception exception = null;
        long elapsedTimeMillis = 0L;
        long startTimestampMs = WXPayUtil.getCurrentTimestampMs();
        boolean firstHasDnsErr = false;
        boolean firstHasConnectTimeout = false;
        boolean firstHasReadTimeout = false;
        IWXPayDomain.DomainInfo domainInfo = this.config.getWXPayDomain().getDomain(this.config);
        if (domainInfo == null) {
            throw new Exception("WXPayConfig.getWXPayDomain().getDomain() is empty or null");
        }
        try {
            String result = requestOnce(domainInfo.domain, urlSuffix, uuid, data, connectTimeoutMs, readTimeoutMs, useCert);
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
            this.config.getWXPayDomain().report(domainInfo.domain, elapsedTimeMillis, null);
            WXPayReport.getInstance(this.config).report(uuid, elapsedTimeMillis, domainInfo.domain, domainInfo.primaryDomain, connectTimeoutMs, readTimeoutMs, firstHasDnsErr, firstHasConnectTimeout, firstHasReadTimeout);
            return result;
        } catch (UnknownHostException ex) {
            exception = ex;
            firstHasDnsErr = true;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
            WXPayUtil.getLogger().warn("UnknownHostException for domainInfo {}", domainInfo);
            WXPayReport.getInstance(this.config).report(uuid, elapsedTimeMillis, domainInfo.domain, domainInfo.primaryDomain, connectTimeoutMs, readTimeoutMs, firstHasDnsErr, firstHasConnectTimeout, firstHasReadTimeout);
        } catch (ConnectTimeoutException ex) {
            ConnectTimeoutException connectTimeoutException1 = ex;
            firstHasConnectTimeout = true;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
            WXPayUtil.getLogger().warn("connect timeout happened for domainInfo {}", domainInfo);
            WXPayReport.getInstance(this.config).report(uuid, elapsedTimeMillis, domainInfo.domain, domainInfo.primaryDomain, connectTimeoutMs, readTimeoutMs, firstHasDnsErr, firstHasConnectTimeout, firstHasReadTimeout);
        } catch (SocketTimeoutException ex) {
            exception = ex;
            firstHasReadTimeout = true;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
            WXPayUtil.getLogger().warn("timeout happened for domainInfo {}", domainInfo);
            WXPayReport.getInstance(this.config).report(uuid, elapsedTimeMillis, domainInfo.domain, domainInfo.primaryDomain, connectTimeoutMs, readTimeoutMs, firstHasDnsErr, firstHasConnectTimeout, firstHasReadTimeout);
        } catch (Exception ex) {
            exception = ex;
            elapsedTimeMillis = WXPayUtil.getCurrentTimestampMs() - startTimestampMs;
            WXPayReport.getInstance(this.config).report(uuid, elapsedTimeMillis, domainInfo.domain, domainInfo.primaryDomain, connectTimeoutMs, readTimeoutMs, firstHasDnsErr, firstHasConnectTimeout, firstHasReadTimeout);
        }
        this.config.getWXPayDomain().report(domainInfo.domain, elapsedTimeMillis, exception);
        throw exception;
    }

    public String requestWithoutCert(String urlSuffix, String uuid, String data, boolean autoReport) throws Exception {
        return request(urlSuffix, uuid, data, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs(), false, autoReport);
    }

    public String requestWithoutCert(String urlSuffix, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean autoReport) throws Exception {
        return request(urlSuffix, uuid, data, connectTimeoutMs, readTimeoutMs, false, autoReport);
    }

    public String requestWithCert(String urlSuffix, String uuid, String data, boolean autoReport) throws Exception {
        return request(urlSuffix, uuid, data, this.config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs(), true, autoReport);
    }

    public String requestWithCert(String urlSuffix, String uuid, String data, int connectTimeoutMs, int readTimeoutMs, boolean autoReport) throws Exception {
        return request(urlSuffix, uuid, data, connectTimeoutMs, readTimeoutMs, true, autoReport);
    }
}
