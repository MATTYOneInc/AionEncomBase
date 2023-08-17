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
package ai.instance.tiamatStronghold;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author Ranastic (Encom)
/****/

@AIName("traitor_kumbanda")
public class TraitorKumbandaAI2 extends AggressiveNpcAI2
{
	private int phase = 0;
	private Future<?> phaseTask;
	private boolean canThink = true;
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			spawn(283088, 880.1549f, 1319.4822f, 394.534f, (byte) 60);
			getPosition().getWorldMapInstance().getDoors().get(369).setOpen(false);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void checkPercentage(int hpPercentage) {
		if (hpPercentage == 95 && phase < 1) {
			phase = 1;
			sendMsg(1500708);
			startPhaseTask();
		} if (hpPercentage == 50 && phase < 2) {
			phase = 2;
			timeSlow(); //Time Slow.
			sendMsg(1500708);
			scheduleSlow();
		} if (hpPercentage == 30 && phase < 3) {
			phase = 3;
			timeStop(); //Time Stop.
			sendMsg(1500708);
			scheduleStop();
		} if (hpPercentage == 10 && phase < 4) {
			phase = 4;
			timeRush(); //Time Rush.
			sendMsg(1500708);
			scheduleRush();
		}
	}
	
	private void startPhaseTask() {
		phaseTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelPhaseTask();
				} else {
					sendMsg(1500708);
					SkillEngine.getInstance().getSkill(getOwner(), 20726, 10, getOwner()).useNoAnimationSkill(); //Time Speed.
					List<Player> players = getLifedPlayers();
					if (!players.isEmpty()) {
						int size = players.size();
						if (players.size() < 6) {
							for (Player p: players) {
								spawnTimeAccelerator(p);
							}
						} else {
							int count = Rnd.get(6, size);
							for (int i = 0; i < count; i++) {
								if (players.isEmpty()) {
									break;
								}
								spawnTimeAccelerator(players.get(Rnd.get(players.size())));
							}
						}
					}
				}
			}
		}, 20000, 40000);
	}
	
	private void spawnTimeAccelerator(Player player) {
		final float x = player.getX();
		final float y = player.getY();
		final float z = player.getZ();
		if (x > 0 && y > 0 && z > 0) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					if (!isAlreadyDead()) {
						spawn(283086, x, y, z, (byte) 0); //Time Accelerator.
					}
				}
			}, 3000);
		}
	}
	
	private void deleteHelpers() {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		if (instance != null) {
			deleteNpcs(instance.getNpcs(283086)); //Time Accelerator.
		}
	}
	
	private void timeStop() {
		AI2Actions.useSkill(this, 20725); //Time Stop.
	}
	
	private void timeRush() {
		AI2Actions.useSkill(this, 20727); //Time Rush.
	}
	
	private void timeSlow() {
		AI2Actions.useSkill(this, 20728); //Time Slow.
	}
	
	private void scheduleStop() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				timeStop();
			}
		}, 5000);
	}
	
	private void scheduleRush() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				timeRush();
			}
		}, 5000);
	}
	
	private void scheduleSlow() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				timeSlow();
			}
		}, 5000);
	}
	
	@Override
	protected void handleDied() {
		cancelPhaseTask();
		sendMsg(1500709);
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(283086)); //Time Accelerator.
			deleteNpcs(p.getWorldMapInstance().getNpcs(283088)); //Time Slow.
			p.getWorldMapInstance().getDoors().get(369).setOpen(true);
		}
		super.handleDied();
	}
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
	private List<Player> getLifedPlayers() {
		List<Player> players = new ArrayList<Player>();
		for (Player player: getKnownList().getKnownPlayers().values()) {
			if (!PlayerActions.isAlreadyDead(player)) {
				players.add(player);
			}
		}
		return players;
	}
	
	private void cancelPhaseTask() {
		if (phaseTask != null && !phaseTask.isDone()) {
			phaseTask.cancel(true);
		}
	}
	
	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
	
	private void sendMsg(int msg) {
		NpcShoutsService.getInstance().sendMsg(getOwner(), msg, getObjectId(), 0, 0);
	}
	
	@Override
	protected void handleDespawned() {
		cancelPhaseTask();
		super.handleDespawned();
	}
	
	@Override
	protected void handleBackHome() {
		canThink = true;
		cancelPhaseTask();
		deleteHelpers();
		isAggred.set(false);
		super.handleBackHome();
	}
}