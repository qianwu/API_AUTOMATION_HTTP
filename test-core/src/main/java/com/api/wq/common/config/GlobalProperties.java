package com.api.wq.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 全局配置
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "api")
public class GlobalProperties {

    /**
     * 当前的环境
     *
     */
    private String evn;

}
