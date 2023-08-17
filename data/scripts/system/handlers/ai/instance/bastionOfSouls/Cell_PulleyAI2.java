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
package ai.instance.bastionOfSouls;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

import java.util.List;

/****/
/** Author (Encom)
/****/

@AIName("Prison_Lever")
public class Cell_PulleyAI2 extends NpcAI2
{
	@Override
    protected void handleDialogStart(Player player) {
        if (player.getInventory().getFirstItemByItemId(185000311) != null) { //Black Oil.
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
        } else {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
			//You need Black Oil to move the lever to the Room of Rehabilitation.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404204));
        }
    }
	
	@Override
    public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
        if (dialogId == 10000 && player.getInventory().decreaseByItemId(185000311, 1)) {
            switch (getNpcId()) {
                case 207173: //IDAb1_Ere_Prison_Lever_01.
                    switch (player.getRace()) {
						case ELYOS:
						    despawnNpc(207181);
						    spawn(207186, 1190.6779f, 642.34625f, 426.32108f, (byte) 0); //IDAb1_Ere_SkyPrisonM_L_04.
						break;
						case ASMODIANS:
						    despawnNpc(207192);
							spawn(207197, 1190.6779f, 642.34625f, 426.32108f, (byte) 0); //IDAb1_Ere_SkyPrisonM_D_04.
						break;
					}
                break;
                case 207174: //IDAb1_Ere_Prison_Lever_02.
                    switch (player.getRace()) {
						case ELYOS:
						    despawnNpc(207182);
							spawn(207187, 1184.7258f, 599.4272f, 427.2402f, (byte) 61); //IDAb1_Ere_SkyPrisonM_L_05.
						break;
						case ASMODIANS:
						    despawnNpc(207193);
							spawn(207198, 1184.7258f, 599.4272f, 427.2402f, (byte) 61); //IDAb1_Ere_SkyPrisonM_D_05.
						break;
					}
                break;
				case 207175: //IDAb1_Ere_Prison_Lever_03.
                    switch (player.getRace()) {
						case ELYOS:
						    despawnNpc(207180);
					        spawn(207185, 1163.5547f, 616.7791f, 426.54364f, (byte) 0); //IDAb1_Ere_SkyPrisonM_L_03.
						break;
						case ASMODIANS:
						    despawnNpc(207191);
							spawn(207196, 1163.5547f, 616.7791f, 426.54364f, (byte) 0); //IDAb1_Ere_SkyPrisonM_D_03.
						break;
					}
                break;
                case 207176: //IDAb1_Ere_Prison_Lever_04.
                    switch (player.getRace()) {
						case ELYOS:
						    despawnNpc(207179);
					        spawn(207184, 1156.1328f, 634.11304f, 424.51465f, (byte) 21); //IDAb1_Ere_SkyPrisonM_L_02.
						break;
						case ASMODIANS:
						    despawnNpc(207190);
							spawn(207195, 1156.1328f, 634.11304f, 424.51465f, (byte) 21); //IDAb1_Ere_SkyPrisonM_D_02.
						break;
					}
                break;
				case 207177: //IDAb1_Ere_Prison_Lever_05.
                    switch (player.getRace()) {
						case ELYOS:
						    despawnNpc(207178);
					        spawn(207183, 1213.492f, 557.3433f, 430.61932f, (byte) 16); //IDAb1_Ere_SkyPrisonM_L_01.
						break;
						case ASMODIANS:
						    despawnNpc(207189);
							spawn(207194, 1213.492f, 557.3433f, 430.61932f, (byte) 16); //IDAb1_Ere_SkyPrisonM_D_01.
						break;
					}
                break;
            }
        }
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
        return true;
    }
	
	private void despawnNpc(int npcId) {
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null) {
			List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc: npcs) {
				npc.getController().onDelete();
			}
		}
	}
}