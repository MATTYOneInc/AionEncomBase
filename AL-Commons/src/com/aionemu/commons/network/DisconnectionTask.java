package com.aionemu.commons.network;

public class DisconnectionTask implements Runnable {
   private AConnection connection;

   public DisconnectionTask(AConnection connection) {
      this.connection = connection;
   }

   public void run() {
      this.connection.onDisconnect();
   }
}
