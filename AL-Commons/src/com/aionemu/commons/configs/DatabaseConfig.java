package com.aionemu.commons.configs;

import com.aionemu.commons.configuration.Property;
import java.io.File;

public class DatabaseConfig {
   @Property(
      key = "database.url",
      defaultValue = "jdbc:mysql://localhost:3306/aion_uni"
   )
   public static String DATABASE_URL;
   @Property(
      key = "database.driver",
      defaultValue = "com.mysql.jdbc.Driver"
   )
   public static Class<?> DATABASE_DRIVER;
   @Property(
      key = "database.user",
      defaultValue = "root"
   )
   public static String DATABASE_USER;
   @Property(
      key = "database.password",
      defaultValue = "root"
   )
   public static String DATABASE_PASSWORD;
   @Property(
      key = "database.bonecp.partition.count",
      defaultValue = "2"
   )
   public static int DATABASE_BONECP_PARTITION_COUNT;
   @Property(
      key = "database.bonecp.partition.connections.min",
      defaultValue = "2"
   )
   public static int DATABASE_BONECP_PARTITION_CONNECTIONS_MIN;
   @Property(
      key = "database.bonecp.partition.connections.max",
      defaultValue = "5"
   )
   public static int DATABASE_BONECP_PARTITION_CONNECTIONS_MAX;
   @Property(
      key = "database.scriptcontext.descriptor",
      defaultValue = "./data/scripts/system/database/database.xml"
   )
   public static File DATABASE_SCRIPTCONTEXT_DESCRIPTOR;
}
