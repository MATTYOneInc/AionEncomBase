package com.aionemu.commons.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.concurrent.Executor;

public class AcceptDispatcherImpl extends Dispatcher {
   public AcceptDispatcherImpl(String name) throws IOException {
      super(name, (Executor)null);
   }

   void dispatch() throws IOException {
      if (this.selector.select() != 0) {
         Iterator selectedKeys = this.selector.selectedKeys().iterator();

         while(selectedKeys.hasNext()) {
            SelectionKey key = (SelectionKey)selectedKeys.next();
            selectedKeys.remove();
            if (key.isValid()) {
               this.accept(key);
            }
         }
      }

   }

   void closeConnection(AConnection con) {
      throw new UnsupportedOperationException("This method should never be called!");
   }
}
