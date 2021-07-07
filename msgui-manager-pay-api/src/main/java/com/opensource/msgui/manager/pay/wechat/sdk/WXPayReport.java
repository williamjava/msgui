package com.opensource.msgui.manager.pay.wechat.sdk;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

public class WXPayReport {
    private static final String REPORT_URL = "http://report.mch.weixin.qq.com/wxpay/report/default";

    private static final int DEFAULT_CONNECT_TIMEOUT_MS = 6000;

    private static final int DEFAULT_READ_TIMEOUT_MS = 8000;

    public static class ReportInfo {
        private String version = "v1";

        private String sdk = "WXPaySDK/3.0.9";

        private String uuid;

        private long timestamp;

        private long elapsedTimeMillis;

        private String firstDomain;

        private boolean primaryDomain;

        private int firstConnectTimeoutMillis;

        private int firstReadTimeoutMillis;

        private int firstHasDnsError;

        private int firstHasConnectTimeout;

        private int firstHasReadTimeout;

        public ReportInfo(String uuid, long timestamp, long elapsedTimeMillis, String firstDomain, boolean primaryDomain, int firstConnectTimeoutMillis, int firstReadTimeoutMillis, boolean firstHasDnsError, boolean firstHasConnectTimeout, boolean firstHasReadTimeout) {
            this.uuid = uuid;
            this.timestamp = timestamp;
            this.elapsedTimeMillis = elapsedTimeMillis;
            this.firstDomain = firstDomain;
            this.primaryDomain = primaryDomain;
            this.firstConnectTimeoutMillis = firstConnectTimeoutMillis;
            this.firstReadTimeoutMillis = firstReadTimeoutMillis;
            this.firstHasDnsError = firstHasDnsError ? 1 : 0;
            this.firstHasConnectTimeout = firstHasConnectTimeout ? 1 : 0;
            this.firstHasReadTimeout = firstHasReadTimeout ? 1 : 0;
        }

        @Override
        public String toString() {
            return "ReportInfo{version='" + this.version + '\'' + ", sdk='" + this.sdk + '\'' + ", uuid='" + this.uuid + '\'' + ", timestamp=" + this.timestamp + ", elapsedTimeMillis=" + this.elapsedTimeMillis + ", firstDomain='" + this.firstDomain + '\'' + ", primaryDomain=" + this.primaryDomain + ", firstConnectTimeoutMillis=" + this.firstConnectTimeoutMillis + ", firstReadTimeoutMillis=" + this.firstReadTimeoutMillis + ", firstHasDnsError=" + this.firstHasDnsError + ", firstHasConnectTimeout=" + this.firstHasConnectTimeout + ", firstHasReadTimeout=" + this.firstHasReadTimeout + '}';
        }

        public String toLineString(String key) {
            String separator = ",";
            Object[] objects = {
                    this.version, this.sdk, this.uuid, Long.valueOf(this.timestamp), Long.valueOf(this.elapsedTimeMillis), this.firstDomain, Boolean.valueOf(this.primaryDomain), Integer.valueOf(this.firstConnectTimeoutMillis), Integer.valueOf(this.firstReadTimeoutMillis), Integer.valueOf(this.firstHasDnsError),
                    Integer.valueOf(this.firstHasConnectTimeout), Integer.valueOf(this.firstHasReadTimeout)};
            StringBuffer sb = new StringBuffer();
            for (Object obj : objects) {
                sb.append(obj).append(separator);
            }
            try {
                String sign = WXPayUtil.HMACSHA256(sb.toString(), key);
                sb.append(sign);
                return sb.toString();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    private LinkedBlockingQueue<String> reportMsgQueue = null;

    private WXPayConfig config;

    private ExecutorService executorService;

    private static volatile WXPayReport INSTANCE;

    private WXPayReport(final WXPayConfig config) {
        this.config = config;
        this.reportMsgQueue = new LinkedBlockingQueue<>(config.getReportQueueMaxSize());
        this.executorService = Executors.newFixedThreadPool(config.getReportWorkerNum(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            }
        });
        if (config.shouldAutoReport()) {
            WXPayUtil.getLogger().info("report worker num: {}", Integer.valueOf(config.getReportWorkerNum()));
            for (int i = 0; i < config.getReportWorkerNum(); i++) {
                this.executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                StringBuffer sb = new StringBuffer();
                                String firstMsg = WXPayReport.this.reportMsgQueue.take();
                                WXPayUtil.getLogger().info("get first report msg: {}", firstMsg);
                                String msg = null;
                                sb.append(firstMsg);
                                int remainNum = config.getReportBatchSize() - 1;
                                for (int j = 0; j < remainNum; j++) {
                                    WXPayUtil.getLogger().info("try get remain report msg");
                                    msg = WXPayReport.this.reportMsgQueue.take();
                                    WXPayUtil.getLogger().info("get remain report msg: {}", msg);
                                    if (msg == null) {
                                        break;
                                    }
                                    sb.append("\n");
                                    sb.append(msg);
                                }
                                WXPayReport.httpRequest(sb.toString(), 6000, 8000);
                            } catch (Exception ex) {
                                WXPayUtil.getLogger().warn("report fail. reason: {}", ex.getMessage());
                            }
                        }
                    }
                });
            }
        }
    }

    public static WXPayReport getInstance(WXPayConfig config) {
        if (INSTANCE == null) {
            synchronized (WXPayReport.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPayReport(config);
                }
            }
        }
        return INSTANCE;
    }

    public void report(String uuid, long elapsedTimeMillis, String firstDomain, boolean primaryDomain, int firstConnectTimeoutMillis, int firstReadTimeoutMillis, boolean firstHasDnsError, boolean firstHasConnectTimeout, boolean firstHasReadTimeout) {
        long currentTimestamp = WXPayUtil.getCurrentTimestamp();
        ReportInfo reportInfo = new ReportInfo(uuid, currentTimestamp, elapsedTimeMillis, firstDomain, primaryDomain, firstConnectTimeoutMillis, firstReadTimeoutMillis, firstHasDnsError, firstHasConnectTimeout, firstHasReadTimeout);
        String data = reportInfo.toLineString(this.config.getKey());
        WXPayUtil.getLogger().info("report {}", data);
        if (data != null) {
            this.reportMsgQueue.offer(data);
        }
    }

    @Deprecated
    private void reportSync(String data) throws Exception {
        httpRequest(data, 6000, 8000);
    }

    @Deprecated
    private void reportAsync(final String data) throws Exception {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    WXPayReport.httpRequest(data, 6000, 8000);
                } catch (Exception ex) {
                    WXPayUtil.getLogger().warn("report fail. reason: {}", ex.getMessage());
                }
            }
        })).start();
    }

    private static String httpRequest(String data, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager((Lookup) RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build(), null, null, null);
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().setConnectionManager((HttpClientConnectionManager) connManager).build();
        HttpPost httpPost = new HttpPost("http://report.mch.weixin.qq.com/wxpay/report/default");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
        httpPost.setConfig(requestConfig);
        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", WXPayConstants.USER_AGENT);
        httpPost.setEntity((HttpEntity) postEntity);
        HttpResponse httpResponse = closeableHttpClient.execute((HttpUriRequest) httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");
    }
}
