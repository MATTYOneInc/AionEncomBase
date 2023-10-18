/*
 * Decompiled with CFR 0.150.
 */
package com.eleanor.processors;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.concurrent.AionRejectedExecutionHandler;
import com.aionemu.commons.utils.concurrent.RunnableWrapper;
import com.aionemu.gameserver.configs.main.ThreadConfig;

public class AGameProcessor {
	protected static final Logger Log = LoggerFactory.getLogger(AGameProcessor.class);
	private ScheduledThreadPoolExecutor _processorPool;

	protected AGameProcessor(int threadsCount) {
		this._processorPool = new ScheduledThreadPoolExecutor(threadsCount);
		this._processorPool.setRejectedExecutionHandler((RejectedExecutionHandler) new AionRejectedExecutionHandler());
		this._processorPool.prestartAllCoreThreads();
	}

	public void execute(Runnable r) {
		this._processorPool.execute(r);
	}

	public ScheduledFuture<?> schedule(Runnable r, long delay) {
		r = new RunnableTaskWrapper((Runnable) r);
		long validated = Math.max(0L, Math.min(Integer.MAX_VALUE, delay));
		if (validated < delay) {
			Log.warn(
					"Determine attempt to post scheduled task with delay {}, but maximal is {}. Delay will be trimmed to maximal",
					(Object) delay, (Object) validated);
		}
		delay = validated;
		return this._processorPool.schedule((Runnable) r, delay, TimeUnit.MILLISECONDS);
	}

	public ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long delay, long period) {
		r = new RunnableTaskWrapper((Runnable) r);
		long validated = Math.max(0L, Math.min(Integer.MAX_VALUE, delay));
		if (validated < delay) {
			Log.warn(
					"Determine attempt to post scheduled task with delay {}, but maximal is {}. Delay will be trimmed to maximal",
					(Object) delay, (Object) validated);
		}
		delay = validated;
		return this._processorPool.scheduleAtFixedRate((Runnable) r, delay, period, TimeUnit.MILLISECONDS);
	}

	public boolean schedule(Runnable r, long delay, Task out) {
		r = new RunnableTaskWrapper((Runnable) r);
		long validated = Math.max(0L, Math.min(Integer.MAX_VALUE, delay));
		if (validated < delay) {
			Log.warn(
					"Determine attempt to post scheduled task with delay {}, but maximal is {}. Action will not be triggered",
					(Object) delay, (Object) validated);
			return false;
		}
		delay = validated;
		out.setTask(this._processorPool.schedule((Runnable) r, delay, TimeUnit.MILLISECONDS));
		return true;
	}

	public static class Task {
		private ScheduledFuture<?> _task;

		public static Task create() {
			return new Task();
		}

		public ScheduledFuture<?> getTask() {
			return this._task;
		}

		private void setTask(ScheduledFuture<?> task) {
			this._task = task;
		}
	}

	private static final class RunnableTaskWrapper extends RunnableWrapper {
		private RunnableTaskWrapper(Runnable runnable) {
			super(runnable, ThreadConfig.MAXIMUM_RUNTIME_IN_MILLISEC_WITHOUT_WARNING);
		}
	}
}
