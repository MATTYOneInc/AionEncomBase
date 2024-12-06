package com.aionemu.commons.scripting;

import java.util.Arrays;

public class CompilationResult {
   private final Class<?>[] compiledClasses;
   private final ScriptClassLoader classLoader;

   public CompilationResult(Class<?>[] compiledClasses, ScriptClassLoader classLoader) {
      this.compiledClasses = compiledClasses;
      this.classLoader = classLoader;
   }

   public ScriptClassLoader getClassLoader() {
      return this.classLoader;
   }

   public Class<?>[] getCompiledClasses() {
      return this.compiledClasses;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("CompilationResult");
      sb.append("{classLoader=").append(this.classLoader);
      sb.append(", compiledClasses=").append(this.compiledClasses == null ? "null" : Arrays.asList(this.compiledClasses).toString());
      sb.append('}');
      return sb.toString();
   }
}
