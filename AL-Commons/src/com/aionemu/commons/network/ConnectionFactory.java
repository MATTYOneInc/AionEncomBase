package com.aionemu.commons.network;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface ConnectionFactory {
   AConnection create(SocketChannel var1, Dispatcher var2) throws IOException;
}
