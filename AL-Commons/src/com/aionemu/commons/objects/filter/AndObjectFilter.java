package com.aionemu.commons.objects.filter;

public class AndObjectFilter<T> implements ObjectFilter<T> {
   private ObjectFilter<? super T>[] filters;

   public AndObjectFilter(ObjectFilter<? super T>... filters) {
      this.filters = filters;
   }

   public boolean acceptObject(T object) {
      ObjectFilter[] arr$ = this.filters;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ObjectFilter<? super T> filter = arr$[i$];
         if (filter != null && !filter.acceptObject(object)) {
            return false;
         }
      }

      return true;
   }
}
