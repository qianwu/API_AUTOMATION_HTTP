package com.api.wq.protocol.http.runner;

import com.api.wq.domain.IHttpConfiger;
import com.api.wq.domain.IRequest;
import com.api.wq.domain.IRequestParser;
import com.api.wq.domain.IResponseParser;
import com.api.wq.protocol.IProtocol;
import com.api.wq.protocol.http.domain.HttpApi;
import com.api.wq.protocol.http.domain.HttpResponseBase;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class HttpApiRunner implements IProtocol {

    private static final Logger logger = LoggerFactory.getLogger(HttpApiRunner.class);

    IRequestParser iRequestParser;
    IResponseParser iResponseParser;
    IHttpConfiger iHttpConfiger;
    String url;

    public IRequestParser getiRequestParser() {
        return iRequestParser;
    }

    public void setiRequestParser(IRequestParser iRequestParser) {
        this.iRequestParser = iRequestParser;
    }

    public IResponseParser getiResponseParser() {
        return iResponseParser;
    }

    public void setiResponseParser(IResponseParser iResponseParser) {
        this.iResponseParser = iResponseParser;
    }

    public IHttpConfiger getiHttpConfiger() {
        return iHttpConfiger;
    }

    public void setiHttpConfiger(IHttpConfiger iHttpConfiger) {
        this.iHttpConfiger = iHttpConfiger;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public abstract String send(IRequest iRequest);


    HttpApi httpApi = new HttpApi();
    public static Map<String,String> currentUrlMap = new HashMap<String, String>();

    /**
     * 配置服务名和URL映射
     * @param serviceName
     * @param url
     */
    public static void setCurrentServiceUrl(String serviceName,String url){
        currentUrlMap.put(serviceName,url);
    }

    /**
     * 调用request解析器将对象转换成HTTP参数
     * @param iRequest
     * @return
     */
    public String parseRequest(IRequest iRequest){
        return iRequestParser.parseRequest(iRequest);
    }

    public <T> T parseResponse(String response){
        return iResponseParser.parseResponse(response);
    }

    public void configHttp(IRequest iRequest){
        this.url = iHttpConfiger.configUrl(iRequest);
        if(StringUtils.isEmpty(this.url)){
            this.url = "";
        }
        iHttpConfiger.config(this);
    }

}
