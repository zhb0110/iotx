package com.example.iotx.z_old_threadPoolConfig;

public abstract interface SchedulingController
{
  public abstract void requestReschedule(SelfSchedulingRunnable paramSelfSchedulingRunnable);
}