package com.aionemu.commons.utils.concurrent;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AionRejectedExecutionHandler implements RejectedExecutionHandler {
   private static final Logger log = LoggerFactory.getLogger(AionRejectedExecutionHandler.class);

   public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
      if (!executor.isShutdown()) {
         log.warn(r + " from " + executor, new RejectedExecutionException());
         if (Thread.currentThread().getPriority() > 5) {
            (new Thread(r)).start();
         } else {
            r.run();
         }

      }
   }
}
