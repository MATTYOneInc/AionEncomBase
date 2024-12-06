package com.aionemu.commons.utils;

public class NetworkUtils {
   public static boolean checkIPMatching(String pattern, String address) {
      if (!pattern.equals("*.*.*.*") && !pattern.equals("*")) {
         String[] mask = pattern.split("\\.");
         String[] ip_address = address.split("\\.");

         for(int i = 0; i < mask.length; ++i) {
            if (!mask[i].equals("*") && !mask[i].equals(ip_address[i])) {
               if (!mask[i].contains("-")) {
                  return false;
               }

               byte min = Byte.parseByte(mask[i].split("-")[0]);
               byte max = Byte.parseByte(mask[i].split("-")[1]);
               byte ip = Byte.parseByte(ip_address[i]);
               if (ip < min || ip > max) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return true;
      }
   }
}
