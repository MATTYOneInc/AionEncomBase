package com.aionemu.commons.utils.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorIterator<V> implements Iterator<V> {
   private Iterator<? extends Iterable<V>> firstLevelIterator;
   private Iterator<V> secondLevelIterator;

   public IteratorIterator(Iterable<? extends Iterable<V>> itit) {
      this.firstLevelIterator = itit.iterator();
   }

   public boolean hasNext() {
      if (this.secondLevelIterator != null && this.secondLevelIterator.hasNext()) {
         return true;
      } else {
         while(this.firstLevelIterator.hasNext()) {
            Iterable<V> iterable = (Iterable)this.firstLevelIterator.next();
            if (iterable != null) {
               this.secondLevelIterator = iterable.iterator();
               if (this.secondLevelIterator.hasNext()) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public V next() {
      if (this.secondLevelIterator != null && this.secondLevelIterator.hasNext()) {
         return this.secondLevelIterator.next();
      } else {
         throw new NoSuchElementException();
      }
   }

   public void remove() {
      throw new UnsupportedOperationException("This operation is not supported.");
   }
}
