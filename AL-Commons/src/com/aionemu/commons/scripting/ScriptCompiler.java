package com.aionemu.commons.scripting;

import java.io.File;

public interface ScriptCompiler {
   void setParentClassLoader(ScriptClassLoader var1);

   void setLibraires(Iterable<File> var1);

   CompilationResult compile(String var1, String var2);

   CompilationResult compile(String[] var1, String[] var2) throws IllegalArgumentException;

   CompilationResult compile(Iterable<File> var1);

   String[] getSupportedFileTypes();
}
