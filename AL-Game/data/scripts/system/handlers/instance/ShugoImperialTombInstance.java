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
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastList;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/** Source: https://www.youtube.com/watch?v=UYCLUuaLVGI + http://aionpowerbook.com/powerbook/Shugo_Imperial_Tomb
/****/

@InstanceID(300560000)
public class ShugoImperialTombInstance extends GeneralInstanceHandler
{
    private Future<?> tombRaidTaskA1;
	private Future<?> tombRaidTaskB1;
	private Future<?> tombRaidTaskC1;
	private Future<?> tombRaidTaskC2;
	/////////////////////////////////
	private int strongKoboldWorker;
	private int diligentKoboldWorker;
	private int swiftKrallGraverobber;
	private int krallLookoutCommander;
	private boolean isInstanceDestroyed;
	private final FastList<Future<?>> imperialTombTask = FastList.newInstance();
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 219508: //Diligent Kobold Worker.
			case 219514: //Strong Kobold Worker.
			case 219521: //Swift Krall Graverobber.
			case 219528: //Krall Lookout Commander.
			    for (Player player: instance.getPlayersInside()) {
					if (player.isOnline()) {
					    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002160, 1)); //Repair.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002158, 1)); //Cursed Chill.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002157, 1)); //Powerful Trickster's Essence.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002156, 1)); //Trickster's Essence.
						switch (Rnd.get(1, 4)) {
					        case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100087, 1)); //Treasure Room Map Piece 1.
				            break;
					        case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100088, 1)); //Treasure Room Map Piece 2.
				            break;
					        case 3:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100089, 1)); //Treasure Room Map Piece 3.
				            break;
					        case 4:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100090, 1)); //Treasure Room Map Piece 4.
				            break;
						}
					}
				}
			break;
			case 219530: //Letu Erezat.
			case 219531: //Captain Lediar.
			case 219544: //Awakened Guardian.
			    for (Player player: instance.getPlayersInside()) {
					if (player.isOnline()) {
					    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002160, 1)); //Repair.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002158, 1)); //Cursed Chill.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002157, 1)); //Powerful Trickster's Essence.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002156, 1)); //Trickster's Essence.
						switch (Rnd.get(1, 4)) {
					        case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100087, 1)); //Treasure Room Map Piece 1.
				            break;
					        case 2:
						        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100088, 1)); //Treasure Room Map Piece 2.
				            break;
					        case 3:
						        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100089, 1)); //Treasure Room Map Piece 3.
						    break;
						    case 4:
						        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100090, 1)); //Treasure Room Map Piece 4.
						    break;
						} switch (Rnd.get(1, 2)) {
						    case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000129, 1)); //Common Treasure Chest Key.
						    break;
						    case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000129, 2)); //Common Treasure Chest Key.
						    break;
						}
					}
				}
			break;
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		spawn(831110, 183.95969f, 237.51074f, 536.16974f, (byte) 71); //Crown Prince's Admirer.
		spawn(831095, 218.27571f, 287.24326f, 550.68805f, (byte) 74); //Shugo Warrior Transformation Device.
	}
	
	@Override
	public void onDie(Npc npc) {
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 219508: //Diligent Kobold Worker.
			    diligentKoboldWorker++;
				if (diligentKoboldWorker == 6) {
					startTombRaidA1_1();
					//A second wave of pillagers will arrive in 10 seconds!
					sendMsgByRace(1401586, Race.PC_ALL, 0);
					//Hold a little longer and you will survive.
				    sendMsgByRace(1402833, Race.PC_ALL, 5000);
				    //Only a few enemies left!
				    sendMsgByRace(1402834, Race.PC_ALL, 10000);
				} else if (diligentKoboldWorker == 20) {
					tombRaidTaskA1.cancel(true);
					spawn(831095, 344.28635f, 425.418f, 294.75867f, (byte) 56); //Shugo Warrior Transformation Device.
					spawn(831114, 183.95969f, 237.51074f, 536.16974f, (byte) 71); //Crown Prince's Delighted Admirer.
					spawn(831111, 340.27893f, 426.2435f, 294.7574f, (byte) 56); //Empress' Admirer.
				}
			break;
			case 219514: //Strong Kobold Worker.
			    strongKoboldWorker++;
				if (strongKoboldWorker == 6) {
					startTombRaidB1_1();
					//A second wave of pillagers will arrive in 10 seconds!
					sendMsgByRace(1401586, Race.PC_ALL, 0);
					//Hold a little longer and you will survive.
				    sendMsgByRace(1402833, Race.PC_ALL, 5000);
				    //Only a few enemies left!
				    sendMsgByRace(1402834, Race.PC_ALL, 10000);
				} else if (strongKoboldWorker == 40) {
					spawnFairyGuardian();
					tombRaidTaskB1.cancel(true);
					//Prepare for combat! More enemies swarming in!
					sendMsgByRace(1402832, Race.PC_ALL, 0);
				}
			break;
			case 219521: //Swift Krall Graverobber.
			    swiftKrallGraverobber++;
				if (swiftKrallGraverobber == 6) {
					startTombRaidC1_1();
					//A second wave of pillagers will arrive in 10 seconds!
					sendMsgByRace(1401586, Race.PC_ALL, 0);
					//Hold a little longer and you will survive.
				    sendMsgByRace(1402833, Race.PC_ALL, 5000);
				    //Only a few enemies left!
				    sendMsgByRace(1402834, Race.PC_ALL, 10000);
				} else if (swiftKrallGraverobber == 30) {
					startLetuErezat();
					tombRaidTaskC1.cancel(true);
					//Hold a little longer and you will survive.
				    sendMsgByRace(1402833, Race.PC_ALL, 5000);
				    //Only a few enemies left!
				    sendMsgByRace(1402834, Race.PC_ALL, 10000);
				}
			break;
			case 219528: //Krall Lookout Commander.
			    krallLookoutCommander++;
			    if (krallLookoutCommander == 30) {
					startCaptainLediar();
					//Hold a little longer and you will survive.
				    sendMsgByRace(1402833, Race.PC_ALL, 5000);
				    //Only a few enemies left!
				    sendMsgByRace(1402834, Race.PC_ALL, 10000);
					tombRaidTaskC2.cancel(true);
				}
			break;
			case 219530: //Letu Erezat.
			    startTombRaidC1_2();
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 0);
				//Only a few enemies left!
				sendMsgByRace(1402834, Race.PC_ALL, 5000);
			break;
			case 219531: //Captain Lediar.
			    deleteNpc(831130); //Crown Prince's Monument.
			    spawn(831116, 443.322f, 110.39832f, 212.20023f, (byte) 92); //Emperor's Delighted Admirer.
				spawn(831119, 440.2393f, 109.80865f, 212.20023f, (byte) 94); //Marayrinerk.
				spawn(831350, 452.43765f, 106.14462f, 212.20023f, (byte) 68); //Imperial Shrine.
			break;
			case 219544: //Awakened Guardian.
			    spawn(831095, 465.13556f, 111.26043f, 214.702f, (byte) 8); //Shugo Warrior Transformation Device.
				spawn(831115, 329.33588f, 432.96265f, 294.76144f, (byte) 100); //Empress's Delighted Admirer.
			    spawn(831112, 452.43765f, 106.14462f, 212.20023f, (byte) 68); //Emperor's Admirer.
			break;
		}
	}
	
	private void spawnFairyGuardian() {
		spawn(219544, 315.94565f, 431.73035f, 294.58875f, (byte) 116);
        spawn(219505, 314.94418f, 428.22006f, 294.58875f, (byte) 115);
        spawn(219505, 318.11328f, 427.66050f, 294.58875f, (byte) 115);
        spawn(219505, 319.69778f, 434.42917f, 294.58875f, (byte) 115);
        spawn(219505, 316.08636f, 435.35806f, 294.58875f, (byte) 115);
        spawn(219505, 321.05954f, 430.44263f, 294.58875f, (byte) 115);
	}
	
   /**
	* TOMB RAID A
	*/
	private void startTombRaidA1_1() {
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219508, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 1000, "ImperialTombUnderpath1");
		        sp(219509, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 5000, "ImperialTombUnderpath1");
		        sp(219510, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 9000, "ImperialTombUnderpath1");
		        sp(219508, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 1000, "ImperialTombUnderpath2");
		        sp(219509, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 5000, "ImperialTombUnderpath2");
		        sp(219510, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 9000, "ImperialTombUnderpath2");
			}
		}, 10000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219508, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 1000, "ImperialTombUnderpath1");
		        sp(219509, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 5000, "ImperialTombUnderpath1");
		        sp(219510, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 9000, "ImperialTombUnderpath1");
		        sp(219508, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 1000, "ImperialTombUnderpath2");
		        sp(219509, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 5000, "ImperialTombUnderpath2");
		        sp(219510, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 9000, "ImperialTombUnderpath2");
			}
		}, 30000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219508, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 1000, "ImperialTombUnderpath1");
		        sp(219509, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 5000, "ImperialTombUnderpath1");
		        sp(219510, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 9000, "ImperialTombUnderpath1");
		        sp(219508, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 1000, "ImperialTombUnderpath2");
		        sp(219509, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 5000, "ImperialTombUnderpath2");
		        sp(219510, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 9000, "ImperialTombUnderpath2");
			}
		}, 50000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219508, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 1000, "ImperialTombUnderpath1");
		        sp(219509, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 5000, "ImperialTombUnderpath1");
		        sp(219510, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 9000, "ImperialTombUnderpath1");
		        sp(219508, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 1000, "ImperialTombUnderpath2");
		        sp(219509, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 5000, "ImperialTombUnderpath2");
		        sp(219510, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 9000, "ImperialTombUnderpath2");
			}
		}, 70000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219508, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 1000, "ImperialTombUnderpath1");
		        sp(219509, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 5000, "ImperialTombUnderpath1");
		        sp(219510, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 9000, "ImperialTombUnderpath1");
		        sp(219508, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 1000, "ImperialTombUnderpath2");
		        sp(219509, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 5000, "ImperialTombUnderpath2");
		        sp(219510, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 9000, "ImperialTombUnderpath2");
			}
		}, 90000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219508, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 1000, "ImperialTombUnderpath1");
		        sp(219509, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 5000, "ImperialTombUnderpath1");
		        sp(219510, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 9000, "ImperialTombUnderpath1");
		        sp(219508, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 1000, "ImperialTombUnderpath2");
		        sp(219509, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 5000, "ImperialTombUnderpath2");
		        sp(219510, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 9000, "ImperialTombUnderpath2");
			}
		}, 110000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219508, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 1000, "ImperialTombUnderpath1");
		        sp(219509, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 5000, "ImperialTombUnderpath1");
		        sp(219510, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 9000, "ImperialTombUnderpath1");
		        sp(219508, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 1000, "ImperialTombUnderpath2");
		        sp(219509, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 5000, "ImperialTombUnderpath2");
		        sp(219510, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 9000, "ImperialTombUnderpath2");
			}
		}, 130000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219508, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 1000, "ImperialTombUnderpath1");
		        sp(219509, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 5000, "ImperialTombUnderpath1");
		        sp(219510, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 9000, "ImperialTombUnderpath1");
		        sp(219508, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 1000, "ImperialTombUnderpath2");
		        sp(219509, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 5000, "ImperialTombUnderpath2");
		        sp(219510, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 9000, "ImperialTombUnderpath2");
			}
		}, 150000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219508, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 1000, "ImperialTombUnderpath1");
		        sp(219509, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 5000, "ImperialTombUnderpath1");
		        sp(219510, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 9000, "ImperialTombUnderpath1");
		        sp(219508, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 1000, "ImperialTombUnderpath2");
		        sp(219509, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 5000, "ImperialTombUnderpath2");
		        sp(219510, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 9000, "ImperialTombUnderpath2");
			}
		}, 170000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219508, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 1000, "ImperialTombUnderpath1");
		        sp(219509, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 5000, "ImperialTombUnderpath1");
		        sp(219510, 199.53075f, 270.43457f, 550.5646f, (byte) 77, 9000, "ImperialTombUnderpath1");
		        sp(219508, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 1000, "ImperialTombUnderpath2");
		        sp(219509, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 5000, "ImperialTombUnderpath2");
		        sp(219510, 209.68540f, 263.57240f, 550.5646f, (byte) 78, 9000, "ImperialTombUnderpath2");
			}
		}, 190000);
	}
	
   /**
	* TOMB RAID B
	*/
	private void startTombRaidB1_1() {
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219514, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 1000, "ImperialTombUnderpath3");
		        sp(219515, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 5000, "ImperialTombUnderpath3");
		        sp(219516, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 9000, "ImperialTombUnderpath3");
		        sp(219514, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 1000, "ImperialTombUnderpath4");
		        sp(219515, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 5000, "ImperialTombUnderpath4");
		        sp(219516, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 9000, "ImperialTombUnderpath4");
		        sp(219514, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 1000, "ImperialTombUnderpath5");
		        sp(219515, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 5000, "ImperialTombUnderpath5");
		        sp(219516, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 9000, "ImperialTombUnderpath5");
		        sp(219514, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 1000, "ImperialTombUnderpath6");
		        sp(219515, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 5000, "ImperialTombUnderpath6");
		        sp(219516, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 9000, "ImperialTombUnderpath6");
			}
		}, 10000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219514, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 1000, "ImperialTombUnderpath3");
		        sp(219515, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 5000, "ImperialTombUnderpath3");
		        sp(219516, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 9000, "ImperialTombUnderpath3");
		        sp(219514, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 1000, "ImperialTombUnderpath4");
		        sp(219515, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 5000, "ImperialTombUnderpath4");
		        sp(219516, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 9000, "ImperialTombUnderpath4");
		        sp(219514, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 1000, "ImperialTombUnderpath5");
		        sp(219515, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 5000, "ImperialTombUnderpath5");
		        sp(219516, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 9000, "ImperialTombUnderpath5");
		        sp(219514, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 1000, "ImperialTombUnderpath6");
		        sp(219515, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 5000, "ImperialTombUnderpath6");
		        sp(219516, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 9000, "ImperialTombUnderpath6");
			}
		}, 30000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219514, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 1000, "ImperialTombUnderpath3");
		        sp(219515, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 5000, "ImperialTombUnderpath3");
		        sp(219516, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 9000, "ImperialTombUnderpath3");
		        sp(219514, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 1000, "ImperialTombUnderpath4");
		        sp(219515, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 5000, "ImperialTombUnderpath4");
		        sp(219516, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 9000, "ImperialTombUnderpath4");
		        sp(219514, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 1000, "ImperialTombUnderpath5");
		        sp(219515, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 5000, "ImperialTombUnderpath5");
		        sp(219516, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 9000, "ImperialTombUnderpath5");
		        sp(219514, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 1000, "ImperialTombUnderpath6");
		        sp(219515, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 5000, "ImperialTombUnderpath6");
		        sp(219516, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 9000, "ImperialTombUnderpath6");
			}
		}, 50000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219514, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 1000, "ImperialTombUnderpath3");
		        sp(219515, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 5000, "ImperialTombUnderpath3");
		        sp(219516, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 9000, "ImperialTombUnderpath3");
		        sp(219514, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 1000, "ImperialTombUnderpath4");
		        sp(219515, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 5000, "ImperialTombUnderpath4");
		        sp(219516, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 9000, "ImperialTombUnderpath4");
		        sp(219514, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 1000, "ImperialTombUnderpath5");
		        sp(219515, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 5000, "ImperialTombUnderpath5");
		        sp(219516, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 9000, "ImperialTombUnderpath5");
		        sp(219514, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 1000, "ImperialTombUnderpath6");
		        sp(219515, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 5000, "ImperialTombUnderpath6");
		        sp(219516, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 9000, "ImperialTombUnderpath6");
			}
		}, 70000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219514, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 1000, "ImperialTombUnderpath3");
		        sp(219515, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 5000, "ImperialTombUnderpath3");
		        sp(219516, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 9000, "ImperialTombUnderpath3");
		        sp(219514, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 1000, "ImperialTombUnderpath4");
		        sp(219515, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 5000, "ImperialTombUnderpath4");
		        sp(219516, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 9000, "ImperialTombUnderpath4");
		        sp(219514, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 1000, "ImperialTombUnderpath5");
		        sp(219515, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 5000, "ImperialTombUnderpath5");
		        sp(219516, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 9000, "ImperialTombUnderpath5");
		        sp(219514, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 1000, "ImperialTombUnderpath6");
		        sp(219515, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 5000, "ImperialTombUnderpath6");
		        sp(219516, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 9000, "ImperialTombUnderpath6");
			}
		}, 90000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219514, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 1000, "ImperialTombUnderpath3");
		        sp(219515, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 5000, "ImperialTombUnderpath3");
		        sp(219516, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 9000, "ImperialTombUnderpath3");
		        sp(219514, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 1000, "ImperialTombUnderpath4");
		        sp(219515, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 5000, "ImperialTombUnderpath4");
		        sp(219516, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 9000, "ImperialTombUnderpath4");
		        sp(219514, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 1000, "ImperialTombUnderpath5");
		        sp(219515, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 5000, "ImperialTombUnderpath5");
		        sp(219516, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 9000, "ImperialTombUnderpath5");
		        sp(219514, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 1000, "ImperialTombUnderpath6");
		        sp(219515, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 5000, "ImperialTombUnderpath6");
		        sp(219516, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 9000, "ImperialTombUnderpath6");
			}
		}, 110000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219514, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 1000, "ImperialTombUnderpath3");
		        sp(219515, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 5000, "ImperialTombUnderpath3");
		        sp(219516, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 9000, "ImperialTombUnderpath3");
		        sp(219514, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 1000, "ImperialTombUnderpath4");
		        sp(219515, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 5000, "ImperialTombUnderpath4");
		        sp(219516, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 9000, "ImperialTombUnderpath4");
		        sp(219514, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 1000, "ImperialTombUnderpath5");
		        sp(219515, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 5000, "ImperialTombUnderpath5");
		        sp(219516, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 9000, "ImperialTombUnderpath5");
		        sp(219514, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 1000, "ImperialTombUnderpath6");
		        sp(219515, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 5000, "ImperialTombUnderpath6");
		        sp(219516, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 9000, "ImperialTombUnderpath6");
			}
		}, 130000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219514, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 1000, "ImperialTombUnderpath3");
		        sp(219515, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 5000, "ImperialTombUnderpath3");
		        sp(219516, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 9000, "ImperialTombUnderpath3");
		        sp(219514, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 1000, "ImperialTombUnderpath4");
		        sp(219515, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 5000, "ImperialTombUnderpath4");
		        sp(219516, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 9000, "ImperialTombUnderpath4");
		        sp(219514, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 1000, "ImperialTombUnderpath5");
		        sp(219515, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 5000, "ImperialTombUnderpath5");
		        sp(219516, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 9000, "ImperialTombUnderpath5");
		        sp(219514, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 1000, "ImperialTombUnderpath6");
		        sp(219515, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 5000, "ImperialTombUnderpath6");
		        sp(219516, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 9000, "ImperialTombUnderpath6");
			}
		}, 150000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219514, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 1000, "ImperialTombUnderpath3");
		        sp(219515, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 5000, "ImperialTombUnderpath3");
		        sp(219516, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 9000, "ImperialTombUnderpath3");
		        sp(219514, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 1000, "ImperialTombUnderpath4");
		        sp(219515, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 5000, "ImperialTombUnderpath4");
		        sp(219516, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 9000, "ImperialTombUnderpath4");
		        sp(219514, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 1000, "ImperialTombUnderpath5");
		        sp(219515, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 5000, "ImperialTombUnderpath5");
		        sp(219516, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 9000, "ImperialTombUnderpath5");
		        sp(219514, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 1000, "ImperialTombUnderpath6");
		        sp(219515, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 5000, "ImperialTombUnderpath6");
		        sp(219516, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 9000, "ImperialTombUnderpath6");
			}
		}, 170000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219514, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 1000, "ImperialTombUnderpath3");
		        sp(219515, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 5000, "ImperialTombUnderpath3");
		        sp(219516, 307.80344f, 434.2390f, 298.31903f, (byte) 25, 9000, "ImperialTombUnderpath3");
		        sp(219514, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 1000, "ImperialTombUnderpath4");
		        sp(219515, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 5000, "ImperialTombUnderpath4");
		        sp(219516, 307.02597f, 433.8582f, 298.31903f, (byte) 88, 9000, "ImperialTombUnderpath4");
		        sp(219514, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 1000, "ImperialTombUnderpath5");
		        sp(219515, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 5000, "ImperialTombUnderpath5");
		        sp(219516, 359.85450f, 421.5649f, 292.48206f, (byte) 30, 9000, "ImperialTombUnderpath5");
		        sp(219514, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 1000, "ImperialTombUnderpath6");
		        sp(219515, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 5000, "ImperialTombUnderpath6");
		        sp(219516, 359.61240f, 421.5032f, 292.48206f, (byte) 82, 9000, "ImperialTombUnderpath6");
			}
		}, 190000);
	}
	
   /**
	* TOMB RAID C-1
	*/
	private void startTombRaidC1_1() {
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219521, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219522, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219523, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219521, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219522, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219523, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219521, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219522, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219523, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 10000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219521, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219522, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219523, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219521, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219522, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219523, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219521, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219522, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219523, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 30000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219521, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219522, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219523, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219521, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219522, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219523, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219521, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219522, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219523, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 50000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219521, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219522, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219523, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219521, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219522, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219523, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219521, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219522, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219523, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 70000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219521, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219522, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219523, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219521, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219522, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219523, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219521, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219522, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219523, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 90000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219521, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219522, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219523, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219521, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219522, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219523, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219521, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219522, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219523, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 110000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219521, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219522, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219523, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219521, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219522, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219523, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219521, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219522, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219523, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 130000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219521, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219522, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219523, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219521, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219522, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219523, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219521, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219522, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219523, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 150000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219521, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219522, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219523, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219521, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219522, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219523, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219521, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219522, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219523, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 170000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219521, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219522, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219523, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219521, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219522, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219523, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219521, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219522, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219523, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 190000);
	}
	
   /**
	* TOMB RAID C-2
	*/
	private void startTombRaidC1_2() {
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219527, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219528, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219529, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219527, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219528, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219529, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219527, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219528, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219529, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 10000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219527, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219528, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219529, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219527, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219528, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219529, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219527, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219528, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219529, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 30000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219527, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219528, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219529, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219527, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219528, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219529, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219527, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219528, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219529, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 50000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219527, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219528, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219529, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219527, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219528, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219529, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219527, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219528, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219529, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 70000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219527, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219528, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219529, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219527, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219528, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219529, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219527, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219528, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219529, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 90000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219527, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219528, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219529, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219527, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219528, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219529, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219527, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219528, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219529, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 110000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219527, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219528, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219529, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219527, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219528, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219529, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219527, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219528, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219529, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 130000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219527, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219528, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219529, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219527, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219528, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219529, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219527, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219528, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219529, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 150000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				sp(219527, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219528, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219529, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219527, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219528, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219529, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219527, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219528, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219529, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 170000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
				sp(219527, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath7");
		        sp(219528, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath7");
		        sp(219529, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath7");
		        sp(219527, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 1000, "ImperialTombUnderpath8");
		        sp(219528, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 5000, "ImperialTombUnderpath8");
		        sp(219529, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 9000, "ImperialTombUnderpath8");
		        sp(219527, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 1000, "ImperialTombUnderpath9");
		        sp(219528, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 5000, "ImperialTombUnderpath9");
		        sp(219529, 419.37616f, 90.95251f, 214.33856f, (byte) 8, 9000, "ImperialTombUnderpath9");
			}
		}, 190000);
	}
	private void startLetuErezat() {
		sp(219530, 398.80435f, 81.94784f, 223.16089f, (byte) 8, 2000, "ImperialTombUnderpath7"); //Letu Erezat.
	}
	private void startCaptainLediar() {
		sp(219531, 398.66214f, 81.80799f, 223.16089f, (byte) 8, 2000, "ImperialTombUnderpath8"); //Captain Lediar.
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 831095: //Shugo Warrior Transformation Device.
				SkillEngine.getInstance().getSkill(npc, 21096, 60, player).useNoAnimationSkill();
			break;
		}
	}
	
	public void removeItems(Player player) {
        Storage storage = player.getInventory();
        storage.decreaseByItemId(182006989, storage.getItemCountByItemId(182006989)); //Emperor's Golden Tag.
		storage.decreaseByItemId(182006990, storage.getItemCountByItemId(182006990)); //Empress' Silver Tag.
        storage.decreaseByItemId(182006991, storage.getItemCountByItemId(182006991)); //Crown Prince's Brass Tag.
		storage.decreaseByItemId(182006999, storage.getItemCountByItemId(182006999)); //Shugo Coin.
    }
	
	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21096);
	}
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
		removeEffects(player);
		//"Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
	}
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
		removeEffects(player);
	}
	
	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendWhiteMessageOnCenter(player, str);
			}
		});
	}
	
	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
	}
	
	private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = imperialTombTask.head(), end = imperialTombTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
        imperialTombTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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