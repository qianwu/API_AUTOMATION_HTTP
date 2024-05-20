package com.api.wq;

import com.api.wq.common.config.GlobalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest(classes = {DemoApplication.class})
public class BaseTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private GlobalProperties globalProperties;

    @Test
    public void t1(){
        System.out.println("当前环境：" + globalProperties.getEvn());
    }
}
