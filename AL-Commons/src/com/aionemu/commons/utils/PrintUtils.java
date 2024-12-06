package com.aionemu.commons.utils;

import java.nio.ByteBuffer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class PrintUtils {
   public static void printSection(String sectionName) {
      StringBuilder sb = new StringBuilder();
      sb.append("-[ " + sectionName + " ]");

      while(sb.length() < 79) {
         sb.insert(0, "=");
      }

      System.out.println(sb.toString());
   }

   public static byte[] hex2bytes(String string) {
      String finalString = string.replaceAll("\\s+", "");
      byte[] bytes = new byte[finalString.length() / 2];

      for(int i = 0; i < bytes.length; ++i) {
         bytes[i] = (byte)Integer.parseInt(finalString.substring(2 * i, 2 * i + 2), 16);
      }

      return bytes;
   }

   public static String bytes2hex(byte[] bytes) {
      StringBuilder result = new StringBuilder();
      byte[] arr$ = bytes;
      int len$ = bytes.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         byte b = arr$[i$];
         int value = b & 255;
         result.append(String.format("%02X", value));
      }

      return result.toString();
   }

   public static String reverseHex(String input) {
      String[] chunked = new String[input.length() / 2];
      int position = 0;

      for(int i = 0; i < input.length(); i += 2) {
         chunked[position] = input.substring(position * 2, position * 2 + 2);
         ++position;
      }

      ArrayUtils.reverse(chunked);
      return StringUtils.join(chunked);
   }

   public static String toHex(ByteBuffer data) {
      int position = data.position();
      StringBuilder result = new StringBuilder();
      int counter = 0;

      while(data.hasRemaining()) {
         if (counter % 16 == 0) {
            result.append(String.format("%04X: ", counter));
         }

         int b = data.get() & 255;
         result.append(String.format("%02X ", b));
         ++counter;
         if (counter % 16 == 0) {
            result.append("  ");
            toText(data, result, 16);
            result.append("\n");
         }
      }

      int rest = counter % 16;
      if (rest > 0) {
         for(int i = 0; i < 17 - rest; ++i) {
            result.append("   ");
         }

         toText(data, result, rest);
      }

      data.position(position);
      return result.toString();
   }

   private static void toText(ByteBuffer data, StringBuilder result, int cnt) {
      int charPos = data.position() - cnt;

      for(int a = 0; a < cnt; ++a) {
         int c = data.get(charPos++);
         if (c > 31 && c < 128) {
            result.append((char)c);
         } else {
            result.append('.');
         }
      }

   }
}
