package com.aionemu.commons.network;

import com.aionemu.commons.network.packet.BaseClientPacket;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketProcessor<T extends AConnection> {
   private static final Logger log = LoggerFactory.getLogger(PacketProcessor.class.getName());
   private final int threadSpawnThreshold;
   private final int threadKillThreshold;
   private final Lock lock;
   private final Condition notEmpty;
   private final List<BaseClientPacket<T>> packets;
   private final List<Thread> threads;
   private final int minThreads;
   private final int maxThreads;
   private final Executor executor;

   public PacketProcessor(int minThreads, int maxThreads, int threadSpawnThreshold, int threadKillThreshold) {
      this(minThreads, maxThreads, threadSpawnThreshold, threadKillThreshold, new PacketProcessor.DummyExecutor());
   }

   public PacketProcessor(int minThreads, int maxThreads, int threadSpawnThreshold, int threadKillThreshold, Executor executor) {
      this.lock = new ReentrantLock();
      this.notEmpty = this.lock.newCondition();
      this.packets = new LinkedList();
      this.threads = new ArrayList();
      Preconditions.checkArgument(minThreads > 0, "Min Threads must be positive");
      Preconditions.checkArgument(maxThreads >= minThreads, "Max Threads must be >= Min Threads");
      Preconditions.checkArgument(threadSpawnThreshold > 0, "Thread Spawn Threshold must be positive");
      Preconditions.checkArgument(threadKillThreshold > 0, "Thread Kill Threshold must be positive");
      this.minThreads = minThreads;
      this.maxThreads = maxThreads;
      this.threadSpawnThreshold = threadSpawnThreshold;
      this.threadKillThreshold = threadKillThreshold;
      this.executor = executor;
      if (minThreads != maxThreads) {
         this.startCheckerThread();
      }

      for(int i = 0; i < minThreads; ++i) {
         this.newThread();
      }

   }

   private void startCheckerThread() {
      (new Thread(new PacketProcessor.CheckerTask(), "PacketProcessor:Checker")).start();
   }

   private boolean newThread() {
      if (this.threads.size() >= this.maxThreads) {
         return false;
      } else {
         String name = "PacketProcessor:" + this.threads.size();
         log.debug("Creating new PacketProcessor Thread: " + name);
         Thread t = new Thread(new PacketProcessor.PacketProcessorTask(), name);
         this.threads.add(t);
         t.start();
         return true;
      }
   }

   private void killThread() {
      if (this.threads.size() < this.minThreads) {
         Thread t = (Thread)this.threads.remove(this.threads.size() - 1);
         log.debug("Killing PacketProcessor Thread: " + t.getName());
         t.interrupt();
      }

   }

   public final void executePacket(BaseClientPacket<T> packet) {
      this.lock.lock();

      try {
         this.packets.add(packet);
         this.notEmpty.signal();
      } finally {
         this.lock.unlock();
      }

   }

   private BaseClientPacket<T> getFirstAviable() {
      label19:
      while(true) {
         if (this.packets.isEmpty()) {
            this.notEmpty.awaitUninterruptibly();
         } else {
            ListIterator it = this.packets.listIterator();

            BaseClientPacket packet;
            do {
               if (!it.hasNext()) {
                  this.notEmpty.awaitUninterruptibly();
                  continue label19;
               }

               packet = (BaseClientPacket)it.next();
            } while(!packet.getConnection().tryLockConnection());

            it.remove();
            return packet;
         }
      }
   }

   private final class CheckerTask implements Runnable {
      private static final int sleepTime = 60000;
      private int lastSize;

      private CheckerTask() {
         this.lastSize = 0;
      }

      public void run() {
         try {
            Thread.sleep(60000L);
         } catch (InterruptedException var2) {
         }

         int packetsToExecute = PacketProcessor.this.packets.size();
         if (packetsToExecute < this.lastSize && packetsToExecute < PacketProcessor.this.threadKillThreshold) {
            PacketProcessor.this.killThread();
         } else if (packetsToExecute > this.lastSize && packetsToExecute > PacketProcessor.this.threadSpawnThreshold && !PacketProcessor.this.newThread() && packetsToExecute >= PacketProcessor.this.threadSpawnThreshold * 3) {
            PacketProcessor.log.info("Lagg detected! [" + packetsToExecute + " client packets are waiting for execution]. You should consider increasing PacketProcessor maxThreads or hardware upgrade.");
         }

         this.lastSize = packetsToExecute;
      }

      // $FF: synthetic method
      CheckerTask(Object x1) {
         this();
      }
   }

   private final class PacketProcessorTask implements Runnable {
      private PacketProcessorTask() {
      }

      public void run() {
         BaseClientPacket packet = null;

         while(true) {
            PacketProcessor.this.lock.lock();

            try {
               if (packet != null) {
                  packet.getConnection().unlockConnection();
               }

               if (Thread.interrupted()) {
                  return;
               }

               packet = PacketProcessor.this.getFirstAviable();
            } finally {
               PacketProcessor.this.lock.unlock();
            }

            PacketProcessor.this.executor.execute(packet);
         }
      }

      // $FF: synthetic method
      PacketProcessorTask(Object x1) {
         this();
      }
   }

   private static class DummyExecutor implements Executor {
      private DummyExecutor() {
      }

      public void execute(Runnable command) {
         command.run();
      }

      // $FF: synthetic method
      DummyExecutor(Object x0) {
         this();
      }
   }
}
