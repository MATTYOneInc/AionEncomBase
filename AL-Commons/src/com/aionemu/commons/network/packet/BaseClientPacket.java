package com.aionemu.commons.network.packet;

import com.aionemu.commons.network.AConnection;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseClientPacket<T extends AConnection> extends BasePacket implements Runnable {
   private static final Logger log = LoggerFactory.getLogger(BaseClientPacket.class);
   private T client;
   private ByteBuffer buf;

   public BaseClientPacket(ByteBuffer buf, int opcode) {
      this(opcode);
      this.buf = buf;
   }

   public BaseClientPacket(int opcode) {
      super(BasePacket.PacketType.CLIENT, opcode);
   }

   public void setBuffer(ByteBuffer buf) {
      this.buf = buf;
   }

   public void setConnection(T client) {
      this.client = client;
   }

   public final boolean read() {
      try {
         this.readImpl();
         if (this.getRemainingBytes() > 0) {
            log.debug("Packet " + this + " not fully readed!");
         }

         return true;
      } catch (Exception var2) {
         log.error("Reading failed for packet " + this, var2);
         return false;
      }
   }

   protected abstract void readImpl();

   public final int getRemainingBytes() {
      return this.buf.remaining();
   }

   protected final int readD() {
      try {
         return this.buf.getInt();
      } catch (Exception var2) {
         log.error("Missing D for: " + this);
         return 0;
      }
   }

   protected final int readC() {
      try {
         return this.buf.get() & 255;
      } catch (Exception var2) {
         log.error("Missing C for: " + this);
         return 0;
      }
   }

   protected final byte readSC() {
      try {
         return this.buf.get();
      } catch (Exception var2) {
         log.error("Missing C for: " + this);
         return 0;
      }
   }

   protected final short readSH() {
      try {
         return this.buf.getShort();
      } catch (Exception var2) {
         log.error("Missing H for: " + this);
         return 0;
      }
   }

   protected final int readH() {
      try {
         return this.buf.getShort() & '\uffff';
      } catch (Exception var2) {
         log.error("Missing H for: " + this);
         return 0;
      }
   }

   protected final double readDF() {
      try {
         return this.buf.getDouble();
      } catch (Exception var2) {
         log.error("Missing DF for: " + this);
         return 0.0D;
      }
   }

   protected final float readF() {
      try {
         return this.buf.getFloat();
      } catch (Exception var2) {
         log.error("Missing F for: " + this);
         return 0.0F;
      }
   }

   protected final long readQ() {
      try {
         return this.buf.getLong();
      } catch (Exception var2) {
         log.error("Missing Q for: " + this);
         return 0L;
      }
   }

   protected final String readS() {
      StringBuffer sb = new StringBuffer();

      char ch;
      try {
         while((ch = this.buf.getChar()) != 0) {
            sb.append(ch);
         }
      } catch (Exception var4) {
         log.error("Missing S for: " + this);
      }

      return sb.toString();
   }

   protected final byte[] readB(int length) {
      byte[] result = new byte[length];

      try {
         this.buf.get(result);
      } catch (Exception var4) {
         log.error("Missing byte[] for: " + this);
      }

      return result;
   }

   protected final byte[] readB(String string) {
      String finalString = string.replaceAll("\\s+", "");
      byte[] bytes = new byte[finalString.length() / 2];

      for(int i = 0; i < bytes.length; ++i) {
         bytes[i] = (byte)Integer.parseInt(finalString.substring(2 * i, 2 * i + 2), 16);
      }

      try {
         this.buf.get(bytes);
      } catch (Exception var5) {
         log.error("Missing byte[] for: " + this);
      }

      return bytes;
   }

   protected abstract void runImpl();

   public final T getConnection() {
      return this.client;
   }
}
