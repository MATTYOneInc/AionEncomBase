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

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanke on 12/02/2017.
 */

class DelayedEvent extends Event implements Comparable<DelayedEvent>
{
    private final Date forecast;
    private final Event event;
	
    public DelayedEvent(Event event, int delay) {
        this.forecast = new Date(System.currentTimeMillis() + delay);
        this.event = event;
    }
	
    @Override
    public int compareTo(DelayedEvent o) {
        int delay = (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        if (delay > (0 - Event.MAX_PRIORITY) * 60 * 1000 && delay < MAX_PRIORITY * 60 * 1000) {
            delay = (int) ((getDelay(TimeUnit.MILLISECONDS) - getEvent().getPriority() * 60 * 1000) - (o.getDelay(TimeUnit.MILLISECONDS) - o.getEvent().getPriority() * 60 * 1000));
        }
        return delay;
    }
	
    public long getDelay(TimeUnit unit) {
        return unit.convert(forecast.compareTo(new Date()), TimeUnit.MILLISECONDS);
    }
	
    public Event getEvent() {
        return event;
    }

    @Override
    public void execute() {
        getEvent().execute();
    }
	
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return event.cancel(mayInterruptIfRunning);
    }
	
    @Override
    public boolean isFinished() {
        return event.isFinished();
    }
	
    @Override
    public int getCooldown() {
        return event.getCooldown();
    }
	
    @Override
    public int getPriority() {
        return event.getPriority();
    }
	
    @Override
    public void setPriority(int priority) {
        event.setPriority(priority);
    }
	
    @Override
    protected void onReset() {
        event.onReset();
    }
}