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
package ai.instance.archivesOfEternity;

import ai.GeneralNpcAI2;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("cryptograph_cube")
public class Cryptograph_CubeAI2 extends GeneralNpcAI2
{
	@Override
	protected void handleDialogStart(Player player) {
		if (player.isArchDaeva()) {
		    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
		}
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000) {
			switch (getNpcId()) {
				case 806152: //Cryptograph Cube.
				case 806153: //Cryptograph Cube.
				    ItemService.addItem(player, 125004516, 1); //ì„ íƒ?ë°›ì?€ ìˆ˜í˜¸ìž?ì?˜ ë‘?ê±´.
			    break;
			}
		}
		AI2Actions.deleteOwner(this);
		return true;
	}
}