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
package com.aionemu.gameserver.services.protectors;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatRateFunction;
import com.aionemu.gameserver.model.templates.serial_guard.GuardRankPenaltyAttr;
import com.aionemu.gameserver.model.templates.serial_guard.GuardRankRestriction;
import com.aionemu.gameserver.model.templates.serial_guard.GuardTypePenaltyAttr;
import com.aionemu.gameserver.model.templates.serial_guard.GuardTypeRestriction;
import com.aionemu.gameserver.skillengine.change.Func;

import java.util.ArrayList;
import java.util.List;

public class ProtectorBuffs implements StatOwner
{
	private GuardRankRestriction guardRankRestriction;
	private GuardTypeRestriction guardTypeRestriction;
	private List<IStatFunction> functions = new ArrayList<IStatFunction>();
	
	public void applyRankEffect(Player player, int rank) {
		if (rank == 0) {
			return;
		}
		guardRankRestriction = DataManager.SERIAL_GUARD_DATA.getGuardRankRestriction(rank);
		if (hasDebuff()) {
			endEffect(player);
		} for (GuardRankPenaltyAttr guardrankPenaltyAttr : guardRankRestriction.getGuardPenaltyAttr()) {
			if (guardrankPenaltyAttr.getFunc().equals(Func.PERCENT)) {
				functions.add(new StatRateFunction(guardrankPenaltyAttr.getStat(), guardrankPenaltyAttr.getValue(), true));
			} else {
				functions.add(new StatAddFunction(guardrankPenaltyAttr.getStat(), guardrankPenaltyAttr.getValue(), true));
			}
		}
		player.getGameStats().addEffect(this, functions);
	}
	
	public void applyTypeEffect(Player player, int type) {
		if (type == 0) {
			return;
		}
		guardTypeRestriction = DataManager.SERIAL_GUARD_DATA.getGuardTypeRestriction(type);
		if (hasDebuff()) {
			endEffect(player);
		} for (GuardTypePenaltyAttr guardtypePenaltyAttr : guardTypeRestriction.getGuardPenaltyAttr()) {
			if (guardtypePenaltyAttr.getFunc().equals(Func.PERCENT)) {
				functions.add(new StatRateFunction(guardtypePenaltyAttr.getStat(), guardtypePenaltyAttr.getValue(), true));
			} else {
				functions.add(new StatAddFunction(guardtypePenaltyAttr.getStat(), guardtypePenaltyAttr.getValue(), true));
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