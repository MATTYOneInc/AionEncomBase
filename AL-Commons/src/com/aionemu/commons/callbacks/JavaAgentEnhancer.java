package com.aionemu.commons.callbacks;

import com.aionemu.commons.callbacks.enhancer.GlobalCallbackEnhancer;
import com.aionemu.commons.callbacks.enhancer.ObjectCallbackEnhancer;
import java.lang.instrument.Instrumentation;

public class JavaAgentEnhancer {
   public static void premain(String args, Instrumentation instrumentation) {
      instrumentation.addTransformer(new ObjectCallbackEnhancer(), true);
      instrumentation.addTransformer(new GlobalCallbackEnhancer(), true);
   }
}
