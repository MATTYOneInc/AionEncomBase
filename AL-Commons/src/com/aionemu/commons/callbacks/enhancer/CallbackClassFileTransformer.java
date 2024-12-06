package com.aionemu.commons.callbacks.enhancer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CallbackClassFileTransformer implements ClassFileTransformer {
   private static final Logger log = LoggerFactory.getLogger(CallbackClassFileTransformer.class);

   public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
      try {
         if (loader != null && !loader.getClass().getName().equals("sun.misc.Launcher$ExtClassLoader")) {
            return this.transformClass(loader, classfileBuffer);
         } else {
            log.trace("Class " + className + " ignored.");
            return null;
         }
      } catch (Exception var8) {
         Error e1 = new Error("Can't transform class " + className, var8);
         log.error(e1.getMessage(), e1);
         if (loader.getClass().getName().equals("sun.misc.Launcher$AppClassLoader")) {
            Runtime.getRuntime().halt(1);
         }

         throw e1;
      }
   }

   protected abstract byte[] transformClass(ClassLoader var1, byte[] var2) throws Exception;
}
