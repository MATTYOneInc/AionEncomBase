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
package ai.instance.rentusBase;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.manager.EmoteManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("infernal_kuhara_the_volatile")
public class InfernalKuharaTheVolatileAI2 extends AggressiveNpcAI2
{
	private boolean canThink = true;
	private Future<?> bombEventTask;
	private Future<?> activeEventTask;
	private Future<?> barrelEventTask;
	private Phase phase = Phase.ACTIVE;
	private AtomicBoolean isHome = new AtomicBoolean(true);
	
	private enum Phase {
		ACTIVE,
		BOMBS,
	}
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
	@Override
	public void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			getPosition().getWorldMapInstance().getDoors().get(43).setOpen(true);
			getPosition().getWorldMapInstance().getDoors().get(150).setOpen(false);
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1500393, getObjectId(), 0, 0);
			startActivEvent();
			startBarrelEvent();
			announceOilBarrel();
		}
	}
	
	private void cancelActiveEventTask() {
		if (activeEventTask != null && !activeEventTask.isDone()) {
			activeEventTask.cancel(true);
		}
	}
	
	private void cancelBarrelEventTask() {
		if (barrelEventTask != null && !barrelEventTask.isDone()) {
			barrelEventTask.cancel(true);
		}
	}
	
	private void cancelBombEventTask() {
		if (bombEventTask != null && !bombEventTask.isDone()) {
			bombEventTask.cancel(true);
		}
	}
	
	private void startBarrelEvent() {
		barrelEventTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelBarrelEventTask();
				} else {
					switch(Rnd.get(1, 4)) {
						case 1:
							spawn(282394, 163.79257f, 266.18692f, 210.0678f, (byte) 74);
				            spawn(282394, 166.12807f, 263.00272f, 210.11052f, (byte) 74);
				            spawn(282394, 162.76723f, 263.038f, 210.0678f, (byte) 74);
						break;
						case 2:
							spawn(282394, 158.55418f, 236.08423f, 210.0678f, (byte) 42);
                            spawn(282394, 155.07852f, 234.39168f, 210.06781f, (byte) 40);
							spawn(282394, 155.78406f, 237.70297f, 210.0678f, (byte) 39);
						break;
						case 3:
							spawn(282394, 119.849335f, 243.8364f, 210.06781f, (byte) 12);
                            spawn(282394, 117.94701f, 246.95924f, 210.06781f, (byte) 12);
                            spawn(282394, 121.0155f, 246.60715f, 210.06781f, (byte) 11);
						break;
						case 4:
							spawn(282394, 124.52082f, 273.96237f, 210.06781f, (byte) 105);
							spawn(282394, 127.199356f, 276.00143f, 210.06781f, (byte) 104);
							spawn(282394, 127.42693f, 273.05597f, 210.06781f, (byte) 103);
						break;
					}
					startBombEvent();
					announceWeakKuhara();
				}
			}
		}, 15000, 15000);
	}
	
	private void announceOilBarrel() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//The Reian Quartermaster found an oil barrel.\nKuhara will be hurt if you make it explode.
					PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDYun_Kuhara_Barrel_Spawn, 0);
				}
			}
		});
	}
	
	private void announceWeakKuhara() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//Kuhara used up all his energy. His defenses will be very weak for a short while.
					PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDYun_Kuhara_StatDown, 5000);
				}
			}
		});
	}
	
	private void startBombEvent() {
		bombEventTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!isAlreadyDead() && !isHome.get() && phase.equals(Phase.ACTIVE)) {
					phase = Phase.BOMBS;
					cancelActiveEventTask();
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1500394, getObjectId(), 0, 0);
					canThink = false;
					EmoteManager.emoteStopAttacking(getOwner());
					setStateIfNot(AIState.WALKING);
					SkillEngine.getInstance().getSkill(getOwner(), 19703, 60, getOwner()).useNoAnimationSkill();
					spawnBombEvent();
					bombEventTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							if (!isAlreadyDead() && !isHome.get() && phase.equals(Phase.BOMBS)) {
								phase = Phase.ACTIVE;
								canThink = true;
								Creature creature = getAggroList().getMostHated();
								if (creature == null || creature.getLifeStats().isAlreadyDead() || !getOwner().canSee(creature)) {
									setStateIfNot(AIState.FIGHT);
									think();
								} else {
									getMoveController().abortMove();
									getOwner().setTarget(creature);
									getOwner().getGameStats().renewLastAttackTime();
									getOwner().getGameStats().renewLastAttackedTime();
									getOwner().getGameStats().renewLastChangeTargetTime();
									getOwner().getGameStats().renewLastSkillTime();
									setStateIfNot(AIState.FIGHT);
									handleMoveValidate();
									SkillEngine.getInstance().getSkill(getOwner(), 19375, 60, getOwner()).useNoAnimationSkill();
								}
								deleteNpcs(getPosition().getWorldMapInstance().getNpcs(282396)); //Kuhara Bomb.
							}
						}
					}, 11000);
				}
			}
		}, 14000);
	}
	
	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
	
	private void spawnBombEvent() {
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(126.528755f, 274.48883f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(126.528755f, 274.48883f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(162.22261f, 263.89288f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(162.22261f, 263.89288f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(156.32321f, 235.733f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(156.32321f, 235.733f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(119.23888f, 245.8903f, 209.81859f)));
		MoveBombToBoss(rndSpawnInRange(282396, Rnd.get(0, 2), new Point3D(119.23888f, 245.8903f, 209.81859f)));
	}
	
	private void MoveBombToBoss(final Npc npc) {
		if (!isAlreadyDead() && !isHome.get() ) {
			npc.setTarget(getOwner());
			npc.getMoveController().moveToTargetObject();
		}
	}
	
	private Npc rndSpawnInRange(int npcId, float distance, Point3D position) {
		float direction = Rnd.get(0, 199) / 100f;
		float x1 = (float) (Math.cos(Math.PI * direction) * distance);
		float y1 = (float) (Math.sin(Math.PI * direction) * distance);
		return (Npc) spawn(npcId, position.getX() + x1, position.getY() + y1, position.getZ(), (byte) 0);
	}
	
	private void startActivEvent() {
		activeEventTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelActiveEventTask();
				} else {
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1500395, getObjectId(), 0, 0);
					ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							if (!isAlreadyDead() && !isHome.get() && phase.equals(Phase.ACTIVE)) {
								SkillEngine.getInstance().getSkill(getOwner(), 19704, 60, getOwner()).useNoAnimationSkill();
								ThreadPoolManager.getInstance().schedule(new Runnable() {
									@Override
									public void run() {
										if (!isAlreadyDead() && !isHome.get() && phase.equals(Phase.ACTIVE)) {
											SkillEngine.getInstance().getSkill(getOwner(), 19705, 60, getOwner()).useNoAnimationSkill();
										}
									}
								}, 3500);
							}
						}
					}, 1000);
				}
			}
		}, 8000, 14000);
	}
	
	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		cancelActiveEventTask();
		cancelBarrelEventTask();
		cancelBombEventTask();
	}
	
	@Override
	protected void handleBackHome() {
		isHome.set(true);
		phase = Phase.ACTIVE;
		canThink = true;
		cancelActiveEventTask();
		cancelBarrelEventTask();
		cancelBombEventTask();
		getPosition().getWorldMapInstance().getDoors().get(43).setOpen(false);
		getPosition().getWorldMapInstance().getDoors().get(150).setOpen(true);
		super.handleBackHome();
	}
	
	@Override
	protected void handleDied() {
		cancelActiveEventTask();
		cancelBarrelEventTask();
		cancelBombEventTask();
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(282394)); //Oil Cask.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282395)); //Spilled Oil.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282396)); //Kuhara Bomb.
			spawn(236705, p.getX(), p.getY(), p.getZ(), p.getHeading()); //Infernal Kuhara Treasure Box.
			p.getWorldMapInstance().getDoors().get(43).setOpen(false);
			p.getWorldMapInstance().getDoors().get(150).setOpen(true);
		}
		super.handleDied();
		AI2Actions.deleteOwner(this);
	}
}