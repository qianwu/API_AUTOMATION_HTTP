package com.api.wq.domain;

import com.alibaba.fastjson.JSON;

public class BaseRequestParser implements IRequestParser {

    @Override
    public String parseRequest(IRequest iRequest) {
        return JSON.toJSONString(iRequest) ;
    }
}
