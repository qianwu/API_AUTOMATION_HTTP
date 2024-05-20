package com.api.wq.protocol.http.runner;

import com.api.wq.domain.IRequest;
import com.api.wq.protocol.http.domain.HttpResponseBase;

import java.util.HashMap;
import java.util.Map;

public class PostHttpApiRunner extends HttpApiRunner{

    @Override
    public String send(IRequest iRequest) {
        configHttp(iRequest);
        return doPost(iRequest);
    }

    @Override
    public HttpResponseBase send(String url, Map<String, Object> headers, String params) throws Exception {
        return httpApi.post(url,headers,params);
    }

    @Override
    public HttpResponseBase send(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
        return httpApi.post(url,headers,params);
    }

    /**
     * 通过request对象直接发HTTP请求
     * @param iRequest
     * @param <T>
     * @return
     */
    private <T> T doPost(IRequest iRequest){
        Map<String,Object> headers = new HashMap<>(16);
        String reqParams = parseRequest(iRequest);
        HttpResponseBase httpResponseBase = httpApi.post(url,headers,reqParams);
        String response = httpResponseBase.getResponse();
        return parseResponse(response);
    }
}
