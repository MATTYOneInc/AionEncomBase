/*
 * Decompiled with CFR 0.150.
 */
package com.eleanor.utils.threading;

public class ManualResetEvent {
	private final Object monitor = new Object();
	private volatile boolean open = false;

	public ManualResetEvent(boolean open) {
		this.open = open;
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */
	public void waitOne() throws InterruptedException {
		Object object = this.monitor;
		synchronized (object) {
			while (!this.open) {
				this.monitor.wait();
			}
		}
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */
	public void resetAndWaitOne() throws InterruptedException {
		Object object = this.monitor;
		synchronized (object) {
			this.open = false;
			while (!this.open) {
				this.monitor.wait();
			}
		}
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */
	public boolean waitOne(long milliseconds) throws InterruptedException {
		Object object = this.monitor;
		synchronized (object) {
			if (this.open) {
				return true;
			}
			this.monitor.wait(milliseconds);
			return this.open;
		}
	}

	/*
	 * WARNING - Removed try catching itself - possible behaviour change.
	 */
	public void set() {
		Object object = this.monitor;
		synchronized (object) {
			this.open = true;
			this.monitor.notifyAll();
		}
	}

	public void reset() {
		this.open = false;
	}
}
