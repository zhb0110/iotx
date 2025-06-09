package com.example.iotx.init;

import com.example.iotx.threadPoolConfig.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 初始化业务数据
 *
 * @Author Zhb
 * @create 2024/7/26 下午6:27
 */
//@Order标记定义了组件的加载顺序。
@Component
@Order(1) //业务初始化组件（业务处理）
public class BusinessInitComponent implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BusinessInitComponent.class);

    @Autowired
    private ThreadPoolUtil threadPoolUtil;


    @Override
    public void run(String... args) throws Exception {

        //线程池-周期报告数据
//        threadPoolUtil.scheduleWithFixedDelay(new Runnable()
//        {
//            public void run() {
//                try {
//                    LocalDateTime now = LocalDateTime.now();
//                    //1如果 整点或者59分时 存储日统计
//                    if ((now.getMinute() == 0 || now.getMinute() == 59) && (BusinessInitComponent.this.lastStatisticsTime == null || Duration.between(BusinessInitComponent.this.lastStatisticsTime, now).toMillis() > 65000L))
//                    {
//
//                        GlobalVariables.GLOBAL_SCHEDULED_SERVICE_POOL.execute(new Runnable()
//                        {
//                            public void run() {
//                                try {
//                                    GlobalVariables.GLOBAL_DATA_STORAGE_SERVICE.storeDayStatics(); //调用-存储日统计
//                                } catch (Exception e) {
//                                    logger.error("Global configuration error occured when periodically report data.", e);
//                                }
//                            }
//                        });
//                        BusinessInitComponent.this.lastStatisticsTime = now;
//                    }
//                    //2如果 5分钟的倍数，执行不同的周期报告数据
//                    if (now.getMinute() % 5 == 0 && (BusinessInitComponent.this.last5MinutesTaskTime == null || Duration.between(BusinessInitComponent.this.last5MinutesTaskTime, now).toMinutes() > 2L))
//                    {
//                        System.arraycopy(GlobalVariables.GLOBAL_MEMORY_BYTES, 0, GlobalVariables.GLOBAL_MEMORY_BYTES_COPY, 0, 80000000);
//                        //整5分钟执行一次报告数据
//                        GlobalVariables.GLOBAL_SCHEDULED_SERVICE_POOL.execute(new Runnable()
//                        {
//                            public void run() {
//                                try {
//                                    GlobalVariables.GLOBAL_DATA_STORAGE_SERVICE.periodReportData("5分钟"); ///调用
//                                } catch (Exception ex) {
//                                    logger.error("Global configuration error occured when periodically report data.", ex);
//                                }
//                            }
//                        });
//                        BusinessInitComponent.this.last5MinutesTaskTime = now;
//                        //15分钟执行一次报告数据
//                        if (now.getMinute() % 15 == 0 && (BusinessInitComponent.this.last15MinutesTaskTime == null || Duration.between(BusinessInitComponent.this.last15MinutesTaskTime, now).toMinutes() > 2L)) {
//                            GlobalVariables.GLOBAL_SCHEDULED_SERVICE_POOL.execute(new Runnable()
//                            {
//                                public void run() {
//                                    try {
//                                        GlobalVariables.GLOBAL_DATA_STORAGE_SERVICE.periodReportData("15分钟");
//                                    } catch (Exception ex) {
//                                        logger.error("Global configuration error occured when periodically report data.", ex);
//                                    }
//                                }
//                            });
//                            BusinessInitComponent.this.last15MinutesTaskTime = now;
//                        }
//                        //30分钟执行一次报告数据
//                        if (now.getMinute() % 30 == 0 && (BusinessInitComponent.this.last30MinutesTaskTime == null || Duration.between(BusinessInitComponent.this.last30MinutesTaskTime, now).toMinutes() > 2L)) {
//                            GlobalVariables.GLOBAL_SCHEDULED_SERVICE_POOL.execute(new Runnable()
//                            {
//                                public void run() {
//                                    try {
//                                        GlobalVariables.GLOBAL_DATA_STORAGE_SERVICE.periodReportData("30分钟");
//                                    } catch (Exception ex) {
//                                        logger.error("Global configuration error occured when periodically report data.", ex);
//                                    }
//                                }
//                            });
//                            BusinessInitComponent.this.last30MinutesTaskTime = now;
//                        }
//                        //1小时执行一次报告数据
//                        if (now.getMinute() == 0 && (BusinessInitComponent.this.last1HourTaskTime == null || Duration.between(BusinessInitComponent.this.last1HourTaskTime, now).toMinutes() > 2L)) {
//                            GlobalVariables.GLOBAL_SCHEDULED_SERVICE_POOL.execute(new Runnable()
//                            {
//                                public void run() {
//                                    try {
//                                        GlobalVariables.GLOBAL_DATA_STORAGE_SERVICE.periodReportData("1小时");
//                                    } catch (Exception ex) {
//                                        logger.error("Global configuration error occured when periodically report data.", ex);
//                                    }
//                                }
//                            });
//                            BusinessInitComponent.this.last1HourTaskTime = now;
//                        }
//                    }
//                    //3如果配置改变了，执行更新或者删除线程
//                    else if (!GlobalVariables.GLOBAL_CONFIGURATION_CHANGING_QUEUE.isEmpty()) {
//
//                        Triplet<String, JSONObject, String> configurationChanging = (Triplet)GlobalVariables.GLOBAL_CONFIGURATION_CHANGING_QUEUE.poll();
//                        if (((String)configurationChanging.getValue2()).equals("update")) {
//                            BusinessInitComponent.this.configurationModifier.updateProcess((String)configurationChanging.getValue0(), (JSONObject)configurationChanging.getValue1());
//                        } else if (((String)configurationChanging.getValue2()).equals("delete")) {
//                            BusinessInitComponent.this.configurationModifier.deleteProcess((String)configurationChanging.getValue0(), (JSONObject)configurationChanging.getValue1());
//                        }
//                    }
//                    //4否则 调用-解析设备变量
//                    else {
//                        Instant startTime = Instant.now();
//                        BusinessInitComponent.this.dataParserService.parseDeviceVariables(); //调用-解析设备变量
//                        Instant endTime = Instant.now();
//
//                        GlobalVariables.TOTAL_PARSING_TIME += Duration.between(startTime, endTime).toMillis();
//                        GlobalVariables.TOTAL_PARSING_COUNT++;
//                    }
//                } catch (Exception e) {
//                    logger.error("Global configuration call data parser error.", e);
//                }
//            }
//        }, 0L, 30L, TimeUnit.MILLISECONDS);  //延迟30毫秒

    }
}
