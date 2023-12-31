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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Dr.Nism
 */
public class SM_TARGET_SELECTED extends AionServerPacket {

	private int level;
	private int maxHp;
	private int currentHp;
	private int maxMp;
	private int currentMp;
	private int targetObjId;

	public SM_TARGET_SELECTED(Player player) {
		if (player != null) {
			if (player.getTarget() instanceof Player) {
				Player pl = (Player) player.getTarget();
				this.level = pl.getLevel();
				this.maxHp = pl.getLifeStats().getMaxHp();
				this.currentHp = pl.getLifeStats().getCurrentHp();
				this.maxMp = pl.getLifeStats().getMaxMp();
				this.currentMp = pl.getLifeStats().getCurrentMp();
			} else if (player.getTarget() instanceof Creature) {
				Creature creature = (Creature) player.getTarget();
				this.level = creature.getLevel();
				this.maxHp = creature.getLifeStats().getMaxHp();
				this.currentHp = creature.getLifeStats().getCurrentHp();
				this.maxMp = creature.getLifeStats().getMaxMp();
				this.currentMp = creature.getLifeStats().getCurrentMp();
			} else {
				// TODO: check various gather on retail
				this.level = 0;
				this.maxHp = 0;
				this.currentHp = 0;
				this.maxMp = 0;
				this.currentMp = 0;
			}

			if (player.getTarget() != null) {
				targetObjId = player.getTarget().getObjectId();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(targetObjId);
		writeH(level);
		writeD(maxHp);
		writeD(currentHp);
		writeD(maxMp);
		writeD(currentMp);
	}
}