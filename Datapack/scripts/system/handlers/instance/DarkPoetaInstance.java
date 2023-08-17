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
package instance;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.DarkPoetaReward;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/** Source: http://aion.power.plaync.com/wiki/%EC%95%94%ED%9D%91%EC%9D%98+%ED%8F%AC%EC%97%90%ED%83%80+-+%EB%A7%88%EC%8A%A4%ED%84%B0+%EB%B3%B4%EC%8A%A4
/****/

@InstanceID(300040000)
public class DarkPoetaInstance extends GeneralInstanceHandler
{
	//**Npc 4.9**//
	private Race spawnRace;
	private long startTime;
	private int powerGenerator;
	private Future<?> timerPrepare;
	private Future<?> timerInstance;
	private boolean isInstanceDestroyed;
	private Map<Integer, StaticDoor> doors;
	private DarkPoetaReward instanceReward;
	//Preparation Time.
	private int prepareTimerSeconds = 120000; //...2Min
	//Duration Instance Time.
	private int instanceTimerSeconds = 14400000; //...4Hrs
	private List<Integer> movies = new ArrayList<Integer>();
	private final FastList<Future<?>> darkPoetaTask = FastList.newInstance();
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 215281: //Calindi Flamelord.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
					    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053788, 1)); //Greater Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
				    }
				}
			break;
			case 215282: //Vanuka Infernus.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
					    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053788, 1)); //Greater Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053290, 1)); //Vanuka's Fabled Accessory Box.
				    }
				}
			break;
			case 215283: //Asaratu Bloodshade.
			case 215284: //Chramati Firetail.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053788, 1)); //Greater Stigma Support Bundle.
				    }
				}
			break;
			case 214864: //Noah's Furious Shade.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
					    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 122001039, 1)); //Noah's Tears.
				    }
				}
			break;
			case 214904: //Brigade General Anuhart.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
					    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053083, 1)); //Tempering Solution Chest.
						switch (Rnd.get(1, 2)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 123000929, 1)); //Brigade General Anuhart's Leather Belt.
				            break;
							case 2:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 123000930, 1)); //Brigade General Anuhart's Sash.
							break;
						}
				    }
				}
			break;
			case 215389: //Spaller Dhatra.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
					    switch (Rnd.get(1, 2)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053287, 1)); //Dhatra's Fabled Earrings Box.
				            break;
							case 2:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053292, 1)); //Dhatra's Earrings Box.
							break;
						}
				    }
				}
			break;
			case 214849: //Marabata Of Strength.
            case 214850: //Marabata Of Aether.
            case 214851: //Marabata Of Poisoning.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
					    switch (Rnd.get(1, 2)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053286, 1)); //Marabata's Fabled Ring Box.
				            break;
							case 2:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053291, 1)); //Marabata's Ring Box.
							break;
						}
				    }
				}
			break;
			case 215280: //Tahabata Pyrelord.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 170490001, 1)); //[Souvenir] Tahabata Statue.
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053083, 1)); //Tempering Solution Chest.
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190020175, 1)); //Tahabata Egg.
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053788, 1)); //Greater Stigma Support Bundle.
						switch (Rnd.get(1, 6)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188051398, 1)); //Tahabata's Eternal Weapon Chest.
				            break;
							case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053276, 1)); //Anuhart's Fabled Pants Box.
				            break;
							case 3:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053277, 1)); //Anuhart's Fabled Shoes Box.
							break;
							case 4:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053278, 1)); //Anuhart's Fabled Gloves Box.
							break;
							case 5:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053279, 1)); //Anuhart's Fabled Chest Box.
							break;
							case 6:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053280, 1)); //Anuhart's Fabled Shoulders Box.
							break;
						}
					}
				}
			break;
			case 237372: //Enraged Inferno Demon.
			case 237373: //Inferno Demon.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053083, 1)); //Tempering Solution Chest.
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053788, 1)); //Greater Stigma Support Bundle.
						switch (Rnd.get(1, 3)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188054178, 1)); //Master Tahabata's Weapon Box.
				            break;
							case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188054179, 1)); //Master Anuhart Elite's Weapon Box.
				            break;
							case 3:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188054183, 1)); //Master Armor Treasure Box.
							break;
						}
					}
				}
			break;
			case 702658: //Abbey Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053579, 1)); //[Event] Abbey Bundle.
		    break;
			case 702659: //Noble Abbey Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053580, 1)); //[Event] Noble Abbey Bundle.
		    break;
		}
	}
	
	@Override
	public void onDie(Npc npc) {
		int points = 0;
		int npcId = npc.getNpcId();
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 701869: //Camouflaging Stone Wall.
				despawnNpc(npc);
			break;
			case 281088: //Light Generator Core.
			case 281089: //Wave Generator Core.
			case 281090: //Torpidity Generator Core.
			case 281091: //Shockwave Generator Core.
			case 281092: //Confusion Generator Core.
			case 281095: //Aquar.
			case 281096: //Pura.
			case 281097: //Hydros.
			    despawnNpc(npc);
			break;
			case 214885: //Mutated Fungie.
			    points = 21;
			break;
			case 214827: //Anuhart Legionary.
			case 214828: //Anuhart Trooper.
			case 214829: //Anuhart Sentinel.
			case 214830: //Anuhart Bloodbinder.
			case 214831: //Anuhart Sergeant.
			case 214832: //Anuhart Fighter.
			case 214833: //Anuhart Ambusher.
			case 214834: //Anuhart Patroller.
			case 214835: //Anuhart Healer.
			case 214836: //Anuhart Warmonger.
			case 214837: //Anuhart Manbane.
			case 214838: //Anuhart Guard.
			case 214839: //Anuhart Scalepriest.
			case 214844: //Anuhart Scalewatch.
			case 214845: //Anuhart Drakeblade.
			case 214846: //Anuhart Ranger.
			case 214847: //Anuhart Mender.
			case 214848: //Anuhart Spotter.
			case 214852: //Anuhart Grappler.
			case 214853: //Anuhart Biteblade.
			case 214854: //Anuhart Searcher.
			case 214855: //Anuhart Mender.
			case 214856: //Anuhart Oculazen.
			case 214857: //Anuhart Scourge.
			case 214858: //Anuhart Willwarper.
			case 214865: //Anuhart Legatus.
			case 214866: //Anuhart Triaris.
			case 214867: //Anuhart Magus.
			case 214868: //Anuhart Curatus.
			case 214872: //Anuhart Bloodboss.
			case 214873: //Anuhart Huntlord.
			case 214874: //Anuhart High Magus.
			case 214875: //Anuhart Chief Surgeon.
			case 214881: //Anuhart Myrmidon.
			case 214882: //Anuhart Pathfinder.
			case 214883: //Anuhart Magist.
			case 214884: //Anuhart Chief Curatus.
			case 214890: //Anuhart Vindicator.
			case 214891: //Anuhart Shadow.
			case 214892: //Anuhart Aionbane.
			case 214893: //Anuhart Dark Healer.
			case 215223: //Anuhart Scaleguard.
			case 215224: //Anuhart Petmaster.
			case 215225: //Anuhart Shadowshot.
			case 215226: //Anuhart Lookout.
			case 215227: //Anuhart Tamer.
			case 215228: //Anuhart Shadowsnipe.
			case 215229: //Anuhart Defender.
			case 215230: //Anuhart Breeder.
			case 215231: //Anuhart Dark Sniper.
			case 215232: //Anuhart Comitatus.
			case 215233: //Anuhart Beastlord.
			case 215234: //Anuhart Bowmaster.
			case 215235: //Anuhart Serpentguard.
			case 215236: //Anuhart Protector.
			case 215237: //Anuhart Outlaw.
			case 215238: //Anuhart Snakepriest.
			case 215240: //Anuhart Curselock.
			case 215244: //Anuhart Overseer.
			case 215245: //Anuhart Daffadar.
			case 215246: //Anuhart Direblade.
			case 215247: //Anuhart Dark Sniper.
			case 215248: //Anuhart Captain.
			case 215249: //Anuhart Spiritlord.
			case 215253: //Anuhart Trainer.
			case 215254: //Anuhart Praetor.
			case 215255: //Anuhart Castigorus.
			case 215256: //Anuhart Arcus.
			case 215257: //Anuhart Honor Guard.
			case 215258: //Anuhart Invoker.
			case 215261: //Anuhart Immunus.
			case 215262: //Anuhart Proconsul.
			case 215263: //Anuhart Praefectus.
			case 215264: //Anuhart Vicarius.
			case 215265: //Anuhart Lictor.
			case 215266: //Anuhart Tetrarch.
			case 215267: //Anuhart Conjurer.
			case 215271: //Anuhart Consul.
			case 215272: //Anuhart High Templar.
			case 215273: //Anuhart High Raider.
			case 215274: //Anuhart High Scout.
			case 215275: //Anuhart Seneschal.
			case 215276: //Anuhart Transporter.
			case 215452: //Anuhart Proconsul.
			    points = 142;
			break;
			case 700517: //Balaur Barricade.
			case 700520: //Drana.
			case 700556: //Balaur Barricade.
			case 700558: //Balaur Barricade.
				points = 157;
				despawnNpc(npc);
			break;
			case 215431: //Vengeful Spirit Of Elyos Combat Captain.
			case 215432: //Spectral Arcanist Captain.
			    points = 164;
			break;
			case 214877: //Thrall Digger.
			case 214878: //Thrall Vigilante.
			case 214887: //Brainwashed Sentinel.
			case 215433: //Thrall Digger Leader.
			case 215434: //Thrall Vigilante Leader.
			    points = 173;
			break;
			case 214841: //Anuhart Tearlach.
			case 214842: //Anuhart Kurinark.
			case 215428: //Anuhart Mage Captain.
			    points = 190;
			break;
			case 215429: //Anuhart Scalewatch Captain.
			    points = 208;
			break;
			case 214849: //Marabata Of Strength.
            case 214850: //Marabata Of Aether.
            case 214851: //Marabata Of Poisoning.
			    points = 319;
			break;
			case 215430: //Anuhart Drakeblade Captain.
			    points = 357;
			break;
			case 214895: //Main Power Generator.
			    points = 377;
				deleteNpc(214898);
			    deleteNpc(214899);
			break;
			case 214896: //Auxiliary Power Generator.
			    points = 377;
				deleteNpc(214900);
			    deleteNpc(214901);
			break;
            case 214897: //Emergency Generator.
			    points = 377;
			    deleteNpc(214902);
			    deleteNpc(214903);
			break;
			case 214871: //Wounded Scar.
			case 215386: //Professor Hewahewa.
			    points = 409;
			break;
			case 214843: //Spiritmaster Atmach.
			    points = 456;
			break;
			case 214864: //Noah's Furious Shade.
			case 214880: //Spaller Echtra.
			case 215387: //Spectral Elim Elder.
			case 215388: //Spaller Rakanatra.
            case 215389: //Spaller Dhatra.
			    points = 789;
			break;
			case 214894: //Telepathy Controller.
			    sendMovie(player, 426);
				deleteNpc(281121); //Controller Protection Device.
				points = 789;
			break;
			case 214904: //Brigade General Anuhart.
			    points = 954;
			break;
		} switch (npc.getNpcId()) {
			case 700439: //Marabata Attack Booster.
			case 700440: //Marabata Defense Booster.
			case 700441: //Marabata Property Controller.
			case 700442: //Marabata Attack Booster.
			case 700443: //Marabata Defense Booster.
			case 700444: //Marabata Property Controller.
			case 700445: //Marabata Attack Booster.
            case 700446: //Marabata Defense Booster.
            case 700447: //Marabata Property Controller.
				toScheduleMarbataController(npcId);
			return;
			case 214849: //Marabata Of Strength.
			    deleteNpc(700439); //Marabata Attack Booster.
				deleteNpc(700440); //Marabata Defense Booster.
				deleteNpc(700441); //Marabata Property Controller.
			break;
            case 214850: //Marabata Of Aether.
			    deleteNpc(700442); //Marabata Attack Booster.
				deleteNpc(700443); //Marabata Defense Booster.
				deleteNpc(700444); //Marabata Property Controller.
			break;
            case 214851: //Marabata Of Poisoning.
			    deleteNpc(700445); //Marabata Attack Booster.
				deleteNpc(700446); //Marabata Defense Booster.
				deleteNpc(700447); //Marabata Property Controller.
			break;
		} if (instanceReward.getInstanceScoreType().isStartProgress()) {
			instanceReward.addNpcKill();
			instanceReward.addPoints(points);
			sendPacket(npc.getObjectTemplate().getNameId(), points);
		} switch (npcId) {
			case 214895: //Main Power Generator.
			case 214896: //Auxiliary Power Generator.
            case 214897: //Emergency Generator.
			    powerGenerator++;
				if (powerGenerator == 1) {
				} else if (powerGenerator == 2) {
				} else if (powerGenerator == 3) {
				    sendMovie(player, 427);
					spawn(214904, 275.34537f, 323.02072f, 130.9302f, (byte) 52); //Brigade General Anuhart.
				}
			break;
			//**[Ver.] 4.9**//
			case 857435: //Tahabata's Heart.
			    despawnNpc(npc);
				spawn(237372, 1176f, 1227f, 145f, (byte) 14); //Enraged Inferno Demon.
			break;
			case 857434: //Calindi's Heart.
			    despawnNpc(npc);
				spawn(237373, 1176f, 1227f, 145f, (byte) 14); //Inferno Demon.
			break;
			case 215280: //Tahabata Pyrelord.
				spawn(857435, 1176.877f, 1230.9423f, 144.3876f, (byte) 19); //Tahabata's Heart.
				switch (Rnd.get(1, 2)) {
		            case 1:
				        spawn(702658, 1180.83f, 1228.874f, 144.45352f, (byte) 23); //Abbey Box.
					break;
					case 2:
					    spawn(702659, 1180.83f, 1228.874f, 144.45352f, (byte) 23); //Noble Abbey Box.
					break;
				}
				spawn(731666, 1179.0000f, 1223.0000f, 146.0000f, (byte) 0, 223);
			break;
			case 215281: //Calindi Flamelord.
				spawn(857434, 1176.877f, 1230.9423f, 144.3876f, (byte) 19); //Calindi's Heart.
			    spawn(731666, 1179.0000f, 1223.0000f, 146.0000f, (byte) 0, 223);
			break;
			case 215282: //Vanuka Infernus.
			case 215283: //Asaratu Bloodshade.
			case 215284: //Chramati Firetail.
			    spawn(731666, 1179.0000f, 1223.0000f, 146.0000f, (byte) 0, 223);
			break;
			case 214904: //Brigade General Anuhart.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
					    instance.doOnAllPlayers(new Visitor<Player>() {
						    @Override
						    public void visit(Player player) {
							    stopInstance(player);
						    }
					    });
					}
				}, 5000);
			break;
		}
	}
	
	private int getTime() {
		long result = (int) (System.currentTimeMillis() - startTime);
		return instanceTimerSeconds - (int) result;
	}
	
	private void sendPacket(final int nameId, final int point) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (nameId != 0) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId(nameId * 2 + 1), point));
				}
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime(), instanceReward, null));
			}
		});
	}
	
	private int checkRank(int totalPoints) {
		int rank = 0;
		if (totalPoints >= 19643) { //Rank S.
			//You may only battle Tahabata Pyrelord within the given time limit.
			sendMsgByRace(1400257, Race.PC_ALL, 3000);
			spawn(215280, 1176f, 1227f, 145f, (byte) 14); //Tahabata Pyrelord.
			rank = 1;
		} else if (totalPoints >= 17046) { //Rank A.
			//Tahabata Pyrelord has left the battle.
			sendMsgByRace(1400258, Race.PC_ALL, 3000);
			//You may only battle Calindi Flamelord within the given time limit.
			sendMsgByRace(1400259, Race.PC_ALL, 6000);
			spawn(215281, 1176f, 1227f, 145f, (byte) 14); //Calindi Flamelord.
			rank = 2;
		} else if (totalPoints >= 13055) { //Rank B.
			//Calindi Flamelord has left the battle.
			sendMsgByRace(1400260, Race.PC_ALL, 3000);
			spawn(215282, 1176f, 1227f, 145f, (byte) 14); //Vanuka Infernus.
			rank = 3;
		} else if (totalPoints >= 9334) { //Rank C.
			spawn(215283, 1176f, 1227f, 145f, (byte) 14); //Asaratu Bloodshade.
			rank = 4;
		} else if (totalPoints >= 6556) { //Rank D.
			spawn(215284, 1176f, 1227f, 145f, (byte) 14); //Chramati Firetail.
			rank = 5;
		} else if (totalPoints >= 1254) { //Rank F.
			rank = 6;
		} else {
			rank = 8;
		}
		spawn(700478, 297.40482f, 316.69537f, 133.12941f, (byte) 56); //Tahabata Abyss Gate.
		return rank;
	}
	
	protected void startInstanceTask() {
		darkPoetaTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {
				    @Override
				    public void visit(Player player) {
					    stopInstance(player);
				    }
			    });
            }
        }, 14400000));
    }
	
	@Override
	public void onOpenDoor(Player player, int doorId) {
		if (doorId == 33) {
			startInstanceTask();
			doors.get(33).setOpen(true);
			//The member recruitment window has passed. You cannot recruit any more members.
			sendMsgByRace(1401181, Race.PC_ALL, 5000);
			//The player has 1 min to prepare !!! [Timer Red]
			if ((timerPrepare != null) && (!timerPrepare.isDone() || !timerPrepare.isCancelled())) {
				//Start the instance time !!! [Timer White]
				startMainInstanceTimer();
			}
		}
	}
	
	@Override
	public void onEnterInstance(final Player player) {
		startPrepareTimer();
		final int npc1 = spawnRace == Race.ASMODIANS ? 805732 : 805728;
		final int npc2 = spawnRace == Race.ASMODIANS ? 805733 : 805729;
		final int npc3 = spawnRace == Race.ASMODIANS ? 805734 : 805730;
		final int npc4 = spawnRace == Race.ASMODIANS ? 805735 : 805731;
		spawn(npc1, 837.0000f, 578.00000f, 118.7500f, (byte) 84);
		spawn(npc2, 650.4983f, 139.6467f, 102.64614f, (byte) 36);
		spawn(npc3, 584.8461f, 162.49805f, 104.1250f, (byte) 21);
		spawn(npc4, 583.2826f, 230.01761f, 106.8750f, (byte) 05);
	}
	
	private void startPrepareTimer() {
		if (timerPrepare == null) {
			timerPrepare = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					startMainInstanceTimer();
				}
			}, prepareTimerSeconds);
		}
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(prepareTimerSeconds, instanceReward, null));
			}
		});
	}
	
	private void startMainInstanceTimer() {
		if (!timerPrepare.isDone()) {
			timerPrepare.cancel(false);
		}
		startTime = System.currentTimeMillis();
		instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
		sendPacket(0, 0);
	}
	
	protected void stopInstance(Player player) {
        stopInstanceTask();
        instanceReward.setRank(6);
		instanceReward.setRank(checkRank(instanceReward.getPoints()));
		instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		sendMsg("[SUCCES]: You have finished <Dark Poeta>");
		sendPacket(0, 0);
	}
	
	protected void despawnNpc(Npc npc) {
        if (npc != null) {
            npc.getController().onDelete();
        }
    }
	
	protected void despawnNpcs(List<Npc> npcs) {
        for (Npc npc: npcs) {
            npc.getController().onDelete();
        }
    }
	
	private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = darkPoetaTask.head(), end = darkPoetaTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
	@Override
	public void onInstanceDestroy() {
		if (timerInstance != null) {
			timerInstance.cancel(false);
		} if (timerPrepare != null) {
			timerPrepare.cancel(false);
		}
		stopInstanceTask();
		isInstanceDestroyed = true;
		doors.clear();
		movies.clear();
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		instanceReward = new DarkPoetaReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
		switch (Rnd.get(1, 2)) {
			case 1:
				spawn(215429, 565.488f, 256.224f, 108.999f, (byte) 52); //Anuhart Scalewatch Captain.
			break;
			case 2:
				spawn(215429, 660.261f, 224.124f, 103.751f, (byte) 20); //Anuhart Scalewatch Captain.
			break;
		} switch (Rnd.get(1, 2)) {
			case 1:
				spawn(215430, 610.018f, 213.538f, 103.249f, (byte) 108); //Anuhart Drakeblade Captain.
			break;
			case 2:
				spawn(215430, 470.792f, 378.285f, 118.125f, (byte) 117); //Anuhart Drakeblade Captain.
			break;
		}
	}
	
	@Override
	public void onGather(Player player, Gatherable gatherable) {
		int points = 0;
		switch (gatherable.getObjectTemplate().getTemplateId()) {
		    case 401111: //Huge Vine.
			case 401112: //Nex.
			    points = 157;
			break;
		} if (instanceReward.getInstanceScoreType().isStartProgress()) {
			instanceReward.addGatherCollection();
			instanceReward.addPoints(points);
			sendPacket(gatherable.getObjectTemplate().getNameId(), points);
		}
	}
	
	private void toScheduleMarbataController(final int npcId) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				Npc boss = null;
				switch (npcId) {
					case 700439: //Marabata Attack Booster.
					case 700440: //Marabata Defense Booster.
                    case 700441: //Marabata Property Controller.
						boss = getNpc(214850); //Marabata Of Strength.
					break;
					case 700442: //Marabata Attack Booster.
					case 700443: //Marabata Defense Booster.
                    case 700444: //Marabata Property Controller.
						boss = getNpc(214851); //Marabata Of Aether.
					break;
					case 700445: //Marabata Attack Booster.
					case 700446: //Marabata Defense Booster.
                    case 700447: //Marabata Property Controller.
						boss = getNpc(214849); //Marabata Of Poisoning.
				} if (!isInstanceDestroyed && boss != null && !boss.getLifeStats().isAlreadyDead()) {
					switch (npcId) {
                        case 700439: //Marabata Attack Booster.
                            spawn(npcId, 665.37400f, 372.75100f, 99.375000f, (byte) 90);
                        break;
						case 700440: //Marabata Defense Booster.
                            spawn(npcId, 681.851013f, 408.625000f, 100.472000f, (byte) 13);
                        break;
						case 700441: //Marabata Property Controller.
                            spawn(npcId, 646.549988f, 406.088013f, 99.375000f, (byte) 49);
                        break;
						case 700442: //Marabata Attack Booster.
                            spawn(npcId, 636.117981f, 325.536987f, 99.375000f, (byte) 49);
                        break;
						case 700443: //Marabata Defense Booster.
                            spawn(npcId, 676.257019f, 319.649994f, 99.375000f, (byte) 4);
                        break;
                        case 700444: //Marabata Property Controller.
                            spawn(npcId, 655.851013f, 292.710999f, 99.375000f, (byte) 90);
                        break;
                        case 700445: //Marabata Attack Booster.
                            spawn(npcId, 605.625000f, 380.479004f, 99.375000f, (byte) 14);
                        break;
                        case 700446: //Marabata Defense Booster.
                            spawn(npcId, 598.706000f, 345.978000f, 99.375000f, (byte) 98);
                        break;
                        case 700447: //Marabata Property Controller.
                            spawn(npcId, 567.775024f, 366.207001f, 99.375000f, (byte) 59);
                        break;
                    }
				}
			}
		}, 30000);
	}
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		//"Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
		if (player.isInGroup2()) {
            PlayerGroupService.removePlayer(player);
        }
	}
	
	@Override
	public void onExitInstance(Player player) {
		InstanceService.destroyInstance(player.getPosition().getWorldMapInstance());
		if (instanceReward.getInstanceScoreType().isEndProgress()) {
			TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
		}
	}
	
	private void sendMovie(Player player, int movie) {
        if (!movies.contains(movie)) {
             movies.add(movie);
             PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
        }
    }
	
	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendWhiteMessageOnCenter(player, str);
			}
		});
	}
	
	protected void sendMsgByRace(final int msg, final Race race, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
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
		}, time);
	}
}