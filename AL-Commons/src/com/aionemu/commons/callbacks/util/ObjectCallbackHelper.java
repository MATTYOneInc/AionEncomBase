package com.aionemu.commons.callbacks.util;

import com.aionemu.commons.callbacks.Callback;
import com.aionemu.commons.callbacks.CallbackResult;
import com.aionemu.commons.callbacks.EnhancedObject;
import com.aionemu.commons.utils.GenericValidator;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectCallbackHelper {
   private static final Logger log = LoggerFactory.getLogger(ObjectCallbackHelper.class);

   private ObjectCallbackHelper() {
   }

   public static void addCallback(Callback callback, EnhancedObject object) {
      try {
         object.getCallbackLock().writeLock().lock();
         Map<Class<? extends Callback>, List<Callback>> cbMap = object.getCallbacks();
         if (cbMap == null) {
            cbMap = Maps.newHashMap();
            object.setCallbacks((Map)cbMap);
         }

         List<Callback> list = (List)((Map)cbMap).get(callback.getBaseClass());
         if (list == null) {
            list = new CopyOnWriteArrayList();
            ((Map)cbMap).put(callback.getBaseClass(), list);
         }

         CallbacksUtil.insertCallbackToList(callback, (List)list);
      } finally {
         object.getCallbackLock().writeLock().unlock();
      }

   }

   public static void removeCallback(Callback callback, EnhancedObject object) {
      try {
         object.getCallbackLock().writeLock().lock();
         Map<Class<? extends Callback>, List<Callback>> cbMap = object.getCallbacks();
         if (GenericValidator.isBlankOrNull(cbMap)) {
            return;
         }

         List<Callback> list = (List)cbMap.get(callback.getBaseClass());
         if (list == null || !list.remove(callback)) {
            log.error("Attempt to remove callback that doesn't exists", new RuntimeException());
            return;
         }

         if (list.isEmpty()) {
            cbMap.remove(callback.getBaseClass());
         }

         if (cbMap.isEmpty()) {
            object.setCallbacks((Map)null);
         }
      } finally {
         object.getCallbackLock().writeLock().unlock();
      }

   }

   public static CallbackResult<?> beforeCall(EnhancedObject obj, Class callbackClass, Object... args) {
      Map<Class<? extends Callback>, List<Callback>> cbMap = obj.getCallbacks();
      if (GenericValidator.isBlankOrNull(cbMap)) {
         return CallbackResult.newContinue();
      } else {
         CallbackResult<?> cr = null;
         List list = null;

         try {
            obj.getCallbackLock().readLock().lock();
            list = (List)cbMap.get(callbackClass);
         } finally {
            obj.getCallbackLock().readLock().unlock();
         }

         if (GenericValidator.isBlankOrNull((Collection)list)) {
            return CallbackResult.newContinue();
         } else {
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
               Callback c = (Callback)i$.next();

               try {
                  cr = c.beforeCall(obj, args);
                  if (cr.isBlockingCallbacks()) {
                     break;
                  }
               } catch (Exception var12) {
                  log.error("Uncaught exception in callback", var12);
               }
            }

            return cr == null ? CallbackResult.newContinue() : cr;
         }
      }
   }

   public static CallbackResult<?> afterCall(EnhancedObject obj, Class callbackClass, Object[] args, Object result) {
      Map<Class<? extends Callback>, List<Callback>> cbMap = obj.getCallbacks();
      if (GenericValidator.isBlankOrNull(cbMap)) {
         return CallbackResult.newContinue();
      } else {
         CallbackResult<?> cr = null;
         List list = null;

         try {
            obj.getCallbackLock().readLock().lock();
            list = (List)cbMap.get(callbackClass);
         } finally {
            obj.getCallbackLock().readLock().unlock();
         }

         if (GenericValidator.isBlankOrNull((Collection)list)) {
            return CallbackResult.newContinue();
         } else {
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
               Callback c = (Callback)i$.next();

               try {
                  cr = c.afterCall(obj, args, result);
                  if (cr.isBlockingCallbacks()) {
                     break;
                  }
               } catch (Exception var13) {
                  log.error("Uncaught exception in callback", var13);
               }
            }

            return cr == null ? CallbackResult.newContinue() : cr;
         }
      }
   }
}
