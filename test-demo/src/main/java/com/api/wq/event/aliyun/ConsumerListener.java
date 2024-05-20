package com.api.wq.event.aliyun;

import com.aliyun.openservices.ons.api.*;
import com.api.wq.config.RocketMQConfig;
import com.api.wq.event.model.DiscountInfo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class ConsumerListener {
    Gson gson = new Gson();
    private static AtomicInteger count = new AtomicInteger();
    private static long startTime = 0L;
    private static long endTime = 0L;

    @Autowired
    private RocketMQConfig rocketMQConfig;


    @PostConstruct
    public void init(){
        Consumer consumer = ONSFactory.createConsumer(rocketMQConfig.getMqProperties());
        consumer.subscribe("eventbus-demo-0720","*",this::onReceive);

        consumer.start();
    }

    public Action onReceive(Message message, ConsumeContext context) {
        log.info("=====> begin to receive");
        byte[] payload = message.getBody();
        log.info("==== reveive : " + new String(payload));
        log.info("当前接受到消息数量为:" + count.getAndIncrement());
        if(startTime == 0L){
            startTime = System.currentTimeMillis();
            log.info("开始时间：" + startTime + "ms");
        }
        if (count.intValue() == 100L) {
            endTime = System.currentTimeMillis();
            log.info("结束时间：" + endTime + "ms");
            log.info("耗时 : " + (endTime - startTime) + "ms");
        }
        return Action.CommitMessage;
    }

}
