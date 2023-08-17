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
package ai.instance.padmarashkaCave;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("padmarashka")
public class PadmarashkaAI2 extends AggressiveNpcAI2
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
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1400527, getOwner().getObjectId(), 0, 1000);
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1400528, getOwner().getObjectId(), 0, 9000);
				startPadmarashkaEggs();
			}
		} if (hpPercentage <= 75) {
			if (isStartedEvent.compareAndSet(false, true)) {
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1400537, getOwner().getObjectId(), 0, 1000);
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1400538, getOwner().getObjectId(), 0, 9000);
				startHugePadmarashkaEggs();
			}
		} if (hpPercentage <= 55) {
			if (isStartedEvent.compareAndSet(false, true)) {
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1400539, getOwner().getObjectId(), 0, 1000);
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1400540, getOwner().getObjectId(), 0, 9000);
				spawnRockslam();
			}
		} if (hpPercentage <= 35) {
			if (isStartedEvent.compareAndSet(false, true)) {
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1400541, getOwner().getObjectId(), 0, 1000);
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1400542, getOwner().getObjectId(), 0, 9000);
				startPhaseTask();
			}
		} if (hpPercentage <= 15) {
			if (isStartedEvent.compareAndSet(false, true)) {
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1400541, getOwner().getObjectId(), 0, 1000);
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1400542, getOwner().getObjectId(), 0, 9000);
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
					SkillEngine.getInstance().getSkill(getOwner(), 19938, 1, getOwner()).useNoAnimationSkill();
					List<Player> players = getLifedPlayers();
					if (!players.isEmpty()) {
						int size = players.size();
						if (players.size() < 6) {
							for (Player p: players) {
								spawnVotaic(p);
							}
						} else {
							int count = Rnd.get(6, size);
							for (int i = 0; i < count; i++) {
								if (players.isEmpty()) {
									break;
								}
								spawnVotaic(players.get(Rnd.get(players.size())));
							}
						}
					}
				}
			}
		}, 3000, 15000);
	}
	
	private void startPadmarashkaEggs() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1401214, 0);
		spawn(282613, 560.94006f, 157.98532f, 66.83846f, (byte) 55);
        spawn(282613, 555.14465f, 160.22182f, 66.76151f, (byte) 114);
        spawn(282613, 556.0417f, 154.67441f, 67.04825f, (byte) 22);
        spawn(282613, 597.1474f, 152.99193f, 66.61924f, (byte) 115);
        spawn(282613, 608.31226f, 151.27039f, 66.90845f, (byte) 59);
        spawn(282613, 603.01324f, 148.44322f, 66.87395f, (byte) 33);
        spawn(282613, 603.347f, 160.63982f, 67.17431f, (byte) 89);
	}
	
	private void startHugePadmarashkaEggs() {
		NpcShoutsService.getInstance().sendMsg(getOwner(), 1401214, 0);
		spawn(282614, 560.94006f, 157.98532f, 66.83846f, (byte) 55);
        spawn(282614, 555.14465f, 160.22182f, 66.76151f, (byte) 114);
        spawn(282614, 556.0417f, 154.67441f, 67.04825f, (byte) 22);
        spawn(282614, 597.1474f, 152.99193f, 66.61924f, (byte) 115);
        spawn(282614, 608.31226f, 151.27039f, 66.90845f, (byte) 59);
        spawn(282614, 603.01324f, 148.44322f, 66.87395f, (byte) 33);
        spawn(282614, 603.347f, 160.63982f, 67.17431f, (byte) 89);
	}
	
	private void spawnRockslam() {
        NpcShoutsService.getInstance().sendMsg(getOwner(), 1401215, 0);
		spawn(283137, 520.99585f, 270.23776f, 66.25f, (byte) 0);
        spawn(283137, 506.8137f, 238.60612f, 66.57414f, (byte) 0);
        spawn(283137, 495.94504f, 218.84671f, 67.58238f, (byte) 0);
        spawn(283137, 502.39307f, 196.61835f, 67.5513f, (byte) 0);
        spawn(283137, 510.88928f, 178.31491f, 66.869156f, (byte) 0);
        spawn(283137, 530.8308f, 160.27686f, 65.926926f, (byte) 0);
        spawn(283137, 551.6416f, 145.72856f, 67.93133f, (byte) 0);
        spawn(283137, 567.2481f, 162.51135f, 66.09399f, (byte) 0);
        spawn(283137, 585.17523f, 188.57863f, 66.125f, (byte) 0);
        spawn(283137, 611.51733f, 186.78618f, 66.25f, (byte) 0);
        spawn(283137, 624.74023f, 205.0654f, 66.255615f, (byte) 0);
        spawn(283137, 627.2288f, 228.05437f, 66.3268f, (byte) 0);
        spawn(283137, 619.18396f, 258.662f, 69.37874f, (byte) 0);
        spawn(283137, 604.1967f, 284.47342f, 66.71312f, (byte) 0);
        spawn(283137, 589.62775f, 293.01245f, 66.75f, (byte) 0);
        spawn(283137, 599.13367f, 264.5496f, 66.25f, (byte) 0);
        spawn(283137, 517.9813f, 220.19554f, 66.86278f, (byte) 0);
        spawn(283137, 533.5666f, 194.63768f, 66.125f, (byte) 0);
    }
	
	private void spawnVotaic(Player player) {
		final float x = player.getX();
		final float y = player.getY();
		final float z = player.getZ();
		if (x > 0 && y > 0 && z > 0) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					if (!isAlreadyDead()) {
						spawn(281829, x, y, z, (byte) 0); //Votaic Column.
					}
				}
			}, 3000);
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
	protected void handleDied() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(281829)); //Votaic Column.
			deleteNpcs(p.getWorldMapInstance().getNpcs(283137)); //Rockslam.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282613)); //Padmarashka's Eggs.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282614)); //Huge Padmarashka's Eggs.
		}
		cancelPhaseTask();
		super.handleDied();
	}
	
	@Override
	protected void handleBackHome() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(281829)); //Votaic Column.
			deleteNpcs(p.getWorldMapInstance().getNpcs(283137)); //Rockslam.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282613)); //Padmarashka's Eggs.
			deleteNpcs(p.getWorldMapInstance().getNpcs(282614)); //Huge Padmarashka's Eggs.
		}
		cancelPhaseTask();
		isAggred.set(false);
		isStartedEvent.set(false);
		super.handleBackHome();
	}
	
	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
}