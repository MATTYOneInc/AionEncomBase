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

import com.aionemu.gameserver.model.atreian_bestiary.PlayerABEntry;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ranastic
 */

public class SM_ATREIAN_BESTIARY_LIST extends AionServerPacket {
	PlayerABEntry[] allAB;
	@SuppressWarnings("unused")
	private Player player;

	public SM_ATREIAN_BESTIARY_LIST(Player player) {
		this.player = player;
		this.allAB = player.getAtreianBestiary().getAllAB();
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(allAB.length);
		for (PlayerABEntry entry : allAB) {
			writeD(entry.getId()); // id
			writeD(entry.getKillCount()); // current kill
			writeC(entry.claimRewardLevel()); // claim Reward
			writeC(entry.getLevel()); // current level
		}
	}
}