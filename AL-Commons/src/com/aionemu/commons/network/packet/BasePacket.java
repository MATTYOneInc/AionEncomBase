package com.aionemu.commons.network.packet;

public abstract class BasePacket {
   public static final String TYPE_PATTERN = "[%s] 0x%02X %s";
   private final BasePacket.PacketType packetType;
   private int opcode;

   protected BasePacket(BasePacket.PacketType packetType, int opcode) {
      this.packetType = packetType;
      this.opcode = opcode;
   }

   protected BasePacket(BasePacket.PacketType packetType) {
      this.packetType = packetType;
   }

   protected void setOpcode(int opcode) {
      this.opcode = opcode;
   }

   public final int getOpcode() {
      return this.opcode;
   }

   public final BasePacket.PacketType getPacketType() {
      return this.packetType;
   }

   public String getPacketName() {
      return this.getClass().getSimpleName();
   }

   public String toString() {
      return String.format("[%s] 0x%02X %s", this.getPacketType().getName(), this.getOpcode(), this.getPacketName());
   }

   public static enum PacketType {
      SERVER("S"),
      CLIENT("C");

      private final String name;

      private PacketType(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }
   }
}
