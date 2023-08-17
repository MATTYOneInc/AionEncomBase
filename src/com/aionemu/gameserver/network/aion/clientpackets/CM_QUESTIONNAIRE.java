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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.HTMLService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xTz
 */
public class CM_QUESTIONNAIRE extends AionClientPacket {

	private int objectId;
	private int itemId;
	@SuppressWarnings("unused")
	private String stringItemsId;
	private int itemSize;
	private List<Integer> items;

	public CM_QUESTIONNAIRE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aionemu.commons.network.packet.BaseClientPacket#readImpl()
	 */
	@Override
	protected void readImpl() {
		objectId = readD();
		itemSize = readH();
		items = new ArrayList<Integer>();
		for (int i = 0; i < itemSize; i++) {
			itemId = readD();
			items.add(itemId);
		}
		stringItemsId = readS();
	}

	/*
	 * (non-Javadoc)
	 * @see com.aionemu.commons.network.packet.BaseClientPacket#runImpl()
	 */
	@Override
	protected void runImpl() {
		if (objectId > 0) {
			Player player = getConnection().getActivePlayer();
			HTMLService.getReward(player, objectId, items);
		}
	}
}