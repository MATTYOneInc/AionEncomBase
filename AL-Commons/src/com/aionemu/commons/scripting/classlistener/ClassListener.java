package com.aionemu.commons.scripting.classlistener;

public interface ClassListener {
   void postLoad(Class<?>[] var1);

   void preUnload(Class<?>[] var1);
}
