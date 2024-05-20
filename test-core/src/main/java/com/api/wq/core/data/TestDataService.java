package com.api.wq.core.data;

import com.alibaba.fastjson.JSON;
import com.api.wq.common.config.EvnDataConfig;
import com.api.wq.common.enums.ConfigFileEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDataService {

    @Autowired
    private EvnDataConfig evnDataConfig;

    public <T> T getData(String key,Class<T> tClass){
        String value = evnDataConfig.getProperty(ConfigFileEnum.DATA,key);
        if(StringUtils.isEmpty(value)){
            return null;
        }
        return JSON.parseObject(value,tClass);
    }
}
