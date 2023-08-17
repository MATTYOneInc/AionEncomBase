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
package com.aionemu.gameserver.services.conquerors;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatRateFunction;
import com.aionemu.gameserver.model.templates.serial_killer.RankPenaltyAttr;
import com.aionemu.gameserver.model.templates.serial_killer.RankRestriction;
import com.aionemu.gameserver.skillengine.change.Func;

import java.util.ArrayList;
import java.util.List;

public class ConquerorBuffs implements StatOwner
{
	private List<IStatFunction> functions = new ArrayList<IStatFunction>();
	private RankRestriction rankRestriction;
	
	public void applyEffect(Player player, int rank) {
		if (rank == 0) {
			return;
		}
		rankRestriction = DataManager.SERIAL_KILLER_DATA.getRankRestriction(rank);
		if (hasDebuff()) {
			endEffect(player);
		} for (RankPenaltyAttr rankPenaltyAttr : rankRestriction.getPenaltyAttr()) {
			if (rankPenaltyAttr.getFunc().equals(Func.PERCENT)) {
				functions.add(new StatRateFunction(rankPenaltyAttr.getStat(), rankPenaltyAttr.getValue(), true));
			} else {
				functions.add(new StatAddFunction(rankPenaltyAttr.getStat(), rankPenaltyAttr.getValue(), true));
			}
		}
		player.getGameStats().addEffect(this, functions);
	}
	
	public boolean hasDebuff() {
		return !functions.isEmpty();
	}
	
	public void endEffect(Player player) {
		functions.clear();
		player.getGameStats().endEffect(this);
	}
}