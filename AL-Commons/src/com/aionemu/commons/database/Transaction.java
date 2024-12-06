package com.aionemu.commons.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Transaction {
   private static final Logger log = LoggerFactory.getLogger(Transaction.class);
   private Connection connection;

   Transaction(Connection con) throws SQLException {
      this.connection = con;
      this.connection.setAutoCommit(false);
   }

   public void insertUpdate(String sql) throws SQLException {
      this.insertUpdate(sql, (IUStH)null);
   }

   public void insertUpdate(String sql, IUStH iusth) throws SQLException {
      PreparedStatement statement = this.connection.prepareStatement(sql);
      if (iusth != null) {
         iusth.handleInsertUpdate(statement);
      } else {
         statement.executeUpdate();
      }

   }

   public Savepoint setSavepoint(String name) throws SQLException {
      return this.connection.setSavepoint(name);
   }

   public void releaseSavepoint(Savepoint savepoint) throws SQLException {
      this.connection.releaseSavepoint(savepoint);
   }

   public void commit() throws SQLException {
      this.commit((Savepoint)null);
   }

   public void commit(Savepoint rollBackToOnError) throws SQLException {
      try {
         this.connection.commit();
      } catch (SQLException var5) {
         log.warn("Error while commiting transaction", var5);

         try {
            if (rollBackToOnError != null) {
               this.connection.rollback(rollBackToOnError);
            } else {
               this.connection.rollback();
            }
         } catch (SQLException var4) {
            log.error("Can't rollback transaction", var4);
         }
      }

      this.connection.setAutoCommit(true);
      this.connection.close();
   }
}
