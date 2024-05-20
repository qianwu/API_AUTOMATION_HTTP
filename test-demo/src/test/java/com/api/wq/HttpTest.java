package com.api.wq;

import com.alibaba.fastjson.JSON;
import com.api.wq.core.context.ContextService;
import com.api.wq.core.data.TestDataService;
import com.api.wq.core.http.HttpUtils;
import com.api.wq.domain.UserRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class HttpTest extends BaseTest{

    /**
     * 数据服务
     */
    @Autowired
    private TestDataService testDataService;

    /**
     * 接口配置
     */
    @Autowired
    private ContextService contextService;

    @SneakyThrows
    @Test
    public void t1(){

        System.out.println();

        String url = contextService.getContext("web.site.url");
        UserRequest request = testDataService.getData("case1.param",UserRequest.class);

        System.out.println(url);
        System.out.println(JSON.toJSONString(request));

        // 发送http 请求
        String result = HttpUtils.httpGet(url);
        System.out.println(result);

    }

}
