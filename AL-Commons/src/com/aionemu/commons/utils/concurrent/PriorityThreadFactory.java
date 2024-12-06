package com.aionemu.commons.utils.concurrent;

import com.aionemu.commons.network.util.ThreadUncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class PriorityThreadFactory implements ThreadFactory {
   private int prio;
   private String name;
   private ExecutorService threadPool;
   private AtomicInteger threadNumber;
   private ThreadGroup group;

   public PriorityThreadFactory(String name, int prio) {
      this.threadNumber = new AtomicInteger(1);
      this.prio = prio;
      this.name = name;
      this.group = new ThreadGroup(this.name);
   }

   public PriorityThreadFactory(String name, ExecutorService defaultPool) {
      this(name, 5);
      this.setDefaultPool(defaultPool);
   }

   protected void setDefaultPool(ExecutorService pool) {
      this.threadPool = pool;
   }

   protected ExecutorService getDefaultPool() {
      return this.threadPool;
   }

   public Thread newThread(Runnable r) {
      Thread t = new Thread(this.group, r);
      t.setName(this.name + "-" + this.threadNumber.getAndIncrement());
      t.setPriority(this.prio);
      t.setUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
      return t;
   }
}
