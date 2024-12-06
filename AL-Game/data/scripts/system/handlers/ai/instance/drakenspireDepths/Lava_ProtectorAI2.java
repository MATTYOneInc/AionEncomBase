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
package ai.instance.drakenspireDepths;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("lava_protector")
public class Lava_ProtectorAI2 extends AggressiveNpcAI2
{
	private boolean canThink = true;
	private Future<?> magmaGluttenTask;
	private Future<?> lavaProtectorTask;
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	private AtomicBoolean isStartedEvent = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			switch (getNpcId()) {
				case 236227: //Lava Protector.
					lavaProtectorTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							AI2Actions.deleteOwner(Lava_ProtectorAI2.this);
							//Thanks to the sacrifice of the Detachment's Rush Squad, the Protectors' Fount has been destroyed.
							PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDSeal_Twin_06, 0);
						}
					}, 300000);
				break;
			}
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
			case 236227:
			    shareSource();
		    break;
		}
	}
	
	private void shareSource() {
	    SkillEngine.getInstance().getSkill(getOwner(), 20769, 1, getOwner()).useNoAnimationSkill(); //Lava Protector.
		SkillEngine.getInstance().getSkill(getOwner(), 21643, 1, getOwner()).useNoAnimationSkill(); //Share Source.
	}
	
	private void checkPercentage(int hpPercentage) {
		if (hpPercentage <= 50) {
			if (isStartedEvent.compareAndSet(false, true)) {
				startMagmaGluttenTask();
			}
		} if (hpPercentage <= 30) {
			if (isStartedEvent.compareAndSet(false, true)) {
				startMagmaGluttenTask();
			}
		}
	}
	
	private void startMagmaGluttenTask() {
		magmaGluttenTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelMagmaGluttenTask();
					cancelLavaProtectorTask();
				} else {
					SkillEngine.getInstance().getSkill(getOwner(), 21645, 60, getOwner()).useNoAnimationSkill(); //Raging Hellfire.
					List<Player> players = getLifedPlayers();
					if (!players.isEmpty()) {
						int size = players.size();
						if (players.size() < 6) {
							for (Player p: players) {
								spawnMagmaGlutten(p);
							}
						} else {
							int count = Rnd.get(6, size);
							for (int i = 0; i < count; i++) {
								if (players.isEmpty()) {
									break;
								}
								spawnMagmaGlutten(players.get(Rnd.get(players.size())));
							}
						}
					}
				}
			}
		}, 20000, 40000);
	}
	
	private void spawnMagmaGlutten(Player player) {
		final float x = player.getX();
		final float y = player.getY();
		final float z = player.getZ();
		if (x > 0 && y > 0 && z > 0) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					if (!isAlreadyDead()) {
						spawn(855621, x, y, z, (byte) 0); //Magma Glutten.
					}
				}
			}, 3000);
		}
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
	
	private void cancelMagmaGluttenTask() {
		if (magmaGluttenTask != null && !magmaGluttenTask.isDone()) {
			magmaGluttenTask.cancel(true);
		}
	}
	
	private void cancelLavaProtectorTask() {
		if (lavaProtectorTask != null && !lavaProtectorTask.isDone()) {
			lavaProtectorTask.cancel(true);
		}
	}
	
	private void deleteHelpers() {
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		if (instance != null) {
			deleteNpcs(instance.getNpcs(855621)); //Magma Glutten.
			deleteNpcs(instance.getNpcs(855709));
		}
	}
	
	@Override
	protected void handleDied() {
		cancelMagmaGluttenTask();
		cancelLavaProtectorTask();
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(855621)); //Magma Glutten.
			deleteNpcs(p.getWorldMapInstance().getNpcs(855709));
		}
		spawn(833053, 534.01245f, 209.25974f, 1681.8224f, (byte) 46); //Treasure Box.
		spawn(833055, 528.0497f, 215.08281f, 1681.8224f, (byte) 105); //Treasure Box.
		super.handleDied();
	}
	
	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
	
	@Override
	protected void handleDespawned() {
		cancelMagmaGluttenTask();
		cancelLavaProtectorTask();
		super.handleDespawned();
	}
	
	@Override
	protected void handleBackHome() {
		canThink = true;
		cancelMagmaGluttenTask();
		cancelLavaProtectorTask();
		deleteHelpers();
		isAggred.set(false);
		isStartedEvent.set(false);
		super.handleBackHome();
	}
}