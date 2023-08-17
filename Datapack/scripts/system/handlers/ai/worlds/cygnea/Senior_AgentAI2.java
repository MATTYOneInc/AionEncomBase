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
package ai.worlds.cygnea;

import ai.GeneralNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("senior_agent")
public class Senior_AgentAI2 extends GeneralNpcAI2
{
	@Override
	protected void handleDialogStart(Player player) {
        switch (getNpcId()) {
			case 805327: //Rike [Cygnea]
			case 805692: //Rike [Cloister Of Kaisinel]
			case 805339: //Skuldun [Enshar]
			case 805693: { //Skuldun [Convent Of Marchutan]
				super.handleDialogStart(player);
				break;
			} default: {
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
				break;
			}
		}
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogId);
		env.setExtendedRewardIndex(extendedRewardIndex);
		if (QuestEngine.getInstance().onDialog(env) && dialogId != 1011) {
			return true;
		} if (dialogId == 104) {
		    switch (getNpcId()) {
			    case 805692: //Rike [Cloister Of Kaisinel]
				    TeleportService2.teleportTo(player, 210070000, 2963.83f, 890.73566f, 573.6276f, (byte) 114);
				break;
				case 805327: //Rike [Cygnea]
				    TeleportService2.teleportTo(player, 110020000, 519.60547f, 499.9686f, 499.59976f, (byte) 61);
				break;
				case 805693: //Skuldun [Convent Of Marchutan]
				    TeleportService2.teleportTo(player, 220080000, 449.13324f, 2240.4626f, 220.04858f, (byte) 26);
				break;
				case 805339: //Skuldun [Enshar]
				    TeleportService2.teleportTo(player, 120020000, 499.4822f, 480.57376f, 499.59976f, (byte) 26);
				break;
			}
		} else if (dialogId == 1011 && questId != 0) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), dialogId, questId));
		}
		return true;
	}
}