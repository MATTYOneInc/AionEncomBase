package com.aionemu.commons.callbacks;

public class CallbackResult<T> {
   public static final int CONTINUE = 0;
   public static final int BLOCK_CALLBACKS = 1;
   public static final int BLOCK_CALLER = 2;
   public static final int BLOCK_ALL = 3;
   private static final CallbackResult INSTANCE_CONTINUE = new CallbackResult(0);
   private static final CallbackResult INSTANCE_BLOCK_CALLBACKS = new CallbackResult(1);
   private final T result;
   private final int blockPolicy;

   private CallbackResult(int blockPolicy) {
      this(null, blockPolicy);
   }

   private CallbackResult(T result, int blockPolicy) {
      this.result = result;
      this.blockPolicy = blockPolicy;
   }

   public T getResult() {
      return this.result;
   }

   public boolean isBlockingCallbacks() {
      return (this.blockPolicy & 1) != 0;
   }

   public boolean isBlockingCaller() {
      return (this.blockPolicy & 2) != 0;
   }

   public static <T> CallbackResult<T> newContinue() {
      return INSTANCE_CONTINUE;
   }

   public static <T> CallbackResult<T> newCallbackBlocker() {
      return INSTANCE_BLOCK_CALLBACKS;
   }

   public static <T> CallbackResult<T> newFullBlocker(T result) {
      return new CallbackResult(result, 3);
   }
}
