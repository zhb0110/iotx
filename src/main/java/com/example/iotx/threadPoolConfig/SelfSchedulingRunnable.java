package com.example.iotx.threadPoolConfig;

public abstract interface SelfSchedulingRunnable extends Runnable
{
  public abstract long getNextExecDelayMillis();

  public abstract void setController(SchedulingController paramSchedulingController);
}