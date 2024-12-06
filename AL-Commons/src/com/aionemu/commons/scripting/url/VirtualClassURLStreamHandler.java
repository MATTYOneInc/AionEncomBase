package com.aionemu.commons.scripting.url;

import com.aionemu.commons.scripting.ScriptClassLoader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class VirtualClassURLStreamHandler extends URLStreamHandler {
   public static final String HANDLER_PROTOCOL = "aescript://";
   private final ScriptClassLoader cl;

   public VirtualClassURLStreamHandler(ScriptClassLoader cl) {
      this.cl = cl;
   }

   protected URLConnection openConnection(URL u) throws IOException {
      return new VirtualClassURLConnection(u, this.cl);
   }
}
