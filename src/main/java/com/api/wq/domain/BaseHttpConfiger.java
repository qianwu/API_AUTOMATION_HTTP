package com.api.wq.domain;

import com.api.wq.protocol.IProtocol;

public class BaseHttpConfiger implements IHttpConfiger {
    @Override
    public void config(IProtocol iProtocol) {

    }

    @Override
    public String configUrl(IRequest iRequest) {
        return null;
    }
}
