/*
 * This file is part of Encom.
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
package ai.instance.seizedDanuarSanctuary;

import ai.ActionItemNpcAI2;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.List;

/****/
/** Author (Encom)
/****/

@AIName("steelrosecannon2")
public class SteelRoseCannonAI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleUseItemFinish(Player player) {
		if (!player.getInventory().decreaseByItemId(186000254, 1)) {
			PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player,
			"You must have <Seal Breaking Magic Cannonball>", ChatType.BRIGHT_YELLOW_CENTER), true);
			return;
		}
		WorldPosition worldPosition = player.getPosition();
		if (worldPosition.isInstanceMap()) {
			//Seized Danuar Sanctuary 4.8
			if (worldPosition.getMapId() == 301140000) {
				//A heavy door has opened somewhere.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDLDF5_Under_02_Canon, 5000);
				SkillEngine.getInstance().getSkill(getOwner(), 21126, 60, getOwner()).useNoAnimationSkill(); //Destroy Seal.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
					public void run() {
					    despawnNpc(233142); //Unyielding Boulder.
					}
				}, 5000);
			}
			//Danuar Sanctuary 4.8
			else if (worldPosition.getMapId() == 301380000) {
				//A heavy door has opened somewhere.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDLDF5_Under_02_Canon, 5000);
				SkillEngine.getInstance().getSkill(getOwner(), 21126, 60, getOwner()).useNoAnimationSkill(); //Destroy Seal.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
					public void run() {
					    despawnNpc(233142); //Unyielding Boulder.
					}
				}, 5000);
			}
		}
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
	
	private void despawnNpc(int npcId) {
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null) {
			List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc: npcs) {
				AI2Actions.killSilently(this, npc);
			}
		}
	}
}