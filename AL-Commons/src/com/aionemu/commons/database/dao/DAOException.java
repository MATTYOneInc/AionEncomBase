package com.aionemu.commons.database.dao;

public class DAOException extends RuntimeException {
   private static final long serialVersionUID = 7637014806313099318L;

   public DAOException() {
   }

   public DAOException(String message) {
      super(message);
   }

   public DAOException(String message, Throwable cause) {
      super(message, cause);
   }

   public DAOException(Throwable cause) {
      super(cause);
   }
}
