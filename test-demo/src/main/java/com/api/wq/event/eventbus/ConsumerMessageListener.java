//package com.api.wq.event;
//
//import com.api.wq.domain.UserRequest;
//import com.ikea.eventbus.mq.MsgRetryStatus;
//import com.ikea.eventbus.mq.consumer.IESMessageListener;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * @author qiawu7
// */
//@Slf4j
//@Component
//public class ConsumerMessageListener extends IESMessageListener<UserRequest> {
//    private AtomicInteger count = new AtomicInteger();
//
//    @Override
//    public MsgRetryStatus onReceive(UserRequest message){
//        log.info("==== reveive : " + message);
//        count.getAndIncrement();
//        log.info("当前接受到消息数量为:" + count);
//        return MsgRetryStatus.SUCCEED;
//    }
//
//    @Override
//    protected String getConsumerGroupName() {
//        return "GID_MAS_QA_NUM2";
//    }
//}
