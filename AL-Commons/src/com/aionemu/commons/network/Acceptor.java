package com.aionemu.commons.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor {
   private final ConnectionFactory factory;
   private final NioServer nioServer;

   Acceptor(ConnectionFactory factory, NioServer nioServer) {
      this.factory = factory;
      this.nioServer = nioServer;
   }

   public final void accept(SelectionKey key) throws IOException {
      ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
      SocketChannel socketChannel = serverSocketChannel.accept();
      socketChannel.configureBlocking(false);
      Dispatcher dispatcher = this.nioServer.getReadWriteDispatcher();
      AConnection con = this.factory.create(socketChannel, dispatcher);
      if (con != null) {
         dispatcher.register(socketChannel, 1, (AConnection)con);
         con.initialized();
      }
   }
}
