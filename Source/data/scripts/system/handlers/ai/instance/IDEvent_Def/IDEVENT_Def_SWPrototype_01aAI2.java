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
package ai.instance.IDEvent_Def;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("IDEVENT_Def_SWPrototype_01a")
public class IDEVENT_Def_SWPrototype_01aAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}
	
	@Override
    public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000 && player.getInventory().decreaseByItemId(186000470, 1)) {
			spawn(836025, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
		} else if (dialogId == 10001 && player.getInventory().decreaseByItemId(186000470, 2)) {
			spawn(836030, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
		} else if (dialogId == 10002 && player.getInventory().decreaseByItemId(186000470, 3)) {
			spawn(836035, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
		} else if (dialogId == 10003 && player.getInventory().decreaseByItemId(186000470, 3)) {
			spawn(836036, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
		} else if (dialogId == 10004 && player.getInventory().decreaseByItemId(186000470, 5)) {
			spawn(836045, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0);
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		AI2Actions.deleteOwner(this);
		return true;
	}
}