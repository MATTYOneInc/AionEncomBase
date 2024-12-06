package com.aionemu.commons.network;

public interface DisconnectionThreadPool {
   void scheduleDisconnection(DisconnectionTask var1, long var2);

   void waitForDisconnectionTasks();
}
