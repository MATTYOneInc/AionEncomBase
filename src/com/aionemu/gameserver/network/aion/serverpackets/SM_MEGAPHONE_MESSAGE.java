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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author (Encom)
 */
public class SM_MEGAPHONE_MESSAGE extends AionServerPacket
{
	private Player player;
	private String message;
	private int itemId;
	private boolean isAll;
	
	public SM_MEGAPHONE_MESSAGE(Player player, String message, int itemId, boolean isAll) {
		this.player = player;
		this.message = message;
		this.itemId = itemId;
		this.isAll = isAll;
	}
	
	@Override
	protected void writeImpl(AionConnection client) {
		writeS(player.getName());
		writeS(message);
		writeD(itemId);
		writeC(this.isAll ? this.player.getRace().getRaceId() : 255);
	}
}