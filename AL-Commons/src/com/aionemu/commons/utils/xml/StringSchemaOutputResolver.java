package com.aionemu.commons.utils.xml;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

public class StringSchemaOutputResolver extends SchemaOutputResolver {
   private StringWriter sw = null;

   public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
      this.sw = new StringWriter();
      StreamResult sr = new StreamResult();
      sr.setSystemId(String.valueOf(System.currentTimeMillis()));
      sr.setWriter(this.sw);
      return sr;
   }

   public String getSchemma() {
      return this.sw != null ? this.sw.toString() : null;
   }
}
