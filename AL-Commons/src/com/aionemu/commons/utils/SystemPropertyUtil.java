package com.aionemu.commons.utils;

import java.util.logging.Level;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SystemPropertyUtil {
   private static boolean initializedLogger = true;
   private static final Logger logger = LoggerFactory.getLogger(SystemPropertyUtil.class);
   private static boolean loggedException;
   private static final Pattern INTEGER_PATTERN = Pattern.compile("-?[0-9]+");

   public static boolean contains(String key) {
      return get(key) != null;
   }

   public static String get(String key) {
      return get(key, (String)null);
   }

   public static String get(String key, String def) {
      if (key == null) {
         throw new NullPointerException("key");
      } else if (key.isEmpty()) {
         throw new IllegalArgumentException("key must not be empty.");
      } else {
         String value = null;

         try {
            value = System.getProperty(key);
         } catch (Exception var4) {
            if (!loggedException) {
               log("Unable to retrieve a system property '" + key + "'; default values will be used.", var4);
               loggedException = true;
            }
         }

         return value == null ? def : value;
      }
   }

   public static boolean getBoolean(String key, boolean def) {
      String value = get(key);
      if (value == null) {
         return def;
      } else {
         value = value.trim().toLowerCase();
         if (value.isEmpty()) {
            return true;
         } else if (!"true".equals(value) && !"yes".equals(value) && !"1".equals(value)) {
            if (!"false".equals(value) && !"no".equals(value) && !"0".equals(value)) {
               log("Unable to parse the boolean system property '" + key + "':" + value + " - " + "using the default value: " + def);
               return def;
            } else {
               return false;
            }
         } else {
            return true;
         }
      }
   }

   public static int getInt(String key, int def) {
      String value = get(key);
      if (value == null) {
         return def;
      } else {
         value = value.trim().toLowerCase();
         if (INTEGER_PATTERN.matcher(value).matches()) {
            try {
               return Integer.parseInt(value);
            } catch (Exception var4) {
            }
         }

         log("Unable to parse the integer system property '" + key + "':" + value + " - " + "using the default value: " + def);
         return def;
      }
   }

   public static long getLong(String key, long def) {
      String value = get(key);
      if (value == null) {
         return def;
      } else {
         value = value.trim().toLowerCase();
         if (INTEGER_PATTERN.matcher(value).matches()) {
            try {
               return Long.parseLong(value);
            } catch (Exception var5) {
            }
         }

         log("Unable to parse the long integer system property '" + key + "':" + value + " - " + "using the default value: " + def);
         return def;
      }
   }

   private static void log(String msg) {
      if (initializedLogger) {
         logger.warn(msg);
      } else {
         java.util.logging.Logger.getLogger(SystemPropertyUtil.class.getName()).log(Level.WARNING, msg);
      }

   }

   private static void log(String msg, Exception e) {
      if (initializedLogger) {
         logger.warn(msg, e);
      } else {
         java.util.logging.Logger.getLogger(SystemPropertyUtil.class.getName()).log(Level.WARNING, msg, e);
      }

   }

   private SystemPropertyUtil() {
   }
}
