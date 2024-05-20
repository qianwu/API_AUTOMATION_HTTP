package com.api.wq.domain;

public class GetRequestParser extends BaseRequestParser {

    @Override
    public String parseRequest(IRequest iRequest){
        return iRequest.toString();
    }

}
