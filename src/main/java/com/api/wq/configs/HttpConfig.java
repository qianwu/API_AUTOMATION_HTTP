package com.api.wq.configs;

import com.api.wq.domain.IHttpConfiger;
import com.api.wq.domain.IRequest;
import com.api.wq.helper.PropertiesRepository;
import com.api.wq.protocol.IProtocol;


public class HttpConfig  implements IHttpConfiger {
    @Override
    public void config(IProtocol iProtocol) {

    }

    @Override
    public String configUrl(IRequest iRequest) {
        if(iRequest == null){
            return null;
        }
        Class clazz = iRequest.getClass();
        String clazzName = clazz.getSimpleName();
        String url = PropertiesRepository.CONTEXTREPOSITORY.get(clazzName);
        return url;
    }
}
