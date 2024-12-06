package com.aionemu.commons.callbacks;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public interface EnhancedObject {
   void addCallback(Callback var1);

   void removeCallback(Callback var1);

   Map<Class<? extends Callback>, List<Callback>> getCallbacks();

   void setCallbacks(Map<Class<? extends Callback>, List<Callback>> var1);

   ReentrantReadWriteLock getCallbackLock();
}
