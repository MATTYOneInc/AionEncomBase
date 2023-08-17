/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.eventEngine;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wanke on 12/02/2017.
 */

public class EventQueue<E extends DelayedEvent> extends AbstractQueue<E> implements BlockingQueue<E>
{
    private transient final ReentrantLock lock = new ReentrantLock();
    private transient final Condition available = lock.newCondition();
    private final PriorityQueue<E> q = new PriorityQueue<E>();
	
    public EventQueue() {
    }
	
    public EventQueue(Collection<? extends E> c) {
        this.addAll(c);
    }
	
    public boolean add(E e) {
        return offer(e);
    }
	
    public boolean offer(E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            E first = q.peek();
            q.offer(e);
            if (first == null || e.compareTo(first) < 0) {
                available.signalAll();
            }
            return true;
        } finally {
            lock.unlock();
        }
    }
	
    public void put(E e) {
        offer(e);
    }
	
    public boolean offer(E e, long timeout, TimeUnit unit) {
        return offer(e);
    }
	
    public E poll() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            E first = q.peek();
            if (first == null || first.getDelay(TimeUnit.NANOSECONDS) > 0) {
                return null;
            } else {
                E x = q.poll();
                assert x != null;
                if (q.size() != 0) {
                    available.signalAll();
                }
                return x;
            }
        } finally {
            lock.unlock();
        }
    }
	
    public E take() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            for (;;) {
                E first = q.peek();
                if (first == null) {
                    available.await();
                } else {
                    long delay = first.getDelay(TimeUnit.NANOSECONDS);
                    if (delay > 0) {
                        @SuppressWarnings("unused")
                        long tl = available.awaitNanos(delay);
                    } else {
                        E x = q.poll();
                        assert x != null;
                        if (q.size() != 0) {
                            available.signalAll();
                        }
                        return x;
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }
	
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            for (;;) {
                E first = q.peek();
                if (first == null) {
                    if (nanos <= 0) {
                        return null;
                    } else {
                        nanos = available.awaitNanos(nanos);
                    }
                } else {
                    long delay = first.getDelay(TimeUnit.NANOSECONDS);
                    if (delay > 0) {
                        if (nanos <= 0) {
                            return null;
                        }
                        if (delay > nanos) {
                            delay = nanos;
                        }
                        long timeLeft = available.awaitNanos(delay);
                        nanos -= delay - timeLeft;
                    } else {
                        E x = q.poll();
                        assert x != null;
                        if (q.size() != 0) {
                            available.signalAll();
                        }
                        return x;
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }
	
    public E peek() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return q.peek();
        } finally {
            lock.unlock();
        }
    }
	
    public int size() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return q.size();
        } finally {
            lock.unlock();
        }
    }
	
    public int drainTo(Collection<? super E> c) {
        if (c == null) {
            throw new NullPointerException();
        } if (c == this) {
            throw new IllegalArgumentException();
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            int n = 0;
            for (;;) {
                E first = q.peek();
                if (first == null || first.getDelay(TimeUnit.NANOSECONDS) > 0) {
                    break;
                }
                c.add(q.poll());
                ++n;
            }
            if (n > 0) {
                available.signalAll();
            }
            return n;
        } finally {
            lock.unlock();
        }
    }
	
    public int drainTo(Collection<? super E> c, int maxElements) {
        if (c == null) {
            throw new NullPointerException();
        } if (c == this) {
            throw new IllegalArgumentException();
        } if (maxElements <= 0) {
            return 0;
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            int n = 0;
            while (n < maxElements) {
                E first = q.peek();
                if (first == null || first.getDelay(TimeUnit.NANOSECONDS) > 0) {
                    break;
                }
                c.add(q.poll());
                ++n;
            } if (n > 0) {
                available.signalAll();
            }
            return n;
        } finally {
            lock.unlock();
        }
    }
	
    public void clear() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            q.clear();
        } finally {
            lock.unlock();
        }
    }
	
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }
	
    public Object[] toArray() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return q.toArray();
        } finally {
            lock.unlock();
        }
    }
	
    public <T> T[] toArray(T[] a) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return q.toArray(a);
        } finally {
            lock.unlock();
        }
    }
	
    public boolean remove(Object o) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return q.remove(o);
        } finally {
            lock.unlock();
        }
    }
	
    public Iterator<E> iterator() {
        return new Itr(toArray());
    }
	
    private class Itr implements Iterator<E> {
        final Object[] array;
        int cursor;
        int lastRet;
        Itr(Object[] array) {
            lastRet = -1;
            this.array = array;
        }
        public boolean hasNext() {
            return cursor < array.length;
        }
        @SuppressWarnings("unchecked")
        public E next() {
            if (cursor >= array.length) {
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            return (E) array[cursor++];
        }
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            Object x = array[lastRet];
            lastRet = -1;
            lock.lock();
            try {
                for (@SuppressWarnings("rawtypes") Iterator it = q.iterator(); it.hasNext();) {
                    if (it.next() == x) {
                        it.remove();
                        return;
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }
}