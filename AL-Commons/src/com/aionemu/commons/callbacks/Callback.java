package com.aionemu.commons.callbacks;

public interface Callback<T> {
   CallbackResult beforeCall(T var1, Object[] var2);

   CallbackResult afterCall(T var1, Object[] var2, Object var3);

   Class<? extends Callback> getBaseClass();
}
