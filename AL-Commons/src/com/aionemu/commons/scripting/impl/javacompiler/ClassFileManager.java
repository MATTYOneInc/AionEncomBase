package com.aionemu.commons.scripting.impl.javacompiler;

import com.aionemu.commons.scripting.ScriptClassLoader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject.Kind;

public class ClassFileManager extends ForwardingJavaFileManager<JavaFileManager> {
   private final Map<String, BinaryClass> compiledClasses = new HashMap();
   protected ScriptClassLoaderImpl loader;
   protected ScriptClassLoader parentClassLoader;

   public ClassFileManager(JavaCompiler compiler, DiagnosticListener<? super JavaFileObject> listener) {
      super(compiler.getStandardFileManager(listener, (Locale)null, (Charset)null));
   }

   public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
      BinaryClass co = new BinaryClass(className);
      this.compiledClasses.put(className, co);
      return co;
   }

   public synchronized ScriptClassLoaderImpl getClassLoader(Location location) {
      if (this.loader == null) {
         if (this.parentClassLoader != null) {
            this.loader = new ScriptClassLoaderImpl(this, this.parentClassLoader);
         } else {
            this.loader = new ScriptClassLoaderImpl(this);
         }
      }

      return this.loader;
   }

   public void setParentClassLoader(ScriptClassLoader classLoader) {
      this.parentClassLoader = classLoader;
   }

   public void addLibrary(File file) throws IOException {
      ScriptClassLoaderImpl classLoader = this.getClassLoader((Location)null);
      classLoader.addJarFile(file);
   }

   public void addLibraries(Iterable<File> files) throws IOException {
      Iterator i$ = files.iterator();

      while(i$.hasNext()) {
         File f = (File)i$.next();
         this.addLibrary(f);
      }

   }

   public Map<String, BinaryClass> getCompiledClasses() {
      return this.compiledClasses;
   }

   public Iterable<JavaFileObject> list(Location location, String packageName, Set<Kind> kinds, boolean recurse) throws IOException {
      Iterable<JavaFileObject> objects = super.list(location, packageName, kinds, recurse);
      if (StandardLocation.CLASS_PATH.equals(location) && kinds.contains(Kind.CLASS)) {
         List<JavaFileObject> temp = new ArrayList();
         Iterator i$ = ((Iterable)objects).iterator();

         while(i$.hasNext()) {
            JavaFileObject object = (JavaFileObject)i$.next();
            temp.add(object);
         }

         temp.addAll(this.loader.getClassesForPackage(packageName));
         objects = temp;
      }

      return (Iterable)objects;
   }

   public String inferBinaryName(Location location, JavaFileObject file) {
      return file instanceof BinaryClass ? ((BinaryClass)file).inferBinaryName((Iterable)null) : super.inferBinaryName(location, file);
   }
}
