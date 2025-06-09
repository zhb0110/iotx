package com.example.iotx.z_old_threadPoolConfig;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract interface ExecutionManager {
    /**
     * 注册固定延迟（时间）执行线程任务<br>
     * 不管线程任务的执行时间，每次都要把任务执行完成后再延迟固定时间后再执行下一次
     * 
     * @param owner 任务所有者
     * @param name 任务名称
     * @param command 任务
     * @param rate 任务周期(毫秒)
     */
    public abstract void register(String owner, String name, Runnable command, int rate);

    /**
     * 注册固定延迟（时间）线程任务<br>
     * 不管线程任务的执行时间，每次都要把任务执行完成后再延迟固定时间后再执行下一次
     * 
     * @param owner 任务所有者
     * @param name 任务名称
     * @param command 任务
     * @param rate 任务周期
     * @param unit 任务周期时间单位
     */
    public abstract void register(String owner, String name, Runnable command, int rate, TimeUnit unit);

    /**
     * 注册周期运行的任务
     * 
     * @param owner 任务所属者
     * @param name 任务名称
     * @param command 任务
     */
    public abstract void register(String owner, String name, SelfSchedulingRunnable command);

    /**
     * 注册固定延迟（时间）线程任务<br>
     * 不管线程任务的执行时间的，每次都要把任务执行完成后再延迟固定时间后再执行下一次
     * 
     * @param owner 任务所有者
     * @param name 任务名称
     * @param command 任务
     * @param rate 固定延迟（时间）毫秒
     * @param initialDelay 任务延时多长时间开始第一次
     */
    public abstract void registerWithInitialDelay(String owner, String name, Runnable command, int rate,
        int initialDelay);

    /**
     * 注册固定延迟（时间）线程任务<br>
     * 不管线程任务的执行时间的，每次都要把任务执行完成后再延迟固定时间后再执行下一次
     * 
     * @param owner 任务所有者
     * @param name 任务名称
     * @param command 任务
     * @param rate 固定延迟（时间）
     * @param unit 固定延迟（时间）单位
     * @param initialDelay 任务延时多长时间开始第一次
     */
    public abstract void registerWithInitialDelay(String owner, String name, Runnable command, int rate, TimeUnit unit,
        int initialDelay);

    /**
     * 注册固定频率线程任务<br>
     * 固定频率的含义就是可能设定的固定时间不足以完成线程任务，但是它不管，达到设定的延迟时间了就要执行下一次了
     * 
     * @param owner 任务所有者
     * @param name 任务名称
     * @param command 任务
     * @param rate 任务频率时长
     * @param unit 任务频率时长单位
     */
    public abstract void registerAtFixedRate(String owner, String name, Runnable command, int rate, TimeUnit unit);

    /**
     * 注册固定频率线程任务<br>
     * 固定频率的含义就是可能设定的固定时间不足以完成线程任务，但是它不管，达到设定的延迟时间了就要执行下一次了
     * 
     * @param owner 任务所有者
     * @param name 任务名称
     * @param command 任务
     * @param rate 任务频率时长
     * @param unit 任务频率时长单位
     * @param initialDelay 任务延时多长时间开始第一次
     */
    public abstract void registerAtFixedRateWithInitialDelay(String owner, String name, Runnable command, int rate,
        TimeUnit unit, int initialDelay);
    /**
     * 执行一次(适合短耗时的任务)
     * 
     * @param command 任务
     */
    public abstract void executeOnce(Runnable commandl);
    /**
     * 执行一次(适合短耗时的任务)
     * 
     * @param command 任务
     * @param keepExec 保证执行，线程池满后用主线程执行 ,true 表示保证执行, false 表示线程池满后不执行
     */
    public abstract void executeOnce(Runnable command, boolean keepExec);

    /**
     * 执行一次(适合短耗时的任务)
     * 
     * @param command 任务
     * @param delay 延时多长时间执行(毫秒)
     * @param keepExec 保证执行，线程池满后用主线程执行 ,true 表示保证执行, false 表示线程池满后不执行
     */
    public abstract void executeOnce(Runnable command, long delay, boolean keepExec);

    /**
     * 执行一次(适合短耗时的任务)
     * 
     * @param command 任务
     * @param delay 延时多长时间执行
     * @param unit 延时时长单位
     */
    public abstract ScheduledFuture<?> executeOnce(Runnable command, long delay, TimeUnit unit);

    /**
     * 运行固定延迟（时间）线程任务<br>
     * 不管线程任务的执行时间，每次都要把任务执行完成后再延迟固定时间后再执行下一次
     * 
     * @param command 周期运行任务
     * @param initialDelay 任务延时多长时间开始第一次
     * @param delay 固定频率（时间）
     * @param unit 固定频率（时间）单位
     */
    public abstract ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay,
        TimeUnit unit);

    /**
     * 注销任务
     * 
     * @param owner 任务所有者
     * @param name 任务名称
     */
    public abstract void unRegister(String owner, String name);

    /**
     * 注销任务
     * 
     * @param owner 任务所有者
     */
    public abstract void unRegisterAll(String owner);

    /**
     * 停止服务
     */
    public abstract void shutdown();

    /**
     * 加载周期运行任务(cron表达式方式)
     * 
     * @param name 任务名称
     * @param task 任务
     * @param cronPattern cron表达式
     * @return 周期任务加载是否成功
     */
    public abstract boolean schedule(String name, Runnable task, String cronPattern);

    /**
     * 卸载周期运行任务
     * 
     * @param name 任务名称
     */
    public abstract void unSchedule(String name);
}