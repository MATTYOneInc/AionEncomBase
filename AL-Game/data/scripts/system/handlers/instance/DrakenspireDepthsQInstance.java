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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@InstanceID(301520000)
public class DrakenspireDepthsQInstance extends GeneralInstanceHandler {
	private int deathCharQ;
	private Race sealSceneRaceQ;
	private Future<?> drakenspireQTask;
	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;
	private List<Integer> movies = new ArrayList<Integer>();
	private FastMap<Integer, VisibleObject> objects = new FastMap<Integer, VisibleObject>();
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		spawn(237237, 151.88565f, 518.48145f, 1749.5945f, (byte) 9); //Beritra.
		//** Breakwall Twin's Boss **//
		SpawnTemplate IDsealFire1st = SpawnEngine.addNewSingleTimeSpawn(301520000, 702695, 558.32593f, 152.40855f, 1683.0303f, (byte) 0);
		IDsealFire1st.setEntityId(408);
		objects.put(702695, SpawnEngine.spawnObject(IDsealFire1st, instanceId));
		//** Breakwall Twin's Boss **//
		SpawnTemplate IDsealFire2st = SpawnEngine.addNewSingleTimeSpawn(301520000, 702696, 558.32593f, 212.02460f, 1683.0303f, (byte) 0);
		IDsealFire2st.setEntityId(409);
		objects.put(702696, SpawnEngine.spawnObject(IDsealFire2st, instanceId));
	}
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 237224: //Fetid Phantomscorch Chimera.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000219, 1)); //Crossroads Choice Key.
		    break;
		}
	}
	
	@Override
    public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		if (sealSceneRaceQ == null) {
            sealSceneRaceQ = player.getRace();
            ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
				    spawnIDSealScene01();
				}
			}, 0);
        } switch (player.getRace()) {
			case ELYOS:
				kaisinelLight();
				//The Agent's presence has boosted your combat skills.
				sendMsgByRace(1403030, Race.ELYOS, 10000);
				//The Agent's presence has greatly boosted your combat skills.
				sendMsgByRace(1403123, Race.ELYOS, 20000);
				// Start
				spawn(209823, 351.19638f, 192.30641f, 1684.2166f, (byte) 90); // Detachment Entry Soldier
				spawn(209823, 347.11496f, 192.31288f, 1684.2166f, (byte) 90); // Detachment Entry Soldier
				spawn(209824, 353.4427f, 182.63031f, 1684.2166f, (byte) 60); // Detachment Entry Leader
				spawn(209881, 351.18042f, 173.7679f, 1684.2166f, (byte) 30); // Detachment Demolisher
				spawn(209881, 347.099f, 173.77437f, 1684.2166f, (byte) 30); // Detachment Demolisher
				// Start Attacker
				spawn(209821, 392.2124f, 175.88617f, 1684.216f, (byte) 0); // Kaisinel Elite Mage
				spawn(209825, 423.92587f, 179.96892f, 1682.2166f, (byte) 0); // Kaisinel Elite Combatant
				spawn(209825, 403.4316f, 180.21465f, 1684.216f, (byte) 90); // Kaisinel Elite Combatant
				spawn(209825, 403.47638f, 186.16238f, 1684.216f, (byte) 30); // Kaisinel Elite Combatant
				spawn(209825, 435.78302f, 177.08868f, 1682.2167f, (byte) 0); // Kaisinel Elite Combatant
				spawn(209826, 392.62598f, 190.2562f, 1684.216f, (byte) 0); // Kaisinel Elite Archer
				spawn(209826, 431.28027f, 179.74289f, 1682.2166f, (byte) 45); // Kaisinel Elite Archer
				spawn(209827, 437.78033f, 184.66359f, 1682.2167f, (byte) 45); // Kaisinel Elite Mage
				spawn(209828, 407.54904f, 190.72981f, 1684.216f, (byte) 60); // Kaisinel Elite Priest
				spawn(209828, 407.55682f, 175.88617f, 1684.216f, (byte) 60); // Kaisinel Elite Priest
				spawn(209828, 426.9866f, 186.64618f, 1682.2166f, (byte) 0); // Kaisinel Elite Priest
				spawn(209828, 436.94476f, 194.55673f, 1682.2168f, (byte) 90); // Kaisinel Elite Priest
			break;
			case ASMODIANS:
				marchutanGrace();
				//The Agent's presence has boosted your combat skills.
				sendMsgByRace(1403030, Race.ASMODIANS, 10000);
				//The Agent's presence has greatly boosted your combat skills.
				sendMsgByRace(1403123, Race.ASMODIANS, 20000);
				// Start
				spawn(209840, 351.19638f, 192.30641f, 1684.2166f, (byte) 90); // Detachment Entry Soldier
				spawn(209840, 347.11496f, 192.31288f, 1684.2166f, (byte) 90); // Detachment Entry Soldier
				spawn(209841, 353.4427f, 182.63031f, 1684.2166f, (byte) 60); // Detachment Entry Leader
				spawn(209900, 351.18042f, 173.7679f, 1684.2166f, (byte) 30); // Detachment Demolisher
				spawn(209900, 347.099f, 173.77437f, 1684.2166f, (byte) 30); // Detachment Demolisher
				// Start Attacker
				spawn(209844, 392.2124f, 175.88617f, 1684.216f, (byte) 0); // Marchutan Elite Mage
				spawn(209842, 423.92587f, 179.96892f, 1682.2166f, (byte) 0); // Marchutan Elite Combatant
				spawn(209842, 403.4316f, 180.21465f, 1684.216f, (byte) 90); // Marchutan Elite Combatant
				spawn(209842, 403.47638f, 186.16238f, 1684.216f, (byte) 30); // Marchutan Elite Combatant
				spawn(209842, 435.78302f, 177.08868f, 1682.2167f, (byte) 0); // Marchutan Elite Combatant
				spawn(209843, 392.62598f, 190.2562f, 1684.216f, (byte) 0); // Marchutan Elite Archer
				spawn(209843, 431.28027f, 179.74289f, 1682.2166f, (byte) 45); // Marchutan Elite Archer
				spawn(209850, 437.78033f, 184.66359f, 1682.2167f, (byte) 45); // Marchutan Elite Mage
				spawn(209845, 407.54904f, 190.72981f, 1684.216f, (byte) 60); // Marchutan Elite Priest
				spawn(209845, 407.55682f, 175.88617f, 1684.216f, (byte) 60); // Marchutan Elite Priest
				spawn(209845, 426.9866f, 186.64618f, 1682.2166f, (byte) 0); // Marchutan Elite Priest
				spawn(209845, 436.94476f, 194.55673f, 1682.2168f, (byte) 90); // Marchutan Elite Priest
			break;
		}
    }
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 237206: //Deathchar Slayer.
			case 237207: //Deathchar Slayer.
				deathCharQ++;
				if (deathCharQ == 4) {
					if (player != null) {
				        switch (player.getRace()) {
					        case ELYOS:
							    //Destroy either the Lava Protector or the Heatvent Protector.
								sendMsgByRace(1402993, Race.ELYOS, 2000);
								//The Empyrean Elite started to advance.
								sendMsgByRace(1402994, Race.ELYOS, 4000);
							    spawn(209686, 412.57935f, 177.6678f, 1684.2161f, (byte) 0);
							    spawn(209687, 412.36823f, 187.22583f, 1684.2161f, (byte) 0);
						    break;
						    case ASMODIANS:
							    //Destroy either the Lava Protector or the Heatvent Protector.
								sendMsgByRace(1402993, Race.ASMODIANS, 2000);
								//The Empyrean Elite started to advance.
								sendMsgByRace(1402994, Race.ASMODIANS, 4000);
							    spawn(209751, 412.57935f, 177.6678f, 1684.2161f, (byte) 0);
							    spawn(209752, 412.36823f, 187.22583f, 1684.2161f, (byte) 0);
						    break;
						}
					}
				}
			break;
			case 237222: //Fetid Deathchar Patroler.
				if (player != null) {
				    switch (player.getRace()) {
					    case ELYOS:
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									//The Empyrean Elite started to advance.
									sendMsgByRace(1402994, Race.ELYOS, 0);
								    spawn(209684, 498.12088f, 206.94075f, 1688.1917f, (byte) 0);
									spawn(209684, 498.19083f, 215.10863f, 1688.1915f, (byte) 0);
									spawn(209685, 504.2284f, 211.17325f, 1688.1797f, (byte) 1);
								}
							}, 0);
						break;
						case ASMODIANS:
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									//The Empyrean Elite started to advance.
									sendMsgByRace(1402994, Race.ASMODIANS, 0);
								    spawn(209749, 498.12088f, 206.94075f, 1688.1917f, (byte) 0);
									spawn(209749, 498.19083f, 215.10863f, 1688.1915f, (byte) 0);
									spawn(209750, 504.2284f, 211.17325f, 1688.1797f, (byte) 1);
								}
							}, 0);
						break;
					}
				}
			break;
			case 237223: //Fetid Deathchar Necromancer.
				if (player != null) {
				    switch (player.getRace()) {
					    case ELYOS:
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									//The Empyrean Elite started to advance.
									sendMsgByRace(1402994, Race.ELYOS, 0);
								    spawn(209684, 497.9484f, 147.90346f, 1688.2479f, (byte) 1);
									spawn(209684, 498.11224f, 155.47922f, 1688.2467f, (byte) 0);
									spawn(209685, 504.3097f, 152.23334f, 1688.1984f, (byte) 0);
								}
							}, 0);
						break;
						case ASMODIANS:
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									//The Empyrean Elite started to advance.
									sendMsgByRace(1402994, Race.ASMODIANS, 0);
								    spawn(209749, 497.9484f, 147.90346f, 1688.2479f, (byte) 1);
									spawn(209749, 498.11224f, 155.47922f, 1688.2467f, (byte) 0);
									spawn(209750, 504.3097f, 152.23334f, 1688.1984f, (byte) 0);
								}
							}, 0);
						break;
					}
				}
			break;
			case 237224: //Fetid Phantomscorch Chimera.
				//Open the gate and the Empyrean Herald will advance.
				sendMsgByRace(1402996, Race.PC_ALL, 0);
				//The Empyrean Herald is advancing on Rapacious Kadena and requires your assistance.
				sendMsgByRace(1402997, Race.PC_ALL, 5000);
		    break;
			case 237228: //Lava Protector.
			case 237229: //Heatvent Protector.
				deleteNpc(702404); //Twin's Firewall.
				deleteNpc(702695); //Breakwall Twin's Boss.
				deleteNpc(702696); //Breakwall Twin's Boss.
				    if (player != null) {
				        switch (player.getRace()) {
					        case ELYOS:
							    deleteNpc(237228);
							    deleteNpc(237229);
								spawn(209690, 552.93365f, 155.68227f, 1683.7301f, (byte) 0);
								spawn(209690, 552.90247f, 215.51768f, 1683.7301f, (byte) 0);
								spawn(209693, 552.98486f, 148.64922f, 1683.7301f, (byte) 1);
								spawn(209693, 553.1255f, 208.44653f, 1683.7301f, (byte) 1);
								//The Empyrean Elite started to advance.
								sendMsgByRace(1402994, Race.ELYOS, 0);
								spawn(209695, 582.48083f, 183.74684f, 1683.7301f, (byte) 116);
								Npc PCGuard_Li_Talk_A = getNpc(209695);
								NpcShoutsService.getInstance().sendMsg(PCGuard_Li_Talk_A, 1402727, PCGuard_Li_Talk_A.getObjectId(), 0, 2000);
								NpcShoutsService.getInstance().sendMsg(PCGuard_Li_Talk_A, 1402728, PCGuard_Li_Talk_A.getObjectId(), 0, 6000);
								NpcShoutsService.getInstance().sendMsg(PCGuard_Li_Talk_A, 1402729, PCGuard_Li_Talk_A.getObjectId(), 0, 10000);
								NpcShoutsService.getInstance().sendMsg(PCGuard_Li_Talk_A, 1402730, PCGuard_Li_Talk_A.getObjectId(), 0, 14000);
									deleteNpc(209695);
									//Eliminate the Fetid Phantomscorch Master and choose a path to proceed.
									sendMsgByRace(1402995, Race.ELYOS, 0);
									//Obtain the Crossroads Choice Key carried by the Fetid Phantomscorch Master.
									sendMsgByRace(1403121, Race.ELYOS, 10000);
									spawn(209700, 582.48083f, 183.74684f, 1683.7301f, (byte) 116);
									spawn(209863, 582.5062f, 180.2349f, 1683.7301f, (byte) 1); //Masionel.
									Npc Masionel = getNpc(209863);
									//Thanks to you, the Detachment got through without any losses. Excellent work!
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501314, Masionel.getObjectId(), 0, 0);
									//This place is protected by a dark power. It cannot be destroyed.
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501312, Masionel.getObjectId(), 0, 6000);
									//Just let me blast us a path...
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501310, Masionel.getObjectId(), 0, 12000);
							    ThreadPoolManager.getInstance().schedule(new Runnable() {
									@Override
									public void run() {
									killNpc(getNpcs(731580));
									Npc Masionel = getNpc(209863);
									//Detachment Demolisher has opened the path to the next area.
									sendMsgByRace(1402689, Race.ELYOS, 0);
									//We can get through now.
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501311, Masionel.getObjectId(), 0, 0);
									//Please take care.
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501313, Masionel.getObjectId(), 0, 6000);
									}
								}, 10000);	
							break;
							case ASMODIANS:
						            deleteNpc(237228);
							        deleteNpc(237229);
									spawn(209755, 552.93365f, 155.68227f, 1683.7301f, (byte) 0);
									spawn(209755, 552.90247f, 215.51768f, 1683.7301f, (byte) 0);
									spawn(209758, 552.98486f, 148.64922f, 1683.7301f, (byte) 1);
									spawn(209758, 553.1255f, 208.44653f, 1683.7301f, (byte) 1);
									//The Empyrean Elite started to advance.
									sendMsgByRace(1402994, Race.ASMODIANS, 0);
									spawn(209760, 582.48083f, 183.74684f, 1683.7301f, (byte) 116);
									Npc PCGuard_Da_Talk_A = getNpc(209760);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Da_Talk_A, 1402727, PCGuard_Da_Talk_A.getObjectId(), 0, 2000);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Da_Talk_A, 1402728, PCGuard_Da_Talk_A.getObjectId(), 0, 6000);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Da_Talk_A, 1402729, PCGuard_Da_Talk_A.getObjectId(), 0, 10000);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Da_Talk_A, 1402730, PCGuard_Da_Talk_A.getObjectId(), 0, 14000);
									deleteNpc(209760);
									//Eliminate the Fetid Phantomscorch Master and choose a path to proceed.
									sendMsgByRace(1402995, Race.ASMODIANS, 0);
									//Obtain the Crossroads Choice Key carried by the Fetid Phantomscorch Master.
									sendMsgByRace(1403121, Race.ASMODIANS, 10000);
									spawn(209765, 582.48083f, 183.74684f, 1683.7301f, (byte) 116);
									spawn(209883, 582.5062f, 180.2349f, 1683.7301f, (byte) 1); //Parsia.
									Npc Parsia = getNpc(209883);
									//Thanks to you, the Detachment got through without any losses. Excellent work!
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501314, Parsia.getObjectId(), 0, 0);
									//This place is protected by a dark power. It cannot be destroyed.
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501312, Parsia.getObjectId(), 0, 6000);
									//Just let me blast us a path...
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501310, Parsia.getObjectId(), 0, 12000);
								ThreadPoolManager.getInstance().schedule(new Runnable() {
									@Override
									public void run() {
									killNpc(getNpcs(731580));
									Npc Parsia = getNpc(209883);
									//Detachment Demolisher has opened the path to the next area.
									sendMsgByRace(1402689, Race.ASMODIANS, 0);
									//We can get through now.
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501311, Parsia.getObjectId(), 0, 0);
									//Please take care.
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501313, Parsia.getObjectId(), 0, 6000);
								    }
								}, 10000);
							break;
						}
					}
			break;
			case 237213: //Frantic Phantomscorch Bonerival.
			case 237214: //Frantic Phantomscorch Chimera.
			case 237212: //Frantic Phantomscorch Contender.
			    Npc phantomscorchBonerival = instance.getNpc(237213); //Frantic Phantomscorch Bonerival.
			    Npc elitePhantomscorchChimera = instance.getNpc(237214); //Frantic Phantomscorch Chimera.
				Npc phantomscorchContender = instance.getNpc(237212); //Frantic Phantomscorch Contender.
			    if (isDead(phantomscorchBonerival) &&
				    isDead(elitePhantomscorchChimera) &&
					isDead(phantomscorchContender)) {
					if (player != null) {
				        switch (player.getRace()) {
					        case ELYOS:
							    ThreadPoolManager.getInstance().schedule(new Runnable() {
							        @Override
								    public void run() {
								        //The Empyrean Elite started to advance.
										sendMsgByRace(1402994, Race.ELYOS, 0);
										//The Empyrean Herald is advancing on Orissan and requires your assistance.
										sendMsgByRace(1402998, Race.ELYOS, 8000);
										//Slay Orissan with the Empyrean Herald.
										sendMsgByRace(1402999, Race.ELYOS, 16000);
										spawn(209704, 821.6019f, 523.7112f, 1706.6428f, (byte) 33);
										spawn(209705, 815.1346f, 522.75665f, 1706.7778f, (byte) 32);
								    }
							    }, 0);
								ThreadPoolManager.getInstance().schedule(new Runnable() {
							        @Override
								    public void run() {
										spawn(237231, 812.2527f, 568.7095f, 1701.0443f, (byte) 93); //Orissan.
										spawn(856554, 812.2527f, 568.7095f, 1701.0443f, (byte) 93); //ShapeChange Flash.
								    }
							    }, 0);
						    break;
						    case ASMODIANS:
							    ThreadPoolManager.getInstance().schedule(new Runnable() {
							        @Override
								    public void run() {
								        //The Empyrean Elite started to advance.
										sendMsgByRace(1402994, Race.ASMODIANS, 0);
										//The Empyrean Herald is advancing on Orissan and requires your assistance.
										sendMsgByRace(1402998, Race.ASMODIANS, 8000);
										//Slay Orissan with the Empyrean Herald.
										sendMsgByRace(1402999, Race.ASMODIANS, 16000);
										spawn(209769, 821.6019f, 523.7112f, 1706.6428f, (byte) 33);
										spawn(209770, 815.1346f, 522.75665f, 1706.7778f, (byte) 32);
								    }
							    }, 0);
								ThreadPoolManager.getInstance().schedule(new Runnable() {
							        @Override
								    public void run() {
										spawn(237231, 812.2527f, 568.7095f, 1701.0443f, (byte) 93); //Exhausted Orissan.
										spawn(856554, 812.2527f, 568.7095f, 1701.0443f, (byte) 93); //ShapeChange Flash.
								    }
							    }, 0);
						    break;
						}
					}
			    }
			break;
			case 237231: //Exhausted Orissan.
			    deleteNpc(856554); //ShapeChange Flash.
				if (player != null) {
				    switch (player.getRace()) {
					    case ELYOS:
						    sendMovie(player, 920);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    spawn(209706, 810.4212f, 550.19934f, 1701.044f, (byte) 31);
									spawn(209707, 818.40704f, 552.7704f, 1701.044f, (byte) 36);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    spawn(209710, 807.78894f, 578.7186f, 1701.0446f, (byte) 34);
									spawn(209710, 815.84827f, 579.7431f, 1701.0446f, (byte) 30);
									spawn(209711, 814.1693f, 588.4347f, 1701.0449f, (byte) 34);
									spawn(209711, 806.99536f, 587.9815f, 1701.0448f, (byte) 30);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    spawn(805363, 811.5f, 583.0642f, 1701.0447f, (byte) 32); //Killios.
									spawn(209713, 810.85767f, 588.2299f, 1701.0449f, (byte) 32);
									Npc PCGuard_Li = getNpc(209713);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Li, 1402727, PCGuard_Li.getObjectId(), 0, 2000);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Li, 1402728, PCGuard_Li.getObjectId(), 0, 6000);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Li, 1402729, PCGuard_Li.getObjectId(), 0, 10000);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Li, 1402730, PCGuard_Li.getObjectId(), 0, 14000);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									Npc Masionel = getNpc(209712);
									//Thanks to you, the Detachment got through without any losses. Excellent work!
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501314, Masionel.getObjectId(), 0, 0);
									//This place is protected by a dark power. It cannot be destroyed.
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501312, Masionel.getObjectId(), 0, 6000);
									//Just let me blast us a path...
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501310, Masionel.getObjectId(), 0, 12000);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									killNpc(getNpcs(700546));
									Npc Masionel = getNpc(209712);
									//The detachment continues to advance.
									sendMsgByRace(1403000, Race.ELYOS, 0);
									//Glide at the Soulfade Labyrinth to use the wind road.
									sendMsgByRace(1402941, Race.ELYOS, 5000);
									//We can get through now.
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501311, Masionel.getObjectId(), 0, 0);
									//Please take care.
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501313, Masionel.getObjectId(), 0, 6000);
								}
							}, 0);
						break;
						case ASMODIANS:
						    sendMovie(player, 917);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    spawn(209771, 810.4212f, 550.19934f, 1701.044f, (byte) 31);
									spawn(209772, 818.40704f, 552.7704f, 1701.044f, (byte) 36);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    spawn(209775, 807.78894f, 578.7186f, 1701.0446f, (byte) 34);
									spawn(209775, 815.84827f, 579.7431f, 1701.0446f, (byte) 30);
									spawn(209776, 814.1693f, 588.4347f, 1701.0449f, (byte) 34);
									spawn(209776, 806.99536f, 587.9815f, 1701.0448f, (byte) 30);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    spawn(805366, 811.5f, 583.0642f, 1701.0447f, (byte) 32); //Aimah.
									spawn(209778, 810.85767f, 588.2299f, 1701.0449f, (byte) 32);
									Npc PCGuard_Da = getNpc(209778);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Da, 1402727, PCGuard_Da.getObjectId(), 0, 2000);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Da, 1402728, PCGuard_Da.getObjectId(), 0, 6000);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Da, 1402729, PCGuard_Da.getObjectId(), 0, 10000);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Da, 1402730, PCGuard_Da.getObjectId(), 0, 14000);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									Npc Parsia = getNpc(209777);
									//Thanks to you, the Detachment got through without any losses. Excellent work!
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501314, Parsia.getObjectId(), 0, 0);
									//This place is protected by a dark power. It cannot be destroyed.
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501312, Parsia.getObjectId(), 0, 6000);
									//Just let me blast us a path...
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501310, Parsia.getObjectId(), 0, 12000);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									killNpc(getNpcs(700546));
									Npc Parsia = getNpc(209777);
									//The detachment continues to advance.
									sendMsgByRace(1403000, Race.ASMODIANS, 0);
									//Glide at the Soulfade Labyrinth to use the wind road.
									sendMsgByRace(1402941, Race.ASMODIANS, 5000);
									//We can get through now.
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501311, Parsia.getObjectId(), 0, 0);
									//Please take care.
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501313, Parsia.getObjectId(), 0, 6000);
								}
							}, 0);
						break;
				    }
				}
			break;
			case 237216: //Grave Cavity Rendclaw.
				if (player != null) {
				    switch (player.getRace()) {
					    case ELYOS:
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    spawn(209722, 631.6413f, 847.15717f, 1599.8486f, (byte) 90);
									spawn(209722, 639.45526f, 847.40265f, 1599.9614f, (byte) 90);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									spawn(209720, 635.39325f, 886.9716f, 1600.7146f, (byte) 90);
									spawn(209721, 635.0717f, 901.48553f, 1600.49f, (byte) 89);
									spawn(209731, 640.8788f, 891.53687f, 1600.5566f, (byte) 94);
									spawn(209731, 629.2198f, 891.1963f, 1600.5817f, (byte) 92);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    spawnIDSeal4ThStageElyos();
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									spawnEmpyreanLordsSiegeWeapon();
									//The Empyrean Herald's Siege Weapon has been summoned.
									sendMsgByRace(1403001, Race.ELYOS, 0);
									//Defend the Detachment and its siege weapons from the Guhena Legion.
									sendMsgByRace(1402705, Race.ELYOS, 5000);
									//If the Detachment loses too many soldiers, they will not be able to assist during the battle against Beritra.
									sendMsgByRace(1402706, Race.ELYOS, 10000);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									spawnWaveDoor();
									//The Flamesquelch Legion has been sighted in the area.
									sendMsgByRace(1403005, Race.ELYOS, 0);
									//Eliminate five Flamesquelch commanders to incite a retreat.
									sendMsgByRace(1403006, Race.ELYOS, 5000);
								}
							}, 25000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									startRaidSeal1();
									doors.get(271).setOpen(true);
									//The Guhena Legion's Commander Virtsha has appeared. You must defeat every captain and commander.
									sendMsgByRace(1402710, Race.ELYOS, 0);
								}
							}, 30000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									startRaidSeal2();
									//The Guhena Legion's second wave of attack has started. There will be three more attack waves.
									sendMsgByRace(1402707, Race.ELYOS, 0);
								}
							}, 65000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    startRaidSeal2_1();
								}
							}, 67000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									startRaidSeal3();
									doors.get(267).setOpen(true);
									//The Guhena Legion's third wave of attack has started. There will be two more attack waves.
									sendMsgByRace(1402708, Race.ELYOS, 0);
								}
							}, 87000);
						break;
						case ASMODIANS:
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    spawn(209787, 631.6413f, 847.15717f, 1599.8486f, (byte) 90);
									spawn(209787, 639.45526f, 847.40265f, 1599.9614f, (byte) 90);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									spawn(209785, 635.39325f, 886.9716f, 1600.7146f, (byte) 90);
									spawn(209786, 635.0717f, 901.48553f, 1600.49f, (byte) 89);
									spawn(209796, 640.8788f, 891.53687f, 1600.5566f, (byte) 94);
									spawn(209796, 629.2198f, 891.1963f, 1600.5817f, (byte) 92);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    spawnIDSeal4ThStageAsmodians();
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									spawnEmpyreanLordsSiegeWeapon();
									//The Empyrean Herald's Siege Weapon has been summoned.
									sendMsgByRace(1403001, Race.ASMODIANS, 0);
									//Defend the Detachment and its siege weapons from the Guhena Legion.
									sendMsgByRace(1402705, Race.ASMODIANS, 5000);
									//If the Detachment loses too many soldiers, they will not be able to assist during the battle against Beritra.
									sendMsgByRace(1402706, Race.ASMODIANS, 10000);
								}
							}, 0);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    spawnWaveDoor();
									//The Flamesquelch Legion has been sighted in the area.
									sendMsgByRace(1403005, Race.ASMODIANS, 0);
									//Eliminate five Flamesquelch commanders to incite a retreat.
									sendMsgByRace(1403006, Race.ASMODIANS, 5000);
								}
							}, 25000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									startRaidSeal1();
									doors.get(271).setOpen(true);
									//The Guhena Legion's Commander Virtsha has appeared. You must defeat every captain and commander.
									sendMsgByRace(1402710, Race.ASMODIANS, 0);
								}
							}, 30000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									startRaidSeal2();
									//The Guhena Legion's second wave of attack has started. There will be three more attack waves.
									sendMsgByRace(1402707, Race.ASMODIANS, 0);
								}
							}, 65000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    startRaidSeal2_1();
								}
							}, 67000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									startRaidSeal3();
									doors.get(267).setOpen(true);
									//The Guhena Legion's third wave of attack has started. There will be two more attack waves.
									sendMsgByRace(1402708, Race.ASMODIANS, 0);
								}
							}, 87000);
						break;
					}
				}
			break;
			case 237232: //Flamesquelch Command Destroyer.
				despawnNpc(npc);
				if (player != null) {
				    switch (player.getRace()) {
					    case ELYOS:
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									startRaidSeal4();
									startRaidSeal5();
									doors.get(7).setOpen(true);
									doors.get(310).setOpen(true);
									//The Guhena Legion's fourth wave of attack has started. There will be one more attack wave.
									sendMsgByRace(1402709, Race.ELYOS, 0);
								}
							}, 15000);
						break;
						case ASMODIANS:
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									startRaidSeal4();
									startRaidSeal5();
									doors.get(7).setOpen(true);
									doors.get(310).setOpen(true);
									//The Guhena Legion's fourth wave of attack has started. There will be one more attack wave.
									sendMsgByRace(1402709, Race.ASMODIANS, 0);
								}
							}, 15000);
						break;
					}
				}
			break;
			case 237233: //Flamesquelch Command Sorcerer.
				despawnNpc(npc);
				if (player != null) {
				    switch (player.getRace()) {
					    case ELYOS:
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									startRaidSeal6();
									doors.get(210).setOpen(true);
									doors.get(312).setOpen(true);
									//Commander Virtsha appeared. Eliminate Virtsha and the Flamesquelch Legion will scatter.
									sendMsgByRace(1403035, Race.ELYOS, 0);
									//The Detachment has suffered severe losses and will not be able to assist any further.
									sendMsgByRace(1402712, Race.ELYOS, 5000);
								}
							}, 15000);
						break;
						case ASMODIANS:
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									startRaidSeal6();
									doors.get(210).setOpen(true);
									doors.get(312).setOpen(true);
									//Commander Virtsha appeared. Eliminate Virtsha and the Flamesquelch Legion will scatter.
									sendMsgByRace(1403035, Race.ASMODIANS, 0);
									//The Detachment has suffered severe losses and will not be able to assist any further.
									sendMsgByRace(1402712, Race.ASMODIANS, 5000);
								}
							}, 15000);
						break;
					}
				}
			break;
			case 237234: //Flamesquelch Command Burnsmark.
				despawnNpc(npc);
				if (player != null) {
				    switch (player.getRace()) {
					    case ELYOS:
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									startRaidSeal3();
									startRaidSeal4();
									//The Detachment has suffered heavy losses and can only assist in limited capacity.
									sendMsgByRace(1402713, Race.ELYOS, 0);
								}
							}, 15000);
						break;
						case ASMODIANS:
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
									startRaidSeal3();
									startRaidSeal4();
									//The Detachment has suffered heavy losses and can only assist in limited capacity.
									sendMsgByRace(1402713, Race.ASMODIANS, 0);
								}
							}, 15000);
						break;
					}
				}
			break;
			case 237236: //Commander Virtsha.
			    despawnNpc(npc);
				if (player != null) {
				    switch (player.getRace()) {
				        case ELYOS:
						    //The Flamesquelch Legion is in disaray from the loss of its commanders.
							sendMsgByRace(1403007, Race.PC_ALL, 0);
						    //The Detachment has suffered some losses, but can assist at almost full capacity.
							sendMsgByRace(1402714, Race.PC_ALL, 5000);
							//You have successfully protected the Detachment. They will assist you during the battle against Beritra.
							sendMsgByRace(1402715, Race.PC_ALL, 10000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									Npc Masionel = getNpc(209720);
									//Thanks to you, the Detachment got through without any losses. Excellent work!
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501314, Masionel.getObjectId(), 0, 0);
									//This place is protected by a dark power. It cannot be destroyed.
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501312, Masionel.getObjectId(), 0, 6000);
									//Darkness blocks our path It is time to use the Empyrean Lord's siege weapon.
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501318, Masionel.getObjectId(), 0, 12000);
								}
							}, 15000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									spawnAgonyWell();
									killNpc(getNpcs(700545));
									Npc Masionel = getNpc(209720);
									//The Empyrean Lord's Stormcannon has blown open the Seal of Darkness.
									sendMsgByRace(1402711, Race.PC_ALL, 0);
									//Fight Beritra with the Empyrean Herald.
									sendMsgByRace(1403002, Race.PC_ALL, 5000);
									//Align yourself with the Empyrean Herald to slay Beritra.
									sendMsgByRace(1403011, Race.PC_ALL, 10000);
									//The Demolisher has opened the path to the next area.
									sendMsgByRace(1403009, Race.PC_ALL, 15000);
									//Charge complete! Fire!!
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501320, Masionel.getObjectId(), 0, 0);
									//Thank you. Now, let's bring the fight to Beritra!
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501321, Masionel.getObjectId(), 0, 6000);
								}
							}, 30000);
						break;
						case ASMODIANS:
						    //The Flamesquelch Legion is in disaray from the loss of its commanders.
							sendMsgByRace(1403007, Race.PC_ALL, 0);
						    //The Detachment has suffered some losses, but can assist at almost full capacity.
							sendMsgByRace(1402714, Race.PC_ALL, 5000);
							//You have successfully protected the Detachment. They will assist you during the battle against Beritra.
							sendMsgByRace(1402715, Race.PC_ALL, 10000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									Npc Parsia = getNpc(209785);
									//Thanks to you, the Detachment got through without any losses. Excellent work!
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501314, Parsia.getObjectId(), 0, 0);
									//This place is protected by a dark power. It cannot be destroyed.
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501312, Parsia.getObjectId(), 0, 6000);
									//Darkness blocks our path It is time to use the Empyrean Lord's siege weapon.
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501318, Parsia.getObjectId(), 0, 12000);
								}
							}, 15000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									spawnAgonyWell();
									killNpc(getNpcs(700545));
									Npc Parsia = getNpc(209785);
									//The Empyrean Lord's Stormcannon has blown open the Seal of Darkness.
									sendMsgByRace(1402711, Race.PC_ALL, 0);
									//Fight Beritra with the Empyrean Herald.
									sendMsgByRace(1403002, Race.PC_ALL, 5000);
									//Align yourself with the Empyrean Herald to slay Beritra.
									sendMsgByRace(1403011, Race.PC_ALL, 10000);
									//The Demolisher has opened the path to the next area.
									sendMsgByRace(1403009, Race.PC_ALL, 15000);
									//Charge complete! Fire!!
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501320, Parsia.getObjectId(), 0, 0);
									//Thank you. Now, let's bring the fight to Beritra!
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501321, Parsia.getObjectId(), 0, 6000);
								}
							}, 30000);
						break;
					}
				}
			break;
			case 237217: //Elite Flamesquelch Destroyer.
			case 237218: //Elite Flamesquelch Burnsmark.
			case 237219: //Elite Flamesquelch Rangerblaze.
			case 237220: //Elite Flamesquelch Sorcerer.
			case 237235: //Flamesquelch Command Extinguisher.
			    despawnNpc(npc);
			break;
			case 237237: //Beritra.
			    despawnNpc(npc);
			    spawn(237238, 127.77517f, 508.3428f, 1749.8322f, (byte) 8); //Beritra [Dragon Form]
			    instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							startDrakenspireTimer();
							killNpc(getNpcs(702695));
						    killNpc(getNpcs(702697));
						    killNpc(getNpcs(702699));
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 420)); //7 Minutes.
						}
					}
				});
			break;
			case 237238: //Beritra [Dragon Form]
				despawnNpc(npc);
			    if (player != null) {
				    switch (player.getRace()) {
				        case ELYOS:
						    sendMovie(player, 919);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									spawnIDSealSceneEnding();
									drakenspireQTask.cancel(true);
									//The Seal of Darkness has been destroyed.
									sendMsgByRace(1403008, Race.PC_ALL, 0);
									//A reaction is taking place within the agent's weapon.
									sendMsgByRace(1403012, Race.PC_ALL, 5000);
									sendMsg("[Congratulation]: you finish <Drakenspire Depths>");
									spawn(731548, 147.01088f, 517.9374f, 1749.5007f, (byte) 2); //Drakenspire Depths Exit.
									Npc Masionel = getNpc(209739);
									//Beritra may have gotten away, but we've taken Drakenspire Depths. You've done an excellent job.
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501326, Masionel.getObjectId(), 0, 12000);
									//Beritra has fled! He may have escaped, but we gave him something to remember us by!
									NpcShoutsService.getInstance().sendMsg(Masionel, 1501327, Masionel.getObjectId(), 0, 22000);
									instance.doOnAllPlayers(new Visitor<Player>() {
										@Override
										public void visit(Player player) {
											if (player.isOnline()) {
												PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
											}
										}
									});
								}
							}, 1000);
						break;
						case ASMODIANS:
						    sendMovie(player, 922);
						    ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									spawnIDSealSceneEnding();
									drakenspireQTask.cancel(true);
									//The Seal of Darkness has been destroyed.
									sendMsgByRace(1403008, Race.PC_ALL, 0);
									//A reaction is taking place within the agent's weapon.
									sendMsgByRace(1403012, Race.PC_ALL, 5000);
									sendMsg("[Congratulation]: you finish <Drakenspire Depths>");
									spawn(731548, 147.01088f, 517.9374f, 1749.5007f, (byte) 2); //Drakenspire Depths Exit.
									Npc Parsia = getNpc(209804);
									//Beritra may have gotten away, but we've taken Drakenspire Depths. You've done an excellent job.
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501326, Parsia.getObjectId(), 0, 12000);
									//Beritra has fled! He may have escaped, but we gave him something to remember us by!
									NpcShoutsService.getInstance().sendMsg(Parsia, 1501327, Parsia.getObjectId(), 0, 22000);
									instance.doOnAllPlayers(new Visitor<Player>() {
										@Override
										public void visit(Player player) {
											if (player.isOnline()) {
												PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
											}
										}
									});
								}
							}, 1000);
						break;
					}
				}
			break;
		}
	}
	
	private void startDrakenspireTimer() {
		//Beritra transforms into a dragon.
		sendMsgByRace(1402721, Race.PC_ALL, 0);
		//Beritra will disappear when the relic is completely extracted in 7 minutes.
		sendMsgByRace(1402722, Race.PC_ALL, 10000);
		//Beritra will disappear when the relic is completely extracted in a moment.
		sendMsgByRace(1402726, Race.PC_ALL, 390000);
		//Beritra will disappear when the relic is completely extracted in 5 minutes.
		this.sendMessage(1402724, 2 * 60 * 1000);
		//Beritra will disappear when the relic is completely extracted in 1 minute.
		this.sendMessage(1402725, 6 * 60 * 1000);
		//The extraction of the Balaur Lord's Relic is complete and Beritra has disappeared.
		this.sendMessage(1402720, 7 * 60 * 1000);
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
				    drakenspireQTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							instance.doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									onExitInstance(player);
								}
							});
							onInstanceDestroy();
						}
					}, 420000); //7 Min.
				}
			}
		});
    }
	
	private void kaisinelLight() {
		for (Player p: instance.getPlayersInside()) {
			SkillTemplate st =  DataManager.SKILL_DATA.getSkillTemplate(22778); //Kaisinel's Light.
			Effect e = new Effect(p, p, st, 1, st.getEffectsDuration(9));
			e.initialize();
			e.applyEffect();
		}
	}
	private void marchutanGrace() {
		for (Player p: instance.getPlayersInside()) {
			SkillTemplate st =  DataManager.SKILL_DATA.getSkillTemplate(22779); //Marchutan's Grace.
			Effect e = new Effect(p, p, st, 1, st.getEffectsDuration(9));
			e.initialize();
			e.applyEffect();
		}
	}
	
	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(22778); //Kaisinel's Light.
		effectController.removeEffect(22779); //Marchutan's Grace.
	}
	
	public void removeItems(Player player) {
        Storage storage = player.getInventory();
        storage.decreaseByItemId(185000219, storage.getItemCountByItemId(185000219)); //Crossroads Choice Key.
    }
	
	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
		removeEffects(player);
	}
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
		removeEffects(player);
	}
	
	private void spawnIDSealScene01() {
		//Advance into Drakenspire Depths with your allies.
		sendMsgByRace(1402991, Race.PC_ALL, 10000);
		//Choose a path to proceed.
		sendMsgByRace(1402992, Race.PC_ALL, 60000);
    }
	
	private void spawnIDSealSceneEnding() {
        final int IDSealSceneEndingQuestNPC = sealSceneRaceQ == Race.ASMODIANS ? 209804 : 209739; //Parsia/Masionel.
		final int IDSealSceneEndingPCGuard1 = sealSceneRaceQ == Race.ASMODIANS ? 209807 : 209742;
        final int IDSealSceneEndingPCGuard2 = sealSceneRaceQ == Race.ASMODIANS ? 209807 : 209742;
        final int IDSealSceneEndingPCGuard3 = sealSceneRaceQ == Race.ASMODIANS ? 209807 : 209742;
		final int IDSealSceneEndingPCGuard4 = sealSceneRaceQ == Race.ASMODIANS ? 209807 : 209742;
		spawn(IDSealSceneEndingQuestNPC, 156.73125f, 518.6457f, 1749.5138f, (byte) 0);
		spawn(IDSealSceneEndingPCGuard1, 155.51422f, 514.9752f, 1749.5044f, (byte) 106);
        spawn(IDSealSceneEndingPCGuard2, 155.3807f, 521.9788f, 1749.5118f, (byte) 15);
        spawn(IDSealSceneEndingPCGuard3, 148.80602f, 521.83466f, 1749.516f, (byte) 45);
		spawn(IDSealSceneEndingPCGuard4, 148.60063f, 515.0635f, 1749.5034f, (byte) 75);
    }
	
	private void spawnEmpyreanLordsSiegeWeapon() {
		final int empyreanLordsSiegeWeapon = sealSceneRaceQ == Race.ASMODIANS ? 702720 : 702719; //Empyrean Lord's Siege Weapon.
		spawn(empyreanLordsSiegeWeapon, 635.24457f, 890.4639f, 1600.5914f, (byte) 90);
    }
	
	private void spawnAgonyWell() {
		SpawnTemplate EnvSkyBoxObject = SpawnEngine.addNewSingleTimeSpawn(301520000, 805377, 635.69067f, 959.46039f, 1615.0714f, (byte) 0);
		EnvSkyBoxObject.setEntityId(50);
		objects.put(805377, SpawnEngine.spawnObject(EnvSkyBoxObject, instanceId));
    }
	
	private void spawnWaveDoor() {
	    SpawnTemplate AionFXPostGlow = SpawnEngine.addNewSingleTimeSpawn(301520000, 731581, 635.3889f, 784.05261f, 1596.7184f, (byte) 0);
		AionFXPostGlow.setEntityId(548);
		objects.put(731581, SpawnEngine.spawnObject(AionFXPostGlow, instanceId));
	}
	
	private void moveToSealForward(final Npc npc, float x, float y, float z, boolean despawn) {
		((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
		npc.setState(1);
		npc.getMoveController().moveToPoint(x, y, z);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}
	
	private void spawnIDSeal4ThStageElyos() {
		moveToSealForward((Npc)spawn(209722, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 632.21173f, 887.62164f, 1600.698f, false);
		moveToSealForward((Npc)spawn(209722, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 638.4854f, 889.0385f, 1600.6517f, false);
		moveToSealForward((Npc)spawn(209722, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 638.0168f, 896.49756f, 1600.4114f, false);
		moveToSealForward((Npc)spawn(209722, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 631.50134f, 895.16174f, 1600.5238f, false);
	}
	
	private void spawnIDSeal4ThStageAsmodians() {
		moveToSealForward((Npc)spawn(209787, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 632.21173f, 887.62164f, 1600.698f, false);
		moveToSealForward((Npc)spawn(209787, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 638.4854f, 889.0385f, 1600.6517f, false);
		moveToSealForward((Npc)spawn(209787, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 638.0168f, 896.49756f, 1600.4114f, false);
		moveToSealForward((Npc)spawn(209787, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 631.50134f, 895.16174f, 1600.5238f, false);
	}
	
	private void raidSeal(final Npc npc) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					for (Player player: instance.getPlayersInside()) {
						npc.setTarget(player);
						((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
						npc.setState(1);
						npc.getMoveController().moveToTargetObject();
						PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
					}
				}
			}
		}, 1000);
	}
	
	public void startRaidSeal1() {
	    raidSeal((Npc)spawn(237217, 632.9971f, 788.14307f, 1596.5493f, (byte) 28));
		raidSeal((Npc)spawn(237217, 637.02356f, 787.7114f, 1596.4082f, (byte) 29));
		raidSeal((Npc)spawn(237218, 686.8446f, 823.1334f, 1610.0796f, (byte) 46));
		raidSeal((Npc)spawn(237218, 689.8247f, 826.4868f, 1610.1107f, (byte) 46));
	}
	
	public void startRaidSeal2() {
	    raidSeal((Npc)spawn(237219, 632.9971f, 788.14307f, 1596.5493f, (byte) 28));
		raidSeal((Npc)spawn(237219, 637.02356f, 787.7114f, 1596.4082f, (byte) 29));
		raidSeal((Npc)spawn(237232, 635.31866f, 789.59717f, 1596.6062f, (byte) 30));
	}
	
	public void startRaidSeal2_1() {
		raidSeal((Npc)spawn(237219, 632.9971f, 788.14307f, 1596.5493f, (byte) 28));
		raidSeal((Npc)spawn(237219, 637.02356f, 787.7114f, 1596.4082f, (byte) 29));
		raidSeal((Npc)spawn(237219, 635.31866f, 789.59717f, 1596.6062f, (byte) 30));
	}
	
	public void startRaidSeal3() {
	    raidSeal((Npc)spawn(237217, 686.8446f, 823.1334f, 1610.0796f, (byte) 46));
		raidSeal((Npc)spawn(237217, 689.8247f, 826.4868f, 1610.1107f, (byte) 46));
		raidSeal((Npc)spawn(237217, 579.17377f, 823.6274f, 1609.9344f, (byte) 14));
		raidSeal((Npc)spawn(237217, 582.5257f, 820.4343f, 1609.9154f, (byte) 15));
	}
	
	public void startRaidSeal4() {
		raidSeal((Npc)spawn(237217, 632.9971f, 788.14307f, 1596.5493f, (byte) 28));
		raidSeal((Npc)spawn(237218, 637.02356f, 787.7114f, 1596.4082f, (byte) 29));
		raidSeal((Npc)spawn(237235, 635.31866f, 789.59717f, 1596.6062f, (byte) 30));
	}
	
	public void startRaidSeal5() {
		raidSeal((Npc)spawn(237219, 574.2141f, 879.9431f, 1600.7627f, (byte) 0));
		raidSeal((Npc)spawn(237219, 574.22955f, 875.5384f, 1601.1173f, (byte) 0));
		raidSeal((Npc)spawn(237220, 703.2663f, 874.8648f, 1604.521f, (byte) 59));
		raidSeal((Npc)spawn(237220, 703.3903f, 880.198f, 1604.7985f, (byte) 62));
		raidSeal((Npc)spawn(237233, 702.1438f, 877.7707f, 1604.4375f, (byte) 59));
	}
	
	public void startRaidSeal6() {
		raidSeal((Npc)spawn(237218, 576.9567f, 939.24054f, 1620.987f, (byte) 104));
		raidSeal((Npc)spawn(237218, 573.4911f, 935.99945f, 1621.0607f, (byte) 104));
		raidSeal((Npc)spawn(237234, 575.50415f, 937.4111f, 1620.9528f, (byte) 106));
		raidSeal((Npc)spawn(237218, 687.77435f, 933.2536f, 1617.8989f, (byte) 68));
		raidSeal((Npc)spawn(237218, 689.8956f, 929.0121f, 1617.4075f, (byte) 68));
		raidSeal((Npc)spawn(237236, 688.66656f, 931.058f, 1617.5339f, (byte) 72));
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
	
	private void sendMessage(final int msgId, long delay) {
        if (delay == 0) {
            this.sendMsg(msgId);
        } else {
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                public void run() {
                    sendMsg(msgId);
                }
            }, delay);
        }
    }
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	protected Npc getNpc(int npcId) {
		if (!isInstanceDestroyed) {
			return instance.getNpc(npcId);
		}
		return null;
	}
	
	protected void killNpc(List<Npc> npcs) {
        for (Npc npc: npcs) {
            npc.getController().die();
        }
    }
	
	protected List<Npc> getNpcs(int npcId) {
		if (!isInstanceDestroyed) {
			return instance.getNpcs(npcId);
		}
		return null;
	}
	
	private boolean isDead(Npc npc) {
		return (npc == null || npc.getLifeStats().isAlreadyDead());
	}
	
	@Override
	public void onInstanceDestroy() {
		doors.clear();
		isInstanceDestroyed = true;
	}
	
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
	private void sendMovie(Player player, int movie) {
        if (!movies.contains(movie)) {
             movies.add(movie);
             PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
        }
    }
}