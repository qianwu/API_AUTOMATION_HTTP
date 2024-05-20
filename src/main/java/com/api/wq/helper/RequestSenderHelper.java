package com.api.wq.helper;

import com.api.wq.domain.IRequest;
import com.api.wq.protocol.IProtocol;
import com.api.wq.protocol.http.domain.HttpResponseBase;

import java.util.Map;

public class RequestSenderHelper {
    IProtocol iProtocol;

    public RequestSenderHelper(){

    }

    public RequestSenderHelper(IProtocol iProtocal){
        this.iProtocol = iProtocal;
    }

    public String send(IRequest iRequest){
        return iProtocol.send(iRequest);
    }

    public HttpResponseBase send(String url, Map<String, Object> headers, String params) throws Exception {
        return iProtocol.send(url, headers, params);
    }

    public HttpResponseBase send(String url, Map<String, Object> headers, Map<String, Object> params) throws Exception {
        return iProtocol.send(url, headers, params);
    }

    public static RequestSenderHelper defaultPostHttpRequestSender(){
        return new RequestSenderHelper(ProtocolCreatorHelper.createDefaultPostHttp());
    }

    public static RequestSenderHelper defaultGetHttpRequestSender(){
        return new RequestSenderHelper(ProtocolCreatorHelper.createDefaultGetHttp());
    }

//    public static RequestSenderHelper defaultMqRequestSender(){
//        return new RequestSenderHelper(ProtocolCreatorHelper.createMq());
//    }
}
