/*

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

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * Created by wanke on 12/02/2017.
 */

public class EventScheduler implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(EventScheduler.class);
	private static final int TIMEOUT = 15; // in minutes
	private static final int WORKING_DELAY = 1000; // in msec
	private final EventQueue<DelayedEvent> queue = new EventQueue<DelayedEvent>();
	private long startTime = 0;
	private AtomicBoolean paused = new AtomicBoolean(false);

	private EventScheduler() {
		ThreadPoolManager.getInstance().schedule(this, 1);
		log.info("[EventScheduler] is initialized...");
	}

	public void schedule(final Event event, int delay) {
		event.reset();
		queue.offer(new DelayedEvent(event, delay));
	}

	public ScheduledFuture<?> scheduleAtFixedRate(Event event, int delay, int period) {
		ScheduledFuture<?> future = ThreadPoolManager.getInstance().scheduleAtFixedRate(new EventScheduleWrapper(event),
				delay, period);
		return future;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (paused.get()) {
			ThreadPoolManager.getInstance().schedule(this, WORKING_DELAY);
			return;
		}

		Event event = queue.poll();
		if (event == null) {
			ThreadPoolManager.getInstance().schedule(this, WORKING_DELAY);
			return;
		}
		execute(event);
	}

	private void execute(Event event) {
		ThreadPoolManager.getInstance().schedule(event, 0);
		startTime = System.currentTimeMillis();
		ThreadPoolManager.getInstance().schedule(new WaitForExecutionRunnable(event), WORKING_DELAY);
		return;
	}

	private void waitForExecution(Event event) {
		if (event.isFinished()) {
			startTime = 0;
			ThreadPoolManager.getInstance().schedule(this, event.getCooldown());
		} else if ((System.currentTimeMillis() - startTime) > TIMEOUT * 60 * 1000) {
			log.warn("An event of class " + event.getClass().getName() + " have runned over " + TIMEOUT
					+ " minutes.\nPlease check whether finish() is called!");
			startTime = 0;
			ThreadPoolManager.getInstance().schedule(this, event.getCooldown());
		} else {
			ThreadPoolManager.getInstance().schedule(new WaitForExecutionRunnable(event), WORKING_DELAY);
		}
	}

	private class WaitForExecutionRunnable implements Runnable {

		private final Event event;

		public WaitForExecutionRunnable(Event event) {
			this.event = event;
		}

		@Override
		public void run() {
			waitForExecution(event);
		}
	}

	public void pause() {
		paused.set(!paused.get());
	}

	private static class SingletonHolder {

		public static EventScheduler singleton = new EventScheduler();
	}

	static public EventScheduler getInstance() {
		return SingletonHolder.singleton;
	}
}