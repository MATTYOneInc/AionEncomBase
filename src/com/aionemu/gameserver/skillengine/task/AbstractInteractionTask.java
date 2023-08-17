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
package com.aionemu.gameserver.skillengine.task;

import com.aionemu.gameserver.configs.main.CraftConfig;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import java.util.concurrent.Future;

public abstract class AbstractInteractionTask {

    protected Future<?> task;
    protected Player requestor;
    protected VisibleObject responder;
	
    public AbstractInteractionTask(Player requestor, VisibleObject responder) {
        this.requestor = requestor;
        if (responder == null) {
            this.responder = requestor;
        } else {
            this.responder = responder;
        }
    }
	
    protected abstract boolean onInteraction();
    protected abstract void onInteractionFinish();
    protected abstract void onInteractionStart();
    protected abstract void onInteractionAbort();
	
    public void start() {
        onInteractionStart();
        task = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!validateParticipants()) {
                    stop(true);
                }
                boolean stopTask = onInteraction();
                if (stopTask) {
                    stop(false);
                }
            }
        }, 1000, 2500);
    }
	
    public void stop(boolean participantNull) {
        if (!participantNull) {
            onInteractionFinish();
        } if (task != null && !task.isCancelled()) {
            task.cancel(false);
            task = null;
        }
    }
	
    public void abort() {
        onInteractionAbort();
        stop(false);
    }
	
    public boolean isInProgress() {
        return task != null && !task.isCancelled();
    }
	
    public boolean validateParticipants() {
        return requestor != null;
    }
}