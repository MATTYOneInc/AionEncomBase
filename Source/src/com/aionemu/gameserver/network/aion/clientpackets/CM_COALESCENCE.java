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
import com.aionemu.gameserver.services.item.CoalescenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ranastic
 */

public class CM_COALESCENCE extends AionClientPacket
{
	private Logger log = LoggerFactory.getLogger(CM_COALESCENCE.class);
	private int mainItemObjId;
	private int materialCount;
	private List<Integer> materialItemObjId;
	private int ItemSize;
	private int upgradedItemObjectId;
	private int Items;
	private List<Integer> ItemsList = new ArrayList();
	
	public CM_COALESCENCE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		materialItemObjId  = new ArrayList<Integer>();
		mainItemObjId = readD();
		materialCount = readH();
		for (int i=0;i<materialCount;i++) {
			materialItemObjId.add(readD());
		}
	}
	
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null || !player.isSpawned()) {
			return;
		} if (player.getController().isInShutdownProgress()) {
			return;
		}
		CoalescenceService.getInstance().letsCoalescence(player, mainItemObjId, materialItemObjId);
	}
}