# apiTest

#### 介绍
api-test
test-admin 管理平台
test-core 测试框架的核心。
    common 核心模块中公用的基础类 
        constant 基础常量
        utils 基础工具类
    core 核心功能区
        assert 断言核心
        data 数据源，测试数据相关
        evn 环境解析，解析当前的测试环境
        http http api 测试核心模块
        notify 测试结果通知核心模块,如：钉钉，微信，邮件
        report 测试报告模块
    domain 基础域对象
        
test-demo 引入测试框架的demo例子

#### 软件架构
软件架构说明


#### 安装教程

1. mvn clean install -DskipTests 打包整个应用
2. deploy test-core 到中央仓库
3. 其他项目引用

#### 使用说明


