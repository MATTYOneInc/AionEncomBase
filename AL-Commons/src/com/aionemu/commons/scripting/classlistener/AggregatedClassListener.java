package com.aionemu.commons.scripting.classlistener;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public class AggregatedClassListener implements ClassListener {
   private final List<ClassListener> classListeners;

   public AggregatedClassListener() {
      this.classListeners = Lists.newArrayList();
   }

   public AggregatedClassListener(List<ClassListener> classListeners) {
      this.classListeners = classListeners;
   }

   public List<ClassListener> getClassListeners() {
      return this.classListeners;
   }

   public void addClassListener(ClassListener cl) {
      this.getClassListeners().add(cl);
   }

   public void postLoad(Class<?>[] classes) {
      Iterator i$ = this.getClassListeners().iterator();

      while(i$.hasNext()) {
         ClassListener cl = (ClassListener)i$.next();
         cl.postLoad(classes);
      }

   }

   public void preUnload(Class<?>[] classes) {
      Iterator i$ = Lists.reverse(this.getClassListeners()).iterator();

      while(i$.hasNext()) {
         ClassListener cl = (ClassListener)i$.next();
         cl.preUnload(classes);
      }

   }
}
