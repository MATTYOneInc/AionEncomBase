package com.aionemu.commons.scripting.impl;

import com.aionemu.commons.scripting.CompilationResult;
import com.aionemu.commons.scripting.ScriptCompiler;
import com.aionemu.commons.scripting.ScriptContext;
import com.aionemu.commons.scripting.classlistener.AggregatedClassListener;
import com.aionemu.commons.scripting.classlistener.ClassListener;
import com.aionemu.commons.scripting.classlistener.OnClassLoadUnloadListener;
import com.aionemu.commons.scripting.classlistener.ScheduledTaskClassListener;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptContextImpl implements ScriptContext {
   private static final Logger log = LoggerFactory.getLogger(ScriptContextImpl.class);
   private final ScriptContext parentScriptContext;
   private Iterable<File> libraries;
   private final File root;
   private CompilationResult compilationResult;
   private Set<ScriptContext> childScriptContexts;
   private ClassListener classListener;
   private String compilerClassName;

   public ScriptContextImpl(File root) {
      this(root, (ScriptContext)null);
   }

   public ScriptContextImpl(File root, ScriptContext parent) {
      if (root == null) {
         throw new NullPointerException("Root file must be specified");
      } else if (root.exists() && root.isDirectory()) {
         this.root = root;
         this.parentScriptContext = parent;
      } else {
         throw new IllegalArgumentException("Root directory not exists or is not a directory");
      }
   }

   public synchronized void init() {
      if (this.compilationResult != null) {
         log.error("Init request on initialized ScriptContext");
      } else {
         ScriptCompiler scriptCompiler = this.instantiateCompiler();
         Collection<File> files = FileUtils.listFiles(this.root, scriptCompiler.getSupportedFileTypes(), true);
         if (this.parentScriptContext != null) {
            scriptCompiler.setParentClassLoader(this.parentScriptContext.getCompilationResult().getClassLoader());
         }

         scriptCompiler.setLibraires(this.libraries);
         this.compilationResult = scriptCompiler.compile(files);
         this.getClassListener().postLoad(this.compilationResult.getCompiledClasses());
         if (this.childScriptContexts != null) {
            Iterator i$ = this.childScriptContexts.iterator();

            while(i$.hasNext()) {
               ScriptContext context = (ScriptContext)i$.next();
               context.init();
            }
         }

      }
   }

   public synchronized void shutdown() {
      if (this.compilationResult == null) {
         log.error("Shutdown of not initialized stript context", new Exception());
      } else {
         if (this.childScriptContexts != null) {
            Iterator i$ = this.childScriptContexts.iterator();

            while(i$.hasNext()) {
               ScriptContext child = (ScriptContext)i$.next();
               child.shutdown();
            }
         }

         this.getClassListener().preUnload(this.compilationResult.getCompiledClasses());
         this.compilationResult = null;
      }
   }

   public void reload() {
      this.shutdown();
      this.init();
   }

   public File getRoot() {
      return this.root;
   }

   public CompilationResult getCompilationResult() {
      return this.compilationResult;
   }

   public synchronized boolean isInitialized() {
      return this.compilationResult != null;
   }

   public void setLibraries(Iterable<File> files) {
      this.libraries = files;
   }

   public Iterable<File> getLibraries() {
      return this.libraries;
   }

   public ScriptContext getParentScriptContext() {
      return this.parentScriptContext;
   }

   public Collection<ScriptContext> getChildScriptContexts() {
      return this.childScriptContexts;
   }

   public void addChildScriptContext(ScriptContext context) {
      synchronized(this) {
         if (this.childScriptContexts == null) {
            this.childScriptContexts = new HashSet();
         }

         if (this.childScriptContexts.contains(context)) {
            log.error("Double child definition, root: " + this.root.getAbsolutePath() + ", child: " + context.getRoot().getAbsolutePath());
            return;
         }

         if (this.isInitialized()) {
            context.init();
         }
      }

      this.childScriptContexts.add(context);
   }

   public void setClassListener(ClassListener cl) {
      this.classListener = cl;
   }

   public ClassListener getClassListener() {
      if (this.classListener == null) {
         if (this.getParentScriptContext() == null) {
            AggregatedClassListener acl = new AggregatedClassListener();
            acl.addClassListener(new OnClassLoadUnloadListener());
            acl.addClassListener(new ScheduledTaskClassListener());
            this.setClassListener(acl);
            return this.classListener;
         } else {
            return this.getParentScriptContext().getClassListener();
         }
      } else {
         return this.classListener;
      }
   }

   public void setCompilerClassName(String className) {
      this.compilerClassName = className;
   }

   public String getCompilerClassName() {
      return this.compilerClassName;
   }

   protected ScriptCompiler instantiateCompiler() throws RuntimeException {
      ClassLoader cl = this.getClass().getClassLoader();
      if (this.getParentScriptContext() != null) {
         cl = this.getParentScriptContext().getCompilationResult().getClassLoader();
      }

      try {
         ScriptCompiler sc = (ScriptCompiler)Class.forName(this.getCompilerClassName(), true, (ClassLoader)cl).newInstance();
         return sc;
      } catch (Exception var4) {
         log.error("Can't create instance of compiler");
         throw new RuntimeException(var4);
      }
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ScriptContextImpl)) {
         return false;
      } else {
         ScriptContextImpl another = (ScriptContextImpl)obj;
         if (this.parentScriptContext == null) {
            return another.getRoot().equals(this.root);
         } else {
            return another.getRoot().equals(this.root) && this.parentScriptContext.equals(another.parentScriptContext);
         }
      }
   }

   public int hashCode() {
      int result = this.parentScriptContext != null ? this.parentScriptContext.hashCode() : 0;
      result = 31 * result + this.root.hashCode();
      return result;
   }

   public void finalize() throws Throwable {
      if (this.compilationResult != null) {
         log.error("Finalization of initialized ScriptContext. Forcing context shutdown.");
         this.shutdown();
      }

      super.finalize();
   }
}
