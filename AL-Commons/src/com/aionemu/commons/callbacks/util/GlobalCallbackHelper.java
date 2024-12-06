package com.aionemu.commons.callbacks.util;

import com.aionemu.commons.callbacks.Callback;
import com.aionemu.commons.callbacks.CallbackResult;
import com.aionemu.commons.utils.ClassUtils;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalCallbackHelper {
   private static final Logger log = LoggerFactory.getLogger(GlobalCallbackHelper.class);
   private static final CopyOnWriteArrayList<Callback> globalCallbacks = new CopyOnWriteArrayList();

   private GlobalCallbackHelper() {
   }

   public static void addCallback(Callback<?> callback) {
      Class var1 = GlobalCallbackHelper.class;
      synchronized(GlobalCallbackHelper.class) {
         CallbacksUtil.insertCallbackToList(callback, globalCallbacks);
      }
   }

   public static void removeCallback(Callback<?> callback) {
      Class var1 = GlobalCallbackHelper.class;
      synchronized(GlobalCallbackHelper.class) {
         globalCallbacks.remove(callback);
      }
   }

   public static CallbackResult<?> beforeCall(Object obj, Class callbackClass, Object... args) {
      CallbackResult<?> cr = null;
      Iterator i$ = globalCallbacks.iterator();

      while(i$.hasNext()) {
         Callback cb = (Callback)i$.next();
         if (ClassUtils.isSubclass(cb.getBaseClass(), callbackClass)) {
            try {
               cr = cb.beforeCall(obj, args);
               if (cr.isBlockingCallbacks()) {
                  break;
               }
            } catch (Exception var7) {
               log.error("Exception in global callback", var7);
            }
         }
      }

      return cr == null ? CallbackResult.newContinue() : cr;
   }

   public static CallbackResult<?> afterCall(Object obj, Class callbackClass, Object[] args, Object result) {
      CallbackResult<?> cr = null;
      Iterator i$ = globalCallbacks.iterator();

      while(i$.hasNext()) {
         Callback cb = (Callback)i$.next();
         if (ClassUtils.isSubclass(cb.getBaseClass(), callbackClass)) {
            try {
               cr = cb.afterCall(obj, args, result);
               if (cr.isBlockingCallbacks()) {
                  break;
               }
            } catch (Exception var8) {
               log.error("Exception in global callback", var8);
            }
         }
      }

      return cr == null ? CallbackResult.newContinue() : cr;
   }
}
