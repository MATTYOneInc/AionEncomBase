package com.aionemu.commons.utils.concurrent;

import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class ScheduledFutureWrapper implements ScheduledFuture<Object> {
   private final ScheduledFuture<?> future;

   public ScheduledFutureWrapper(ScheduledFuture<?> future) {
      this.future = future;
   }

   public long getDelay(TimeUnit unit) {
      return this.future.getDelay(unit);
   }

   public int compareTo(Delayed o) {
      return this.future.compareTo(o);
   }

   public boolean cancel(boolean mayInterruptIfRunning) {
      return this.future.cancel(false);
   }

   public Object get() throws InterruptedException, ExecutionException {
      return this.future.get();
   }

   public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
      return this.future.get(timeout, unit);
   }

   public boolean isCancelled() {
      return this.future.isCancelled();
   }

   public boolean isDone() {
      return this.future.isDone();
   }
}
