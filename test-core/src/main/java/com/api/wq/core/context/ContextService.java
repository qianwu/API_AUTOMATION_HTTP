package com.api.wq.core.context;

import com.api.wq.common.config.EvnDataConfig;
import com.api.wq.common.enums.ConfigFileEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContextService {
    @Autowired
    private EvnDataConfig evnDataConfig;

    public String getContext(String key){
        return evnDataConfig.getProperty(ConfigFileEnum.CONTEXT,key);
    }

}
