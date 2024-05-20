//package com.api.wq.testng.report;
//
//
//import com.youku.autotest.common.enums.*;
//import com.youku.autotest.common.function.TestAssert;
//import com.youku.autotest.common.function.TestRecord;
//import com.youku.ykqa.arsenal.core.testng.checker.AssertService;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//
//
//    private CaseRecordImp() {
//    }
//
//    private static CaseRecordImp instance = new CaseRecordImp();
//
//    public static CaseRecordImp getInstance() {
//        return instance;
//    }
//
//    private List<TestRecord> testRecords = new ArrayList<>();
//
//    private TestReportExtend testReportExtend = new TestReportExtend();
//
//    public static void setInstance(CaseRecordImp instance) {
//        CaseRecordImp.instance = instance;
//    }
//
//    public TestReportExtend getTestReportExtend() {
//        return testReportExtend;
//    }
//
//    public void setTestReportExtend(TestReportExtend testReportExtend) {
//        this.testReportExtend = testReportExtend;
//    }
//
//    private void record(LogLevelEnum level, RecordTypeEnum recordType, ContentTypeEnum contentType, String msg) {
//        TestRecord tr = new TestRecord();
//        tr.setMsg(msg);
//        tr.setTime(new Date());
//        tr.setLevel(level);
//        tr.setType(recordType);
//        tr.setContentType(contentType);
//        if (recordType != RecordTypeEnum.FLAG) {
//            System.out.println(tr);
//        }
//        // 根据loglevel的长度限制，截取超长日志
//        if (level.getMaxLength() != 0 && msg.length() > level.getMaxLength()) {
//            msg = StringUtils.substring(msg, 0, level.getMaxLength()) + "...";
//        }
//        tr.setMsg(msg);
//        // trace日志不作为testreport的步骤
//        if (level != LogLevelEnum.TRACE) {
//            testRecords.add(tr);
//        }
//    }
//
//    @Override
//    public void info(String msg) {
//        log(LogLevelEnum.INFO, ContentTypeEnum.TXT, msg);
//    }
//
//    @Override
//    public void warn(String msg) {
//        log(LogLevelEnum.WARN, ContentTypeEnum.TXT, msg);
//    }
//
//    @Override
//    public void debug(String msg) {
//        log(LogLevelEnum.DEBUG, ContentTypeEnum.TXT, msg);
//    }
//
//    @Override
//    public void error(String msg) {
//        log(LogLevelEnum.ERROR, ContentTypeEnum.TXT, msg);
//    }
//
//    @Override
//    public void trace(String msg) {
//        log(LogLevelEnum.TRACE, ContentTypeEnum.TXT, msg);
//    }
//
//    public void reset() {
//        setTestRecords(new ArrayList<>());
//    }
//
//    public List<TestRecord> getTestRecords() {
//        return new ArrayList<>(testRecords);
//    }
//
//    public void setTestRecords(List<TestRecord> testRecords) {
//        this.testRecords = testRecords;
//    }
//
//    public void logFlag(String msg) {
//        // 测试标记必须为debug，否则不能插入测试步骤
//        record(LogLevelEnum.DEBUG, RecordTypeEnum.FLAG, ContentTypeEnum.TXT, msg);
//    }
//
//    public void logAssert(TestAssert ta) {
//        String msg = String.format("%s %s, 期望值: %s, 实际值: %s", ta.getOperator().getDescription(), ta.getDescription(), ta.getExpect(), ta.getActual());
//        if (ta.getOperator() == AssertEnum.FAIL) {
//            msg = String.format("%s %s", ta.getOperator().getDescription(), ta.getDescription());
//        }
//        if (ta.getStatus() == ResultEnum.PASS) {
//            record(LogLevelEnum.INFO, RecordTypeEnum.ASSERT, ta.getContentType(), msg);
//        } else if (ta.getStatus() == ResultEnum.FAIL) {
//            record(LogLevelEnum.ERROR, RecordTypeEnum.ASSERT, ta.getContentType(), msg);
//        }
//        AssertService.addAssert(ta);
//    }
//
//    public void logOperate(LogLevelEnum logLevel, ContentTypeEnum contentType, String msg) {
//        record(logLevel, RecordTypeEnum.OPERATE, contentType, msg);
//    }
//
//    @Override
//    public void log(LogLevelEnum logLevel, ContentTypeEnum contentType, String msg) {
//        record(logLevel, RecordTypeEnum.LOG, contentType, msg);
//    }
//
//
//
//}
