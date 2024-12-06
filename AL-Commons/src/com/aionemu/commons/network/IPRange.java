package com.aionemu.commons.network;

import java.util.Arrays;

public class IPRange {
   private final long min;
   private final long max;
   private final byte[] address;

   public IPRange(String min, String max, String address) {
      this.min = toLong(toByteArray(min));
      this.max = toLong(toByteArray(max));
      this.address = toByteArray(address);
   }

   public IPRange(byte[] min, byte[] max, byte[] address) {
      this.min = toLong(min);
      this.max = toLong(max);
      this.address = address;
   }

   public boolean isInRange(String address) {
      long addr = toLong(toByteArray(address));
      return addr >= this.min && addr <= this.max;
   }

   public byte[] getAddress() {
      return this.address;
   }

   public byte[] getMinAsByteArray() {
      return toBytes(this.min);
   }

   public byte[] getMaxAsByteArray() {
      return toBytes(this.max);
   }

   private static long toLong(byte[] bytes) {
      long result = 0L;
      result += (long)(bytes[3] & 255);
      result += (long)((bytes[2] & 255) << 8);
      result += (long)((bytes[1] & 255) << 16);
      result += (long)(bytes[0] << 24);
      return result & 4294967295L;
   }

   private static byte[] toBytes(long val) {
      byte[] result = new byte[]{(byte)((int)(val >> 24 & 255L)), (byte)((int)(val >> 16 & 255L)), (byte)((int)(val >> 8 & 255L)), (byte)((int)(val & 255L))};
      return result;
   }

   public static byte[] toByteArray(String address) {
      byte[] result = new byte[4];
      String[] strings = address.split("\\.");
      int i = 0;

      for(int n = strings.length; i < n; ++i) {
         result[i] = (byte)Integer.parseInt(strings[i]);
      }

      return result;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof IPRange)) {
         return false;
      } else {
         IPRange ipRange = (IPRange)o;
         return this.max == ipRange.max && this.min == ipRange.min && Arrays.equals(this.address, ipRange.address);
      }
   }

   public int hashCode() {
      int result = (int)(this.min ^ this.min >>> 32);
      result = 31 * result + (int)(this.max ^ this.max >>> 32);
      result = 31 * result + Arrays.hashCode(this.address);
      return result;
   }
}
