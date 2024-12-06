package com.aionemu.commons.callbacks.util;

import com.aionemu.commons.callbacks.Callback;
import com.aionemu.commons.callbacks.CallbackPriority;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;

public class CallbacksUtil {
   public static boolean isAnnotationPresent(CtMethod method, Class<? extends Annotation> annotation) {
      Iterator i$ = method.getMethodInfo().getAttributes().iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         if (o instanceof AnnotationsAttribute) {
            AnnotationsAttribute attribute = (AnnotationsAttribute)o;
            if (attribute.getAnnotation(annotation.getName()) != null) {
               return true;
            }
         }
      }

      return false;
   }

   public static int getCallbackPriority(Callback callback) {
      if (callback instanceof CallbackPriority) {
         CallbackPriority instancePriority = (CallbackPriority)callback;
         return 0 - instancePriority.getPriority();
      } else {
         return 0;
      }
   }

   protected static void insertCallbackToList(Callback callback, List<Callback> list) {
      int callbackPriority = getCallbackPriority(callback);
      if (!list.isEmpty()) {
         int i = 0;

         for(int n = list.size(); i < n; ++i) {
            Callback c = (Callback)list.get(i);
            int cPrio = getCallbackPriority(c);
            if (callbackPriority < cPrio) {
               list.add(i, callback);
               return;
            }
         }
      }

      list.add(callback);
   }
}
