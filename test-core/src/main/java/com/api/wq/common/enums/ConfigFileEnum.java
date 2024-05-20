package com.api.wq.common.enums;

public enum ConfigFileEnum {
    CONTEXT("context.properties","配置"),
    DATA("data.properties","测试数据")
    ;

    private String code;
    private String desc;

    ConfigFileEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
