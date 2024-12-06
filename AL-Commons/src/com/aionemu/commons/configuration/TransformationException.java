package com.aionemu.commons.configuration;

public class TransformationException extends RuntimeException {
   private static final long serialVersionUID = -6641235751743285902L;

   public TransformationException() {
   }

   public TransformationException(String message) {
      super(message);
   }

   public TransformationException(String message, Throwable cause) {
      super(message, cause);
   }

   public TransformationException(Throwable cause) {
      super(cause);
   }
}
