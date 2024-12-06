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
package ai.event.daevaDay;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("daeva_day_cake")
public class Daeva_Day_CakeAI2 extends NpcAI2
{
	@Override
    protected void handleDialogStart(Player player) {
        if (player.getInventory().getFirstItemByItemId(186000188) != null) { //[Event] Aether Flame.
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
        } else {
            PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player,
			"You must have 1 <Aether Flame>", ChatType.BRIGHT_YELLOW_CENTER), true);
        }
    }
	
	@Override
    public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		PlayerEffectController effectController = player.getEffectController();
		if (dialogId == 10000 && player.getInventory().decreaseByItemId(186000188, 1)) { //[Event] Aether Flame.
			switch (getNpcId()) {
			    case 832180: //Daeva's Day Cake E.
				case 832181: //Daeva's Day Cake A.
				    switch (Rnd.get(1, 3)) {
						case 1:
							SkillEngine.getInstance().applyEffectDirectly(20884, player, player, 14400000 * 1);
							effectController.removeEffect(20885);
							effectController.removeEffect(20886);
						break;
						case 2:
							SkillEngine.getInstance().applyEffectDirectly(20885, player, player, 14400000 * 1);
							effectController.removeEffect(20884);
							effectController.removeEffect(20886);
						break;
						case 3:
							SkillEngine.getInstance().applyEffectDirectly(20886, player, player, 14400000 * 1);
							effectController.removeEffect(20884);
							effectController.removeEffect(20885);
						break;
					}
				break;
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
        return true;
    }
}