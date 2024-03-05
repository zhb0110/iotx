package com.example.iotx.execution;

import com.example.iotx.threadPoolConfig.ExecutionManager;
import com.example.iotx.threadPoolConfig.SchedulingController;
import com.example.iotx.threadPoolConfig.SelfSchedulingRunnable;
import com.example.iotx.threadPoolConfig.TaskStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * 计划任务执行引擎实现
 *
 * @author Thinker
 */
public class BasicExecutionEngine implements ExecutionManager {
    private static int engineCount = 0;
    private static final int DEFAULT_THREADPOOL_SIZE = 30;
    private ScheduledExecutorService executor;

    private final Object taskMapLock = new Object();
    private Map<String, Map<String, ScheduledFuture<?>>> taskMap;
    private Map<String, Map<String, TaskStats>> taskStatsMap;
    private Logger log;
    private String name;

    public BasicExecutionEngine() {
        this(DEFAULT_THREADPOOL_SIZE, "ExecEngine-" + Integer.valueOf(++engineCount).toString());
    }

    public BasicExecutionEngine(int threadCount, String name) {
        this(threadCount, name, false);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public BasicExecutionEngine(int threadCount, String name, boolean useDaemonThreads) {
        this.name = name;
        this.executor = new ScheduledThreadPoolExecutor(threadCount, new NamedThreadFactory(name, useDaemonThreads));
        this.taskMap = new HashMap();

        this.taskStatsMap = new HashMap();

        this.log = LoggerFactory.getLogger("Common.BasicExecutionEngine");
    }

    @Override
    public boolean schedule(String name, Runnable task, String cronPattern) {
        return false;
    }

    @Override
    public void unSchedule(String name) {

    }

    public String getName() {
        return this.name;
    }

    /**
     * 停止服务
     */
    @Override
    public void shutdown() {
        long start = System.currentTimeMillis();
        this.log.debug("Execution engine shutting down...");
        synchronized (this.taskMapLock) {
            this.executor.shutdownNow();
            this.taskMap.clear();
            this.taskStatsMap.clear();
        }
        this.log.info(String.format("Execution engine '%s' shut down in %dms",
                new Object[]{getName(), Long.valueOf(System.currentTimeMillis() - start)}));
    }

    /**
     * 注册固定延迟（时间）执行线程任务<br>
     * 不管线程任务的执行时间，每次都要把任务执行完成后再延迟固定时间后再执行下一次
     *
     * @param owner   任务所有者
     * @param name    任务名称
     * @param command 任务
     * @param rate    任务周期
     */
    @Override
    public void register(String owner, String name, Runnable command, int rate) {
        register(owner, name, command, rate, TimeUnit.MILLISECONDS);
    }

    /**
     * 注册固定延迟（时间）线程任务<br>
     * 不管线程任务的执行时间，每次都要把任务执行完成后再延迟固定时间后再执行下一次
     *
     * @param owner   任务所有者
     * @param name    任务名称
     * @param command 任务
     * @param rate    任务周期
     * @param unit    任务周期时间单位
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void register(String owner, String name, Runnable command, int rate, TimeUnit unit) {
        owner = owner.toLowerCase();
        name = name.toLowerCase();

        synchronized (this.taskMapLock) {
            if ((this.taskMap.containsKey(owner)) && (((Map) this.taskMap.get(owner)).containsKey(name))) {
                this.log.debug(String.format("Modifying an existing execution unit. [Owner=%s, Name=%s, New rate=%d]",
                        new Object[]{owner, name, Long.valueOf(unit.toMillis(rate))}));

                modify(owner, name, command, rate, unit);
            } else {
                if (rate == 0) {
                    this.log.debug(
                            String.format("Tried to register unit [%s %s] with rate of 0. Task will not be executed.",
                                    new Object[]{owner, name}));

                    return;
                }
                this.log.debug(String.format("Registering an execution unit. [Owner=%s, Name=%s, New rate=%d]",
                        new Object[]{owner, name, Long.valueOf(unit.toMillis(rate))}));

                Runnable tracked = new TrackedTask(owner, name, unit.toMillis(rate), command);
                synchedUpdateTaskMap(owner, name, this.executor.scheduleWithFixedDelay(tracked, 0L, rate, unit));
            }
        }
    }

    /**
     * 注册固定频率线程任务<br>
     * 固定频率的含义就是可能设定的固定时间不足以完成线程任务，但是它不管，达到设定的延迟时间了就要执行下一次了
     *
     * @param owner   任务所有者
     * @param name    任务名称
     * @param command 任务
     * @param rate    任务频率时长
     * @param unit    任务频率时长单位
     */
    @Override
    public void registerAtFixedRate(String owner, String name, Runnable command, int rate, TimeUnit unit) {
        registerAtFixedRateWithInitialDelay(owner, name, command, rate, unit, 0);
    }

    /**
     * 注册固定频率线程任务<br>
     * 固定频率的含义就是可能设定的固定时间不足以完成线程任务，但是它不管，达到设定的延迟时间了就要执行下一次了
     *
     * @param owner        任务所有者
     * @param name         任务名称
     * @param command      任务
     * @param rate         任务频率时长
     * @param unit         任务频率时长单位
     * @param initialDelay 任务延时多长时间开始第一次
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void registerAtFixedRateWithInitialDelay(String owner, String name, Runnable command, int rate,
                                                    TimeUnit unit, int initialDelay) {
        owner = owner.toLowerCase();
        name = name.toLowerCase();

        if (rate == 0) {
            this.log.warn(String.format("Tried to register unit [%s %s] with rate of 0. Will be ignored.",
                    new Object[]{owner, name}));
            return;
        }

        synchronized (this.taskMapLock) {
            if ((this.taskMap.containsKey(owner)) && (((Map) this.taskMap.get(owner)).containsKey(name))) {
                this.log.info(String.format("Modifying an existing execution unit. [Owner=%s, Name=%s]",
                        new Object[]{owner, name}));
                modifyFixedRate(owner, name, command, rate, unit);
            } else {
                Runnable tracked = new TrackedTask(owner, name, unit.toMillis(rate), command);
                synchedUpdateTaskMap(owner, name, this.executor.scheduleAtFixedRate(tracked, initialDelay, rate, unit));
            }
        }
    }

    /**
     * 设置任务状态对象
     *
     * @param owner  任务所有者
     * @param name   任务名称
     * @param future 任务状态对象
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void synchedUpdateTaskMap(String owner, String name, ScheduledFuture<?> future) {
        synchronized (taskMapLock) {
            Map ownerMap = (Map) this.taskMap.get(owner);
            if (ownerMap == null) {
                ownerMap = new HashMap();
                this.taskMap.put(owner, ownerMap);
            }
            ownerMap.put(name, future);
        }
    }

    /**
     * 注册固定延迟（时间）线程任务<br>
     * 不管线程任务的执行时间的，每次都要把任务执行完成后再延迟固定时间后再执行下一次
     *
     * @param owner        任务所有者
     * @param name         任务名称
     * @param command      任务
     * @param rate         固定延迟（时间）毫秒
     * @param initialDelay 任务延时多长时间开始第一次
     */
    @Override
    public void registerWithInitialDelay(String owner, String name, Runnable command, int rate, int initialDelay) {
        registerWithInitialDelay(owner, name, command, rate, TimeUnit.MILLISECONDS, initialDelay);
    }

    /**
     * 注册固定延迟（时间）线程任务<br>
     * 不管线程任务的执行时间的，每次都要把任务执行完成后再延迟固定时间后再执行下一次
     *
     * @param owner        任务所有者
     * @param name         任务名称
     * @param command      任务
     * @param rate         固定延迟（时间）
     * @param unit         固定延迟（时间）单位
     * @param initialDelay 任务延时多长时间开始第一次
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void registerWithInitialDelay(String owner, String name, Runnable command, int rate, TimeUnit unit,
                                         int initialDelay) {
        owner = owner.toLowerCase();
        name = name.toLowerCase();

        if (rate == 0) {
            return;
        }
        synchronized (this.taskMapLock) {
            if ((this.taskMap.containsKey(owner)) && (((Map) this.taskMap.get(owner)).containsKey(name))) {
                return;
            }
            Map ownerMap = (Map) this.taskMap.get(owner);
            if (ownerMap == null) {
                ownerMap = new HashMap();
                this.taskMap.put(owner, ownerMap);
            }
            Runnable tracked = new TrackedTask(owner, name, unit.toMillis(rate), command);
            ownerMap.put(name, this.executor.scheduleWithFixedDelay(tracked, initialDelay, rate, unit));
        }
    }

    public void modify(String owner, String name, Runnable command, int newRate, TimeUnit newUnit) {
        unRegister(owner, name, false);
        register(owner, name, command, newRate, newUnit);
    }

    /**
     * 修改固定频率线程任务
     *
     * @param owner   任务所有者
     * @param name    任务名称
     * @param command 任务
     * @param newRate 固定频率（时间）
     * @param newUnit 固定频率（时间）单位
     */

    public void modifyFixedRate(String owner, String name, Runnable command, int newRate, TimeUnit newUnit) {
        unRegister(owner, name, false);
        registerAtFixedRate(owner, name, command, newRate, newUnit);
    }

    /**
     * 注销任务
     *
     * @param owner 任务所有者
     * @param name  任务名称
     */
    @Override
    public void unRegister(String owner, String name) {
        unRegister(owner, name, true);
    }

    /**
     * 注销任务
     *
     * @param owner     任务所有者
     * @param name      任务名称
     * @param interrupt 是否中断正在执行的任务,true表示中断，false表示等待任务完成
     */

    @SuppressWarnings("rawtypes")
    private void unRegister(String owner, String name, boolean interrupt) {
        owner = owner.toLowerCase();
        name = name.toLowerCase();
        if (this.log.isDebugEnabled()) {
            this.log.debug(String.format("Unregistering unit [%s %s], allow interrupt: %s",
                    new Object[]{owner, name, Boolean.valueOf(interrupt)}));
        }
        synchronized (this.taskMapLock) {
            if ((!this.taskMap.containsKey(owner)) || (!((Map) this.taskMap.get(owner)).containsKey(name))) {
                this.log
                        .debug(String.format("Tried to unregister non existant unit [%s %s].", new Object[]{owner, name}));
                return;
            }

            Map ownerMap = (Map) this.taskMap.get(owner);
            ScheduledFuture future = (ScheduledFuture) ownerMap.get(name);

            future.cancel(interrupt);

            ownerMap.remove(name);
            if (ownerMap.isEmpty()) {
                this.taskMap.remove(owner);
            }

            removeTaskStats(owner, name);
        }
    }

    /**
     * 移除任务状态
     *
     * @param owner 任务所有者
     * @param name  任务名称
     */
    @SuppressWarnings("rawtypes")
    private void removeTaskStats(String owner, String name) {
        owner = owner.toLowerCase();
        name = name.toLowerCase();
        synchronized (this.taskMapLock) {
            Map taskOwnerMap = (Map) this.taskStatsMap.get(owner);
            if (taskOwnerMap != null) {
                taskOwnerMap.remove(name);

                if (taskOwnerMap.isEmpty())
                    this.taskStatsMap.remove(owner);
            }
        }
    }

    /**
     * 注销任务
     *
     * @param owner 任务所有者
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void unRegisterAll(String owner) {
        String o;
        if (owner != null) {
            o = owner.toLowerCase();
            List<String> keys = new ArrayList();
            synchronized (this.taskMapLock) {
                if (this.taskMap.containsKey(o)) {
                    keys.addAll(((Map) this.taskMap.get(o)).keySet());
                }
            }
            for (String key : keys)
                unRegister(o, key, true);
        }
    }

    @Override
    public void executeOnce(Runnable command) {
        executeOnce(command, false);
    }

    @Override
    public void executeOnce(Runnable command, boolean keepExec) {
        executeOnce(command, 0L, keepExec);
    }

    @Override
    public void executeOnce(Runnable command, long delay, boolean keepExec) {
        try {
            executeOnce(command, delay, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException e) {
            this.log.warn(String.format("Execution rejected... running in calling thread [%s] immediately.",
                    new Object[]{Thread.currentThread().getName()}));

            if (keepExec) {
                command.run();
            }
        }
    }

    @Override
    public ScheduledFuture<?> executeOnce(Runnable command, long delay, TimeUnit unit)
            throws RejectedExecutionException {
        return this.executor.schedule(new ThrowableCatchingRunnable(command), delay, unit);
    }

    /**
     * 注册周期运行的任务
     *
     * @param owner   任务所属者
     * @param name    任务名称
     * @param command 任务
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void register(String owner, String name, SelfSchedulingRunnable command) {
        synchronized (taskMapLock) {
            if ((this.taskMap.containsKey(owner)) && (((Map) this.taskMap.get(owner)).containsKey(name))) {
                throw new IllegalArgumentException(
                        String.format("There is already a scheduled task for %s.%s", new Object[]{owner, name}));
            }
        }

        SelfSchedulingRunner ssr = new SelfSchedulingRunner(owner, name, command);
        command.setController(ssr);
        scheduleNext(ssr);
    }

    /**
     * 执行一次周期运行任务(周期任务会不断的调用此方法，实现周期运行)
     *
     * @param ssr 周期运行任务
     */
    @SuppressWarnings("rawtypes")
    protected void scheduleNext(SelfSchedulingRunner ssr) {
        synchronized (this.taskMapLock) {
            long delay = ssr.getDelayMS();

            if (delay > 0L) {
                Runnable tracked = new TrackedTask(ssr.getOwner(), ssr.getName(), delay, ssr);
                ScheduledFuture future = this.executor.schedule(tracked, delay, TimeUnit.MILLISECONDS);
                ssr.setFuture(future);
                synchedUpdateTaskMap(ssr.getOwner(), ssr.getName(), future);
            }
        }
    }

    /**
     * 运行固定延迟（时间）线程任务<br>
     * 不管线程任务的执行时间，每次都要把任务执行完成后再延迟固定时间后再执行下一次
     *
     * @param command      周期运行任务
     * @param initialDelay 任务延时多长时间开始第一次
     * @param delay        固定频率（时间）
     * @param unit         固定频率（时间）单位
     */
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        String name = String.format("%s (initialDelay=%s,delay=%s,unit=%s)",
                new Object[]{command.getClass().getSimpleName(), Long.valueOf(initialDelay), Long.valueOf(delay), unit});

        Runnable tracked = new TrackedTask("No owner, fixed delay", name, unit.toMillis(delay), command);
        return this.executor.scheduleWithFixedDelay(tracked, initialDelay, delay, unit);
    }

    /**
     * 获得任务所有者集合
     *
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<String> getTaskOwners() {
        List owners;
        synchronized (this.taskMapLock) {
            owners = new ArrayList(this.taskStatsMap.keySet());
        }
        Collections.sort(owners);
        return owners;
    }

    /**
     * 获得指定所有者的任务
     *
     * @param owner
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<TaskStats> getTasks(String owner) {
        List list;
        synchronized (this.taskMapLock) {
            Map map = (Map) this.taskStatsMap.get(owner);
            if (map == null)
                list = new ArrayList(0);
            else {
                list = new ArrayList(map.values());
            }
        }

        Collections.sort(list);
        return list;
    }

    public String toString() {
        return getName();
    }

    /**
     * 带状态返回的Runnable<br>
     * 可以查询运行状态
     *
     * @author zl
     */
    private class TrackedTask implements Runnable {
        private String owner;
        private String name;
        private long delay;
        private Runnable runnable;

        public TrackedTask(String owner, String name, long delay, Runnable runnable) {
            this.owner = owner.toLowerCase();
            this.name = name.toLowerCase();
            this.delay = delay;
            this.runnable = runnable;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        public void run() {
            try {
                TaskStats taskStats;
                synchronized (BasicExecutionEngine.this.taskMapLock) {
                    Map ownerMap = (Map) BasicExecutionEngine.this.taskStatsMap.get(this.owner);

                    if (ownerMap == null) {
                        ownerMap = new HashMap();
                        BasicExecutionEngine.this.taskStatsMap.put(this.owner, ownerMap);
                    }

                    taskStats = (TaskStats) ownerMap.get(this.name);
                    if (taskStats == null) {
                        taskStats = new TaskStats(this.name);
                        ownerMap.put(this.name, taskStats);
                    }
                }

                Throwable problem = null;
                long startTime = System.currentTimeMillis();
                taskStats.start();
                try {
                    this.runnable.run();
                } catch (Throwable t) {
                    problem = t;
                    BasicExecutionEngine.this.log.error(
                            String.format("Task %s %s threw uncaught exception.", new Object[]{this.owner, this.name}), t);
                } finally {
                    long delay = (this.runnable instanceof SelfSchedulingRunner)
                            ? ((SelfSchedulingRunner) this.runnable).getDelayMS() : this.delay;

                    taskStats.done(startTime, delay, problem);
                }
            } catch (Exception ex) {
                BasicExecutionEngine.this.log.error(
                        String.format("Error executing tracked task %s %s.", new Object[]{this.owner, this.name}), ex);
            }
        }
    }

    /**
     * 带try-catch的Runnable<br>
     * 可对传入的Runnable try-catch包装处理
     *
     * @author zl
     */
    public class ThrowableCatchingRunnable implements Runnable {
        private Runnable runnable;

        private ThrowableCatchingRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        public void run() {
            try {
                this.runnable.run();
            } catch (Throwable t) {
                BasicExecutionEngine.this.log.error(String.format("One-shot task %s threw uncaught exception.",
                        new Object[]{this.runnable.toString()}), t);
            }
        }
    }

    /**
     * 带周期运行的Runnable<br>
     * 根据设定周期运行
     *
     * @author zl
     */
    protected class SelfSchedulingRunner implements Runnable, SchedulingController {
        private SelfSchedulingRunnable runnable;
        private String owner;
        private String name;
        private final Object futureLock = new Object();
        private Future<?> future;

        public SelfSchedulingRunner(String owner, String name, SelfSchedulingRunnable run) {
            this.owner = owner.toLowerCase();
            this.name = name.toLowerCase();
            this.runnable = run;
        }

        public String getOwner() {
            return this.owner;
        }

        public String getName() {
            return this.name;
        }

        public long getDelayMS() {
            return this.runnable.getNextExecDelayMillis();
        }

        public void run() {
            try {
                this.runnable.run();
            } catch (Exception e) {
                BasicExecutionEngine.this.log.error("Uncaught exception executing self scheduled runnable.", e);
            }
            BasicExecutionEngine.this.scheduleNext(this);
        }

        public void requestReschedule(SelfSchedulingRunnable source) {
            assert (source == this.runnable);
            synchronized (this.futureLock) {
                if ((this.future != null) && (!this.future.isDone())) {
                    this.future.cancel(false);
                    this.future = null;
                }
            }

            BasicExecutionEngine.this.scheduleNext(this);
        }

        public void setFuture(Future<?> future) {
            synchronized (this.futureLock) {
                this.future = future;
            }
        }
    }
}
