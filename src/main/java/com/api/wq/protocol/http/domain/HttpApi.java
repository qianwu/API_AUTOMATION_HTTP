package com.api.wq.protocol.http.domain;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class HttpApi {

    private HttpUtil httpUtil = new HttpUtil();

    private HttpConfig httpConfig = new HttpConfig();

    public HttpApi(HttpConfig httpConfig) {
        this.httpConfig = httpConfig;
        httpUtil = new HttpUtil(httpConfig);
    }

    public HttpApi() {
        httpConfig.setLogSwitch(true);
        httpUtil = new HttpUtil(httpConfig);
    }

    public HttpApi(Map<String, String[]> currentHostList) {
        httpConfig.setLogSwitch(true);
        httpUtil = new HttpUtil(httpConfig, currentHostList);
    }

    public HttpResponseBase get(String url, Map<String, Object> headers, String params) {
        return httpUtil.get(url, headers, params);
    }


    public HttpResponseBase get(String url) {
        return httpUtil.get(url, new HashMap<>(), new HashMap<>());
    }

    public  HttpResponseBase post(String url, Map<String, Object> headers, Map<String, Object> params){
        return httpUtil.post(url,headers,params);
    }

    public HttpResponseBase post(String url, Map<String, Object> headers, String body) {
        return httpUtil.post(url, headers, body);
    }

    public HttpResponseBase post(String url) {
        return httpUtil.post(url, new HashMap<>(), "");
    }

    public <T> T getForBean(String url, Map<String, Object> headers, Map<String, Object> params, Class<T> clazz) {
        return JSON.parseObject(httpUtil.get(url, headers, params).getResponse(), clazz);
    }

    public <T> T getForBean(String url, Class<T> clazz) {
        return JSON.parseObject(httpUtil.get(url, new HashMap<>(), new HashMap<>()).getResponse(), clazz);
    }

    public <T> T postForBean(String url, Map<String, Object> headers, Map<String, Object> params, Class<T> clazz) {
        return JSON.parseObject(httpUtil.post(url, headers, params).getResponse(), clazz);
    }

    public <T> T postForBean(String url, Map<String, Object> headers, String body, Class<T> clazz) {
        return JSON.parseObject(httpUtil.post(url, headers, body).getResponse(), clazz);
    }

    public <T> T postForBean(String url, Class<T> clazz) {
        return JSON.parseObject(httpUtil.post(url, new HashMap<>(), "").getResponse(), clazz);
    }

}
