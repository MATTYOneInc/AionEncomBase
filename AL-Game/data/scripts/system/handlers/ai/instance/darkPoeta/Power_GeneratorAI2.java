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
package ai.instance.darkPoeta;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("power_generator")
public class Power_GeneratorAI2 extends AggressiveNpcAI2
{
	private Future<?> phaseTask;
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	private AtomicBoolean isStartedEvent = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 95) {
			if (isStartedEvent.compareAndSet(false, true)) {
				startPhaseTask();
			}
		} if (hpPercentage <= 75) {
			if (isStartedEvent.compareAndSet(false, true)) {
				startPhaseTask();
			}
		} if (hpPercentage <= 55) {
			if (isStartedEvent.compareAndSet(false, true)) {
				startPhaseTask();
			}
		} if (hpPercentage <= 35) {
			if (isStartedEvent.compareAndSet(false, true)) {
				startPhaseTask();
			}
		} if (hpPercentage <= 15) {
			if (isStartedEvent.compareAndSet(false, true)) {
				startPhaseTask();
			}
		}
	}
	
	private void startPhaseTask() {
		phaseTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelPhaseTask();
				} else {
					SkillEngine.getInstance().getSkill(getOwner(), 18126, 46, getOwner()).useNoAnimationSkill(); //Electrocution.
					List<Player> players = getLifedPlayers();
					if (!players.isEmpty()) {
						int size = players.size();
						if (players.size() < 6) {
							for (Player p: players) {
								spawnGeneratorCore(p);
							}
						} else {
							int count = Rnd.get(6, size);
							for (int i = 0; i < count; i++) {
								if (players.isEmpty()) {
									break;
								}
								spawnGeneratorCore(players.get(Rnd.get(players.size())));
							}
						}
					}
				}
			}
		}, 3000, 15000);
	}
	
	private void spawnGeneratorCore(Player player) {
		final float x = player.getX();
		final float y = player.getY();
		final float z = player.getZ();
		if (x > 0 && y > 0 && z > 0) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					if (!isAlreadyDead()) {
						switch (Rnd.get(1, 5)) {
						    case 1:
							    spawn(281088, x, y, z, (byte) 0); //Light Generator Core.
							break;
							case 2:
							    spawn(281089, x, y, z, (byte) 0); //Wave Generator Core.
							break;
							case 3:
							    spawn(281090, x, y, z, (byte) 0); //Torpidity Generator Core.
							break;
							case 4:
							    spawn(281091, x, y, z, (byte) 0); //Shockwave Generator Core.
							break;
							case 5:
							    spawn(281092, x, y, z, (byte) 0); //Confusion Generator Core.
							break;
						}
					}
				}
			}, 1000);
		}
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
	
	@Override
	protected void handleDespawned() {
		cancelPhaseTask();
		super.handleDespawned();
	}
	
	@Override
	protected void handleBackHome() {
		cancelPhaseTask();
		isStartedEvent.set(false);
		isAggred.set(false);
		super.handleBackHome();
	}
	
	@Override
	protected void handleDied() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(281088)); //Light Generator Core.
			deleteNpcs(p.getWorldMapInstance().getNpcs(281089)); //Wave Generator Core.
			deleteNpcs(p.getWorldMapInstance().getNpcs(281090)); //Torpidity Generator Core.
			deleteNpcs(p.getWorldMapInstance().getNpcs(281091)); //Shockwave Generator Core.
			deleteNpcs(p.getWorldMapInstance().getNpcs(281092)); //Confusion Generator Core.
		}
		cancelPhaseTask();
		super.handleDied();
	}
	
	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
}