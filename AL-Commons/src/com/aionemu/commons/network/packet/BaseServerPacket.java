package com.aionemu.commons.network.packet;

import com.aionemu.commons.utils.PrintUtils;
import java.nio.ByteBuffer;

public abstract class BaseServerPacket extends BasePacket {
   public ByteBuffer buf;

   protected BaseServerPacket(int opcode) {
      super(BasePacket.PacketType.SERVER, opcode);
   }

   protected BaseServerPacket() {
      super(BasePacket.PacketType.SERVER);
   }

   public void setBuf(ByteBuffer buf) {
      this.buf = buf;
   }

   protected final void writeD(int value) {
      this.buf.putInt(value);
   }

   protected final void writeH(int value) {
      this.buf.putShort((short)value);
   }

   protected final void writeC(int value) {
      this.buf.put((byte)value);
   }

   protected final void writeDF(double value) {
      this.buf.putDouble(value);
   }

   protected final void writeF(float value) {
      this.buf.putFloat(value);
   }

   protected final void writeQ(long value) {
      this.buf.putLong(value);
   }

   protected final void writeS(String text) {
      if (text == null) {
         this.buf.putChar('\u0000');
      } else {
         int len = text.length();

         for(int i = 0; i < len; ++i) {
            this.buf.putChar(text.charAt(i));
         }

         this.buf.putChar('\u0000');
      }

   }

   protected final void writeB(byte[] data) {
      this.buf.put(data);
   }

   protected final void writeB(String bytes) {
      this.writeB(PrintUtils.hex2bytes(bytes));
   }
}
