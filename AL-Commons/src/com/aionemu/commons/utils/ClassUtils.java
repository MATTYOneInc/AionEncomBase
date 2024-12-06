package com.aionemu.commons.utils;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassUtils {
   private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);

   public static boolean isSubclass(Class<?> a, Class<?> b) {
      if (a == b) {
         return true;
      } else if (a != null && b != null) {
         for(Class x = a; x != null; x = x.getSuperclass()) {
            if (x == b) {
               return true;
            }

            if (b.isInterface()) {
               Class<?>[] interfaces = x.getInterfaces();
               Class[] arr$ = interfaces;
               int len$ = interfaces.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  Class<?> anInterface = arr$[i$];
                  if (isSubclass(anInterface, b)) {
                     return true;
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static boolean isPackageMember(Class<?> clazz, String packageName) {
      return isPackageMember(clazz.getName(), packageName);
   }

   public static boolean isPackageMember(String className, String packageName) {
      if (className.contains(".")) {
         String classPackage = className.substring(0, className.lastIndexOf(46));
         return packageName.equals(classPackage);
      } else {
         return packageName == null || packageName.isEmpty();
      }
   }

   public static Set<String> getClassNamesFromDirectory(File directory) throws IllegalArgumentException {
      if (directory.isDirectory() && directory.exists()) {
         return getClassNamesFromPackage(directory, (String)null, true);
      } else {
         throw new IllegalArgumentException("Directory " + directory + " doesn't exists or is not directory");
      }
   }

   public static Set<String> getClassNamesFromPackage(File directory, String packageName, boolean recursive) {
      Set<String> classes = new HashSet();
      if (!directory.exists()) {
         return classes;
      } else {
         File[] files = directory.listFiles();
         File[] arr$ = files;
         int len$ = files.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            File file = arr$[i$];
            String newPackage;
            if (file.isDirectory()) {
               if (recursive) {
                  newPackage = file.getName();
                  if (!GenericValidator.isBlankOrNull(packageName)) {
                     newPackage = packageName + "." + newPackage;
                  }

                  classes.addAll(getClassNamesFromPackage(file, newPackage, recursive));
               }
            } else if (file.getName().endsWith(".class")) {
               newPackage = file.getName().substring(0, file.getName().length() - 6);
               if (!GenericValidator.isBlankOrNull(packageName)) {
                  newPackage = packageName + "." + newPackage;
               }

               classes.add(newPackage);
            }
         }

         return classes;
      }
   }

   public static Set<String> getClassNamesFromJarFile(File file) throws IOException {
      if (file.exists() && !file.isDirectory()) {
         Set<String> result = new HashSet();
         JarFile jarFile = null;

         try {
            jarFile = new JarFile(file);
            Enumeration entries = jarFile.entries();

            while(entries.hasMoreElements()) {
               JarEntry entry = (JarEntry)entries.nextElement();
               String name = entry.getName();
               if (name.endsWith(".class")) {
                  name = name.substring(0, name.length() - 6);
                  name = name.replace('/', '.');
                  result.add(name);
               }
            }
         } finally {
            if (jarFile != null) {
               try {
                  jarFile.close();
               } catch (IOException var11) {
                  log.error("Failed to close jar file " + jarFile.getName(), var11);
               }
            }

         }

         return result;
      } else {
         throw new IllegalArgumentException("File " + file + " is not valid jar file");
      }
   }
}
