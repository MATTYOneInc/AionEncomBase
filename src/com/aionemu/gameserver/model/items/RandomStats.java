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
package com.aionemu.gameserver.model.items;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.bonuses.StatBonusType;

/**
 * @author Ranastic
 */

public class RandomStats {
	private final RandomBonusEffect rndBonusEffect;

	public RandomStats(int setId, int setNumber) {
		rndBonusEffect = new RandomBonusEffect(StatBonusType.INVENTORY, setId, setNumber);
	}

	public void onEquip(final Player player) {
		rndBonusEffect.applyEffect(player);
	}

	public void onUnEquip(Player player) {
		rndBonusEffect.endEffect(player);
	}
}