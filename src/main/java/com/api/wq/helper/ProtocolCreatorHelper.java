package com.api.wq.helper;

import com.api.wq.configs.HttpConfig;
//import com.api.wq.configs.MqConfig;
import com.api.wq.domain.BaseRequestParser;
import com.api.wq.domain.BaseResponseParser;
import com.api.wq.domain.GetRequestParser;
import com.api.wq.protocol.IProtocol;
import com.api.wq.protocol.http.runner.GetHttpApiRunner;
import com.api.wq.protocol.http.runner.PostHttpApiRunner;

public class ProtocolCreatorHelper {
    public static IProtocol createDefaultPostHttp() {
        PostHttpApiRunner postHttpApiRunner = new PostHttpApiRunner();
        postHttpApiRunner.setiHttpConfiger(new HttpConfig());
        postHttpApiRunner.setiRequestParser(new BaseRequestParser());
        postHttpApiRunner.setiResponseParser(new BaseResponseParser());
        return postHttpApiRunner;
    }

    public static IProtocol createDefaultGetHttp() {
        GetHttpApiRunner getHttpApiRunner = new GetHttpApiRunner();
        getHttpApiRunner.setiHttpConfiger(new HttpConfig());
        getHttpApiRunner.setiRequestParser(new GetRequestParser());
        getHttpApiRunner.setiResponseParser(new BaseResponseParser());
        return getHttpApiRunner;
    }

//    public static IProtocol createMq() {
//        return new MqHelper(MqConfig.createMqConnector());
//    }
}
