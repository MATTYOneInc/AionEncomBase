package com.aionemu.commons.database;

import com.aionemu.commons.configs.DatabaseConfig;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseFactory {
   private static final Logger log = LoggerFactory.getLogger(DatabaseFactory.class);
   private static BoneCP connectionPool;
   private static String databaseName;
   private static int databaseMajorVersion;
   private static int databaseMinorVersion;

   public static synchronized void init() {
      if (connectionPool == null) {
         try {
            DatabaseConfig.DATABASE_DRIVER.newInstance();
         } catch (Exception var5) {
            log.error("Error obtaining DB driver", var5);
            throw new Error("DB Driver doesnt exist!");
         }

         if (DatabaseConfig.DATABASE_BONECP_PARTITION_CONNECTIONS_MIN > DatabaseConfig.DATABASE_BONECP_PARTITION_CONNECTIONS_MAX) {
            log.error("Please check your database configuration. Minimum amount of connections is > maximum");
            DatabaseConfig.DATABASE_BONECP_PARTITION_CONNECTIONS_MAX = DatabaseConfig.DATABASE_BONECP_PARTITION_CONNECTIONS_MIN;
         }

         BoneCPConfig config = new BoneCPConfig();
         config.setPartitionCount(DatabaseConfig.DATABASE_BONECP_PARTITION_COUNT);
         config.setMinConnectionsPerPartition(DatabaseConfig.DATABASE_BONECP_PARTITION_CONNECTIONS_MIN);
         config.setMaxConnectionsPerPartition(DatabaseConfig.DATABASE_BONECP_PARTITION_CONNECTIONS_MAX);
         config.setUsername(DatabaseConfig.DATABASE_USER);
         config.setPassword(DatabaseConfig.DATABASE_PASSWORD);
         config.setJdbcUrl(DatabaseConfig.DATABASE_URL);
         config.setDisableJMX(true);

         try {
            connectionPool = new BoneCP(config);
         } catch (SQLException var4) {
            log.error("Error while creating DB Connection pool", var4);
            throw new Error("DatabaseFactory not initialized!", var4);
         }

         try {
            Connection c = getConnection();
            DatabaseMetaData dmd = c.getMetaData();
            databaseName = dmd.getDatabaseProductName();
            databaseMajorVersion = dmd.getDatabaseMajorVersion();
            databaseMinorVersion = dmd.getDatabaseMinorVersion();
            c.close();
         } catch (Exception var3) {
            log.error("Error with connection string: " + DatabaseConfig.DATABASE_URL, var3);
            throw new Error("DatabaseFactory not initialized!");
         }

         log.info("Successfully connected to database");
      }
   }

   public static Connection getConnection() throws SQLException {
      Connection con = connectionPool.getConnection();
      if (!con.getAutoCommit()) {
         log.error("Connection Settings Error: Connection obtained from database factory should be in auto-commitmode. Forsing auto-commit to true. Please check source code for connections beeing not properlyclosed.");
         con.setAutoCommit(true);
      }

      return con;
   }

   public int getActiveConnections() {
      return connectionPool.getTotalLeased();
   }

   public int getIdleConnections() {
      return connectionPool.getStatistics().getTotalFree();
   }

   public static synchronized void shutdown() {
      try {
         connectionPool.shutdown();
      } catch (Exception var1) {
         log.warn("Failed to shutdown DatabaseFactory", var1);
      }

      connectionPool = null;
   }

   public static void close(PreparedStatement st, Connection con) {
      close(st);
      close(con);
   }

   public static void close(PreparedStatement st) {
      if (st != null) {
         try {
            if (!st.isClosed()) {
               st.close();
            }
         } catch (SQLException var2) {
            log.error("Can't close Prepared Statement", var2);
         }

      }
   }

   public static void close(Connection con) {
      if (con != null) {
         try {
            if (!con.getAutoCommit()) {
               con.setAutoCommit(true);
            }
         } catch (SQLException var3) {
            log.error("Failed to set autocommit to true while closing connection: ", var3);
         }

         try {
            con.close();
         } catch (SQLException var2) {
            log.error("DatabaseFactory: Failed to close database connection!", var2);
         }

      }
   }

   public static String getDatabaseName() {
      return databaseName;
   }

   public static int getDatabaseMajorVersion() {
      return databaseMajorVersion;
   }

   public static int getDatabaseMinorVersion() {
      return databaseMinorVersion;
   }

   private DatabaseFactory() {
   }
}
