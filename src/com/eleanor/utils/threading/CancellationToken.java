/*
 * Decompiled with CFR 0.150.
 */
package com.eleanor.utils.threading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CancellationToken {
	private AtomicBoolean _isCancelled;
	private BlockingQueue<Runnable> _cancelActions = new SynchronousQueue<Runnable>();

	public CancellationToken() {
		this._isCancelled = new AtomicBoolean(false);
	}

	public void cancel() throws InterruptedException {
		if (this._isCancelled.compareAndSet(false, true)) {
			Runnable run = null;
			while ((run = (Runnable) this._cancelActions.poll()) != null) {
				run.run();
			}
		}
	}

	public void addAction(Runnable runnable) throws InterruptedException {
		if (!this._isCancelled.get()) {
			this._cancelActions.put(runnable);
		}
	}

	public boolean isCancelled() {
		return this._isCancelled.get();
	}
}
