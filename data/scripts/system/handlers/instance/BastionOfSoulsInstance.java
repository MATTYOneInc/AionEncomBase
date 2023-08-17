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
import com.aionemu.commons.network.util.ThreadPoolManager;

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
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/** Source: http://aionpowerbook.com/powerbook/Narakali
/** Video: 1 https://www.youtube.com/watch?v=xO20tT7-iSM
/** Video: 2 https://www.youtube.com/watch?v=o6x3MxpiG9Q#t=229.183915
/****/

@InstanceID(302340000)
public class BastionOfSoulsInstance extends GeneralInstanceHandler
{
	private int prisonIce;
	private int bossWitch;
	private int prisonCore;
	private Race spawnRace;
	private Race videoRace;
	private long startTime;
	private int IDAb1EreWave;
	private long instanceTime;
	private int startDrakanHigh;
	private int bridgeDrakanHigh;
	private Future<?> instanceTimer;
	//Boss Wave.
	private Future<?> bastionTaskA1;
	private Future<?> bastionTaskA2;
	private Future<?> bastionTaskA3;
	private Future<?> bastionTaskA4;
	private Future<?> bastionTaskA5;
	private Future<?> bastionTaskA6;
	private Future<?> bastionTaskA7;
	private Future<?> bastionTaskA8;
	private Future<?> bastionTaskA9;
	private Future<?> bastionTaskA10;
	private Future<?> bastionTaskA11;
	private Future<?> bastionTaskA12;
	private boolean isInstanceDestroyed;
	private Map<Integer, StaticDoor> doors;
	private List<Integer> movies = new ArrayList<Integer>();
	private final FastList<Future<?>> bastionTask = FastList.newInstance();
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 835484: //Faded Bastion Of Souls Treasure Chest.
			case 835485: //Simple Bastion Of Souls Treasure Chest.
			case 835486: //Ornate Bastion Of Souls Treasure Chest.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188058413, 1)); //ì?´ê³„ ì•”ë£¡ì?˜ ë¬´ê¸° ìƒ?ìž?.
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 152012766, 4)); //material_idere_evolution_01.
				        switch (Rnd.get(1, 4)) {
					        case 1:
						        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188058361, 1)); //Harvester's Armor Box.
					        break;
					        case 2:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188058362, 1)); //Harvester's Weapon Box.
					        break;
							case 3:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188058363, 1)); //Harvester's Accessory Box.
							break;
							case 4:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188058364, 1)); //Harvester's Hat Box.
							break;
						} switch (Rnd.get(1, 7)) {
							case 1:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188055498, 1)); //Tike Manastone Bundle: +8.
							break;
							case 2:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188057819, 1)); //Legendary Illusion Godstone Bundle.
							break;
							case 3:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190080005, 5)); //Lesser Minion Contract.
							break;
							case 4:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190080006, 5)); //Greater Minion Contract.
							break;
							case 5:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190080007, 5)); //Major Minion Contract.
							break;
							case 6:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190080008, 5)); //Cute Minion Contract.
							break;
							case 7:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190200000, 150)); //Minium.
							break;
						}
					}
				}
			break;
			case 246727: //Sahidtan.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000308, 1)); //Meditation Chamber Key.
			break;
			case 246730: //Manusha.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000309, 1)); //Lift To Enlightenment Key.
			break;
			case 246731: //Kramush.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000310, 1)); //Enlightenment Key.
			break;
			case 246798: //Kargata.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000311, 5)); //Black Oil.
			break;
			case 246881: //Harvesters Drakan Butcher Captain.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002394, 5)); //Jail Key.
			break;
			case 246885: //Harvesters Drakan Slaughterer.
			case 247181: //Harvesters Drakan Slaughterer.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000302, 1)); //Purification Chamber Key - 4.
			break;
			case 246895: //Harvesters Drakan Slaughterer.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000303, 1)); //Purification Chamber Key - 3.
			break;
			case 246905: //Harvesters Drakan Slaughterer.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000304, 1)); //Purification Chamber Key - 2.
			break;
        }
    }
	
	@Override
	public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		//Daeva Rescue.
		sendPacket(player, "UI_Gauge_01", 0 + 1);
		//Destroy The Generator.
		sendPacket(player, "UI_Gauge_02", 0 + 1);
		//Additional Aid.
		sendPacket(player, "UI_Gauge_03", 0 + 1);
		if (spawnRace == null) {
			spawnRace = player.getRace();
			SpawnBastionRace();
		}
	}
	
	private void startRescueDaevaTimer() {
		//The additional missions that you carry out in the 15 minutes of the Daeva rescue mission can change the difficulty and the rewards for the Bastion of Souls.
		//If the rescue mission ends after 15 minutes or the Warden Mahorosh appears, the difficulty and the rewards stay the same.
		sendMsgByRace(1404231, Race.PC_ALL, 30000);
		//The additional mission "Finding a Friend" has failed.
		sendMsgByRace(1404298, Race.PC_ALL, 905000);
		//10 minutes are left for the Daeva rescue mission.
		this.sendMessage(1404232, 5 * 60 * 1000);
		//5 minutes are left for the Daeva rescue mission.
		this.sendMessage(1404233, 10 * 60 * 1000);
		//1 minute is left for the Daeva rescue mission.
		this.sendMessage(1404234, 14 * 60 * 1000);
		//The Daeva rescue mission is over.
		//Any additional missions carried out now will have no influence on the Bastion of Souls difficulty or rewards.
		this.sendMessage(1404235, 15 * 60 * 1000);
    }
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		spawnBastionRings();
		startBatiskanBastiel();
		doors = instance.getDoors();
		//Harvesters Drakan Butcher Captain.
		switch (Rnd.get(1, 9)) {
			case 1:
				spawn(246881, 1269.2292f, 758.1833f, 410.23138f, (byte) 102);
			break;
			case 2:
				spawn(246881, 1216.3656f, 585.80774f, 430.61932f, (byte) 46);
			break;
			case 3:
				spawn(246881, 1283.8224f, 760.045f, 412.38046f, (byte) 99);
			break;
			case 4:
				spawn(246881, 1183.9055f, 860.2695f, 400.96716f, (byte) 30);
			break;
			case 5:
				spawn(246881, 1161.5554f, 615.48553f, 426.54364f, (byte) 30);
			break;
			case 6:
				spawn(246881, 1284.129f, 698.8427f, 412.38046f, (byte) 30);
			break;
			case 7:
				spawn(246881, 1191.202f, 796.9921f, 403.94336f, (byte) 18);
			break;
			case 8:
				spawn(246881, 1168.3768f, 801.82306f, 403.94336f, (byte) 119);
			break;
			case 9:
				spawn(246881, 1172.8901f, 660.59076f, 427.28555f, (byte) 104);
			break;
		}
		//Harvesters Drakan Slaughterer.
		switch (Rnd.get(1, 6)) {
			case 1:
				spawn(246885, 1151.6058f, 633.95593f, 424.51465f, (byte) 0);
			break;
			case 2:
				spawn(246885, 1213.9562f, 634.6267f, 431.6261f, (byte) 90);
			break;
			case 3:
				spawn(246885, 1152.1942f, 550.33075f, 432.20688f, (byte) 15);
			break;
			case 4:
				spawn(246885, 1213.8907f, 551.93286f, 430.61932f, (byte) 44);
			break;
			case 5:
				spawn(246885, 1167.3315f, 637.53217f, 424.51465f, (byte) 60);
			break;
			case 6:
				spawn(246885, 1159.9093f, 659.30707f, 424.51465f, (byte) 90);
			break;
		}
		//Harvesters Drakan Slaughterer.
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(246895, 1270.3204f, 736.34894f, 410.23138f, (byte) 30);
			break;
			case 2:
				spawn(246895, 1283.079f, 729.1816f, 413.41144f, (byte) 0);
			break;
			case 3:
				spawn(246895, 1357.6373f, 729.20496f, 416.01593f, (byte) 62);
			break;
		}
		//Harvesters Drakan Slaughterer.
		switch (Rnd.get(1, 6)) {
			case 1:
				spawn(246905, 1159.7638f, 813.6465f, 405.2975f, (byte) 78);
			break;
			case 2:
				spawn(246905, 1208.8795f, 814.61975f, 405.2975f, (byte) 106);
			break;
			case 3:
				spawn(246905, 1156.4237f, 843.0972f, 405.2975f, (byte) 19);
			break;
			case 4:
				spawn(246905, 1156.4637f, 822.4204f, 405.2975f, (byte) 20);
			break;
			case 5:
				spawn(246905, 1150.1736f, 886.94727f, 400.19974f, (byte) 113);
			break;
			case 6:
				spawn(246905, 1218.5275f, 889.6074f, 399.86673f, (byte) 63);
			break;
		}
	}
	
	@Override
    public boolean onPassFlyingRing(Player player, String flyingRing) {
        if (flyingRing.equals("BASTION_OF_SOULS")) {
			instance.doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					if (player.isOnline()) {
						bastionToStartRoom(player);
					}
				}
			});
		}
		return false;
	}
	
	private void spawnBastionRings() {
        FlyRing f1 = new FlyRing(new FlyRingTemplate("BASTION_OF_SOULS", mapId,
        new Point3D(1169.6204, 1153.8145, 491.13086),
		new Point3D(1164.8580, 1152.0957, 497.11038),
        new Point3D(1159.7225, 1153.7646, 491.1022), 90), instanceId);
        f1.spawn();
    }
	
	private void bastionToStartRoom(Player player) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendMovie(player, 957);
						}
					}
				});
			}
		}, 3000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				startRescueDaevaTimer();
			}
		}, 60000);
		teleport(player, 1183.3602f, 734.0874f, 433.22742f, (byte) 90);
	}
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 246728: //Shuratna.
			    //The additional mission "Killing Supervisor Shuratna" is complete.
				sendMsgByRace(1404211, Race.PC_ALL, 2000);
				//Due to the additional mission you undertook while you were on the Daeva rescue mission,
				//the difficulty of the Bastion of Souls and its rewards have increased to a higher level.
				sendMsgByRace(1404214, Race.PC_ALL, 120000);
			    sp(207188, 1212.1577f, 855.89954f, 406.49112f, (byte) 0, 3000, 0, null); //Lever Of The Sorting Room.
			break;
			case 246941: //Harvesters Drakan Slaughterer Captain.
			case 246942: //Harvesters Drakan Butcherer.
			case 246943: //Harvesters Drakan Surgeon.
			    startDrakanHigh++;
				if (startDrakanHigh == 3) {
					doors.get(224).setOpen(true);
				}
			break;
			case 246944: //Harvesters Drakan Butcher Captain.
			case 246945: //Harvesters Drakan Practitioner Captain.
			case 246946: //Harvesters Drakan Slaughterer.
			case 246947: //Harvesters Drakan Surgeon.
			case 246948: //Harvesters Drakan Skinner.
			    bridgeDrakanHigh++;
				if (bridgeDrakanHigh == 12) {
					doors.get(225).setOpen(true);
				}
			break;
			case 731796: //Purification Jail.
			    prisonIce++;
				//Daeva Rescue.
				if (prisonIce == 10) {
					//The additional mission "Finding a Friend" is complete.
					sendMsgByRace(1404226, Race.PC_ALL, 2000);
				}
				sendPacket(player, "UI_Gauge_01", 1 + prisonIce);
			break;
			case 247026: //Prison Camp Generator.
			    prisonCore++;
				//Destroy The Generator.
				if (prisonCore == 1) {
					sendPacket(player, "UI_Gauge_02", 1 + 1);
				} else if (prisonCore == 2) {
					sendPacket(player, "UI_Gauge_02", 2 + 1);
				} else if (prisonCore == 3) {
					sendPacket(player, "UI_Gauge_02", 3 + 1);
				} else if (prisonCore == 4) {
					//All Prison Camp generators were removed and Warden Mahorosh has appeared.
					//The additional missions at the Incarna Prison Camp will not influence the difficulty and rewards anymore.
					sendMsgByRace(1404217, Race.PC_ALL, 2000);
					doors.get(115).setOpen(true);
					sendPacket(player, "UI_Gauge_02", 4 + 1);
					switch (Rnd.get(1, 3)) {
						case 1:
						    //Warden Mahorosh is activating the Ice Cannons in the Guidance Chamber.
						    sendMsgByRace(1404236, Race.PC_ALL, 2000);
						    spawn(246544, 1016.8816f, 725.72644f, 387.3341f, (byte) 0); //Unconscious Mahorosh.
						break;
						case 2:
						    //Warden Mahorosh is activating the Ice Cannons in the Guidance Chamber.
						    sendMsgByRace(1404236, Race.PC_ALL, 2000);
						    spawn(246545, 1016.8816f, 725.72644f, 387.3341f, (byte) 0); //Enraged Mahorosh.
						break;
						case 3:
						    //Warden Mahorosh is activating the Ice Cannons in the Guidance Chamber.
						    sendMsgByRace(1404236, Race.PC_ALL, 2000);
						    spawn(246546, 1016.8816f, 725.72644f, 387.3341f, (byte) 0); //Cold Mahorosh.
						break;
					}
				}
			break;
			case 246544: //Unconscious Mahorosh.
			case 246545: //Enraged Mahorosh.
			case 246546: //Cold Mahorosh.
				doors.get(561).setOpen(true);
				doors.get(796).setOpen(true);
			break;
			case 246520: //Harvesters Klaw Peon.
			case 246521: //Harvesters Klaw Skinner.
			case 246522: //Harvesters Klaw Patrol.
			case 246523: //Harvesters Klaw Veteran Assaulter.
			    IDAb1EreWave++;
				//Additional Aid.
				if (IDAb1EreWave == 4) {
					startInstanceTask();
					sendPacket(player, "UI_Gauge_03", 1 + 1);
				}
			break;
			case 246566: //Harvesters Drakan Protector.
			    doors.get(1096).setOpen(true);
				//Your path is blocked by Incarna roots. Talk to the Captain to remove the roots, and then help him completely restore his HP.
				sendMsgByRace(1404314, Race.PC_ALL, 3000);
			    //The Harvesters are attacking Captain Bastiel. Restore Bastiel's HP to get rid of the Incarna roots.
				sendMsgByRace(1404218, Race.ELYOS, 30000);
				//The Harvesters are attacking Captain Batiskan. Restore Batiskan's HP to get rid of the Incarna roots.
				sendMsgByRace(1404219, Race.ASMODIANS, 30000);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						spawn(246519, 233.40959f, 745.22186f, 421.30054f, (byte) 81);
						final int Bastiel_Batiskan06 = spawnRace == Race.ASMODIANS ? 806703 : 806702;
						spawn(Bastiel_Batiskan06, 229.58844f, 729.6629f, 422.73956f, (byte) 80);
					}
				}, 60000);
			break;
			case 246506: //Arrogant Prida.
			case 246507: //Jealous Invilda.
			case 246508: //Enraged Halda.
			case 246509: //Envious Lida.
			    bossWitch++;
				if (bossWitch == 1) {
				} else if (bossWitch == 2) {
				} else if (bossWitch == 3) {
				} else if (bossWitch == 4) {
					deleteNpc(246511); //Opel.
					deleteNpc(246512); //Shurak.
					switch (Rnd.get(1, 3)) {
						case 1:
						    final int bossFinalHard = spawnRace == Race.ASMODIANS ? 246496 : 246493;
							spawn(bossFinalHard, 125.554344f, 648.39526f, 443.92804f, (byte) 14);
						break;
						case 2:
						    final int bossFinalNormal = spawnRace == Race.ASMODIANS ? 246497 : 246494;
							spawn(bossFinalNormal, 125.554344f, 648.39526f, 443.92804f, (byte) 14);
						break;
						case 3:
						    final int bossFinalEasy = spawnRace == Race.ASMODIANS ? 246498 : 246495;
							spawn(bossFinalEasy, 125.554344f, 648.39526f, 443.92804f, (byte) 14);
						break;
					}
				}
			break;
			//Boss Hard Elyos.
			case 246493: //Suffering Opel.
			    spawnBastionHardChest();
				sendMsg("[SUCCES]: You have finished <Bastion Of Souls>");
				final int IDAb1EreVideo1 = videoRace == Race.ASMODIANS ? 961 : 959;
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, IDAb1EreVideo1));
				//Bastion Exit.
				final int bastionExit1 = spawnRace == Race.ASMODIANS ? 731806 : 731805;
				spawn(bastionExit1, 110.215485f, 633.55597f, 443.95813f, (byte) 14);
			break;
			//Boss Normal Elyos.
			case 246494: //Cursed Opel.
			    spawnBastionNormalChest();
			    sendMsg("[SUCCES]: You have finished <Bastion Of Souls>");
				final int IDAb1EreVideo2 = videoRace == Race.ASMODIANS ? 961 : 959;
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, IDAb1EreVideo2));
				//Bastion Exit.
				final int bastionExit2 = spawnRace == Race.ASMODIANS ? 731806 : 731805;
				spawn(bastionExit2, 110.215485f, 633.55597f, 443.95813f, (byte) 14);
			break;
			//Boss Easy Elyos.
			case 246495: //Twisted Opel.
			    spawnBastionEasyChest();
				sendMsg("[SUCCES]: You have finished <Bastion Of Souls>");
				final int IDAb1EreVideo3 = videoRace == Race.ASMODIANS ? 961 : 959;
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, IDAb1EreVideo3));
				//Bastion Exit.
				final int bastionExit3 = spawnRace == Race.ASMODIANS ? 731806 : 731805;
				spawn(bastionExit3, 110.215485f, 633.55597f, 443.95813f, (byte) 14);
			break;
			///////////////////////////////////////////////////////////////////////////////
			//Boss Hard Asmodians.
			case 246496: //Suffering Opel.
			    spawnBastionHardChest();
				sendMsg("[SUCCES]: You have finished <Bastion Of Souls>");
				final int IDAb1EreVideo4 = videoRace == Race.ASMODIANS ? 961 : 959;
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, IDAb1EreVideo4));
				//Bastion Exit.
				final int bastionExit4 = spawnRace == Race.ASMODIANS ? 731806 : 731805;
				spawn(bastionExit4, 110.215485f, 633.55597f, 443.95813f, (byte) 14);
			break;
			//Boss Normal Asmodians.
			case 246497: //Cursed Opel.
			    spawnBastionNormalChest();
			    sendMsg("[SUCCES]: You have finished <Bastion Of Souls>");
				final int IDAb1EreVideo5 = videoRace == Race.ASMODIANS ? 961 : 959;
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, IDAb1EreVideo5));
				//Bastion Exit.
				final int bastionExit5 = spawnRace == Race.ASMODIANS ? 731806 : 731805;
				spawn(bastionExit5, 110.215485f, 633.55597f, 443.95813f, (byte) 14);
			break;
			//Boss Easy Asmodians.
			case 246498: //Twisted Opel.
			    spawnBastionEasyChest();
				sendMsg("[SUCCES]: You have finished <Bastion Of Souls>");
				final int IDAb1EreVideo6 = videoRace == Race.ASMODIANS ? 961 : 959;
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, IDAb1EreVideo6));
				//Bastion Exit.
				final int bastionExit6 = spawnRace == Race.ASMODIANS ? 731806 : 731805;
				spawn(bastionExit6, 110.215485f, 633.55597f, 443.95813f, (byte) 14);
			break;
		}
	}
	
	private void spawnBastionEasyChest() {
		spawn(835484, 119.39699f, 642.50430f, 443.95374f, (byte) 15);
        spawn(835484, 115.42487f, 638.64166f, 443.97333f, (byte) 15);
        spawn(835484, 120.47298f, 635.68460f, 443.92804f, (byte) 23);
        spawn(835484, 112.50187f, 643.73267f, 443.92804f, (byte) 5);
	}
	private void spawnBastionNormalChest() {
		spawn(835485, 119.39699f, 642.50430f, 443.95374f, (byte) 15);
        spawn(835485, 115.42487f, 638.64166f, 443.97333f, (byte) 15);
        spawn(835485, 120.47298f, 635.68460f, 443.92804f, (byte) 23);
        spawn(835485, 112.50187f, 643.73267f, 443.92804f, (byte) 5);
	}
	private void spawnBastionHardChest() {
		spawn(835486, 119.39699f, 642.50430f, 443.95374f, (byte) 15);
        spawn(835486, 115.42487f, 638.64166f, 443.97333f, (byte) 15);
        spawn(835486, 120.47298f, 635.68460f, 443.92804f, (byte) 23);
        spawn(835486, 112.50187f, 643.73267f, 443.92804f, (byte) 5);
	}
	
	private void sendPacket(Player player, final String variable, final int value) {
		instance.doOnAllPlayers(new Visitor<Player>() {
		    @Override
			public void visit(Player player) {
				if (player.isOnline()) {
					PacketSendUtility.sendPacket(player, new SM_CONDITION_VARIABLE(player, variable, value));
				}
			}
		});
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 246519:
			    despawnNpc(npc);
				switch (Rnd.get(1, 2)) {
					case 1:
					    SkillEngine.getInstance().getSkill(npc, 17649, 1, player).useNoAnimationSkill(); //IDAb1_Ere_Hero_HealBoost.
					break;
					case 2:
					    SkillEngine.getInstance().getSkill(npc, 17672, 1, player).useNoAnimationSkill(); //IDAb1_Ere_Hero_HealBoost_2.
					break;
				}
			break;
			case 835445: //Bomb Box I.
			case 835446: //Bomb Box II.
			    if (player.getInventory().isFull()) {
					sendMsgByRace(1390149, Race.PC_ALL, 0);
				} switch (Rnd.get(1, 2)) {
					case 1:
					    ItemService.addItem(player, 188100423, 1); //Assembly_Idab1_Ere_Wick_01.
					break;
					case 2:
					    ItemService.addItem(player, 188100424, 1); //Assembly_Idab1_Ere_Powder_01.
					break;
				}
			break;
		}
	}
	
	private void startBatiskanBastiel() {
		final int StartBatiskan_Bastiel = spawnRace == Race.ASMODIANS ? 806592 : 806583;
		spawn(StartBatiskan_Bastiel, 1213.3298f, 728.104f, 417.55914f, (byte) 89);
	}
	
	private void SpawnBastionRace() {
		//Daeva.
		final int DeadDeva = spawnRace == Race.ASMODIANS ? 731788 : 731785;
		final int DyingDeva = spawnRace == Race.ASMODIANS ? 731789 : 731786;
		final int PainDeva = spawnRace == Race.ASMODIANS ? 731790 : 731787;
		//Fiance.
		final int Fiance = spawnRace == Race.ASMODIANS ? 806599 : 806590;
		//Sky Prison.
		final int SkyPrison1 = spawnRace == Race.ASMODIANS ? 207189 : 207178;
		final int SkyPrison2 = spawnRace == Race.ASMODIANS ? 207190 : 207179;
		final int SkyPrison3 = spawnRace == Race.ASMODIANS ? 207191 : 207180;
		final int SkyPrison4 = spawnRace == Race.ASMODIANS ? 207192 : 207181;
		final int SkyPrison5 = spawnRace == Race.ASMODIANS ? 207193 : 207182;
		//Boss Final.
		final int BossFinalHeroSon = spawnRace == Race.ASMODIANS ? 246512 : 246511;
		//Prison.
		final int RoundPrisonM = spawnRace == Race.ASMODIANS ? 247139 : 247137;
		final int RoundPrisonF = spawnRace == Race.ASMODIANS ? 247140 : 247138;
		//Round Gossip.
		final int RoundGossip1 = spawnRace == Race.ASMODIANS ? 247186 : 247185;
		final int RoundGossip10 = spawnRace == Race.ASMODIANS ? 247220 : 247219;
		//Work Deva.
		final int WorkDevaM = spawnRace == Race.ASMODIANS ? 247018 : 247016;
		final int WorkDevaF = spawnRace == Race.ASMODIANS ? 247019 : 247017;
		//Mad Deva.
		final int MadDevaM = spawnRace == Race.ASMODIANS ? 247097 : 247095;
		final int MadDevaF = spawnRace == Race.ASMODIANS ? 247098 : 247096;
		//Sit Deva.
		final int SitDevaM = spawnRace == Race.ASMODIANS ? 247101 : 247099;
		final int SitDevaF = spawnRace == Race.ASMODIANS ? 247102 : 247100;
		//Sleep Deva.
		final int SleepDevaM = spawnRace == Race.ASMODIANS ? 247105 : 247103;
		//Prison Deva.
		final int PrisonDeva = spawnRace == Race.ASMODIANS ? 806618 : 806603;
		//Bastiel-Batiskan 01.
		final int Bastiel_Batiskan01 = spawnRace == Race.ASMODIANS ? 247204 : 247203;
		//Bastiel-Batiskan 02.
		final int Bastiel_Batiskan02 = spawnRace == Race.ASMODIANS ? 247206 : 247205;
		//Bastiel-Batiskan 03.
		final int Bastiel_Batiskan03 = spawnRace == Race.ASMODIANS ? 806593 : 806584;
		//Bastiel-Batiskan 04.
		final int Bastiel_Batiskan04 = spawnRace == Race.ASMODIANS ? 806594 : 806585;
		//Bastiel-Batiskan 05.
		final int Bastiel_Batiskan05 = spawnRace == Race.ASMODIANS ? 806598 : 806589;
		//Daeva.
		spawn(DeadDeva, 302.37018f, 801.74664f, 428.22058f, (byte) 0, 116);
		spawn(DyingDeva, 341.62643f, 741.94934f, 428.54651f, (byte) 0, 433);
		spawn(PainDeva, 302.62418f, 695.31512f, 426.87469f, (byte) 0, 436);
		//Fiance.
		spawn(Fiance, 248.73918f, 795.69189f, 427.61301f, (byte) 0, 165);
		//Sky Prison.
		spawn(SkyPrison1, 1213.5228f, 557.40729f, 431.50104f, (byte) 0, 1053);
		spawn(SkyPrison2, 1156.2153f, 634.08002f, 425.58981f, (byte) 0, 4);
		spawn(SkyPrison3, 1163.5190f, 616.74103f, 427.34256f, (byte) 0, 1054);
		spawn(SkyPrison4, 1191.3264f, 642.02643f, 427.39340f, (byte) 0, 1055);
		spawn(SkyPrison5, 1184.5991f, 599.38751f, 428.29214f, (byte) 0, 1052);
		//Boss Final.
		spawn(BossFinalHeroSon, 125.561966f, 648.5562f, 443.92804f, (byte) 29);
		//Prison.
		spawn(RoundPrisonM, 1218.2537f, 895.02203f, 400.92175f, (byte) 61);
		spawn(RoundPrisonF, 1202.7615f, 891.0025f, 401.07822f, (byte) 104);
		spawn(RoundPrisonF, 1164.0812f, 886.5736f, 400.6619f, (byte) 66);
		//Round Gossip.
		spawn(RoundGossip1, 1212.2343f, 853.5162f, 405.29352f, (byte) 30);
		spawn(RoundGossip10, 1184.5503f, 655.08954f, 427.28555f, (byte) 94);
		//Work Deva.
		spawn(WorkDevaM, 1337.1613f, 932.3105f, 404.81616f, (byte) 63);
        spawn(WorkDevaM, 1351.9149f, 912.46375f, 404.6229f, (byte) 91);
        spawn(WorkDevaM, 1336.3319f, 887.6009f, 405.9579f, (byte) 97);
        spawn(WorkDevaM, 1352.2985f, 918.0303f, 404.53137f, (byte) 8);
		spawn(WorkDevaM, 1341.0846f, 910.80347f, 404.99115f, (byte) 65);
		spawn(WorkDevaF, 1338.6947f, 937.916f, 404.53137f, (byte) 66);
        spawn(WorkDevaF, 1348.1666f, 936.5799f, 404.81543f, (byte) 4);
        spawn(WorkDevaF, 1328.1765f, 893.304f, 405.9579f, (byte) 44);
		spawn(WorkDevaF, 1337.4083f, 919.7019f, 405.2923f, (byte) 57);
		spawn(WorkDevaF, 1346.0502f, 901.90546f, 404.53137f, (byte) 14);
		//Mad Deva.
		spawn(MadDevaM, 1359.8538f, 703.15076f, 416.63916f, (byte) 48);
        spawn(MadDevaM, 1160.0083f, 601.2188f, 431.123f, (byte) 104);
        spawn(MadDevaM, 1190.2412f, 569.8828f, 429.6774f, (byte) 47);
        spawn(MadDevaM, 1368.9288f, 725.1537f, 416.664f, (byte) 66);
        spawn(MadDevaM, 1313.1796f, 726.799f, 413.9595f, (byte) 4);
        spawn(MadDevaM, 1363.5707f, 732.2216f, 416.64908f, (byte) 51);
        spawn(MadDevaM, 1168.1194f, 583.6761f, 431.123f, (byte) 82);
        spawn(MadDevaM, 1218.2742f, 883.53467f, 399.82135f, (byte) 46);
        spawn(MadDevaM, 1357.1973f, 708.3318f, 416.62976f, (byte) 93);
        spawn(MadDevaM, 1344.6594f, 696.5749f, 416.6216f, (byte) 23);
        spawn(MadDevaM, 1319.7157f, 737.30707f, 413.9595f, (byte) 102);
		//Mad Deva.
		spawn(MadDevaF, 1180.1338f, 895.1975f, 400.74057f, (byte) 97);
        spawn(MadDevaF, 1189.1792f, 584.48425f, 429.6774f, (byte) 66);
        spawn(MadDevaF, 1198.1525f, 624.5861f, 426.32108f, (byte) 32);
        spawn(MadDevaF, 1346.2982f, 691.83057f, 416.6307f, (byte) 21);
        spawn(MadDevaF, 1148.1729f, 882.67755f, 400.04767f, (byte) 15);
        spawn(MadDevaF, 1323.5608f, 722.1448f, 413.9595f, (byte) 40);
        spawn(MadDevaF, 1166.06f, 895.8356f, 399.82135f, (byte) 67);
        spawn(MadDevaF, 1361.0577f, 755.0399f, 416.62833f, (byte) 72);
        spawn(MadDevaF, 1184.7257f, 849.7001f, 400.47562f, (byte) 87);
        spawn(MadDevaF, 1364.59f, 714.60974f, 416.6605f, (byte) 45);
        spawn(MadDevaF, 1364.3134f, 725.589f, 416.65567f, (byte) 61);
        spawn(MadDevaF, 1192.0729f, 886.594f, 400.74057f, (byte) 51);
        spawn(MadDevaF, 1175.765f, 577.2988f, 429.6774f, (byte) 112);
        spawn(MadDevaF, 1216.1758f, 882.44336f, 399.82135f, (byte) 35);
        spawn(MadDevaF, 1340.0725f, 766.9074f, 416.62653f, (byte) 91);
        spawn(MadDevaF, 1176.7711f, 826.69366f, 400.47562f, (byte) 106);
        spawn(MadDevaF, 1173.4569f, 563.52966f, 429.6774f, (byte) 19);
        spawn(MadDevaF, 1192.1564f, 619.4942f, 426.32108f, (byte) 20);
        spawn(MadDevaF, 1159.1156f, 591.661f, 431.123f, (byte) 7);
        spawn(MadDevaF, 1197.3494f, 616.6646f, 426.32108f, (byte) 34);
        spawn(MadDevaF, 1178.3425f, 843.2838f, 400.47562f, (byte) 111);
        spawn(MadDevaF, 1182.5349f, 591.75287f, 429.6774f, (byte) 90);
		//Sit Deva.
		spawn(SitDevaM, 1175.8107f, 569.9384f, 429.6774f, (byte) 5);
        spawn(SitDevaM, 1190.4326f, 824.4356f, 400.47562f, (byte) 45);
        spawn(SitDevaM, 1199.306f, 882.8208f, 399.82135f, (byte) 14);
        spawn(SitDevaM, 1350.8231f, 763.3704f, 416.62646f, (byte) 72);
        spawn(SitDevaM, 1181.3295f, 847.28033f, 400.47562f, (byte) 104);
        spawn(SitDevaM, 1347.4022f, 765.881f, 416.62888f, (byte) 95);
        spawn(SitDevaM, 1317.8734f, 722.17834f, 413.9595f, (byte) 19);
        spawn(SitDevaM, 1344.7246f, 766.82587f, 416.629f, (byte) 77);
        spawn(SitDevaM, 1326.3198f, 768.81647f, 416.65982f, (byte) 78);
        spawn(SitDevaM, 1312.5515f, 731.7577f, 413.9595f, (byte) 110);
        spawn(SitDevaM, 1193.7145f, 615.8017f, 426.32108f, (byte) 21);
        spawn(SitDevaM, 1328.0808f, 689.0418f, 416.62906f, (byte) 1);
        spawn(SitDevaM, 1361.984f, 751.3607f, 416.62634f, (byte) 64);
        spawn(SitDevaM, 1178.7422f, 838.63617f, 400.47562f, (byte) 5);
        spawn(SitDevaM, 1356.91f, 754.8755f, 416.62173f, (byte) 108);
        spawn(SitDevaM, 1365.9019f, 741.11426f, 416.65604f, (byte) 54);
        spawn(SitDevaM, 1322.9473f, 764.2691f, 416.65158f, (byte) 20);
        spawn(SitDevaM, 1174.0844f, 584.88983f, 429.6774f, (byte) 105);
        spawn(SitDevaM, 1339.5428f, 689.9676f, 416.63013f, (byte) 18);
        spawn(SitDevaM, 1188.1359f, 892.51715f, 400.74057f, (byte) 76);
        spawn(SitDevaM, 1179.8717f, 829.7911f, 400.47562f, (byte) 109);
        spawn(SitDevaM, 1330.8044f, 768.2091f, 416.65872f, (byte) 1);
        spawn(SitDevaM, 1322.8658f, 768.6766f, 416.65958f, (byte) 95);
		spawn(SitDevaM, 1187.1161f, 845.84686f, 400.47562f, (byte) 71);
        spawn(SitDevaM, 1347.0094f, 764.75665f, 416.6267f, (byte) 90);
        spawn(SitDevaM, 1198.0352f, 632.958f, 426.32108f, (byte) 73);
        spawn(SitDevaM, 1176.5939f, 888.41144f, 400.74057f, (byte) 104);
        spawn(SitDevaM, 1331.1262f, 764.4313f, 416.6519f, (byte) 97);
        spawn(SitDevaM, 1337.3823f, 696.7754f, 416.6172f, (byte) 100);
        spawn(SitDevaM, 1187.8665f, 828.90436f, 400.47562f, (byte) 67);
        spawn(SitDevaM, 1179.9353f, 882.7061f, 400.74057f, (byte) 107);
        spawn(SitDevaM, 1188.5713f, 840.5731f, 400.47562f, (byte) 75);
		spawn(SitDevaF, 1333.6642f, 769.7026f, 416.66144f, (byte) 81);
        spawn(SitDevaF, 1330.9159f, 691.95807f, 416.62415f, (byte) 35);
        spawn(SitDevaF, 1168.8026f, 608.0805f, 431.123f, (byte) 82);
        spawn(SitDevaF, 1200.2471f, 882.24176f, 399.82135f, (byte) 14);
        spawn(SitDevaF, 1182.8541f, 567.81665f, 429.6774f, (byte) 30);
        spawn(SitDevaF, 1316.675f, 735.44324f, 413.9595f, (byte) 102);
        spawn(SitDevaF, 1362.1189f, 738.9375f, 416.64862f, (byte) 53);
        spawn(SitDevaF, 1362.5602f, 736.26483f, 416.64856f, (byte) 68);
        spawn(SitDevaF, 1366.9102f, 742.5694f, 416.6583f, (byte) 63);
        spawn(SitDevaF, 1366.8f, 718.27594f, 416.66296f, (byte) 58);
        spawn(SitDevaF, 1197.9783f, 641.0993f, 426.32108f, (byte) 79);
        spawn(SitDevaF, 1341.7201f, 692.7224f, 416.62662f, (byte) 36);
        spawn(SitDevaF, 1190.8685f, 637.75854f, 426.32108f, (byte) 114);
        spawn(SitDevaF, 1367.6082f, 722.1445f, 416.66284f, (byte) 53);
        spawn(SitDevaF, 1343.7491f, 767.2277f, 416.62915f, (byte) 78);
        spawn(SitDevaF, 1349.6709f, 695.7414f, 416.62585f, (byte) 38);
        spawn(SitDevaF, 1366.2611f, 715.57806f, 416.66306f, (byte) 53);
        spawn(SitDevaF, 1367.5247f, 735.51154f, 416.65717f, (byte) 75);
        spawn(SitDevaF, 1362.3627f, 704.534f, 416.6414f, (byte) 53);
        spawn(SitDevaF, 1327.4696f, 688.0927f, 416.6307f, (byte) 19);
        spawn(SitDevaF, 1177.2306f, 584.7701f, 429.6774f, (byte) 108);
        spawn(SitDevaF, 1168.6189f, 597.08026f, 431.123f, (byte) 37);
		//Sleep Deva.
		spawn(SleepDevaM, 1162.9143f, 883.4652f, 399.82135f, (byte) 56);
		//Prison Deva.
		spawn(PrisonDeva, 1191.3267f, 895.774f, 400.9108f, (byte) 60);
        spawn(PrisonDeva, 1176.1115f, 895.8272f, 400.9108f, (byte) 0);
        spawn(PrisonDeva, 1191.3956f, 882.6344f, 400.9108f, (byte) 59);
        spawn(PrisonDeva, 1177.9648f, 850.58325f, 401.13937f, (byte) 0);
        spawn(PrisonDeva, 1189.8513f, 850.66754f, 401.14188f, (byte) 60);
        spawn(PrisonDeva, 1177.9836f, 834.45056f, 401.13596f, (byte) 119);
		spawn(PrisonDeva, 1189.6991f, 834.4552f, 401.14066f, (byte) 60);
		spawn(PrisonDeva, 1177.9556f, 818.2843f, 401.13516f, (byte) 30);
		spawn(PrisonDeva, 1313.5878f, 736.4435f, 414.69138f, (byte) 0);
		spawn(PrisonDeva, 1313.4708f, 723.14624f, 414.68463f, (byte) 0);
		spawn(PrisonDeva, 1327.8927f, 723.2844f, 414.6927f, (byte) 59);
		spawn(PrisonDeva, 1327.9568f, 736.42175f, 414.6903f, (byte) 59);
		spawn(PrisonDeva, 1203.9976f, 640.4458f, 427.18018f, (byte) 59);
		spawn(PrisonDeva, 1203.9475f, 634.118f, 427.18225f, (byte) 59);
		spawn(PrisonDeva, 1203.9961f, 624.77106f, 427.17136f, (byte) 59);
        spawn(PrisonDeva, 1203.9604f, 618.62585f, 427.17578f, (byte) 60);
		spawn(PrisonDeva, 1187.015f, 589.31604f, 430.56235f, (byte) 90);
		spawn(PrisonDeva, 1178.3707f, 589.2427f, 430.5583f, (byte) 90);
		spawn(PrisonDeva, 1187.282f, 565.71655f, 430.5608f, (byte) 30);
		spawn(PrisonDeva, 1178.275f, 565.73016f, 430.5587f, (byte) 30);
		spawn(PrisonDeva, 1164.002f, 587.5437f, 431.93124f, (byte) 90);
		spawn(PrisonDeva, 1164.0331f, 596.536f, 431.9306f, (byte) 89);
		spawn(PrisonDeva, 1163.9614f, 605.57825f, 431.92865f, (byte) 90);
		spawn(PrisonDeva, 1189.8373f, 818.3949f, 401.14017f, (byte) 29);
		spawn(PrisonDeva, 1176.2666f, 882.7186f, 400.9108f, (byte) 0);
		//Bastiel-Batiskan 01.
		spawn(Bastiel_Batiskan01, 1314.1300f, 767.75836f, 416.41507f, (byte) 0);
		//Bastiel-Batiskan 02.
		spawn(Bastiel_Batiskan02, 1137.8638f, 872.1641f, 399.85068f, (byte) 29);
		//Bastiel-Batiskan 03.
		spawn(Bastiel_Batiskan03, 1073.3077f, 732.935f, 394.59494f, (byte) 70);
		//Bastiel-Batiskan 04.
		spawn(Bastiel_Batiskan04, 995.53577f, 722.54333f, 393.16934f, (byte) 12);
		//Bastiel-Batiskan 05.
		spawn(Bastiel_Batiskan05, 148.12717f, 648.1179f, 443.94836f, (byte) 60);
	}
	
	protected void startInstanceTask() {
    	instanceTime = System.currentTimeMillis();
		bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startBastionA2();
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(246519, 233.40959f, 745.22186f, 421.30054f, (byte) 81);
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendPacket(player, "UI_Gauge_03", 2 + 1);
						}
					}
				});
            }
        }, 60000)); //...1Min
		bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startBastionA3();
				bastionTaskA2.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(246519, 233.40959f, 745.22186f, 421.30054f, (byte) 81);
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendPacket(player, "UI_Gauge_03", 3 + 1);
						}
					}
				});
            }
        }, 120000)); //...2Min
		bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startBastionA4();
				bastionTaskA3.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(246519, 233.40959f, 745.22186f, 421.30054f, (byte) 81);
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendPacket(player, "UI_Gauge_03", 4 + 1);
						}
					}
				});
            }
        }, 180000)); //...3Min
		bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startBastionA5();
				bastionTaskA4.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(246519, 233.40959f, 745.22186f, 421.30054f, (byte) 81);
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendPacket(player, "UI_Gauge_03", 5 + 1);
						}
					}
				});
            }
        }, 240000)); //...4Min
		bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startBastionA6();
				bastionTaskA5.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(246519, 233.40959f, 745.22186f, 421.30054f, (byte) 81);
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendPacket(player, "UI_Gauge_03", 6 + 1);
						}
					}
				});
            }
        }, 300000)); //...5Min
		bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startBastionA7();
				bastionTaskA6.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(246519, 233.40959f, 745.22186f, 421.30054f, (byte) 81);
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendPacket(player, "UI_Gauge_03", 7 + 1);
						}
					}
				});
            }
        }, 360000)); //...6Min
		bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startBastionA8();
				bastionTaskA7.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(246519, 233.40959f, 745.22186f, 421.30054f, (byte) 81);
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendPacket(player, "UI_Gauge_03", 8 + 1);
						}
					}
				});
            }
        }, 420000)); //...7Min
		bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startBastionA9();
				bastionTaskA8.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(246519, 233.40959f, 745.22186f, 421.30054f, (byte) 81);
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendPacket(player, "UI_Gauge_03", 9 + 1);
						}
					}
				});
            }
        }, 480000)); //...8Min
		bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startBastionA10();
				bastionTaskA9.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(246519, 233.40959f, 745.22186f, 421.30054f, (byte) 81);
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendPacket(player, "UI_Gauge_03", 10 + 1);
						}
					}
				});
            }
        }, 540000)); //...9Min
		bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startBastionA11();
				bastionTaskA10.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(246519, 233.40959f, 745.22186f, 421.30054f, (byte) 81);
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendPacket(player, "UI_Gauge_03", 11 + 1);
						}
					}
				});
            }
        }, 600000)); //...10Min
		bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startBastionA12();
				bastionTaskA11.cancel(true);
				//Prepare for combat! Enemies approaching!
				sendMsgByRace(1402785, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(246519, 233.40959f, 745.22186f, 421.30054f, (byte) 81);
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							sendPacket(player, "UI_Gauge_03", 12 + 1);
						}
					}
				});
            }
        }, 660000)); //...11Min
		bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				//The additional mission "Attack of the Harvesters" is complete.
				sendMsgByRace(1404222, Race.PC_ALL, 0);
				final int Bastiel_Batiskan07 = spawnRace == Race.ASMODIANS ? 806597 : 806588;
				spawn(Bastiel_Batiskan07, 229.58844f, 729.6629f, 422.73956f, (byte) 80);
				instance.doOnAllPlayers(new Visitor<Player>() {
				    @Override
				    public void visit(Player player) {
						deleteNpc(806702);
						deleteNpc(806703);
						stopInstance(player);
						killNpc(getNpcs(731804));
						bastionTaskA12.cancel(true);
				    }
			    });
            }
        }, 720000)); //...12Min
    }
	
	private void rushBastion(final Npc npc) {
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
	
	private void startBastionA2() {
		bastionTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 1000);
		bastionTaskA2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 30000);
	}
	
	private void startBastionA3() {
		bastionTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 1000);
		bastionTaskA3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 30000);
	}
	
	private void startBastionA4() {
		bastionTaskA4 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 1000);
		bastionTaskA4 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246810, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 30000);
	}
	
	private void startBastionA5() {
		bastionTaskA5 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 1000);
		bastionTaskA5 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 30000);
	}
	
	private void startBastionA6() {
		bastionTaskA6 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 1000);
		bastionTaskA6 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246810, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 30000);
	}
	
	private void startBastionA7() {
		bastionTaskA7 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 1000);
		bastionTaskA7 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 30000);
	}
	
	private void startBastionA8() {
		bastionTaskA8 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 1000);
		bastionTaskA8 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246810, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 30000);
	}
	
	private void startBastionA9() {
		bastionTaskA9 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 1000);
		bastionTaskA9 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 30000);
	}
	
	private void startBastionA10() {
		bastionTaskA10 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 1000);
		bastionTaskA10 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246810, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 30000);
	}
	
	private void startBastionA11() {
		bastionTaskA11 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 1000);
		bastionTaskA11 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 30000);
	}
	
	private void startBastionA12() {
		bastionTaskA12 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246525, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 1000);
		bastionTaskA12 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushBastion((Npc)spawn(246523, 260.62317f, 753.64874f, 421.74033f, (byte) 67));
				rushBastion((Npc)spawn(246524, 229.63022f, 773.9174f, 421.83142f, (byte) 94));
				rushBastion((Npc)spawn(246525, 220.49199f, 743.5425f, 421.625f, (byte) 14));
				rushBastion((Npc)spawn(246810, 244.47363f, 733.99384f, 421.38748f, (byte) 33));
			}
		}, 30000);
	}
	
	protected void stopInstance(Player player) {
		stopInstanceTask();
		sendMsg("[SUCCES]: You survived !!! :) ");
	}
	
	private int getTime() {
		long result = System.currentTimeMillis() - instanceTime;
		if (result < 60000) {
			return (int) (60000 - result);
		} else if (result < 720000) {
			return (int) (720000 - (result - 60000));
		}
		return 0;
	}
	
	private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = bastionTask.head(), end = bastionTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
        sp(npcId, x, y, z, h, 0, time, 0, null);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race) {
        sp(npcId, x, y, z, h, 0, time, msg, race);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time, final int msg, final Race race) {
        bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        bastionTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
	
	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendWhiteMessageOnCenter(player, str);
			}
		});
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
	
	private void removeEffects(Player player) {
		PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(17649);
		effectController.removeEffect(17672);
	}
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(185000302, storage.getItemCountByItemId(185000302));
		storage.decreaseByItemId(185000303, storage.getItemCountByItemId(185000303));
		storage.decreaseByItemId(185000304, storage.getItemCountByItemId(185000304));
		storage.decreaseByItemId(185000309, storage.getItemCountByItemId(185000309));
		storage.decreaseByItemId(188100423, storage.getItemCountByItemId(188100423));
		storage.decreaseByItemId(188100424, storage.getItemCountByItemId(188100424));
	}
	
	@Override
    public void onPlayerLogOut(Player player) {
        removeItems(player);
    }
	
	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
	}
	
	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		stopInstanceTask();
		movies.clear();
		doors.clear();
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
	
	private void sendMovie(Player player, int movie) {
		if (!movies.contains(movie)) {
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}
	
	private void teleport(float x, float y, float z, byte h) {
		for (Player playerInside: instance.getPlayersInside()) {
			if (playerInside.isOnline()) {
				bastionToStartRoom(playerInside);
			}
		}
	}
	
	protected void teleport(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, mapId, instanceId, x, y, z, h);
	}
}