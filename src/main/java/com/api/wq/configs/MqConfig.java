//package com.api.wq.configs;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.ResourceBundle;
//
//public class MqConfig {
//    private static Logger logger = LoggerFactory.getLogger(MqConfig.class);
////    static MqConnector mqConnector;
//    static ResourceBundle resourceBundle = PropertyUtil.getResourceBundle("selectedEnv/touda");
//
//    public static MqConnector createMqConnector() {
//        String host = resourceBundle.getString("touda.mq.pcsa.host");
//        int port = Integer.parseInt(resourceBundle.getString("touda.mq.pcsa.port"));
//        String qmr = resourceBundle.getString("touda.mq.pcsa.qmr");
//        String cname = resourceBundle.getString("touda.mq.pcsa.cname");
//        String rqueue = resourceBundle.getString("touda.mq.pcsa.rqueue");
//        int ccsid = Integer.valueOf(resourceBundle.getString("touda.mq.pcsa.ccsid"));
//
//        if (null == host || "".equals(host)) {
//            throw new IllegalArgumentException("mq host is null");
//        }
//        if (null == qmr || "".equals(qmr)) {
//            throw new IllegalArgumentException("MQ队列管理器名称为空");
//        }
//        if (null == cname || "".equals(cname)) {
//            throw new IllegalArgumentException("MQ通道名称为空");
//        }
//        if (null == rqueue || "".equals(rqueue)) {
//            throw new IllegalArgumentException("MQ请求队列名称为空");
//        }
//        try {
//            mqConnector = new MqConnector("pcsa", host, port, qmr, cname, rqueue, ccsid);
//        } catch (Exception e) {
//            logger.error("初始化MQ时出现异常：" + e.getMessage(), e);
//        }
//        return mqConnector;
//    }
//}
