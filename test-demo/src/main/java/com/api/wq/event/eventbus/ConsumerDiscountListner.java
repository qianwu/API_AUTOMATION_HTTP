package com.api.wq.event.eventbus;

import com.api.wq.event.model.DiscountInfo;
import com.ikea.eventbus.mq.MsgRetryStatus;
import com.ikea.eventbus.mq.consumer.IESMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qiawu7
 */
@Slf4j
@Component
public class ConsumerDiscountListner extends IESMessageListener<DiscountInfo> {
    private static AtomicInteger count = new AtomicInteger();
    private static long startTime = 0L;
    private static long endTime = 0L;

    @Override
    protected String getConsumerGroupName() {
//        return "GID_MAS_QA_NUM2";
        return "GID_MAS_EVENT_BUS_CLIENT";
    }

    @Override
    public MsgRetryStatus onReceive(DiscountInfo message) {
        log.info("==== reveive : " + message);
        log.info("当前接受到消息数量为:" + count.getAndIncrement());
        if(startTime == 0L){
            startTime = System.currentTimeMillis();
            log.info("开始时间：" + startTime + "ms");
        }
        if (count.intValue() == 20000) {
            endTime = System.currentTimeMillis();
            log.info("结束时间：" + endTime + "ms");
            log.info("耗时 : " + (endTime - startTime) + "ms");
        }
        return MsgRetryStatus.SUCCEED;
    }

    @Override
    public MsgRetryStatus onReceive(List<DiscountInfo> messages) {
        return super.onReceive(messages);
    }
}
