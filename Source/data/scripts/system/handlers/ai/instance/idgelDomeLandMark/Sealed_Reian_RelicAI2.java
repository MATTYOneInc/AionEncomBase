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
package ai.instance.idgelDomeLandMark;

import ai.ActionItemNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

import java.util.List;

/****/
/** Author (Encom)
/****/

@AIName("Sealed_Reian_Relic")
public class Sealed_Reian_RelicAI2 extends ActionItemNpcAI2
{
    private boolean isRewarded;
	
    @Override
    protected void handleDialogStart(Player player) {
        InstanceReward<?> instance = getPosition().getWorldMapInstance().getInstanceHandler().getInstanceReward();
        if (instance != null && !instance.isStartProgress()) {
            return;
        }
        super.handleDialogStart(player);
    }
	
    @Override
    protected void handleUseItemFinish(Player player) {
        if (!isRewarded) {
            isRewarded = true;
			AI2Actions.handleUseItemFinish(this, player);
			switch (getNpcId()) {
			    case 833898: //Sealed Reian Relic.
				    switch (player.getRace()) {
						case ELYOS:
						    IDFortressWarElyos();
							//The Elyos removed the last stage of the device.
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403432));
						break;
						case ASMODIANS:
						    IDFortressWarAsmodians();
							//The Asmodians removed the last stage of the device.
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403439));
					    break;
					}
					ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							spawn(833898, 264.65891f, 259.27396f, 88.502739f, (byte) 0, 60); //Sealed Reian Relic.
						}
					}, 30000);
				break;
			}
        }
    }
	
	private void IDFortressWarElyos() {
		despawnNpc(806277); //IDFortressWar_v01_Flag_D.
		despawnNpc(806278); //IDFortressWar_v01_Flag_Dr.
		despawnNpc(806280); //IDLDF5_Fortress_War_Step_D01.
		despawnNpc(806326); //IDFortressWar_Main_Cycle_D01.
		despawnNpc(806359); //IDFortressWar_Sub_Cycle_D01.
		despawnNpc(806360); //IDFortressWar_Sub_Cycle_D02.
		despawnNpc(806361); //IDFortressWar_Sub_Cycle_D03.
		despawnNpc(806362); //IDFortressWar_Sub_Cycle_D04.
		despawnNpc(806375); //IDFortressWar_Sub_Jewel_D01.
		despawnNpc(806376); //IDFortressWar_Sub_Jewel_D02.
		despawnNpc(806377); //IDFortressWar_Sub_Jewel_D03.
		despawnNpc(806378); //IDFortressWar_Sub_Jewel_D04.
	    spawn(806276, 264.65891f, 259.27396f, 88.502739f, (byte) 0); //IDFortressWar_v01_Flag_L.
	    spawn(806325, 264.65494f, 259.27081f, 88.502739f, (byte) 0, 147); //IDFortressWar_Main_Cycle_L01.
		ThreadPoolManager.getInstance().schedule(new Runnable() {
		    @Override
			public void run() {
				spawn(806343, 264.20355f, 273.34286f, 85.728119f, (byte) 0, 191); //IDFortressWar_Sub_Jewel_L01.
			}
		}, 5000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
		    @Override
			public void run() {
				spawn(806344, 265.11926f, 245.07037f, 85.728119f, (byte) 0, 158); //IDFortressWar_Sub_Jewel_L02.
			}
		}, 10000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
		    @Override
			public void run() {
				spawn(806345, 250.49094f, 258.72949f, 85.728119f, (byte) 0, 163); //IDFortressWar_Sub_Jewel_L03.
			}
		}, 15000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
		    @Override
			public void run() {
				spawn(806346, 278.78870f, 259.61780f, 85.728119f, (byte) 0, 155); //IDFortressWar_Sub_Jewel_L04.
			}
		}, 20000);
	}
	
	private void IDFortressWarAsmodians() {
		despawnNpc(806276); //IDFortressWar_v01_Flag_L.
		despawnNpc(806278); //IDFortressWar_v01_Flag_Dr.
		despawnNpc(806279); //IDLDF5_Fortress_War_Step_L01.
		despawnNpc(806325); //IDFortressWar_Main_Cycle_L01.
		despawnNpc(806327); //IDFortressWar_Sub_Cycle_L01.
		despawnNpc(806328); //IDFortressWar_Sub_Cycle_L02.
		despawnNpc(806329); //IDFortressWar_Sub_Cycle_L03.
		despawnNpc(806330); //IDFortressWar_Sub_Cycle_L04.
		despawnNpc(806343); //IDFortressWar_Sub_Jewel_L01.
		despawnNpc(806344); //IDFortressWar_Sub_Jewel_L02.
		despawnNpc(806345); //IDFortressWar_Sub_Jewel_L03.
		despawnNpc(806346); //IDFortressWar_Sub_Jewel_L04.
		spawn(806277, 264.65891f, 259.27396f, 88.502739f, (byte) 0); //IDFortressWar_v01_Flag_D.
		spawn(806326, 264.65494f, 259.27081f, 88.502739f, (byte) 0, 115); //IDFortressWar_Main_Cycle_D01.
		ThreadPoolManager.getInstance().schedule(new Runnable() {
		    @Override
			public void run() {
				spawn(806375, 264.20355f, 273.34286f, 85.728119f, (byte) 0, 190); //IDFortressWar_Sub_Jewel_D01.
			}
		}, 5000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
		    @Override
			public void run() {
				spawn(806376, 265.11926f, 245.07037f, 85.728119f, (byte) 0, 157); //IDFortressWar_Sub_Jewel_D02.
			}
		}, 10000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
		    @Override
			public void run() {
				spawn(806377, 250.49094f, 258.72949f, 85.728119f, (byte) 0, 159); //IDFortressWar_Sub_Jewel_D03.
			}
		}, 15000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
		    @Override
			public void run() {
				spawn(806378, 278.78870f, 259.61780f, 85.728119f, (byte) 0, 156); //IDFortressWar_Sub_Jewel_D04.
			}
		}, 20000);
	}
	
	private void despawnNpc(int npcId) {
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null) {
			List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc: npcs) {
				npc.getController().onDelete();
			}
		}
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}