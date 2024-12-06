package com.aionemu.commons.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AEInfos {
   private static final Logger log = LoggerFactory.getLogger(AEInfos.class);

   public static String[] getMemoryInfo() {
      double max = (double)(Runtime.getRuntime().maxMemory() / 1024L);
      double allocated = (double)(Runtime.getRuntime().totalMemory() / 1024L);
      double nonAllocated = max - allocated;
      double cached = (double)(Runtime.getRuntime().freeMemory() / 1024L);
      double used = allocated - cached;
      double useable = max - used;
      DecimalFormat df = new DecimalFormat(" (0.0000'%')");
      DecimalFormat df2 = new DecimalFormat(" # 'KB'");
      return new String[]{"+----", "| Global Memory Informations at " + getRealTime().toString() + ":", "|    |", "| Allowed Memory:" + df2.format(max), "|    |= Allocated Memory:" + df2.format(allocated) + df.format(allocated / max * 100.0D), "|    |= Non-Allocated Memory:" + df2.format(nonAllocated) + df.format(nonAllocated / max * 100.0D), "| Allocated Memory:" + df2.format(allocated), "|    |= Used Memory:" + df2.format(used) + df.format(used / max * 100.0D), "|    |= Unused (cached) Memory:" + df2.format(cached) + df.format(cached / max * 100.0D), "| Useable Memory:" + df2.format(useable) + df.format(useable / max * 100.0D), "+----"};
   }

   public static String[] getCPUInfo() {
      return new String[]{"Avaible CPU(s): " + Runtime.getRuntime().availableProcessors(), "Processor(s) Identifier: " + System.getenv("PROCESSOR_IDENTIFIER"), "..................................................", ".................................................."};
   }

   public static String[] getOSInfo() {
      return new String[]{"OS: " + System.getProperty("os.name") + " Build: " + System.getProperty("os.version"), "OS Arch: " + System.getProperty("os.arch"), "..................................................", ".................................................."};
   }

   public static String[] getJREInfo() {
      return new String[]{"Java Platform Information", "Java Runtime  Name: " + System.getProperty("java.runtime.name"), "Java Version: " + System.getProperty("java.version"), "Java Class Version: " + System.getProperty("java.class.version"), "..................................................", ".................................................."};
   }

   public static String[] getJVMInfo() {
      return new String[]{"Virtual Machine Information (JVM)", "JVM Name: " + System.getProperty("java.vm.name"), "JVM installation directory: " + System.getProperty("java.home"), "JVM version: " + System.getProperty("java.vm.version"), "JVM Vendor: " + System.getProperty("java.vm.vendor"), "JVM Info: " + System.getProperty("java.vm.info"), "..................................................", ".................................................."};
   }

   public static String getRealTime() {
      SimpleDateFormat String = new SimpleDateFormat("H:mm:ss");
      return String.format(new Date());
   }

   public static void printMemoryInfo() {
      String[] arr$ = getMemoryInfo();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String line = arr$[i$];
         log.info(line);
      }

   }

   public static void printCPUInfo() {
      String[] arr$ = getCPUInfo();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String line = arr$[i$];
         log.info(line);
      }

   }

   public static void printOSInfo() {
      String[] arr$ = getOSInfo();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String line = arr$[i$];
         log.info(line);
      }

   }

   public static void printJREInfo() {
      String[] arr$ = getJREInfo();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String line = arr$[i$];
         log.info(line);
      }

   }

   public static void printJVMInfo() {
      String[] arr$ = getJVMInfo();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String line = arr$[i$];
         log.info(line);
      }

   }

   public static void printRealTime() {
      log.info(getRealTime().toString());
   }

   public static void printAllInfos() {
      printOSInfo();
      printCPUInfo();
      printJREInfo();
      printJVMInfo();
      printMemoryInfo();
   }
}
