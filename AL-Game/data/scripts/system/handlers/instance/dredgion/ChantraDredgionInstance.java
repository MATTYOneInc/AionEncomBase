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
package instance.dredgion;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.configs.main.RateConfig;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.DredgionReward;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.playerreward.DredgionPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastList;
import org.apache.commons.lang.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@InstanceID(300210000)
public class ChantraDredgionInstance extends GeneralInstanceHandler
{
	private int bulkhead;
	private int secretCache;
	private int surkanaKills;
	private long instanceTime;
	private Map<Integer, StaticDoor> doors;
	protected DredgionReward dredgionReward;
	private float loosingGroupMultiplier = 1;
	private boolean isInstanceDestroyed = false;
	protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
	private final FastList<Future<?>> chantraTask = FastList.newInstance();
	
	protected DredgionPlayerReward getPlayerReward(Player player) {
		Integer object = player.getObjectId();
		if (dredgionReward.getPlayerReward(object) == null) {
			addPlayerToReward(player);
		}
		return (DredgionPlayerReward) dredgionReward.getPlayerReward(object);
	}
	
	protected void captureRoom(Race race, int roomId) {
		dredgionReward.getDredgionRoomById(roomId).captureRoom(race);
	}
	
	private void addPlayerToReward(Player player) {
		dredgionReward.addPlayerReward(new DredgionPlayerReward(player.getObjectId()));
	}
	
	private boolean containPlayer(Integer object) {
		return dredgionReward.containPlayer(object);
	}
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 216886: //Captain Zanata.
				for (Player player: instance.getPlayersInside()) {
					if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053788, 1)); //Greater Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052582, 1)); //Dragon's Conquerer Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
					}
				}
			break;
			case 216875: //Shipmate Badala.
			case 216876: //Horizonist Anuta.
			case 216877: //First Mate Rukana.
			case 216878: //Skylord Vundar.
			case 216879: //First Mate Dubakar.
			case 216880: //Chief Daraka.
			case 216881: //Trigger.
			case 216882: //Sahadena The Abettor.
			case 216883: //Quartermaster Nupakun.
			case 216884: //Takahan.
			case 216885: //Hookmatan.
			case 216887: //Skyguard Parishka.
			case 216888: //Quartermaster Bhati.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052582, 1)); //Dragon's Conquerer Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
					}
				}
			break;
			case 216889: //Rajaya The Inquisitor.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052582, 1)); //Dragon's Conquerer Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
						switch (Rnd.get(1, 4)) {
							case 1:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 123001097, 1)); //Rajaya's Belt.
							break;
							case 2:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 120001117, 1)); //Rajaya's Earrings.
							break;
							case 3:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 123000941, 1)); //Rajaya's Corundum Necklace.
							break;
							case 4:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 121001032, 1)); //Rajaya's Turquoise Necklace.
							break;
						}
					}
				}
			break;
			case 216890: //Windfinder Kumar.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052582, 1)); //Dragon's Conquerer Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
						switch (Rnd.get(1, 4)) {
							case 1:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 123001096, 1)); //Kumar's Belt.
							break;
							case 2:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 120001118, 1)); //Kumar's Earrings.
							break;
							case 3:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 122001266, 1)); //Kumar's Corundum Ring.
							break;
							case 4:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 122001267, 1)); //Kumar's Turquoise Ring.
							break;
						}
					}
				}
			break;
		   /**
			* Obtain the Captain’s Key by killing Gatekeeper Sarta.
			* The Captain’s Key opens the door to the Captain’s Cabin.
			*/
			case 217037: //Gatekeeper Sarta.
				for (Player player: instance.getPlayersInside()) {
				    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000105, 1)); //Captain's Cabin Passage Key.
				    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000189, 1)); //Secret Cache Key.
					if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052582, 1)); //Dragon's Conquerer Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
					}
				}
			break;
		}
	}
	
	private void onDieSurkan(Npc npc, Player mostPlayerDamage, int points) {
		Race race = mostPlayerDamage.getRace();
		captureRoom(race, npc.getNpcId() + 14 - 700851); //Captain's Cabin Power Surkana.
		for (Player player: instance.getPlayersInside()) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400199, new DescriptionId(race.equals(Race.ASMODIANS) ? 1800483 : 1800481), new DescriptionId(npc.getObjectTemplate().getNameId() * 2 + 1)));
		} if (++surkanaKills == 5) {
            //Captain Zanata has appeared in the Captain's Cabin.
			sendMsgByRace(1400632, Race.PC_ALL, 0);
			spawn(216886, 485.47916f, 812.4957f, 416.68475f, (byte) 31); //Captain Zanata.
        }
		getPlayerReward(mostPlayerDamage).captureZone();
		updateScore(mostPlayerDamage, npc, points, false);
		npc.getController().onDelete();
	}
	
	protected void startInstanceTask() {
		instanceTime = System.currentTimeMillis();
		chantraTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				openFirstDoors();
				//The bulkhead has been activated and the passage between the First Armory and Gravity Control has been sealed.
				sendMsgByRace(1400604, Race.PC_ALL, 5000);
				//The bulkhead has been activated and the passage between the Second Armory and Gravity Control has been sealed.
				sendMsgByRace(1400605, Race.PC_ALL, 10000);
				dredgionReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
				sendPacket();
				switch (Rnd.get(1, 2)) {
					case 1:
					    spawn(216888, 415.2769f, 282.0216f, 409.7311f, (byte) 118); //Quartermaster Bhati.
					break;
					case 2:
					    spawn(216888, 556.53534f, 279.2918f, 409.7311f, (byte) 33); //Quartermaster Bhati.
					break;
				} switch (Rnd.get(1, 2)) {
					case 1:
					    spawn(216887, 485.25455f, 877.04614f, 405.01407f, (byte) 90); //Skyguard Parishka.
					break;
					case 2:
					    spawn(216885, 485.25455f, 877.04614f, 405.01407f, (byte) 90); //Hookmatan.
					break;
				}
			}
		}, 60000));
	   /**
		* Chantra Dredgion Teleportation Devices:
		* There are numerous teleportation devices located inside the Chantra Dredgion.
		* These teleportation devices allow players to teleport to different areas of the Dredgion with ease.
		* Central Teleporter: This teleporter activates 10 minutes after the Instanced Dungeon has begun.
		*/
		chantraTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//A teleport device has been activated in the Emergency Exit.
				sendMsgByRace(1401424, Race.PC_ALL, 0);
				spawn(730311, 415.07663f, 173.85265f, 432.53436f, (byte) 0, 34); //Portside Central Teleporter.
				spawn(730312, 554.83081f, 173.87158f, 432.52448f, (byte) 0, 9); //Starboard Central Teleporter.
			}
		}, 600000));
	   /**
		* Officer Kamanya:
		* Location: Gravity Control
		* Time Elapsed: 15 Minutes
		* Valor: 1,000 Points
		*/
		chantraTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Officer Kamanya has appeared in Gravity Control.
				sendMsgByRace(1400633, Race.PC_ALL, 0);
				spawn(216941, 485.4811f, 313.925f, 403.71857f, (byte) 36); //Officer Kamanya.
			}
		}, 900000));
		chantraTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!dredgionReward.isRewarded()) {
					Race winningRace = dredgionReward.getWinningRaceByScore();
					stopInstance(winningRace);
				}
			}
		}, 3600000));
	}
	
	@Override
	public void onDie(Npc npc) {
		int point = 0;
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
        if (mostPlayerDamage == null) {
            return;
        }
		Race race = mostPlayerDamage.getRace();
		switch (npc.getObjectTemplate().getTemplateId()) {
		   /**
			* There are six weapon chests located near the Chantra Dredgion entrance, and each chest awards 100 points if destroyed. 
			* These chests are also related to Quests for both Elyos and Asmodians. 
			*/
		    case 700836: //Weapon Chest.
                point = 100;
				despawnNpc(npc);
            break;
		   /**
			* The Surkana:
			* Destroy Surkana in each room can obtain a higher score.
			* 2. When you add monsters to attack Surkana is around 20m range. First, it is safe to be cleaned up monsters.
			* 3. When you destroy a race that destroyed Surkana is displayed on the map. It is through you can guess the path of the opposing faction.
			* 4. Captain Room Teleport appeared to be destroyed 5 Surkana.
			*/
			case 700838: //Armory Maintenance Surkana.
			case 700839: //Armory Maintenance Surkana.
			    despawnNpc(npc);
				onDieSurkan(npc, mostPlayerDamage, 500);
			break;
			case 700840: //Gravity Control Surkana.
			    despawnNpc(npc);
				onDieSurkan(npc, mostPlayerDamage, 900);
			break;
			case 700841: //Nuclear Control Surkana.
			case 700842: //Nuclear Control Surkana.
			    despawnNpc(npc);
				onDieSurkan(npc, mostPlayerDamage, 1100);
			break;
			case 700843: //Main Cannon Control Surkana.
			case 700844: //Main Cannon Control Surkana.
			    despawnNpc(npc);
				onDieSurkan(npc, mostPlayerDamage, 800);
			break;
			case 700845: //Drop Device Surkana.
			case 700846: //Drop Device Surkana.
			    despawnNpc(npc);
				onDieSurkan(npc, mostPlayerDamage, 600);
			break;
			case 700847: //Fighter Enhancing Surkana.
			    despawnNpc(npc);
				onDieSurkan(npc, mostPlayerDamage, 700);
			break;
			case 700848: //Storage Power Surkana.
			case 700849: //Storage Power Surkana.
			    despawnNpc(npc);
				onDieSurkan(npc, mostPlayerDamage, 500);
			break;
			case 700850: //Bridge Power Surkana.
			    despawnNpc(npc);
				onDieSurkan(npc, mostPlayerDamage, 700);
			break;
			case 700851: //Captain's Cabin Power Surkana.
				despawnNpc(npc);
				onDieSurkan(npc, mostPlayerDamage, 1100);
			break;
		   /**
			* Captain’s Cabin Passage:
			* There are paths to the left and right of the Captain’s Cabin’s on the second floor, but the doors are blocked.
			* These doors cannot be demolished, and can only be opened with a key dropped by a specific Named Monster.
			* Groups desiring the Captain’s Cabin Passage Key will need to defeat "Sahadena The Abettor" in the center of the Dredgion.
			* Only one Group can loot the key.
			* The Captain’s Cabin Teleport Device is located just beyond the Barracks, and can make reaching Captain Zanata much easier.
			*/
			case 216882: //Sahadena The Abettor.
				if (race.equals(Race.ELYOS)) {
				   //Captain's Cabin teleport device has been created at the end of the Atrium.
				   sendMsgByRace(1400652, Race.ELYOS, 0);
				   spawn(730357, 473.62231f, 761.99506f, 388.66f, (byte) 0, 33); //Elyos Captain's Cabin Teleporter.
				} else if (race.equals(Race.ASMODIANS)) {
				   //Captain's Cabin teleport device has been created at the end of the Atrium.
				   sendMsgByRace(1400652, Race.ASMODIANS, 0);
				   spawn(730358, 496.52225f, 761.99506f, 388.66f, (byte) 0, 186); //Asmodian Captain's Cabin Teleporter.
				}
				point = 1000;
            break;
		   /**
			* Supply Room Teleporter:
			* This teleporter activates after the destruction of the Teleporter Generator in the Barracks.
			*/
			case 730349: //Portside Teleporter Generator.
                despawnNpc(npc);
				//Supplies Storage teleport device has been created at Escape Hatch.
				sendMsgByRace(1400631, Race.PC_ALL, 0);
				spawn(730314, 397.11661f, 184.29782f, 432.8032f, (byte) 0, 42); //Port Supply Room Teleporter.
            break;
			case 730350: //Starboard Teleporter Generator.
                despawnNpc(npc);
				//Supplies Storage teleport device has been created at the Secondary Escape Hatch.
				sendMsgByRace(1400641, Race.PC_ALL, 0);
				spawn(730315, 572.10443f, 185.23933f, 432.56024f, (byte) 0, 10); //Starboard Supply Room Teleporter.
            break;
		   /**
			* Defense Shield Generator:
			* When the Defense Shield Generator on the Weapons Deck or Lower Weapons deck is demolished, a shield appears in Ready Room 1 or 2.
			* This shield blocks access to the center of the Chantra Dredgion.
			* The Ready Room is the shortest route to the center of the Dredgion, and the quickest route to the opposing race’s area.
			* Different tactics can be used in this area to maximize the Group’s accumulation of points.
			* For example, if one Group decides to destroy the opposing Group’s Shield Generator, it will make it difficult for the opposing Group to reach the center of the Dredgion.
			* In some cases, it might wiser for one Group to destroy their own Defense Shield Generator, and delay engagement with the opposing race in order to accumulate more points.
			*/
			case 730345: //Portside Defense Shield.
			case 730346: //Starboard Defense Shield.
				despawnNpc(npc);
			break;
			case 730351: //Portside Defense Shield Generator.
				despawnNpc(npc);
				//The Portside Defense Shield has been generated in Ready Room 1.
				sendMsgByRace(1400226, Race.PC_ALL, 0);
			break;
			case 730352: //Starboard Defense Shield Generator.
				despawnNpc(npc);
				//The Starboard Defense Shield has been generated in Ready Room 2.
				sendMsgByRace(1400227, Race.PC_ALL, 0);
			break;
		   /**
			* The Bulkhead:
			* These shields are activated by the Chantra Sentinel when first encountered at the beginning of the battle.
			* These shields block the entrance from the Armories to Gravity Control, and can be demolished with attacks, but also have a significant amount of health.
			* Groups often opt to move around the shields instead of demolishing them.
			* It’s worth noting that after a certain amount of time has passed, Officer Kamanya spawns in the Gravity Control room, and gives 1,000 points when defeated.
			* There is also a chance that Rajaya the Inquisitor, a Hero grade Named Monster, will spawn.
			* Rajaya the Inquisitor has a chance to drop Fabled and Heroic accessories. 
			*/
			case 730353: //Port Bulkhead.
			case 730354: //Starboard Bulkhead.
				bulkhead++;
				if (bulkhead == 2) {
					switch (Rnd.get(1, 2)) {
					    case 1:
					        spawn(216889, 456.3946f, 319.65912f, 402.69315f, (byte) 28); //Rajaya The Inquisitor.
					    break;
					    case 2:
					        spawn(216875, 513.9867f, 319.86224f, 402.68634f, (byte) 4); //Shipmate Badala.
					    break;
				    }
				}
				despawnNpc(npc);
			break;
			case 216875: //Shipmate Badala.
			case 216876: //Horizonist Anuta.
			case 216877: //First Mate Rukana.
			case 216878: //Skylord Vundar.
			case 216879: //First Mate Dubakar.
			case 216880: //Chief Daraka.
			case 216881: //Trigger.
			case 216883: //Quartermaster Nupakun.
			case 216884: //Takahan.
			case 217037: //Gatekeeper Sarta.
			    secretCache++;
				if (secretCache == 6) {
				    //A Dredgion Treasure Chest has appeared in the Drop Zone!
					sendMsgByRace(1401421, Race.PC_ALL, 0);
					spawn(701455, 482.82455f, 496.16556f, 397.28323f, (byte) 92); //Dredgion Opportunity Bundle.
				}
			    point = 200;
            break;
			case 216885: //Hookmatan.
			    point = 500;
            break;
			case 216887: //Skyguard Parishka.
			case 216889: //Rajaya The Inquisitor.
			case 216888: //Quartermaster Bhati.
			case 216890: //Windfinder Kumar.
			case 216941: //Officier Kamanya.
				point = 1000;
			break;
			case 216886: //Captain Zanata.
				point = 1000;
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
					public void run() {
						if (!dredgionReward.isRewarded()) {
							Race winningRace = dredgionReward.getWinningRaceByScore();
							stopInstance(winningRace);
						}
					}
				}, 30000);
			break;
		}
		updateScore(mostPlayerDamage, npc, point, false);
	}
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	protected void openFirstDoors() {
		openDoor(4);
		openDoor(173);
	}
	
	@Override
	public void onEnterInstance(final Player player) {
		if (!containPlayer(player.getObjectId())) {
			addPlayerToReward(player);
		}
		sendPacket();
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		dredgionReward = new DredgionReward(mapId, instanceId);
		dredgionReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
		startInstanceTask();
	}
	
	protected void stopInstance(Race race) {
		stopInstanceTask();
		dredgionReward.setWinningRace(race);
		dredgionReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		doReward();
		sendPacket();
	}
	
	public void doReward() {
		for (Player player : instance.getPlayersInside()) {
			InstancePlayerReward playerReward = getPlayerReward(player);
			float abyssPoint = playerReward.getPoints() * RateConfig.DREDGION_REWARD_RATE;
			if (player.getRace().equals(dredgionReward.getWinningRace())) {
				abyssPoint += dredgionReward.getWinnerPoints();
			} else {
				abyssPoint += dredgionReward.getLooserPoints();
			}
			AbyssPointsService.addAp(player, (int) abyssPoint);
			QuestEnv env = new QuestEnv(null, player, 0, 0);
			QuestEngine.getInstance().onDredgionReward(env);
		}
		for (Npc npc : instance.getNpcs()) {
			npc.getController().onDelete();
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					for (Player player : instance.getPlayersInside()) {
						if (PlayerActions.isAlreadyDead(player)) {
							PlayerReviveService.duelRevive(player);
						}
						onExitInstance(player);
					}
					AutoGroupService.getInstance().unRegisterInstance(instanceId);
				}
			}
		}, 120000);
	}
	
	private int getTime() {
		long result = System.currentTimeMillis() - instanceTime;
		if (result < 60000) {
			return (int) (60000 - result);
		} else if (result < 3600000) {
			return (int) (3600000 - (result - 60000));
		}
		return 0;
	}
	
	@Override
	public boolean onReviveEvent(Player player) {
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PlayerReviveService.revive(player, 100, 100, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		dredgionReward.portToPosition(player);
		return true;
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker) {
		int points = 60;
		if (lastAttacker instanceof Player) {
			if (lastAttacker.getRace() != player.getRace()) {
				InstancePlayerReward playerReward = getPlayerReward(player);
				if (getPointsByRace(lastAttacker.getRace()).compareTo(getPointsByRace(player.getRace())) < 0) {
					points *= loosingGroupMultiplier;
				} else if (loosingGroupMultiplier == 10 || playerReward.getPoints() == 0) {
					points = 0;
				}
			    updateScore((Player) lastAttacker, player, points, true);
			}
		}
		updateScore(player, player, -points, false);
		return true;
	}
	
	private MutableInt getPointsByRace(Race race) {
		return dredgionReward.getPointsByRace(race);
	}
	
	private void addPointsByRace(Race race, int points) {
		dredgionReward.addPointsByRace(race, points);
	}
	
	private void addPointToPlayer(Player player, int points) {
		getPlayerReward(player).addPoints(points);
	}
	
	private void addPvPKillToPlayer(Player player) {
		getPlayerReward(player).addPvPKillToPlayer();
	}
	
	private void addBalaurKillToPlayer(Player player) {
		getPlayerReward(player).addMonsterKillToPlayer();
	}
	
	protected void updateScore(Player player, Creature target, int points, boolean pvpKill) {
		if (points == 0) {
			return;
		}
		addPointsByRace(player.getRace(), points);
		List<Player> playersToGainScore = new ArrayList<Player>();
		if (target != null && player.isInGroup2()) {
			for (Player member : player.getPlayerGroup2().getOnlineMembers()) {
				if (member.getLifeStats().isAlreadyDead()) {
					continue;
				} if (MathUtil.isIn3dRange(member, target, GroupConfig.GROUP_MAX_DISTANCE)) {
					playersToGainScore.add(member);
				}
			}
		} else {
			playersToGainScore.add(player);
		}
		for (Player playerToGainScore : playersToGainScore) {
			addPointToPlayer(playerToGainScore, points / playersToGainScore.size());
			if (target instanceof Npc) {
				PacketSendUtility.sendPacket(playerToGainScore, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId(((Npc) target).getObjectTemplate().getNameId() * 2 + 1), points));
			} else if (target instanceof Player) {
				PacketSendUtility.sendPacket(playerToGainScore, new SM_SYSTEM_MESSAGE(1400237, target.getName(), points));
			}
		}
		int pointDifference = getPointsByRace(Race.ASMODIANS).intValue() - (getPointsByRace(Race.ELYOS)).intValue();
		if (pointDifference < 0) {
			pointDifference *= -1;
		} if (pointDifference >= 3000) {
			loosingGroupMultiplier = 10;
		} else if (pointDifference >= 1000) {
			loosingGroupMultiplier = 1.5f;
		} else {
			loosingGroupMultiplier = 1;
		} if (pvpKill && points > 0) {
			addPvPKillToPlayer(player);
		} else if (target instanceof Npc && ((Npc) target).getRace().equals(Race.DRAKAN)) {
			addBalaurKillToPlayer(player);
		}
		sendPacket();
	}
	
	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		dredgionReward.clear();
		stopInstanceTask();
		doors.clear();
	}
	
	protected void openDoor(int doorId) {
		StaticDoor door = doors.get(doorId);
		if (door != null) {
			door.setOpen(true);
		}
	}
	
	private void sendPacket() {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime(), dredgionReward, instance.getPlayersInside()));
			}
		});
	}
	
	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
        sp(npcId, x, y, z, h, 0, time, 0, null);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race) {
        sp(npcId, x, y, z, h, 0, time, msg, race);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time, final int msg, final Race race) {
        chantraTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!isInstanceDestroyed) {
                    spawn(npcId, x, y, z, h, entityId);
                    if (msg > 0) {
                        sendMsgByRace(msg, race, 0);
                    }
                }
            }
        }, time));
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
        chantraTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!isInstanceDestroyed) {
                    Npc npc = (Npc) spawn(npcId, x, y, z, h);
                    npc.getSpawn().setWalkerId(walkerId);
                    WalkManager.startWalking((NpcAI2) npc.getAi2());
                }
            }
        }, time));
    }
	
    protected void sendMsgByRace(final int msg, final Race race, int time) {
        chantraTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                instance.doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player player) {
                        if (player.getRace().equals(race) || race.equals(Race.PC_ALL)) {
                            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
                        }
                    }
                });
            }
        }, time));
    }
	
	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendWhiteMessageOnCenter(player, str);
			}
		});
	}
	
	private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = chantraTask.head(), end = chantraTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
	@Override
	public InstanceReward<?> getInstanceReward() {
		return dredgionReward;
	}
	
	@Override
    public void onExitInstance(Player player) {
        TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
    }
	
	@Override
    public void onLeaveInstance(Player player) {
        stopInstanceTask();
		//"Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
        if (player.isInGroup2()) {
            PlayerGroupService.removePlayer(player);
        }
    }
}