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
package ai.instance.shugoImperialTomb;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.*;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("Empress_Admirer")
public class Empress_AdmirerAI2 extends NpcAI2
{
	private boolean isInstanceDestroyed;
	
	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 10000) {
			switch (getNpcId()) {
				case 831111: //Empress Admirer.
					startTombWaveB1();
					//Pillagers incoming. Guard the Empress' Monument!
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401583));
					spawn(831130, 340.27893f, 426.2435f, 294.7574f, (byte) 56); //Crown Prince's Monument.
					spawn(831304, 337.75626f, 415.45035f, 294.76086f, (byte) 56); //Empress' Monument.
					spawn(831305, 342.7412f, 436.7674f, 294.75598f, (byte) 56); //Empress' Monument.
				break;
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		AI2Actions.deleteOwner(this);
		return true;
	}
	
	private void attackEvent(final Npc npc, float x, float y, float z, boolean despawn) {
		((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
		npc.setState(1);
		npc.getMoveController().moveToPoint(x, y, z);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}
	
	private void startTombWaveB1() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				attackEvent((Npc)spawn(219514, 313.3913f, 443.98557f, 296.40808f, (byte) 13), 333.79936f, 453.9313f, 296.40808f, false);
				attackEvent((Npc)spawn(219519, 313.3913f, 443.98557f, 296.40808f, (byte) 13), 333.79936f, 453.9313f, 296.40808f, false);
				attackEvent((Npc)spawn(219514, 316.16348f, 431.76547f, 294.58875f, (byte) 78), 335.83118f, 427.14017f, 294.7588f, false);
				attackEvent((Npc)spawn(219519, 316.16348f, 431.76547f, 294.58875f, (byte) 78), 335.83118f, 427.14017f, 294.7588f, false);
				attackEvent((Npc)spawn(219514, 307.97067f, 422.196f, 296.40808f, (byte) 78), 322.31113f, 405.27344f, 296.40808f, false);
				attackEvent((Npc)spawn(219519, 316.16348f, 431.76547f, 294.58875f, (byte) 78), 335.83118f, 427.14017f, 294.7588f, false);
			}
		}, 10000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				attackEvent((Npc)spawn(219515, 313.3913f, 443.98557f, 296.40808f, (byte) 13), 333.79936f, 453.9313f, 296.40808f, false);
				attackEvent((Npc)spawn(219519, 313.3913f, 443.98557f, 296.40808f, (byte) 13), 333.79936f, 453.9313f, 296.40808f, false);
				attackEvent((Npc)spawn(219515, 316.16348f, 431.76547f, 294.58875f, (byte) 78), 335.83118f, 427.14017f, 294.7588f, false);
				attackEvent((Npc)spawn(219519, 316.16348f, 431.76547f, 294.58875f, (byte) 78), 335.83118f, 427.14017f, 294.7588f, false);
				attackEvent((Npc)spawn(219515, 307.97067f, 422.196f, 296.40808f, (byte) 78), 322.31113f, 405.27344f, 296.40808f, false);
				attackEvent((Npc)spawn(219519, 316.16348f, 431.76547f, 294.58875f, (byte) 78), 335.83118f, 427.14017f, 294.7588f, false);
			}
		}, 30000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				attackEvent((Npc)spawn(219514, 313.3913f, 443.98557f, 296.40808f, (byte) 13), 333.79936f, 453.9313f, 296.40808f, false);
				attackEvent((Npc)spawn(219519, 313.3913f, 443.98557f, 296.40808f, (byte) 13), 333.79936f, 453.9313f, 296.40808f, false);
				attackEvent((Npc)spawn(219514, 316.16348f, 431.76547f, 294.58875f, (byte) 78), 335.83118f, 427.14017f, 294.7588f, false);
				attackEvent((Npc)spawn(219519, 316.16348f, 431.76547f, 294.58875f, (byte) 78), 335.83118f, 427.14017f, 294.7588f, false);
				attackEvent((Npc)spawn(219514, 307.97067f, 422.196f, 296.40808f, (byte) 78), 322.31113f, 405.27344f, 296.40808f, false);
				attackEvent((Npc)spawn(219519, 316.16348f, 431.76547f, 294.58875f, (byte) 78), 335.83118f, 427.14017f, 294.7588f, false);
			}
		}, 50000);
	}
	
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}