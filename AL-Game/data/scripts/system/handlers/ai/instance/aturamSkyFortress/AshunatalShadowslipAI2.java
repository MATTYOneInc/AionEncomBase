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
package ai.instance.aturamSkyFortress;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("ashunatal_shadowslip")
public class AshunatalShadowslipAI2 extends AggressiveNpcAI2
{
	private boolean isSummoned;
	private boolean canThink = true;
	private AtomicBoolean isHome = new AtomicBoolean(true);
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			getPosition().getWorldMapInstance().getDoors().get(2).setOpen(true);
			getPosition().getWorldMapInstance().getDoors().get(17).setOpen(true);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 80 && !isSummoned) {
			isSummoned = true;
			SkillEngine.getInstance().getSkill(getOwner(), 19428, 1, getOwner()).useNoAnimationSkill();
			doSchedule();
		}
	}
	
	@Override
	protected void handleBackHome() {
		isHome.set(true);
		isSummoned = false;
		super.handleBackHome();
		getPosition().getWorldMapInstance().getDoors().get(17).setOpen(true);
		getPosition().getWorldMapInstance().getDoors().get(2).setOpen(false);
		Npc npc = getPosition().getWorldMapInstance().getNpc(219186);
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	/**
	 * Security:
	 * if Ashunatal Die!!!!
	 */
	@Override
	protected void handleDied() {
		super.handleDied();
		getPosition().getWorldMapInstance().getDoors().get(17).setOpen(true);
	}
	
	private void despawn() {
		AI2Actions.deleteOwner(this);
	}
	
	private void doSchedule() {
		if (!isAlreadyDead()) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					if (!isAlreadyDead()) {
						//Ashunatal has retreated to another room. Hunt her down!
						NpcShoutsService.getInstance().sendMsg(getOwner(), 1401391, 0);
						SkillEngine.getInstance().getSkill(getOwner(), 19417, 49, getOwner()).useNoAnimationSkill();
						ThreadPoolManager.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								if (!isAlreadyDead()) {
									WorldPosition p = getPosition();
									spawn(219186, p.getX(), p.getY(), p.getZ(), p.getHeading());
									canThink = false;
									getSpawnTemplate().setWalkerId("3002400001");
									setStateIfNot(AIState.WALKING);
									think();
									getOwner().setState(1);
									PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
									ThreadPoolManager.getInstance().schedule(new Runnable() {
										@Override
										public void run() {
											if (!isAlreadyDead()) {
												despawn();
											}
										}
									}, 4000);
								}
							}
						}, 3000);
					}
				}
			}, 2000);
		}
	}
}