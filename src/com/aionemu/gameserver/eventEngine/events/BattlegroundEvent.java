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
package com.aionemu.gameserver.eventEngine.events;

import com.aionemu.gameserver.eventEngine.Event;
import com.aionemu.gameserver.services.events.LadderService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanke on 12/02/2017.
 */

public class BattlegroundEvent extends Event
{
    private List<Integer> battlegrounds = new ArrayList<Integer>();
	
    @Override
    public void execute() {
        LadderService.getInstance().createNormalBgs(this);
    }
	
    public int getBgCount() {
        return battlegrounds.size();
    }
	
    public void onCreate(Integer bgId) {
        if (!battlegrounds.contains(bgId)) {
            battlegrounds.add(bgId);
        }
    }
	
    public void onEnd(Integer bgId) {
        battlegrounds.remove(bgId);
        if (battlegrounds.isEmpty()) {
            this.onEnd();
        }
    }
	
    public void onEnd() {
        super.finish();
    }
	
    @Override
    protected void onReset() {
        battlegrounds.clear();
    }
	
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }
}