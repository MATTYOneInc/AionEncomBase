package com.aionemu.commons.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurableProcessor {
   private static final Logger log = LoggerFactory.getLogger(ConfigurableProcessor.class);

   public static void process(Object object, Properties... properties) {
      Class clazz;
      if (object instanceof Class) {
         clazz = (Class)object;
         object = null;
      } else {
         clazz = object.getClass();
      }

      process(clazz, object, properties);
   }

   private static void process(Class<?> clazz, Object obj, Properties[] props) {
      processFields(clazz, obj, props);
      if (obj == null) {
         Class[] arr$ = clazz.getInterfaces();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class<?> itf = arr$[i$];
            process(itf, obj, props);
         }
      }

      Class<?> superClass = clazz.getSuperclass();
      if (superClass != null && superClass != Object.class) {
         process(superClass, obj, props);
      }

   }

   private static void processFields(Class<?> clazz, Object obj, Properties[] props) {
      Field[] arr$ = clazz.getDeclaredFields();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Field f = arr$[i$];
         if ((!Modifier.isStatic(f.getModifiers()) || obj == null) && (Modifier.isStatic(f.getModifiers()) || obj != null) && f.isAnnotationPresent(Property.class)) {
            if (Modifier.isFinal(f.getModifiers())) {
               log.error("Attempt to proceed final field " + f.getName() + " of class " + clazz.getName());
               throw new RuntimeException();
            }

            processField(f, obj, props);
         }
      }

   }

   private static void processField(Field f, Object obj, Properties[] props) {
      boolean oldAccessible = f.isAccessible();
      f.setAccessible(true);

      try {
         Property property = (Property)f.getAnnotation(Property.class);
         if ("DO_NOT_OVERWRITE_INITIALIAZION_VALUE".equals(property.defaultValue()) && !isKeyPresent(property.key(), props)) {
            if (log.isDebugEnabled()) {
               log.debug("Field " + f.getName() + " of class " + f.getDeclaringClass().getName() + " wasn't modified");
            }
         } else {
            f.set(obj, getFieldValue(f, props));
         }
      } catch (Exception var5) {
         log.error("Can't transform field " + f.getName() + " of class " + f.getDeclaringClass());
         throw new RuntimeException();
      }

      f.setAccessible(oldAccessible);
   }

   private static Object getFieldValue(Field field, Properties[] props) throws TransformationException {
      Property property = (Property)field.getAnnotation(Property.class);
      String defaultValue = property.defaultValue();
      String key = property.key();
      String value = null;
      if (key.isEmpty()) {
         log.warn("Property " + field.getName() + " of class " + field.getDeclaringClass().getName() + " has empty key");
      } else {
         value = findPropertyByKey(key, props);
      }

      if (value == null || value.trim().equals("")) {
         value = defaultValue;
         if (log.isDebugEnabled()) {
            log.debug("Using default value for field " + field.getName() + " of class " + field.getDeclaringClass().getName());
         }
      }

      PropertyTransformer<?> pt = PropertyTransformerFactory.newTransformer(field.getType(), property.propertyTransformer());
      return pt.transform(value, field);
   }

   private static String findPropertyByKey(String key, Properties[] props) {
      Properties[] arr$ = props;
      int len$ = props.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Properties p = arr$[i$];
         if (p.containsKey(key)) {
            return p.getProperty(key);
         }
      }

      return null;
   }

   private static boolean isKeyPresent(String key, Properties[] props) {
      return findPropertyByKey(key, props) != null;
   }
}
