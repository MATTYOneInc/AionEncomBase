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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.teleport.TeleporterTemplate;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.MathUtil;

import org.slf4j.LoggerFactory;

/**
 * 0000: CB 06 00 00 04 00 00 00 00 00
 */
public class CM_TELEPORT_SELECT extends AionClientPacket
{
	public int targetObjectId;
	public int locId;
	
	public CM_TELEPORT_SELECT(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		targetObjectId = readD();
		locId = readD();
	}
	
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player.getLifeStats().isAlreadyDead()) {
			return;
		}
		AionObject obj = player.getKnownList().getObject(targetObjectId);
		if (obj != null && obj instanceof Npc) {
			Npc npc = (Npc)obj;
			int npcId = npc.getNpcId();
			if (!MathUtil.isInRange(npc, player, npc.getObjectTemplate().getTalkDistance() + 2)) {
				return;
			}
			TeleporterTemplate teleport = DataManager.TELEPORTER_DATA.getTeleporterTemplateByNpcId(npcId);
			if (teleport != null) {
				TeleportService2.teleport(teleport, locId, player, npc, TeleportAnimation.JUMP_ANIMATION);
			} else {
				LoggerFactory.getLogger(CM_TELEPORT_SELECT.class).warn("teleportation id "+locId+" was not found on npc "+npcId);
			}
		} else {
			LoggerFactory.getLogger(CM_TELEPORT_SELECT.class).debug("player "+player.getName()+" requested npc "+targetObjectId+" for teleportation "+locId+", but he doesnt have such npc in knownlist");
		}
	}
}