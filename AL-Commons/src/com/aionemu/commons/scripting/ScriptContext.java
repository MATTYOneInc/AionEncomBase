package com.aionemu.commons.scripting;

import com.aionemu.commons.scripting.classlistener.ClassListener;
import java.io.File;
import java.util.Collection;

public interface ScriptContext {
   void init();

   void shutdown();

   void reload();

   File getRoot();

   CompilationResult getCompilationResult();

   boolean isInitialized();

   void setLibraries(Iterable<File> var1);

   Iterable<File> getLibraries();

   ScriptContext getParentScriptContext();

   Collection<ScriptContext> getChildScriptContexts();

   void addChildScriptContext(ScriptContext var1);

   void setClassListener(ClassListener var1);

   ClassListener getClassListener();

   void setCompilerClassName(String var1);

   String getCompilerClassName();

   boolean equals(Object var1);

   int hashCode();

   void finalize() throws Throwable;
}
