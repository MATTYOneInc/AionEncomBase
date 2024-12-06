package com.aionemu.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

public class PropertiesUtils {
   public static Properties load(String file) throws IOException {
      return load(new File(file));
   }

   public static Properties load(File file) throws IOException {
      FileInputStream fis = new FileInputStream(file);
      Properties p = new Properties();
      p.load(fis);
      fis.close();
      return p;
   }

   public static Properties[] load(String... files) throws IOException {
      Properties[] result = new Properties[files.length];

      for(int i = 0; i < result.length; ++i) {
         result[i] = load(files[i]);
      }

      return result;
   }

   public static Properties[] load(File... files) throws IOException {
      Properties[] result = new Properties[files.length];

      for(int i = 0; i < result.length; ++i) {
         result[i] = load(files[i]);
      }

      return result;
   }

   public static Properties[] loadAllFromDirectory(String dir) throws IOException {
      return loadAllFromDirectory(new File(dir), false);
   }

   public static Properties[] loadAllFromDirectory(File dir) throws IOException {
      return loadAllFromDirectory(dir, false);
   }

   public static Properties[] loadAllFromDirectory(String dir, boolean recursive) throws IOException {
      return loadAllFromDirectory(new File(dir), recursive);
   }

   public static Properties[] loadAllFromDirectory(File dir, boolean recursive) throws IOException {
      Collection<File> files = FileUtils.listFiles(dir, new String[]{"properties"}, recursive);
      return load((File[])files.toArray(new File[files.size()]));
   }

   public static Properties[] overrideProperties(Properties[] initialProperties, Properties[] properties) {
      if (properties != null) {
         Properties[] arr$ = properties;
         int len$ = properties.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Properties props = arr$[i$];
            overrideProperties(initialProperties, props);
         }
      }

      return initialProperties;
   }

   public static Properties[] overrideProperties(Properties[] initialProperties, Properties properties) {
      if (properties != null) {
         Properties[] arr$ = initialProperties;
         int len$ = initialProperties.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Properties initialProps = arr$[i$];
            initialProps.putAll(properties);
         }
      }

      return initialProperties;
   }
}
