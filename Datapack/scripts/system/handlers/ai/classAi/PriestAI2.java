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
package ai.classAi;

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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("priest")
public class PriestAI2 extends AggressiveNpcAI2
{
	private int priestPhase = 0;
	private Future<?> phaseTask;
	private boolean canThink = true;
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void checkPercentage(int hpPercentage) {
		if (hpPercentage == 95 && priestPhase < 1) {
			priestPhase = 1;
			startPhaseTask();
		} if (hpPercentage == 15 && priestPhase < 2) {
			priestPhase = 2;
			startPhaseTask();
		}
	}
	
	private void startPhaseTask() {
		phaseTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelPhaseTask();
				} else {
					List<Player> players = getLifedPlayers();
					if (!players.isEmpty()) {
						int size = players.size();
						if (players.size() < 1) {
							for (Player p: players) {
								spawnServant(p);
							}
						} else {
							int count = Rnd.get(1, size);
							for (int i = 0; i < count; i++) {
								if (players.isEmpty()) {
									break;
								}
								spawnServant(players.get(Rnd.get(players.size())));
							}
						}
					}
				}
			}
		}, 3000, 15000);
	}
	
	private void spawnServant(Player player) {
		final float x = player.getX();
		final float y = player.getY();
		final float z = player.getZ();
		if (x > 0 && y > 0 && z > 0) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					if (!isAlreadyDead()) {
						switch (Rnd.get(1, 4)) {
						    case 1:
							    spawn(280638, x, y, z, (byte) 0); //Sacred Dragon Relic I.
							break;
							case 2:
							    spawn(280639, x, y, z, (byte) 0); //Sacred Dragon Relic II.
							break;
							case 3:
							    spawn(280640, x, y, z, (byte) 0); //Sacred Dragon Relic III.
							break;
							case 4:
							    spawn(281301, x, y, z, (byte) 0); //Holy Servant I.
							break;
						}
					}
				}
			}, 1000);
		}
	}
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
	private void sendMsg(int msg) {
		NpcShoutsService.getInstance().sendMsg(getOwner(), msg, getObjectId(), false, 0, 0);
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
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
			case 214830:
			case 214835:
			case 214839:
			case 214847:
			case 214855:
			case 214859:
			case 214868:
			case 214875:
			case 214884:
			case 214893:
			case 215238:
			case 215270:
		        anuhartBravery();
			break;
		} switch (getNpcId()) {
		    case 233915:
		        survivalInstinct();
			break;
		} switch (getNpcId()) {
			case 234087:
			case 234088:
			case 234102:
			case 234103:
			case 234769:
			case 234770:
			case 234787:
			case 234788:
			case 234805:
			case 234806:
			case 234817:
			case 234818:
			case 234829:
			case 234830:
			case 234841:
			case 234842:
			case 234853:
			case 234854:
			case 234865:
			case 234866:
			case 234877:
			case 234878:
			case 234889:
			case 234890:
			case 234901:
			case 234902:
			case 256701:
			case 256702:
			case 256713:
			case 256714:
			case 256725:
			case 256726:
			case 256737:
			case 256738:
			case 257039:
			case 257040:
			case 257129:
			case 257130:
			case 257174:
			case 257175:
			case 257339:
			case 257340:
			case 257429:
			case 257430:
			case 257474:
			case 257475:
			case 257639:
			case 257640:
			case 257939:
			case 257940:
			case 258029:
			case 258030:
			case 258074:
			case 258075:
			case 263061:
			case 263066:
			case 263361:
			case 263366:
			case 263661:
			case 263666:
			case 263961:
			case 263966:
			case 264261:
			case 264266:
			case 264561:
			case 264566:
			case 264861:
			case 264866:
			case 265161:
			case 265166:
			case 265461:
			case 265466:
			case 265761:
			case 265766:
			case 266061:
			case 266066:
			case 279201:
			case 279287:
			case 279495:
			case 279581:
			case 883078:
			case 883084:
			case 883090:
			case 883096:
			case 883102:
			case 883108:
			case 883114:
			case 883120:
			case 883126:
			case 883132:
			case 883138:
			case 883144:
			case 883150:
			case 883156:
			case 883162:
			case 883168:
			case 883174:
			case 883180:
			case 883186:
			case 883192:
			case 883198:
			case 883204:
			case 883210:
			case 883216:
			case 883222:
			case 883228:
			case 883234:
			case 883240:
			case 883246:
			case 883252:
			case 883258:
			case 883264:
				conquerorPassion();
			break;
		} switch (getNpcId()) {
		    case 230804:
			case 230805:
			    midnightRobe();
			break;
		} switch (getNpcId()) {
			case 882461:
			case 882626:
			case 882791:
			case 882982:
			case 882988:
			case 882994:
			case 883000:
			case 883006:
			case 883012:
			case 883054:
			case 883060:
			case 883066:
			case 883072:
				ereshkigalRage();
			break;
		} switch (getNpcId()) {
			case 234086:
			case 234101:
			case 234768:
			case 234786:
			case 234804:
			case 234816:
			case 234828:
			case 234840:
			case 234852:
			case 234864:
			case 234876:
			case 234888:
			case 234900:
			case 235177:
			case 883018:
			case 883024:
			case 883030:
			case 883036:
			case 883042:
			case 883048:
			    brokenMorale();
			break;
		}
	}
	
	private void anuhartBravery() {
	    SkillEngine.getInstance().getSkill(getOwner(), 18168, 1, getOwner()).useNoAnimationSkill(); //Anuhart's Bravery.
	}
	private void survivalInstinct() {
	    SkillEngine.getInstance().getSkill(getOwner(), 20656, 1, getOwner()).useNoAnimationSkill(); //Survival Instinct.
	}
	private void conquerorPassion() {
		SkillEngine.getInstance().getSkill(getOwner(), 20665, 1, getOwner()).useNoAnimationSkill(); //Conqueror's Passion.
	}
	private void midnightRobe() {
		SkillEngine.getInstance().getSkill(getOwner(), 20700, 1, getOwner()).useNoAnimationSkill(); //Midnight Robe.
	}
	private void ereshkigalRage() {
	    SkillEngine.getInstance().getSkill(getOwner(), 22682, 1, getOwner()).useNoAnimationSkill(); //Ereshkigal Rage.
	}
	private void brokenMorale() {
		SkillEngine.getInstance().getSkill(getOwner(), 22791, 1, getOwner()).useNoAnimationSkill(); //Broken Morale.
	}
	
	@Override
	protected void handleDespawned() {
		cancelPhaseTask();
		super.handleDespawned();
	}
	
	@Override
	protected void handleBackHome() {
		canThink = true;
		deleteHelpers();
		cancelPhaseTask();
		isAggred.set(false);
		super.handleBackHome();
	}
	
	private void deleteHelpers() {
		despawnNpc(280638); //Sacred Dragon Relic I.
		despawnNpc(280639); //Sacred Dragon Relic II.
		despawnNpc(280640); //Sacred Dragon Relic III.
		despawnNpc(281621); //Holy Servant I.
		despawnNpc(281839); //Holy Servant II.
	}
	
	@Override
	protected void handleDied() {
		deleteHelpers();
		cancelPhaseTask();
		despawnNpc(280638); //Sacred Dragon Relic I.
		despawnNpc(280639); //Sacred Dragon Relic II.
		despawnNpc(280640); //Sacred Dragon Relic III.
		despawnNpc(281621); //Holy Servant I.
		despawnNpc(281839); //Holy Servant II.
		super.handleDied();
	}
	
	private void despawnNpc(int npcId) {
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null) {
			List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc: npcs) {
				npc.getController().onDelete();
			}
		}
	}
}