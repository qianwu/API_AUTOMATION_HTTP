package com.api.wq.common.config;

import com.alibaba.fastjson.JSON;
import com.api.wq.common.enums.ConfigFileEnum;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EvnDataConfig {


    /**
     * key = fileName
     *   key = properties 文件里的key
     */
    private static Map<ConfigFileEnum,Map<String,String>> MAP = new HashMap<>();

    @Autowired
    private GlobalProperties globalProperties;


    /**
     * 初始化
     */
    @PostConstruct
    public void init() throws IOException {
        for(ConfigFileEnum fileEnum : ConfigFileEnum.values()){
            // 读取内容
            ClassPathResource classPathResource = new ClassPathResource("evn/"+globalProperties.getEvn()+"/" + fileEnum.getCode());
            List<String> lineList = FileUtils.readLines(classPathResource.getFile(),"utf-8");
            Map<String,String> map = new HashMap<>();
            for(String line : lineList){
                String[] ary = line.split("=");
                if(ary.length != 2){
                    continue;
                }
                map.put(ary[0],ary[1]);
            }
            MAP.put(fileEnum,map);
        }
    }

    /**
     *
     * @param key
     * @return
     */
    public String getProperty(ConfigFileEnum fileType,String key){
        if(!MAP.containsKey(fileType)){
            return "";
        }
        if(MAP.get(fileType).containsKey(key)){
            return MAP.get(fileType).get(key);
        }
        return "";
    }





}
