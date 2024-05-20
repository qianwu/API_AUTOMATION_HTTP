package com.api.wq.protocol.http.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.CookieStore;

import java.util.Map;

public class HttpResponseBase {

    private String response;

    private int code;

    private Map<String,String> headers;

    private CookieStore cookies;

    private Map<String,Object> parameters;

    private byte[] outputStream;

    private HttpRequestContext httpRequestContext;

    public HttpResponseBase(HttpResponseBase httpResponseBase) {
        this.response = httpResponseBase.getResponse();
        this.code = httpResponseBase.getCode();
        this.headers = httpResponseBase.getHeaders();
        this.cookies = httpResponseBase.getCookies();
    }

    public HttpResponseBase(HttpResponseBase httpResponseBase,Map<String,Object> parameters){
        this.response = httpResponseBase.getResponse();
        this.code = httpResponseBase.getCode();
        this.headers = httpResponseBase.getHeaders();
        this.cookies = httpResponseBase.getCookies();
        this.parameters = httpResponseBase.getParameters();
    }

    public HttpResponseBase(){}


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }


    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public byte[] getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(byte[] outputStream) {
        this.outputStream = outputStream;
    }

    public HttpRequestContext getHttpRequestContext() {
        return httpRequestContext;
    }

    public void setHttpRequestContext(HttpRequestContext httpRequestContext) {
        this.httpRequestContext = httpRequestContext;
    }

    public CookieStore getCookies() {
        return cookies;
    }

    public void setCookies(CookieStore cookies) {
        this.cookies = cookies;
    }

    public String getResponseDataString(){
        JSONObject dataObject = new JSONObject();
        dataObject.put("data", JSON.parseObject(getResponse()).getJSONArray("data"));
        return dataObject.toJSONString();
    }

    public String getResponseData(){return JSON.parseObject(getResponse()).get("data").toString();}
}
