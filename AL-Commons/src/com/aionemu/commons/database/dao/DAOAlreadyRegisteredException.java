package com.aionemu.commons.database.dao;

public class DAOAlreadyRegisteredException extends DAOException {
   private static final long serialVersionUID = -4966845154050833016L;

   public DAOAlreadyRegisteredException() {
   }

   public DAOAlreadyRegisteredException(String message) {
      super(message);
   }

   public DAOAlreadyRegisteredException(String message, Throwable cause) {
      super(message, cause);
   }

   public DAOAlreadyRegisteredException(Throwable cause) {
      super(cause);
   }
}
