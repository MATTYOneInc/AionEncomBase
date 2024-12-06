package com.aionemu.commons.network;

public class ServerCfg {
   public final String hostName;
   public final int port;
   public final String connectionName;
   public final ConnectionFactory factory;

   public ServerCfg(String hostName, int port, String connectionName, ConnectionFactory factory) {
      this.hostName = hostName;
      this.port = port;
      this.connectionName = connectionName;
      this.factory = factory;
   }
}
