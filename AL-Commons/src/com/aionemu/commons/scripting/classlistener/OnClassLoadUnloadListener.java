package com.aionemu.commons.scripting.classlistener;

import com.aionemu.commons.scripting.metadata.OnClassLoad;
import com.aionemu.commons.scripting.metadata.OnClassUnload;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnClassLoadUnloadListener implements ClassListener {
   private static final Logger log = LoggerFactory.getLogger(OnClassLoadUnloadListener.class);

   public void postLoad(Class<?>[] classes) {
      Class[] arr$ = classes;
      int len$ = classes.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class<?> c = arr$[i$];
         this.doMethodInvoke(c.getDeclaredMethods(), OnClassLoad.class);
      }

   }

   public void preUnload(Class<?>[] classes) {
      Class[] arr$ = classes;
      int len$ = classes.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class<?> c = arr$[i$];
         this.doMethodInvoke(c.getDeclaredMethods(), OnClassUnload.class);
      }

   }

   protected final void doMethodInvoke(Method[] methods, Class<? extends Annotation> annotationClass) {
      Method[] arr$ = methods;
      int len$ = methods.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Method m = arr$[i$];
         if (Modifier.isStatic(m.getModifiers())) {
            boolean accessible = m.isAccessible();
            m.setAccessible(true);
            if (m.getAnnotation(annotationClass) != null) {
               try {
                  m.invoke((Object)null);
               } catch (IllegalAccessException var9) {
                  log.error("Can't access method " + m.getName() + " of class " + m.getDeclaringClass().getName(), var9);
               } catch (InvocationTargetException var10) {
                  log.error("Can't invoke method " + m.getName() + " of class " + m.getDeclaringClass().getName(), var10);
               }
            }

            m.setAccessible(accessible);
         }
      }

   }
}
