package com.aionemu.commons.scripting.url;

import com.aionemu.commons.scripting.ScriptClassLoader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class VirtualClassURLConnection extends URLConnection {
   private InputStream is;

   protected VirtualClassURLConnection(URL url, ScriptClassLoader cl) {
      super(url);
      this.is = new ByteArrayInputStream(cl.getByteCode(url.getHost()));
   }

   public void connect() throws IOException {
   }

   public InputStream getInputStream() throws IOException {
      return this.is;
   }
}
