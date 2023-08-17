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
package com.aionemu.gameserver.model.bonus_service;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatRateFunction;
import com.aionemu.gameserver.model.templates.abyss_bonus.AbyssPenaltyAttr;
import com.aionemu.gameserver.model.templates.abyss_bonus.AbyssServiceAttr;
import com.aionemu.gameserver.skillengine.change.Func;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Rinzler (Encom)
 */

public class AbyssServiceBuff implements StatOwner
{
	private static final Logger log = LoggerFactory.getLogger(AbyssServiceBuff.class);
	private List<IStatFunction> functions = new ArrayList<IStatFunction>();
	private AbyssServiceAttr abyssBonusAttr;
	
	public AbyssServiceBuff(int buffId) {
		abyssBonusAttr = DataManager.ABYSS_BUFF_DATA.getInstanceBonusattr(buffId);
	}
	
	public void applyAbyssEffect(Player player, int buffId) {
		if (abyssBonusAttr == null) {
			return;
		} for (AbyssPenaltyAttr abyssPenaltyAttr: abyssBonusAttr.getPenaltyAttr()) {
			if (abyssPenaltyAttr.getFunc().equals(Func.PERCENT)) {
				functions.add(new StatRateFunction(abyssPenaltyAttr.getStat(), abyssPenaltyAttr.getValue(), true));
			} else {
				functions.add(new StatAddFunction(abyssPenaltyAttr.getStat(), abyssPenaltyAttr.getValue(), true));
			}
		}
		player.setAbyssBonus(true);
		player.getGameStats().addEffect(this, functions);
	}
	
	public void endEffect(Player player, int buffId) {
		functions.clear();
		player.setAbyssBonus(false);
		player.getGameStats().endEffect(this);
	}
}