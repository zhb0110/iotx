package com.example.iotx.z_old_threadPoolConfig;

import java.util.Date;

public class TaskStats
        implements Comparable<TaskStats> {
    private String taskName;
    private StatMetric statMetric;
    private Date lastRun;
    private Date nextRun;
    private long lastRunDuration;
    private long delay;
    private boolean running;
    private Throwable problem;
    private long problemCount;

    public TaskStats(String taskName) {
        this.taskName = taskName;
        this.statMetric = new StatMetric();
        this.lastRun = null;
        this.nextRun = null;
        this.lastRunDuration = -1L;
        this.delay = -1L;
    }

    public synchronized void start() {
        this.running = true;
    }

    public synchronized void done(long startTime, long delay, Throwable problem) {
        this.running = false;
        this.problem = problem;
        if (problem != null) {
            this.problemCount += 1L;
        }

        this.statMetric.update(startTime);

        this.lastRunDuration = (System.currentTimeMillis() - startTime);
        this.lastRun = new Date(startTime);

        this.delay = delay;

        if (delay != -1L) {
            this.nextRun = new Date(this.lastRun.getTime() + delay);
        }
    }

    public StatMetric getStatMetric() {
        return this.statMetric;
    }

    public Date getLastRun() {
        return this.lastRun;
    }

    public void setLastRun(Date lastRun) {
        this.lastRun = lastRun;
    }

    public Date getNextRun() {
        return this.nextRun;
    }

    public long getLastRunDuration() {
        return this.lastRunDuration;
    }

    public long getDelay() {
        return this.delay;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public int compareTo(TaskStats o) {
        return this.taskName.compareTo(o.taskName);
    }

    public boolean isRunning() {
        return this.running;
    }

    public Throwable getProblem() {
        return this.problem;
    }

    public void setProblem(Throwable problem) {
        this.problem = problem;
    }

    public long getProblemCount() {
        return this.problemCount;
    }
}