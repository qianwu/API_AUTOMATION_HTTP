package com.api.wq.protocol.http.domain;

import java.util.Map;

public class HttpRequestContext {

    private String url;

    private Map<String,Object> headers;

    private Map<String,Object> params;

    private String body;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpRequestContext{" + "url=\""+url +"\"" + ", headers=" + headers + ", params=" + params + ", body =\"" + body + "\""+'}';
    }
}
