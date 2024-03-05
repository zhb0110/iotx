package com.example.iotx.threadPoolConfig;

public abstract interface SchedulingController
{
  public abstract void requestReschedule(SelfSchedulingRunnable paramSelfSchedulingRunnable);
}