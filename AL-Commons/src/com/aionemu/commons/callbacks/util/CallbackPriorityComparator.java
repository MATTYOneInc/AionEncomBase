package com.aionemu.commons.callbacks.util;

import com.aionemu.commons.callbacks.Callback;
import java.util.Comparator;

public class CallbackPriorityComparator implements Comparator<Callback<?>> {
   public int compare(Callback<?> o1, Callback<?> o2) {
      int p1 = CallbacksUtil.getCallbackPriority(o1);
      int p2 = CallbacksUtil.getCallbackPriority(o2);
      if (p1 < p2) {
         return -1;
      } else {
         return p1 == p2 ? 0 : 1;
      }
   }
}
