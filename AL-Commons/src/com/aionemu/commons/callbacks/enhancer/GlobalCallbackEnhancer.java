package com.aionemu.commons.callbacks.enhancer;

import com.aionemu.commons.callbacks.CallbackResult;
import com.aionemu.commons.callbacks.metadata.GlobalCallback;
import com.aionemu.commons.callbacks.util.CallbacksUtil;
import com.aionemu.commons.callbacks.util.GlobalCallbackHelper;
import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalCallbackEnhancer extends CallbackClassFileTransformer {
   private static final Logger log = LoggerFactory.getLogger(GlobalCallbackEnhancer.class);

   protected byte[] transformClass(ClassLoader loader, byte[] clazzBytes) throws Exception {
      ClassPool cp = new ClassPool();
      cp.appendClassPath(new LoaderClassPath(loader));
      CtClass clazz = cp.makeClass(new ByteArrayInputStream(clazzBytes));
      Set<CtMethod> methdosToEnhance = new HashSet();
      CtMethod[] arr$ = clazz.getDeclaredMethods();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         CtMethod method = arr$[i$];
         if (this.isEnhanceable(method)) {
            methdosToEnhance.add(method);
         }
      }

      if (methdosToEnhance.isEmpty()) {
         log.trace("Class " + clazz.getName() + " was not enhanced");
         return null;
      } else {
         log.debug("Enhancing class: " + clazz.getName());
         Iterator i$ = methdosToEnhance.iterator();

         while(i$.hasNext()) {
            CtMethod method = (CtMethod)i$.next();
            log.debug("Enhancing method: " + method.getLongName());
            this.enhanceMethod(method);
         }

         return clazz.toBytecode();
      }
   }

   protected void enhanceMethod(CtMethod method) throws CannotCompileException, NotFoundException, ClassNotFoundException {
      ClassPool cp = method.getDeclaringClass().getClassPool();
      method.addLocalVariable("___globalCallbackResult", cp.get(CallbackResult.class.getName()));
      CtClass listenerClazz = cp.get(((GlobalCallback)method.getAnnotation(GlobalCallback.class)).value().getName());
      boolean isStatic = Modifier.isStatic(method.getModifiers());
      String listenerFieldName = "$$$" + (isStatic ? "Static" : "") + listenerClazz.getSimpleName();
      CtClass clazz = method.getDeclaringClass();

      try {
         clazz.getField(listenerFieldName);
      } catch (NotFoundException var8) {
         clazz.addField(CtField.make((isStatic ? "static " : "") + "Class " + listenerFieldName + " = Class.forName(\"" + listenerClazz.getName() + "\");", clazz));
      }

      int paramLength = method.getParameterTypes().length;
      method.insertBefore(this.writeBeforeMethod(method, paramLength, listenerFieldName));
      method.insertAfter(this.writeAfterMethod(method, paramLength, listenerFieldName));
   }

   protected String writeBeforeMethod(CtMethod method, int paramLength, String listenerFieldName) throws NotFoundException {
      StringBuilder sb = new StringBuilder();
      sb.append('{');
      sb.append(" ___globalCallbackResult = ");
      sb.append(GlobalCallbackHelper.class.getName()).append(".beforeCall(");
      if (Modifier.isStatic(method.getModifiers())) {
         sb.append(method.getDeclaringClass().getName()).append(".class, " + listenerFieldName);
         sb.append(", ");
      } else {
         sb.append("this, " + listenerFieldName);
         sb.append(", ");
      }

      if (paramLength > 0) {
         sb.append("new Object[]{");

         for(int i = 1; i <= paramLength; ++i) {
            sb.append("($w)$").append(i);
            if (i < paramLength) {
               sb.append(',');
            }
         }

         sb.append("}");
      } else {
         sb.append("null");
      }

      sb.append(");");
      sb.append("if(___globalCallbackResult.isBlockingCaller()){");
      CtClass returnType = method.getReturnType();
      if (returnType.equals(CtClass.voidType)) {
         sb.append("return");
      } else if (returnType.equals(CtClass.booleanType)) {
         sb.append("return false");
      } else if (returnType.equals(CtClass.charType)) {
         sb.append("return 'a'");
      } else if (returnType.equals(CtClass.byteType) || returnType.equals(CtClass.shortType) || returnType.equals(CtClass.intType) || returnType.equals(CtClass.floatType) || returnType.equals(CtClass.longType) || returnType.equals(CtClass.longType)) {
         sb.append("return 0");
      }

      sb.append(";}}");
      return sb.toString();
   }

   protected String writeAfterMethod(CtMethod method, int paramLength, String listenerFieldName) throws NotFoundException {
      StringBuilder sb = new StringBuilder();
      sb.append('{');
      if (!method.getReturnType().equals(CtClass.voidType)) {
         sb.append("if(___globalCallbackResult.isBlockingCaller()){");
         sb.append("$_ = ($r)($w)___globalCallbackResult.getResult();");
         sb.append("}");
      }

      sb.append("___globalCallbackResult = ").append(GlobalCallbackHelper.class.getName()).append(".afterCall(");
      if (Modifier.isStatic(method.getModifiers())) {
         sb.append(method.getDeclaringClass().getName()).append(".class, " + listenerFieldName);
         sb.append(", ");
      } else {
         sb.append("this, ");
         sb.append(listenerFieldName + ", ");
      }

      if (paramLength > 0) {
         sb.append("new Object[]{");

         for(int i = 1; i <= paramLength; ++i) {
            sb.append("($w)$").append(i);
            if (i < paramLength) {
               sb.append(',');
            }
         }

         sb.append("}");
      } else {
         sb.append("null");
      }

      sb.append(", ($w)$_);");
      sb.append("if(___globalCallbackResult.isBlockingCaller()){");
      if (method.getReturnType().equals(CtClass.voidType)) {
         sb.append("return;");
      } else {
         sb.append("return ($r)($w)___globalCallbackResult.getResult();");
      }

      sb.append("}");
      sb.append("else {return $_;}");
      sb.append("}");
      return sb.toString();
   }

   protected boolean isEnhanceable(CtMethod method) {
      int modifiers = method.getModifiers();
      return !Modifier.isAbstract(modifiers) && !Modifier.isNative(modifiers) && CallbacksUtil.isAnnotationPresent(method, GlobalCallback.class);
   }
}
