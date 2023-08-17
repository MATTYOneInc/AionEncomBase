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
package ai;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.handler.FollowEventHandler;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/** Author (Encom)
/****/

@AIName("deliveryman")
public class DeliveryManAI2 extends FollowingNpcAI2
{
	private Player owner;
	public static int EVENT_SET_CREATOR = 1;
	private static int SERVICE_TIME = 10 * 60 * 1000;
	private static int SPAWN_ACTION_DELAY = 1000;
	
	@Override
	protected void handleSpawned() {
		ThreadPoolManager.getInstance().schedule(new DeleteDeliveryMan(), SERVICE_TIME);
		ThreadPoolManager.getInstance().schedule(new DeliveryManSpawnAction(), SPAWN_ACTION_DELAY);
		super.handleSpawned();
	}
	
	@Override
	protected void handleDespawned() {
		sendMsg(390267, getObjectId(), false, 0);
		super.handleDespawned();
	}
	
	@Override
	protected void handleDialogStart(Player player) {
		if (player.equals(owner)) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 18));
			player.getMailbox().sendMailList(true);
		}
	}
	
	@Override
	protected void handleCreatureMoved(Creature creature) {
		if (creature == owner) {
			FollowEventHandler.creatureMoved(this, creature);
		}
	}
	
	@Override
	protected void handleCustomEvent(int eventId, Object... args) {
		if (eventId == EVENT_SET_CREATOR) {
			owner = (Player) args[0];
		}
	}
	
	private final class DeleteDeliveryMan implements Runnable {
		@Override
		public void run() {
			AI2Actions.deleteOwner(DeliveryManAI2.this);
		}
	}
	
	private final class DeliveryManSpawnAction implements Runnable {
		@Override
		public void run() {
		    sendMsg(390266, getObjectId(), false, 2000);
			sendMsg(390268, getObjectId(), false, 5000);
			handleFollowMe(owner);
			handleCreatureMoved(owner);
		}
	}
	
	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}