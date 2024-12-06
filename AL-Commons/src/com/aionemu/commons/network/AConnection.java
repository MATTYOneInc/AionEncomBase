package com.aionemu.commons.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public abstract class AConnection {
   private final SocketChannel socketChannel;
   private final Dispatcher dispatcher;
   private SelectionKey key;
   protected boolean pendingClose;
   protected boolean isForcedClosing;
   protected boolean closed;
   protected final Object guard = new Object();
   public final ByteBuffer writeBuffer;
   public final ByteBuffer readBuffer;
   private final String ip;
   private boolean locked = false;

   public AConnection(SocketChannel sc, Dispatcher d, int rbSize, int wbSize) throws IOException {
      this.socketChannel = sc;
      this.dispatcher = d;
      this.writeBuffer = ByteBuffer.allocate(wbSize);
      this.writeBuffer.flip();
      this.writeBuffer.order(ByteOrder.LITTLE_ENDIAN);
      this.readBuffer = ByteBuffer.allocate(rbSize);
      this.readBuffer.order(ByteOrder.LITTLE_ENDIAN);
      this.ip = this.socketChannel.socket().getInetAddress().getHostAddress();
   }

   final void setKey(SelectionKey key) {
      this.key = key;
   }

   protected final void enableWriteInterest() {
      if (this.key.isValid()) {
         this.key.interestOps(this.key.interestOps() | 4);
         this.key.selector().wakeup();
      }

   }

   final Dispatcher getDispatcher() {
      return this.dispatcher;
   }

   public SocketChannel getSocketChannel() {
      return this.socketChannel;
   }

   public final void close(boolean forced) {
      synchronized(this.guard) {
         if (!this.isWriteDisabled()) {
            this.isForcedClosing = forced;
            this.getDispatcher().closeConnection(this);
         }
      }
   }

   final boolean onlyClose() {
      synchronized(this.guard) {
         if (this.closed) {
            return false;
         } else {
            try {
               if (this.socketChannel.isOpen()) {
                  this.socketChannel.close();
                  this.key.attach((Object)null);
                  this.key.cancel();
               }

               this.closed = true;
            } catch (IOException var4) {
            }

            return true;
         }
      }
   }

   final boolean isPendingClose() {
      return this.pendingClose && !this.closed;
   }

   protected final boolean isWriteDisabled() {
      return this.pendingClose || this.closed;
   }

   public final String getIP() {
      return this.ip;
   }

   boolean tryLockConnection() {
      return this.locked ? false : (this.locked = true);
   }

   void unlockConnection() {
      this.locked = false;
   }

   protected abstract boolean processData(ByteBuffer var1);

   protected abstract boolean writeData(ByteBuffer var1);

   protected abstract void initialized();

   protected abstract long getDisconnectionDelay();

   protected abstract void onDisconnect();

   protected abstract void onServerClose();
}
