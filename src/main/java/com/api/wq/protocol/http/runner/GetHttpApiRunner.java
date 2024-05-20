package com.api.wq.protocol.http.runner;

import com.api.wq.domain.IRequest;
import com.api.wq.protocol.http.domain.HttpResponseBase;

import java.util.HashMap;
import java.util.Map;

public class GetHttpApiRunner extends HttpApiRunner {

    @Override
    public String send(IRequest iRequest) {
        configHttp(iRequest);
        return doGet(iRequest);
    }

    @Override
    public HttpResponseBase send(String url, Map<String, Object> headers, String params) throws Exception {
        return httpApi.get(url,headers,params);
    }

    @Override
    public HttpResponseBase send(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
        throw new Exception("Illegal method for get");
    }

    /**
     * 通过request对象直接发请求
     */
    private <T> T doGet(IRequest iRequest){
        Map<String,Object> headers = new HashMap<>(16);
        String reqParams = parseRequest(iRequest);
        HttpResponseBase httpResponseBase = httpApi.get(url,headers,reqParams);
        String response = httpResponseBase.getResponse();
        return parseResponse(response);
    }
}
