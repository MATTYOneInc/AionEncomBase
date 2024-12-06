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

@AIName("mage")
public class MageAI2 extends AggressiveNpcAI2
{
	private int magePhase = 0;
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
		if (hpPercentage == 95 && magePhase < 1) {
			magePhase = 1;
			startPhaseTask();
		} if (hpPercentage == 15 && magePhase < 2) {
			magePhase = 2;
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
								spawnSpirit(p);
							}
						} else {
							int count = Rnd.get(1, size);
							for (int i = 0; i < count; i++) {
								if (players.isEmpty()) {
									break;
								}
								spawnSpirit(players.get(Rnd.get(players.size())));
							}
						}
					}
				}
			}
		}, 3000, 15000);
	}
	
	private void spawnSpirit(Player player) {
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
							    spawn(285470, x, y, z, (byte) 0); //Water Spirit.
							break;
							case 2:
							    spawn(285473, x, y, z, (byte) 0); //Fire Spirit.
							break;
							case 3:
							    spawn(285469, x, y, z, (byte) 0); //Earth Spirit.
							break;
							case 4:
							    spawn(285471, x, y, z, (byte) 0); //Wind Spirit.
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
		    case 212810:
		        iceClawBlessing();
			break;
		} switch (getNpcId()) {
		    case 211596:
			case 212734:
			    mistShadeBlessing();
		    break;
		} switch (getNpcId()) {
			case 214867:
			case 214874:
			case 215428:
		        anuhartBravery();
			break;
		} switch (getNpcId()) {
		    case 233912:
		        survivalInstinct();
			break;
		} switch (getNpcId()) {
			case 209667:
			case 209668:
			case 209673:
			case 209674:
			case 219877:
            case 219883:
            case 219890:
            case 219896:
            case 219902:
            case 219909:
            case 219916:
            case 219922:
            case 220076:
            case 220080:
            case 220084:
            case 220088:
            case 220092:
            case 220096:
            case 220100:
            case 220165:
            case 220168:
            case 220171:
            case 220174:
			case 234084:
			case 234085:
			case 234099:
			case 234100:
			case 234766:
			case 234767:
			case 234784:
			case 234785:
			case 234802:
			case 234803:
			case 236025:
			case 236031:
			case 236038:
			case 236044:
			case 236051:
			case 236058:
			case 236064:
			case 236070:
			case 236676:
			case 236680:
			case 236684:
			case 236688:
			case 236692:
			case 236696:
			case 236700:
			case 256704:
			case 256705:
			case 256716:
			case 256717:
			case 256728:
			case 256729:
			case 256740:
			case 256741:
			case 257036:
			case 257037:
			case 257126:
			case 257127:
			case 257171:
			case 257172:
			case 257336:
			case 257337:
			case 257426:
			case 257427:
			case 257471:
			case 257472:
			case 257636:
			case 257637:
			case 257726:
			case 257727:
			case 257771:
			case 257772:
			case 257936:
			case 257937:
			case 258026:
			case 258027:
			case 258071:
			case 258072:
			case 263046:
			case 263051:
			case 263346:
			case 263351:
			case 264546:
			case 264551:
			case 264846:
			case 264851:
			case 265146:
			case 265151:
			case 265446:
			case 265451:
			case 265746:
			case 265751:
			case 266046:
			case 266051:
			case 279109:
			case 279111:
			case 279403:
			case 279405:
			case 279308:
			case 279311:
			case 279202:
			case 279216:
			case 279496:
			case 279510:
			case 279602:
			case 279605:
			case 883081:
			case 883087:
			case 883093:
			case 883099:
			case 883105:
			case 883111:
			case 883117:
			case 883123:
			case 883129:
			case 883135:
			case 883141:
			case 883147:
			case 883153:
			case 883159:
			case 883165:
			case 883171:
			case 883177:
			case 883183:
			case 883189:
			case 883195:
			case 883201:
			case 883207:
			case 883213:
			case 883219:
			case 883225:
			case 883231:
			case 883237:
			case 883243:
			case 883249:
			case 883255:
			case 883261:
			case 883267:
				conquerorPassion();
			break;
		} switch (getNpcId()) {
		    case 230798:
			case 230799:
			case 233336:
			case 233337:
			    midnightRobe();
			break;
		} switch (getNpcId()) {
			case 882471:
			case 882636:
			case 882637:
			case 882801:
			case 882802:
			case 882985:
			case 882991:
			case 882997:
			case 883003:
			case 883009:
			case 883015:
			case 883057:
			case 883063:
			case 883069:
			case 883075:
				ereshkigalRage();
			break;
		} switch (getNpcId()) {
			case 234083:
			case 234098:
			case 234765:
			case 234783:
			case 234801:
			case 235174:
			case 883021:
			case 883027:
			case 883033:
			case 883039:
			case 883045:
			case 883051:
				brokenMorale();
			break;
		}
	}
	
	private void iceClawBlessing() {
	    SkillEngine.getInstance().getSkill(getOwner(), 16979, 1, getOwner()).useNoAnimationSkill(); //Ice Claw Blessing.
	}
	private void mistShadeBlessing() {
	    SkillEngine.getInstance().getSkill(getOwner(), 16980, 1, getOwner()).useNoAnimationSkill(); //Mist Shade Blessing.
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
		despawnNpc(285470); //Water Spirit.
		despawnNpc(285473); //Fire Spirit.
		despawnNpc(285469); //Earth Spirit.
		despawnNpc(285471); //Wind Spirit.
	}
	
	@Override
	protected void handleDied() {
		deleteHelpers();
		cancelPhaseTask();
		despawnNpc(285470); //Water Spirit.
		despawnNpc(285473); //Fire Spirit.
		despawnNpc(285469); //Earth Spirit.
		despawnNpc(285471); //Wind Spirit.
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