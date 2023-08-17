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
package ai.worlds.reshanta.worldBoss.kysis;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author (Encom)
/****/

@AIName("unsealed_kysis")
public class Kysis_OverlordAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
	}
	
	@Override
    protected void handleSpawned() {
        super.handleSpawned();
		//bossShield();
		announceUnsealedKysis();
    }
	
	private void bossShield() {
   		SkillEngine.getInstance().getSkill(getOwner(), 18296, 60, getOwner()).useNoAnimationSkill(); //Boss Shield.
 	}
	
	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			//Kysis Fortress 5.3
			case 279345:
			case 279639:
			case 269011:
				treasureChest();
				ThreadPoolManager.getInstance().schedule(new Runnable() {
			        @Override
			        public void run() {
						spawnTreasureChest(701481);
			        }
		        }, 10000);
			break;
		}
		super.handleDied();
	}
	
	private void treasureChest() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//A treasure chest has appeared.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDAbRe_Core_NmdC_BoxSpawn);
			}
		});
	}
	
	private void announceUnsealedKysis() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//Unsealed Kysis.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Ab1_Dkisas_Named_Spawn);
				//The Kysis Balaur Prince has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1231_Boss_hide_Dispel, 20000);
			}
		});
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
}