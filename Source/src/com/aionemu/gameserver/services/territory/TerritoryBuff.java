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
package com.aionemu.gameserver.services.territory;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;

import java.util.ArrayList;
import java.util.List;

public class TerritoryBuff implements StatOwner
{
    private List<IStatFunction> functions = new ArrayList<IStatFunction>();
	
    public void applyEffect(Player player) {
        int addvalue = 60;
        if (hasBuff()) {
            endEffect(player);
		}
        functions.add(new StatAddFunction(StatEnum.PVP_DEFEND_RATIO, addvalue, true));
        player.getGameStats().addEffect(this, functions);
    }
	
    public boolean hasBuff() {
        return !functions.isEmpty();
    }
	
    public void endEffect(Player player) {
        functions.clear();
        player.getGameStats().endEffect(this);
    }
}