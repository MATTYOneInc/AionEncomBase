package com.aionemu.commons.scripting;

import com.aionemu.commons.scripting.url.VirtualClassURLStreamHandler;
import com.aionemu.commons.utils.ClassUtils;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ScriptClassLoader extends URLClassLoader {
   private static final Logger log = LoggerFactory.getLogger(ScriptClassLoader.class);
   private final VirtualClassURLStreamHandler urlStreamHandler = new VirtualClassURLStreamHandler(this);
   private Set<String> libraryClassNames = new HashSet();
   private Set<File> loadedLibraries = new HashSet();

   public ScriptClassLoader(URL[] urls, ClassLoader parent) {
      super(urls, parent);
   }

   public ScriptClassLoader(URL[] urls) {
      super(urls);
   }

   public ScriptClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
      super(urls, parent, factory);
   }

   public void addJarFile(File file) throws IOException {
      if (!this.loadedLibraries.contains(file)) {
         Set<String> jarFileClasses = ClassUtils.getClassNamesFromJarFile(file);
         this.libraryClassNames.addAll(jarFileClasses);
         this.loadedLibraries.add(file);
      }

   }

   public URL getResource(String name) {
      if (!name.endsWith(".class")) {
         return super.getResource(name);
      } else {
         String newName = name.substring(0, name.length() - 6);
         newName = newName.replace('/', '.');
         if (this.getCompiledClasses().contains(newName)) {
            try {
               return new URL((URL)null, "aescript://" + newName, this.urlStreamHandler);
            } catch (MalformedURLException var4) {
               log.error("Can't create url for compiled class", var4);
            }
         }

         return super.getResource(name);
      }
   }

   public Class<?> loadClass(String name) throws ClassNotFoundException {
      boolean isCompiled = this.getCompiledClasses().contains(name);
      if (!isCompiled) {
         return super.loadClass(name, true);
      } else {
         Class<?> c = this.getDefinedClass(name);
         if (c == null) {
            byte[] b = this.getByteCode(name);
            c = super.defineClass(name, b, 0, b.length);
            this.setDefinedClass(name, c);
         }

         return c;
      }
   }

   protected Set<String> getLibraryClassNames() {
      return Collections.unmodifiableSet(this.libraryClassNames);
   }

   public abstract Set<String> getCompiledClasses();

   public abstract byte[] getByteCode(String var1);

   public abstract Class<?> getDefinedClass(String var1);

   public abstract void setDefinedClass(String var1, Class<?> var2) throws IllegalArgumentException;
}
