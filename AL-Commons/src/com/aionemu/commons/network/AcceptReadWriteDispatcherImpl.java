package com.aionemu.commons.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public class AcceptReadWriteDispatcherImpl extends Dispatcher {
   private final List<AConnection> pendingClose = new ArrayList();

   public AcceptReadWriteDispatcherImpl(String name, Executor dcPool) throws IOException {
      super(name, dcPool);
   }

   void dispatch() throws IOException {
      int selected = this.selector.select();
      this.processPendingClose();
      if (selected != 0) {
         Iterator selectedKeys = this.selector.selectedKeys().iterator();

         while(selectedKeys.hasNext()) {
            SelectionKey key = (SelectionKey)selectedKeys.next();
            selectedKeys.remove();
            if (key.isValid()) {
               switch(key.readyOps()) {
               case 1:
                  this.read(key);
                  break;
               case 4:
                  this.write(key);
                  break;
               case 5:
                  this.read(key);
                  if (key.isValid()) {
                     this.write(key);
                  }
                  break;
               case 16:
                  this.accept(key);
               }
            }
         }
      }

   }

   void closeConnection(AConnection con) {
      synchronized(this.pendingClose) {
         this.pendingClose.add(con);
      }
   }

   private void processPendingClose() {
      synchronized(this.pendingClose) {
         Iterator i$ = this.pendingClose.iterator();

         while(i$.hasNext()) {
            AConnection connection = (AConnection)i$.next();
            this.closeConnectionImpl(connection);
         }

         this.pendingClose.clear();
      }
   }
}
