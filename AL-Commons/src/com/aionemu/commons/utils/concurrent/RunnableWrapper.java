package com.aionemu.commons.utils.concurrent;

public class RunnableWrapper implements Runnable {
   private final Runnable runnable;
   private final long maxRuntimeMsWithoutWarning;

   public RunnableWrapper(Runnable runnable) {
      this(runnable, Long.MAX_VALUE);
   }

   public RunnableWrapper(Runnable runnable, long maxRuntimeMsWithoutWarning) {
      this.runnable = runnable;
      this.maxRuntimeMsWithoutWarning = maxRuntimeMsWithoutWarning;
   }

   public final void run() {
      ExecuteWrapper.execute(this.runnable, this.maxRuntimeMsWithoutWarning);
   }
}
