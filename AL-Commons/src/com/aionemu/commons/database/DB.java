package com.aionemu.commons.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DB {
   protected static final Logger log = LoggerFactory.getLogger(DB.class);

   private DB() {
   }

   public static boolean select(String query, ReadStH reader) {
      return select(query, reader, (String)null);
   }

   public static boolean select(String query, ReadStH reader, String errMsg) {
      Connection con = null;
      PreparedStatement stmt = null;

      boolean var7;
      try {
         con = DatabaseFactory.getConnection();
         stmt = con.prepareStatement(query);
         if (reader instanceof ParamReadStH) {
            ((ParamReadStH)reader).setParams(stmt);
         }

         ResultSet rset = stmt.executeQuery();
         reader.handleRead(rset);
         return true;
      } catch (Exception var17) {
         if (errMsg == null) {
            log.warn("Error executing select query " + var17, var17);
         } else {
            log.warn(errMsg + " " + var17, var17);
         }

         var7 = false;
      } finally {
         try {
            if (con != null) {
               con.close();
            }

            if (stmt != null) {
               stmt.close();
            }
         } catch (Exception var16) {
            log.warn("Failed to close DB connection " + var16, var16);
         }

      }

      return var7;
   }

   public static boolean call(String query, ReadStH reader) {
      return call(query, reader, (String)null);
   }

   public static boolean call(String query, ReadStH reader, String errMsg) {
      Connection con = null;
      CallableStatement stmt = null;

      boolean var7;
      try {
         con = DatabaseFactory.getConnection();
         stmt = con.prepareCall(query);
         if (reader instanceof CallReadStH) {
            ((CallReadStH)reader).setParams(stmt);
         }

         ResultSet rset = stmt.executeQuery();
         reader.handleRead(rset);
         return true;
      } catch (Exception var17) {
         if (errMsg == null) {
            log.warn("Error calling stored procedure " + var17, var17);
         } else {
            log.warn(errMsg + " " + var17, var17);
         }

         var7 = false;
      } finally {
         try {
            if (con != null) {
               con.close();
            }

            if (stmt != null) {
               stmt.close();
            }
         } catch (Exception var16) {
            log.warn("Failed to close DB connection " + var16, var16);
         }

      }

      return var7;
   }

   public static boolean insertUpdate(String query) {
      return insertUpdate(query, (IUStH)null, (String)null);
   }

   public static boolean insertUpdate(String query, String errMsg) {
      return insertUpdate(query, (IUStH)null, errMsg);
   }

   public static boolean insertUpdate(String query, IUStH batch) {
      return insertUpdate(query, batch, (String)null);
   }

   public static boolean insertUpdate(String query, IUStH batch, String errMsg) {
      Connection con = null;
      PreparedStatement stmt = null;

      boolean var6;
      try {
         con = DatabaseFactory.getConnection();
         stmt = con.prepareStatement(query);
         if (batch != null) {
            batch.handleInsertUpdate(stmt);
         } else {
            stmt.executeUpdate();
         }

         return true;
      } catch (Exception var16) {
         if (errMsg == null) {
            log.warn("Failed to execute IU query " + var16, var16);
         } else {
            log.warn(errMsg + " " + var16, var16);
         }

         var6 = false;
      } finally {
         try {
            if (con != null) {
               con.close();
            }

            if (stmt != null) {
               stmt.close();
            }
         } catch (Exception var15) {
            log.warn("Failed to close DB connection " + var15, var15);
         }

      }

      return var6;
   }

   public static Transaction beginTransaction() throws SQLException {
      Connection con = DatabaseFactory.getConnection();
      return new Transaction(con);
   }

   public static PreparedStatement prepareStatement(String sql) {
      return prepareStatement(sql, 1003, 1007);
   }

   public static PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) {
      Connection c = null;
      PreparedStatement ps = null;

      try {
         c = DatabaseFactory.getConnection();
         ps = c.prepareStatement(sql, resultSetType, resultSetConcurrency);
      } catch (Exception var8) {
         log.error("Can't create PreparedStatement for querry: " + sql, var8);
         if (c != null) {
            try {
               c.close();
            } catch (SQLException var7) {
               log.error("Can't close connection after exception", var7);
            }
         }
      }

      return ps;
   }

   public static int executeUpdate(PreparedStatement statement) {
      try {
         return statement.executeUpdate();
      } catch (Exception var2) {
         log.error("Can't execute update for PreparedStatement", var2);
         return -1;
      }
   }

   public static void executeUpdateAndClose(PreparedStatement statement) {
      executeUpdate(statement);
      close(statement);
   }

   public static ResultSet executeQuerry(PreparedStatement statement) {
      ResultSet rs = null;

      try {
         rs = statement.executeQuery();
      } catch (Exception var3) {
         log.error("Error while executing querry", var3);
      }

      return rs;
   }

   public static void close(PreparedStatement statement) {
      try {
         if (statement.isClosed()) {
            log.warn("Attempt to close PreparedStatement that is closes already", new Exception());
            return;
         }

         Connection c = statement.getConnection();
         statement.close();
         c.close();
      } catch (Exception var2) {
         log.error("Error while closing PreparedStatement", var2);
      }

   }
}
