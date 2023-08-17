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
package com.aionemu.gameserver.taskmanager;

import com.aionemu.gameserver.utils.ThreadPoolManager;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author NB4L1
 * Going to remove this - Nemesiss
 */
public abstract class FIFOExecutableQueue implements Runnable {

	private static final byte NONE = 0;
	private static final byte QUEUED = 1;
	private static final byte RUNNING = 2;

	private final ReentrantLock lock = new ReentrantLock();

	private volatile byte state = NONE;

	protected final void execute() {
		lock();
		try {
			if (state != NONE) {
				return;
            }
			state = QUEUED;
		}
		finally {
			unlock();
		}
		ThreadPoolManager.getInstance().execute(this);
	}

	public final void lock() {
		lock.lock();
	}

	public final void unlock() {
		lock.unlock();
	}

	public final void run() {
		try {
			while (!isEmpty()) {
				setState(QUEUED, RUNNING);

				try {
					while (!isEmpty()) {
						removeAndExecuteFirst();
					}
				}
				finally {
					setState(RUNNING, QUEUED);
				}
			}
		}
		finally {
			setState(QUEUED, NONE);
		}
	}

	private void setState(byte expected, byte value) {
		lock();
		try {
			if (state != expected) {
				throw new IllegalStateException("state: " + state + ", expected: " + expected);
			}
		}
		finally {
			state = value;

			unlock();
		}
	}

	protected abstract boolean isEmpty();

	protected abstract void removeAndExecuteFirst();
}