package com.api.wq.protocol.http.domain;

import org.apache.http.client.config.RequestConfig;

import java.util.Map;

/**
 * @author wuqian
 * @date 2017/11/29
 */
public class HttpConfig {

    public enum HttpMode{
        /**
         * http代理模式
         */
        PROXY,
        /**
         * http dns模式
         */
        DNS,
        /**
         * 普通模式
         */
        DEFAULT
    }
    private HttpMode httpMode;

    private String httpProxyIp;

    private int httpProxyPort;

    private RequestConfig requestConfig;

    private Map<String, String[]> hostList;

    private String currentUrl;

    private Boolean logSwitch;

    public Boolean getLogSwitch() {
        return logSwitch;
    }

    public void setLogSwitch(Boolean logSwitch) {
        this.logSwitch = logSwitch;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public HttpMode getHttpMode() {
        return httpMode;
    }

    public void setHttpMode(HttpMode httpMode) {
        this.httpMode = httpMode;
    }

    public String getHttpProxyIp() {
        return httpProxyIp;
    }

    public void setHttpProxyIp(String httpProxyIp) {
        this.httpProxyIp = httpProxyIp;
    }

    public int getHttpProxyPort() {
        return httpProxyPort;
    }

    public void setHttpProxyPort(int httpProxyPort) {
        this.httpProxyPort = httpProxyPort;
    }

    public HttpConfig() {
    }

    public Map<String, String[]> getHostList() {
        return hostList;
    }

    public void setHostList(Map<String, String[]> hostList) {
        this.hostList = hostList;
    }
}
