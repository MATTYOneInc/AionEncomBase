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
package ai.rvr.kenovikan;

import ai.GeneralNpcAI2;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("Armored_Transport_Delta")
public class Armored_Transport_DeltaAI2 extends GeneralNpcAI2
{
    private boolean canThink = true;
	private String walkerId = "220110004";
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	private AtomicBoolean startedEvent = new AtomicBoolean(false);
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
	private void removeF6RewardTrans() {
   		getOwner().getEffectController().removeEffect(17774);
 	}
	
	private void F6RewardTrans() {
   		SkillEngine.getInstance().getSkill(getOwner(), 17774, 1, getOwner()).useNoAnimationSkill();
 	}
	
	@Override
	protected void handleCreatureMoved(Creature creature) {
		if (creature instanceof Player) {
			final Player player = (Player) creature;
			if (MathUtil.getDistance(getOwner(), player) <= 100) {
				if (startedEvent.compareAndSet(false, true)) {
					canThink = false;
					getSpawnTemplate().setWalkerId("220110004");
					WalkManager.startWalking(this);
					getOwner().setState(1);
					PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
				}
			}
		}
	}
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			switch (getNpcId()) {
				case 246466:
				    announceF6RaidSumAtta01Light();
				break;
			}
		}
	}
	
	private void announceF6RaidSumAtta01Light() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//The enemy is retrieving our fragment.
					//Destroy the enemy's carrier and stop them from taking the fragment!
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404263));
				}
			}
		});
	}
	private void announceF6RaidSumKill04DarkDie() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//Armored Transport Delta was destroyed, and some users were given a special effect.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404270));
				}
			}
		});
	}
	
	@Override
	protected void handleSpawned() {
  		F6RewardTrans();
		super.handleSpawned();
	}
	
	@Override
	protected void handleBackHome() {
		F6RewardTrans();
		super.handleBackHome();
	}
	
	@Override
	protected void handleDied() {
		removeF6RewardTrans();
		announceF6RaidSumKill04DarkDie();
		super.handleDied();
	}
}