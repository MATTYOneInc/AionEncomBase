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
package ai;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.ai.Percentage;
import com.aionemu.gameserver.model.ai.SummonGroup;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/****/
/** Author (Encom)
/****/

@AIName("summoner")
public class SummonerAI2 extends AggressiveNpcAI2
{
	private final List<Integer> spawnedNpc = new ArrayList<Integer>();
	private List<Percentage> percentage = Collections.emptyList();
	private int spawnedPercent = 0;
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		synchronized(spawnedNpc) {
			removeHelpersSpawn();
			spawnedNpc.clear();
		}
		percentage.clear();
	}
	
	@Override
	protected void handleBackHome() {
		super.handleBackHome();
		synchronized(spawnedNpc) {
			removeHelpersSpawn();
			spawnedNpc.clear();
		}
		spawnedPercent = 0;
	}
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
			case 215240:
			case 215241:
			    anuhartBravery();
		    break;
		} switch (getNpcId()) {
			case 235975:
			    bellowingRoar();
		    break;
		} switch (getNpcId()) {
			case 237111:
			case 237112:
			case 237113:
			case 237114:
			case 237246:
			case 237247:
			case 237250:
			//5.0
			case 220425:
			    elementalLordship();
		    break;
		}
		percentage = DataManager.AI_DATA.getAiTemplate().get(getNpcId()).getSummons().getPercentage();
	}
	
	private void anuhartBravery() {
	    SkillEngine.getInstance().getSkill(getOwner(), 18168, 1, getOwner()).useNoAnimationSkill(); //Anuhart's Bravery.
	}
	private void bellowingRoar() {
	    SkillEngine.getInstance().getSkill(getOwner(), 22659, 1, getOwner()).useNoAnimationSkill(); //Bellowing Roar.
	}
	private void elementalLordship() {
	    SkillEngine.getInstance().getSkill(getOwner(), 22744, 1, getOwner()).useNoAnimationSkill(); //Elemental Lordship.
	}
	
	@Override
	protected void handleDied() {
		super.handleDied();
		switch (getNpcId()) {
		    //Tarmat & Prime Tarmat.
			case 234610:
			    addGpPlayer();
				announceTarmatDie();
			break;
			case 219998:
			case 220001:
			case 236727:
			case 236728:
			case 236732:
				announceTarmatDie();
			break;
		}
		removeHelpersSpawn();
		spawnedNpc.clear();
		percentage.clear();
	}
	
	private void addGpPlayer() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (MathUtil.isIn3dRange(player, getOwner(), 15)) {
					AbyssPointsService.addGp(player, 500);
				}
			}
		});
	}
	private void announceTarmatDie() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Devil Unit's Tarmat Beta has been destroyed.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_WORLDRAID_MESSAGE_DIE_02);
			}
		});
	}
	
	private void removeHelpersSpawn() {
		for (Integer object : spawnedNpc) {
			VisibleObject npc = World.getInstance().findVisibleObject(object);
			if (npc != null && npc.isSpawned()) {
				npc.getController().onDelete();
			}
		}
	}
	
	protected void addHelpersSpawn(int objId) {	
		synchronized(spawnedNpc) {
			spawnedNpc.add(objId);
		}
	}
	
	private void checkPercentage(int hpPercentage) {
		for (Percentage percent : percentage) {
			if (spawnedPercent != 0 && spawnedPercent <= percent.getPercent()) {
				continue;
			} if (hpPercentage <= percent.getPercent()) {
				int skill = percent.getSkillId();
				if (skill != 0) {
					AI2Actions.useSkill(this, skill);
				} if (percent.isIndividual()) {
					handleIndividualSpawnedSummons(percent);
				} else if (percent.getSummons() != null) {
					handleBeforeSpawn(percent);
					for (SummonGroup summonGroup : percent.getSummons()) {
						final SummonGroup sg = summonGroup;
						ThreadPoolManager.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								spawnHelpers(sg);
							}
						}, summonGroup.getSchedule());
					}
				}
				spawnedPercent = percent.getPercent();
			}
		}
	}
	
	protected void spawnHelpers(SummonGroup summonGroup) {
		if (!isAlreadyDead() && checkBeforeSpawn()) {
			int count = 0;
			if (summonGroup.getCount() != 0) {
				count = summonGroup.getCount();
			} else {
				count = Rnd.get(summonGroup.getMinCount(), summonGroup.getMaxCount());
			} for (int i = 0; i < count; i++) {
				SpawnTemplate summon = null;
				if (summonGroup.getDistance() != 0) {
					summon = rndSpawnInRange(summonGroup.getNpcId(), summonGroup.getDistance());
				} else {
					summon = SpawnEngine.addNewSingleTimeSpawn(getPosition().getMapId(), summonGroup.getNpcId(), summonGroup.getX(), summonGroup.getY(), summonGroup.getZ(), summonGroup.getH());
				}
				VisibleObject npc = SpawnEngine.spawnObject(summon, getPosition().getInstanceId());
				addHelpersSpawn(npc.getObjectId());
			}
			handleSpawnFinished(summonGroup);
		}
	}
	
	protected SpawnTemplate rndSpawnInRange(int npcId, float distance) {
		float direction = Rnd.get(0, 199) / 100f;
		float x = (float) (Math.cos(Math.PI * direction) * distance);
		float y = (float) (Math.sin(Math.PI * direction) * distance);
		return SpawnEngine.addNewSingleTimeSpawn(getPosition().getMapId(), npcId, getPosition().getX() + x, getPosition().getY() + y, getPosition().getZ(), getPosition().getHeading());
	}
	
	protected boolean checkBeforeSpawn() {
		return true;
	}
	
	protected void handleBeforeSpawn(Percentage percent) {
	}
	
	protected void handleSpawnFinished(SummonGroup summonGroup) {
	}
	
	protected void handleIndividualSpawnedSummons(Percentage percent) {
	}
}