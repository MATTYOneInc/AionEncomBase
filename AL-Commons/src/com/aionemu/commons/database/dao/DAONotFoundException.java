package com.aionemu.commons.database.dao;

public class DAONotFoundException extends DAOException {
   private static final long serialVersionUID = 4241980426435305296L;

   public DAONotFoundException() {
   }

   public DAONotFoundException(String message) {
      super(message);
   }

   public DAONotFoundException(String message, Throwable cause) {
      super(message, cause);
   }

   public DAONotFoundException(Throwable cause) {
      super(cause);
   }
}
