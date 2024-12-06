package com.aionemu.commons.taskmanager;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public abstract class AbstractLockManager {
   private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
   private final WriteLock writeLock;
   private final ReadLock readLock;

   public AbstractLockManager() {
      this.writeLock = this.lock.writeLock();
      this.readLock = this.lock.readLock();
   }

   public final void writeLock() {
      this.writeLock.lock();
   }

   public final void writeUnlock() {
      this.writeLock.unlock();
   }

   public final void readLock() {
      this.readLock.lock();
   }

   public final void readUnlock() {
      this.readLock.unlock();
   }
}
