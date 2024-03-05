 package com.example.iotx.execution;

 import java.util.concurrent.ThreadFactory;
 import java.util.concurrent.atomic.AtomicInteger;

 public class NamedThreadFactory
   implements ThreadFactory
 {
   final ThreadGroup group;
   final AtomicInteger threadNumber = new AtomicInteger(1);
   final String namePrefix;
   final boolean useDaemonThreads;

   public NamedThreadFactory(String name)
   {
     this(name, false);
   }

   public NamedThreadFactory(String name, boolean useDaemonThreads) {
     SecurityManager s = System.getSecurityManager();
     this.group = (s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup());
     this.namePrefix = (name + "-thread-");
     this.useDaemonThreads = useDaemonThreads;
   }

   public Thread newThread(Runnable r) {
     Thread t = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
     if ((t.isDaemon()) && (!this.useDaemonThreads))
       t.setDaemon(false);
     else if ((!t.isDaemon()) && (this.useDaemonThreads)) {
       t.setDaemon(true);
     }
     if (t.getPriority() != 5) {
       t.setPriority(5);
     }
     return t;
   }
 }
