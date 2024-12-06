package com.aionemu.commons.scripting.impl.javacompiler;

import java.net.URI;
import javax.tools.SimpleJavaFileObject;
import javax.tools.JavaFileObject.Kind;

public class JavaSourceFromString extends SimpleJavaFileObject {
   private final String code;

   public JavaSourceFromString(String className, String code) {
      super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
      this.code = code;
   }

   public CharSequence getCharContent(boolean ignoreEncodingErrors) {
      return this.code;
   }
}
