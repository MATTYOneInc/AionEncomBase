package com.aionemu.commons.scripting.scriptmanager;

import com.aionemu.commons.scripting.ScriptCompiler;
import com.aionemu.commons.scripting.ScriptContext;
import com.aionemu.commons.scripting.ScriptContextFactory;
import com.aionemu.commons.scripting.classlistener.ClassListener;
import com.aionemu.commons.scripting.impl.javacompiler.ScriptCompilerImpl;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptManager {
   private static final Logger log = LoggerFactory.getLogger(ScriptManager.class);
   public static final Class<? extends ScriptCompiler> DEFAULT_COMPILER_CLASS = ScriptCompilerImpl.class;
   private Set<ScriptContext> contexts = new HashSet();
   private ClassListener globalClassListener;

   public synchronized void load(File scriptDescriptor) throws Exception {
      FileInputStream fin = new FileInputStream(scriptDescriptor);
      JAXBContext c = JAXBContext.newInstance(ScriptInfo.class, ScriptList.class);
      Unmarshaller u = c.createUnmarshaller();
      ScriptList list = null;

      try {
         list = (ScriptList)u.unmarshal(fin);
      } catch (Exception var11) {
         throw var11;
      } finally {
         if (fin != null) {
            fin.close();
         }

      }

      Iterator i$ = list.getScriptInfos().iterator();

      while(i$.hasNext()) {
         ScriptInfo si = (ScriptInfo)i$.next();
         ScriptContext context = this.createContext(si, (ScriptContext)null);
         if (context != null) {
            this.contexts.add(context);
            context.init();
         }
      }

   }

   public synchronized void loadDirectory(File directory) throws RuntimeException {
      Collection<File> libraries = FileUtils.listFiles(directory, new String[]{"jar"}, true);
      ArrayList list = Lists.newArrayList(libraries);

      try {
         this.loadDirectory(directory, list, DEFAULT_COMPILER_CLASS.getName());
      } catch (Exception var5) {
         throw new RuntimeException("Failed to load script context from directory " + directory.getAbsolutePath(), var5);
      }
   }

   public synchronized void loadDirectory(File directory, List<File> libraries, String compilerClassName) throws Exception {
      if (!directory.isDirectory()) {
         throw new IllegalArgumentException("File should be directory");
      } else {
         ScriptInfo si = new ScriptInfo();
         si.setRoot(directory);
         si.setCompilerClass(compilerClassName);
         si.setScriptInfos(Collections.emptyList());
         si.setLibraries(libraries);
         ScriptContext sc = this.createContext(si, (ScriptContext)null);
         this.contexts.add(sc);
         sc.init();
      }
   }

   protected ScriptContext createContext(ScriptInfo si, ScriptContext parent) throws Exception {
      ScriptContext context = ScriptContextFactory.getScriptContext(si.getRoot(), parent);
      context.setLibraries(si.getLibraries());
      context.setCompilerClassName(si.getCompilerClass());
      if (parent == null && this.contexts.contains(context)) {
         log.warn("Double root script context definition: " + si.getRoot().getAbsolutePath());
         return null;
      } else {
         if (si.getScriptInfos() != null && !si.getScriptInfos().isEmpty()) {
            Iterator i$ = si.getScriptInfos().iterator();

            while(i$.hasNext()) {
               ScriptInfo child = (ScriptInfo)i$.next();
               this.createContext(child, context);
            }
         }

         if (parent == null && this.globalClassListener != null) {
            context.setClassListener(this.globalClassListener);
         }

         return context;
      }
   }

   public synchronized void shutdown() {
      Iterator i$ = this.contexts.iterator();

      while(i$.hasNext()) {
         ScriptContext context = (ScriptContext)i$.next();
         context.shutdown();
      }

      this.contexts.clear();
   }

   public synchronized void reload() {
      Iterator i$ = this.contexts.iterator();

      while(i$.hasNext()) {
         ScriptContext context = (ScriptContext)i$.next();
         this.reloadContext(context);
      }

   }

   public void reloadContext(ScriptContext ctx) {
      ctx.reload();
   }

   public synchronized Collection<ScriptContext> getScriptContexts() {
      return Collections.unmodifiableSet(this.contexts);
   }

   public void setGlobalClassListener(ClassListener instance) {
      this.globalClassListener = instance;
   }
}
