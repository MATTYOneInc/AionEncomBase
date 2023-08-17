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
import com.aionemu.gameserver.world.WorldPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("ranger")
public class RangerAI2 extends AggressiveNpcAI2
{
	private int rangerPhase = 0;
	private Future<?> phaseTask;
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void checkPercentage(int hpPercentage) {
		if (hpPercentage == 95 && rangerPhase < 1) {
			rangerPhase = 1;
			startPhaseTask();
		} if (hpPercentage == 15 && rangerPhase < 2) {
			rangerPhase = 2;
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
								spawnTrap(p);
							}
						} else {
							int count = Rnd.get(1, size);
							for (int i = 0; i < count; i++) {
								if (players.isEmpty()) {
									break;
								}
								spawnTrap(players.get(Rnd.get(players.size())));
							}
						}
					}
				}
			}
		}, 3000, 15000);
	}
	
	private void spawnTrap(Player player) {
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
							    spawn(282372, x, y, z, (byte) 0); //Spike Bite Trap I.
							break;
							case 2:
							    spawn(294706, x, y, z, (byte) 0); //Trap Of Slowing III.
							break;
							case 3:
							    spawn(294707, x, y, z, (byte) 0); //Sleep Trap I.
							break;
							case 4:
							    spawn(294708, x, y, z, (byte) 0); //Explosive I.
							break;
						}
					}
				}
			}, 1000);
		}
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
			case 214846:
		        anuhartBravery();
			break;
		} switch (getNpcId()) {
            case 209665:
			case 209666:
			case 209671:
			case 209672:
            case 219879:
            case 219885:
            case 219892:
            case 219898:
            case 219904:
            case 219911:
            case 219918:
            case 219924:
            case 220075:
            case 220079:
            case 220083:
            case 220087:
            case 220091:
            case 220095:
            case 220099:
            case 220164:
            case 220167:
            case 220170:
            case 220173:
			case 234081:
			case 234082:
			case 234096:
			case 234097:
			case 234763:
			case 234764:
			case 234781:
			case 234782:
			case 234799:
			case 234800:
			case 236027:
			case 236033:
			case 236040:
			case 236046:
			case 236053:
			case 236060:
			case 236066:
			case 236072:
			case 236675:
			case 236679:
			case 236683:
			case 236687:
			case 236691:
			case 236695:
			case 236699:
			case 252434:
			case 252439:
			case 252449:
			case 252454:
			case 252464:
			case 252469:
			case 252479:
			case 252484:
			case 257033:
			case 257034:
			case 257102:
			case 257109:
			case 257147:
			case 257154:
			case 257123:
			case 257124:
			case 257168:
			case 257169:
			case 257333:
			case 257334:
			case 257402:
			case 257407:
			case 257447:
			case 257452:
			case 257423:
			case 257424:
			case 257468:
			case 257469:
			case 257633:
			case 257634:
			case 257702:
			case 257707:
			case 257723:
			case 257724:
			case 257747:
			case 257752:
			case 257768:
			case 257769:
			case 257933:
			case 257934:
			case 258002:
			case 258007:
			case 258023:
			case 258024:
			case 258047:
			case 258052:
			case 258068:
			case 258069:
			case 263031:
			case 263036:
			case 263331:
			case 263336:
			case 263601:
			case 263606:
			case 263901:
			case 263906:
			case 263631:
			case 263636:
			case 263931:
			case 263936:
			case 264201:
			case 264206:
			case 264231:
			case 264236:
			case 264531:
			case 264536:
			case 264801:
			case 264806:
			case 264831:
			case 264836:
			case 265101:
			case 265106:
			case 265131:
			case 265136:
			case 265401:
			case 265406:
			case 265431:
			case 265436:
			case 265701:
			case 265706:
			case 265731:
			case 265736:
			case 266001:
			case 266006:
			case 266031:
			case 266036:
			case 883079:
			case 883085:
			case 883091:
			case 883097:
			case 883103:
			case 883109:
			case 883115:
			case 883121:
			case 883127:
			case 883133:
			case 883139:
			case 883145:
			case 883151:
			case 883157:
			case 883163:
			case 883169:
			case 883175:
			case 883181:
			case 883187:
			case 883193:
			case 883199:
			case 883205:
			case 883211:
			case 883217:
			case 883223:
			case 883229:
			case 883235:
			case 883241:
			case 883247:
			case 883253:
			case 883259:
			case 883265:
				conquerorPassion();
			break;
		} switch (getNpcId()) {
			case 882466:
			case 882631:
			case 882632:
			case 882796:
			case 882797:
			case 882983:
			case 882989:
			case 882995:
			case 883001:
			case 883007:
			case 883013:
			case 883055:
			case 883061:
			case 883067:
			case 883073:
			case 883643:
				ereshkigalRage();
			break;
		} switch (getNpcId()) {
			case 234080:
			case 234095:
			case 234762:
			case 234780:
			case 234798:
			case 235176:
			case 883019:
			case 883025:
			case 883031:
			case 883037:
			case 883043:
			case 883049:
			    brokenMorale();
			break;
		}
	}
	
	private void anuhartBravery() {
	    SkillEngine.getInstance().getSkill(getOwner(), 18168, 1, getOwner()).useNoAnimationSkill(); //Anuhart's Bravery.
	}
	private void conquerorPassion() {
		SkillEngine.getInstance().getSkill(getOwner(), 20665, 1, getOwner()).useNoAnimationSkill(); //Conqueror's Passion.
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
		cancelPhaseTask();
		isAggred.set(false);
		super.handleBackHome();
	}
	
	@Override
	protected void handleDied() {
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(282372)); //Spike Bite Trap I.
			deleteNpcs(p.getWorldMapInstance().getNpcs(294706)); //Trap Of Slowing III.
			deleteNpcs(p.getWorldMapInstance().getNpcs(294707)); //Sleep Trap I.
			deleteNpcs(p.getWorldMapInstance().getNpcs(294708)); //Explosive I.
		}
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