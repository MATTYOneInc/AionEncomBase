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

import com.aionemu.gameserver.utils.ThreadPoolManager;

import java.util.concurrent.ScheduledFuture;

/**
 * Created by wanke on 12/02/2017.
 */

class EventScheduleWrapper implements Runnable
{
    private static final int RECHECK_DELAY = 2;
    private final Event event;
    private boolean first = true;
    private ScheduledFuture<?> last_future;
	
    public EventScheduleWrapper(Event event) {
        this.event = event;
    }
	
    @Override
    public void run() {
        if (last_future != null) {
            if (!last_future.isDone()) {
                return;
            }
        } if (!check()) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    check();
                }
            };
            last_future = ThreadPoolManager.getInstance().schedule(runnable, RECHECK_DELAY * 60 * 1000);
        }
    }
	
    private boolean check() {
        if (event.isFinished() || first) {
            first = false;
            EventScheduler.getInstance().schedule(event, 10);
            return true;
        } else {
            event.cancel(true);
            return false;
        }
    }
}