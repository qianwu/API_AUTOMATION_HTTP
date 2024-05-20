package com.api.wq.domain;

import com.api.wq.protocol.IProtocol;

public interface IHttpConfiger {
    void config(IProtocol iProtocol);

    String configUrl(IRequest iRequest);
}
