package com.aionemu.commons.scripting.impl.javacompiler;

import com.aionemu.commons.scripting.CompilationResult;
import com.aionemu.commons.scripting.ScriptClassLoader;
import com.aionemu.commons.scripting.ScriptCompiler;
import com.sun.tools.javac.api.JavacTool;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager.Location;
import javax.tools.JavaFileObject.Kind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptCompilerImpl implements ScriptCompiler {
   private static final Logger log = LoggerFactory.getLogger(ScriptCompilerImpl.class);
   protected final JavaCompiler javaCompiler = JavacTool.create();
   protected Iterable<File> libraries;
   protected ScriptClassLoader parentClassLoader;

   public ScriptCompilerImpl() {
      if (this.javaCompiler == null && ToolProvider.getSystemJavaCompiler() != null) {
         throw new RuntimeException(new InstantiationException("JavaCompiler is not aviable."));
      }
   }

   public void setParentClassLoader(ScriptClassLoader classLoader) {
      this.parentClassLoader = classLoader;
   }

   public void setLibraires(Iterable<File> files) {
      this.libraries = files;
   }

   public CompilationResult compile(String className, String sourceCode) {
      return this.compile(new String[]{className}, new String[]{sourceCode});
   }

   public CompilationResult compile(String[] classNames, String[] sourceCode) throws IllegalArgumentException {
      if (classNames.length != sourceCode.length) {
         throw new IllegalArgumentException("Amount of classes is not equal to amount of sources");
      } else {
         List<JavaFileObject> compilationUnits = new ArrayList();

         for(int i = 0; i < classNames.length; ++i) {
            JavaFileObject compilationUnit = new JavaSourceFromString(classNames[i], sourceCode[i]);
            compilationUnits.add(compilationUnit);
         }

         return this.doCompilation(compilationUnits);
      }
   }

   public CompilationResult compile(Iterable<File> compilationUnits) {
      List<JavaFileObject> list = new ArrayList();
      Iterator i$ = compilationUnits.iterator();

      while(i$.hasNext()) {
         File f = (File)i$.next();
         list.add(new JavaSourceFromFile(f, Kind.SOURCE));
      }

      return this.doCompilation(list);
   }

   protected CompilationResult doCompilation(Iterable<JavaFileObject> compilationUnits) {
      List<String> options = Arrays.asList("-encoding", "UTF-8", "-g");
      DiagnosticListener<JavaFileObject> listener = new ErrorListener();
      ClassFileManager manager = new ClassFileManager(JavacTool.create(), listener);
      manager.setParentClassLoader(this.parentClassLoader);
      if (this.libraries != null) {
         try {
            manager.addLibraries(this.libraries);
         } catch (IOException var8) {
            log.error("Can't set libraries for compiler.", var8);
         }
      }

      CompilationTask task = this.javaCompiler.getTask((Writer)null, manager, listener, options, (Iterable)null, compilationUnits);
      if (!task.call()) {
         throw new RuntimeException("Error while compiling classes");
      } else {
         ScriptClassLoader cl = manager.getClassLoader((Location)null);
         Class<?>[] compiledClasses = this.classNamesToClasses(manager.getCompiledClasses().keySet(), cl);
         return new CompilationResult(compiledClasses, cl);
      }
   }

   protected Class<?>[] classNamesToClasses(Collection<String> classNames, ScriptClassLoader cl) {
      Class<?>[] classes = new Class[classNames.size()];
      int i = 0;

      for(Iterator i$ = classNames.iterator(); i$.hasNext(); ++i) {
         String className = (String)i$.next();

         try {
            Class<?> clazz = cl.loadClass(className);
            classes[i] = clazz;
         } catch (ClassNotFoundException var8) {
            throw new RuntimeException(var8);
         }
      }

      return classes;
   }

   public String[] getSupportedFileTypes() {
      return new String[]{"java"};
   }
}
