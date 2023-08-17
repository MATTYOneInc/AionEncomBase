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
package ai.npcSupport;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("blessed_relic")
public class Blessed_RelicsAI2 extends NpcAI2
{
    @Override
	protected void handleDialogStart(Player player) {
        if (player.getInventory().getFirstItemByItemId(186000344) != null) { //Prestige Crystal.
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
        } else {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
			PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player,
			"You must have 1 <Prestige Crystal>", ChatType.BRIGHT_YELLOW_CENTER), true);
        }
    }
	
	@Override
    public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000 && player.getInventory().decreaseByItemId(186000344, 1)) { //Prestige Crystal.
			switch (getNpcId()) {
			    case 831987: //Lesser Blessed Relics. 
				case 831988: //Minor Blessed Relics.
				case 831989: //Major Blessed Relics.
				case 831990: //Greater Blessed Relics.
					SkillEngine.getInstance().applyEffectDirectly(21650, player, player, 1800000 * 1); //Prestigious Blessing.
				break;
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
        return true;
    }
}