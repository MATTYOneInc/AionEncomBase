package com.aionl.slf4j.conversion;

import ch.qos.logback.core.rolling.TriggeringPolicyBase;
import java.io.File;

public final class SimpleStartupTriggeringPolicy<E> extends TriggeringPolicyBase<E> {
   private boolean fired = false;

   public boolean isTriggeringEvent(File activeFile, E event) {
      boolean result = !this.fired && activeFile.length() > 0L;
      this.fired = true;
      if (result) {
         this.addInfo("Triggering rollover for " + activeFile);
      }

      return result;
   }
}
