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
package ai.worlds.panesterra;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author (Encom)
/****/

@AIName("enraged_guardian_general")
public class EnragedGuardianGeneralAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
	}
	
	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			case 277410: //Enraged Guardian General.
				killedTheGuardianGeneral();
				ThreadPoolManager.getInstance().schedule(new Runnable() {
			        @Override
			        public void run() {
			            spawnTreasureChest(701481);
			        }
		        }, 10000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
			        @Override
			        public void run() {
					    spawn(802219, 1024.12f, 1078.747f, 1530.2688f, (byte) 90);
			        }
		        }, 480000);
			break;
			case 277425: //Enraged Guardian General.
				killedTheGuardianGeneral();
				ThreadPoolManager.getInstance().schedule(new Runnable() {
			        @Override
			        public void run() {
			            spawnTreasureChest(701481);
			        }
		        }, 10000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
			        @Override
			        public void run() {
					    spawn(802221, 1024.12f, 1078.747f, 1530.2688f, (byte) 90);
			        }
		        }, 480000);
			break;
			case 277440: //Enraged Guardian General.
				killedTheGuardianGeneral();
				ThreadPoolManager.getInstance().schedule(new Runnable() {
			        @Override
			        public void run() {
			            spawnTreasureChest(701481);
			        }
		        }, 10000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
			        @Override
			        public void run() {
					    spawn(802223, 1024.12f, 1078.747f, 1530.2688f, (byte) 90);
			        }
		        }, 480000);
			break;
			case 277455: //Enraged Guardian General.
				killedTheGuardianGeneral();
				ThreadPoolManager.getInstance().schedule(new Runnable() {
			        @Override
			        public void run() {
			            spawnTreasureChest(701481);
			        }
		        }, 10000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
			        @Override
			        public void run() {
					    spawn(802225, 1024.12f, 1078.747f, 1530.2688f, (byte) 90);
			        }
		        }, 480000);
			break;
		}
		super.handleDied();
	}
	
	private void spawnTreasureChest(int npcId) {
		rndSpawnInRange(npcId, Rnd.get(1, 4));
		rndSpawnInRange(npcId, Rnd.get(1, 4));
		rndSpawnInRange(npcId, Rnd.get(1, 4));
		rndSpawnInRange(npcId, Rnd.get(1, 4));
		rndSpawnInRange(npcId, Rnd.get(1, 4));
		rndSpawnInRange(npcId, Rnd.get(1, 4));
	}
	
	private Npc rndSpawnInRange(int npcId, float distance) {
		float direction = Rnd.get(0, 199) / 100f;
		float x1 = (float) (Math.cos(Math.PI * direction) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction) * distance);
		WorldPosition p = getPosition();
		return (Npc) spawn(npcId, p.getX() + x1, p.getY() + y1, p.getZ(), (byte) 0);
	}
	
	private void killedTheGuardianGeneral() {
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//Loading the Advance Corridor Shield... Please wait.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_01, 0);
				//The entrance to the Transidium Annex will open in 8 minutes.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_02, 10000);
				//The entrance to the Transidium Annex will open in 6 minutes.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_03, 120000);
				//The entrance to the Transidium Annex will open in 4 minutes.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_04, 240000);
				//The entrance to the Transidium Annex will open in 2 minutes.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_05, 360000);
				//The entrance to the Transidium Annex will open in 1 minute.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_06, 420000);
				//The entrance to the Transidium Annex has opened.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_08, 480000);
			}
		});
    }
}