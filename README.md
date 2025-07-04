# iotx物联网

java 8 +springboot 2.7 + rabbitmq 3.11.16

# 技术使用

消息队列：rabbitmq
mqtt：rabbitmq mqtt
websocket：websocket+前端
数据框架：mybatis+数据库事务

modbus等

iot参考学习thingsboard源码

+八股文

后端架构--混乱
1. config
   2. rabbitMQ配置
   3. rabbitMQ消费者
   4. rabbitMQ生产者
   5. websocket配置
   6. websorkcet服务端
2. controller
   3. 设备管理
   4. 测试管理
   5. 测试线程池管理
   6. 用户管理
3. corsConfig
   4. 跨域问题管理
4. enums
   5. 用户性别枚举
5. execution
   6. 计划任务执行引擎实现--线程池
   7. 命名线程工厂
6. mapper
   7. 用户mapper
7. model
   8. 消息对象
   9. RPC对象
   10. 用户对象
8. mqttConfig
   9. Mqtt配置
   11. Mqtt服务
10. redisConfig
   10. Redis配置
   11. Session配置
10. threadPoolConfig
    11. 执行管理器
    12. 调度控制器
    13. 自我调度可运行
    14. 统计指标
    15. 自定义线程池的配置类
    16. 任务统计
    17. 线程安全注解
11. IotxApplication
12. ThreadFactoryImpl

前端架构
    ├── build                      // 构建相关  
    ├── bin                        // 执行脚本
    ├── public                     // 公共文件
    │   ├── favicon.ico            // favicon图标
    │   └── index.html             // html模板
    ├── src                        // 源代码
    │   ├── App.vue                // 入口页面
    │   ├── components             // 组件
    │   │   ├── HelloWorld.vue     // 首页，测试websockt订阅数据，并通过其进行下发控制设备，并测试和后端正常接口调度
    │   ├── test.vue      // 测试页1：纯测试页
    │   └── test2.vue      // 测试页2：纯测试页
    │   ├── assets                 // 资源
    │   │   ├── logo.png               // 界面logo




