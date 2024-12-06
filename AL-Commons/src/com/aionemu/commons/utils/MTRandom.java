package com.aionemu.commons.utils;

import java.util.Random;

public class MTRandom extends Random {
   private static final long serialVersionUID = -515082678588212038L;
   private static final int UPPER_MASK = Integer.MIN_VALUE;
   private static final int LOWER_MASK = Integer.MAX_VALUE;
   private static final int N = 624;
   private static final int M = 397;
   private static final int[] MAGIC = new int[]{0, -1727483681};
   private static final int MAGIC_FACTOR1 = 1812433253;
   private static final int MAGIC_FACTOR2 = 1664525;
   private static final int MAGIC_FACTOR3 = 1566083941;
   private static final int MAGIC_MASK1 = -1658038656;
   private static final int MAGIC_MASK2 = -272236544;
   private static final int MAGIC_SEED = 19650218;
   private static final long DEFAULT_SEED = 5489L;
   private transient int[] mt;
   private transient int mti;
   private transient boolean compat;
   private transient int[] ibuf;

   public MTRandom() {
      this(false);
   }

   public MTRandom(boolean compatible) {
      super(0L);
      this.compat = false;
      this.compat = compatible;
      this.setSeed(this.compat ? 5489L : System.currentTimeMillis());
   }

   public MTRandom(long seed) {
      super(seed);
      this.compat = false;
   }

   public MTRandom(byte[] buf) {
      super(0L);
      this.compat = false;
      this.setSeed(buf);
   }

   public MTRandom(int[] buf) {
      super(0L);
      this.compat = false;
      this.setSeed(buf);
   }

   private void setSeed(int seed) {
      if (this.mt == null) {
         this.mt = new int[624];
      }

      this.mt[0] = seed;

      for(this.mti = 1; this.mti < 624; ++this.mti) {
         this.mt[this.mti] = 1812433253 * (this.mt[this.mti - 1] ^ this.mt[this.mti - 1] >>> 30) + this.mti;
      }

   }

   public final synchronized void setSeed(long seed) {
      if (this.compat) {
         this.setSeed((int)seed);
      } else {
         if (this.ibuf == null) {
            this.ibuf = new int[2];
         }

         this.ibuf[0] = (int)seed;
         this.ibuf[1] = (int)(seed >>> 32);
         this.setSeed(this.ibuf);
      }

   }

   public final void setSeed(byte[] buf) {
      this.setSeed(pack(buf));
   }

   public final synchronized void setSeed(int[] buf) {
      int length = buf.length;
      if (length == 0) {
         throw new IllegalArgumentException("Seed buffer may not be empty");
      } else {
         int i = 1;
         int j = 0;
         int k = 624 > length ? 624 : length;
         this.setSeed(19650218);

         for(; k > 0; --k) {
            this.mt[i] = (this.mt[i] ^ (this.mt[i - 1] ^ this.mt[i - 1] >>> 30) * 1664525) + buf[j] + j;
            ++i;
            ++j;
            if (i >= 624) {
               this.mt[0] = this.mt[623];
               i = 1;
            }

            if (j >= length) {
               j = 0;
            }
         }

         for(k = 623; k > 0; --k) {
            this.mt[i] = (this.mt[i] ^ (this.mt[i - 1] ^ this.mt[i - 1] >>> 30) * 1566083941) - i;
            ++i;
            if (i >= 624) {
               this.mt[0] = this.mt[623];
               i = 1;
            }
         }

         this.mt[0] = Integer.MIN_VALUE;
      }
   }

   protected final synchronized int next(int bits) {
      int y;
      if (this.mti >= 624) {
         int kk;
         for(kk = 0; kk < 227; ++kk) {
            y = this.mt[kk] & Integer.MIN_VALUE | this.mt[kk + 1] & Integer.MAX_VALUE;
            this.mt[kk] = this.mt[kk + 397] ^ y >>> 1 ^ MAGIC[y & 1];
         }

         while(kk < 623) {
            y = this.mt[kk] & Integer.MIN_VALUE | this.mt[kk + 1] & Integer.MAX_VALUE;
            this.mt[kk] = this.mt[kk + -227] ^ y >>> 1 ^ MAGIC[y & 1];
            ++kk;
         }

         y = this.mt[623] & Integer.MIN_VALUE | this.mt[0] & Integer.MAX_VALUE;
         this.mt[623] = this.mt[396] ^ y >>> 1 ^ MAGIC[y & 1];
         this.mti = 0;
      }

      y = this.mt[this.mti++];
      y ^= y >>> 11;
      y ^= y << 7 & -1658038656;
      y ^= y << 15 & -272236544;
      y ^= y >>> 18;
      return y >>> 32 - bits;
   }

   public static int[] pack(byte[] buf) {
      int blen = buf.length;
      int ilen = buf.length + 3 >>> 2;
      int[] ibuf = new int[ilen];

      for(int n = 0; n < ilen; ++n) {
         int m = n + 1 << 2;
         if (m > blen) {
            m = blen;
         }

         --m;

         int var10000;
         int k;
         for(k = buf[m] & 255; (m & 3) != 0; k = var10000 | buf[m] & 255) {
            var10000 = k << 8;
            --m;
         }

         ibuf[n] = k;
      }

      return ibuf;
   }
}
