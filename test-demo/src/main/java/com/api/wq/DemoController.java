package com.api.wq;

import com.alibaba.fastjson.JSON;
import com.api.wq.common.config.GlobalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private GlobalProperties properties;

    @GetMapping("/config")
    public String config(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(JSON.toJSONString(properties.getEvn()));
        return stringBuilder.toString();
    }
}
