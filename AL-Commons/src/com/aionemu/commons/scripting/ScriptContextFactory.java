package com.aionemu.commons.scripting;

import com.aionemu.commons.scripting.impl.ScriptContextImpl;
import java.io.File;

public final class ScriptContextFactory {
   public static ScriptContext getScriptContext(File root, ScriptContext parent) throws InstantiationException {
      ScriptContextImpl ctx;
      if (parent == null) {
         ctx = new ScriptContextImpl(root);
      } else {
         ctx = new ScriptContextImpl(root, parent);
         parent.addChildScriptContext(ctx);
      }

      return ctx;
   }
}
