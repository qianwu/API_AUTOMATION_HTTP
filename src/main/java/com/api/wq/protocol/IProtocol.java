package com.api.wq.protocol;


import com.api.wq.domain.IRequest;
import com.api.wq.protocol.http.domain.HttpResponseBase;

import java.util.Map;

public interface IProtocol {

    String send(IRequest iRequest);

    HttpResponseBase send(String url, Map<String,Object> headers, String params) throws Exception;

    HttpResponseBase send(String url, Map<String,Object> headers,Map<String,Object> params) throws Exception;
}
