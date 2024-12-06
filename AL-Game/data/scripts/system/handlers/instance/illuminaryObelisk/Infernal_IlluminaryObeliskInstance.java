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
package instance.illuminaryObelisk;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
Author (Encom)

You start at the top of the instance so you need to move down as fast as you can.
If you die inside you will restart at the entrance of the instance.  
The tower has two floors, bridges on east and west are connected to the second floor and north and south bridges are connected to the first floor.  
An elevator in the center connects both floors.  
Four Shield Units must be defended from monsters, and each shield needs to charge 3 phases, when all shields are at third phase the final boss will spawn.  
Cannons are located around the shield units, enter them with the right item and make wide area damage to the monsters.

Special Features:
Successfully defend the towers and the final boss will appear.
You will need to collect the items, defend the towers and then the "Boss" will appear.
Updrafts are placed around the map so you can travel faster between floors and bridges, if you fall outside the updraft it will instant kill you.
The rolls of the party members is important, designate a person to collect the items, another for the cannons, and to defend the shield units.
**/

@InstanceID(301370000)
public class Infernal_IlluminaryObeliskInstance extends GeneralInstanceHandler
{
	private long startTime;
	private Race videoRace;
	private int illuminaryWave;
	private Future<?> instanceTimer;
	//Eastern Shield Wave.
	private Future<?> easternTaskE1;
	private Future<?> easternTaskE2;
	private Future<?> easternTaskE3;
	private Future<?> easternTaskE4;
	//Western Shield Wave.
	private Future<?> westernTaskW1;
	private Future<?> westernTaskW2;
	private Future<?> westernTaskW3;
	private Future<?> westernTaskW4;
	//Southern Shield Wave.
	private Future<?> southernTaskS1;
	private Future<?> southernTaskS2;
	private Future<?> southernTaskS3;
	private Future<?> southernTaskS4;
	//Northern Shield Wave.
	private Future<?> northernTaskN1;
	private Future<?> northernTaskN2;
	private Future<?> northernTaskN3;
	private Future<?> northernTaskN4;
	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;
	private List<Integer> movies = new ArrayList<Integer>();
	private final FastList<Future<?>> illuminaryTask1 = FastList.newInstance();
	private final FastList<Future<?>> illuminaryTask2 = FastList.newInstance();
	private final FastList<Future<?>> illuminaryTask3 = FastList.newInstance();
	private final FastList<Future<?>> illuminaryTask4 = FastList.newInstance();
	
   /**
	* Reward:
	* After a successful capture of the boss you will get a small chance of obtaining mythical wings, and a variety of items.
	* Boxes are for all the members and the wings only for one person in the group.
	*/
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 702018: //Supply Box.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053100, 1)); //Pure Dynatoum's Equipment Crux Box.
					} switch (Rnd.get(1, 2)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052830, 1)); //Dynatoum's Brazen Weapon Box.
				        break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052831, 1)); //Dynatoum's Brazen Armor Box.
				        break;
					}
				}
			break;
			case 702658: //Abbey Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053579, 1)); //[Event] Abbey Bundle.
		    break;
			case 702659: //Noble Abbey Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053580, 1)); //[Event] Noble Abbey Bundle.
		    break;
		   /**
			* Each "Shield Generator" unit needs 3 ide items, 12 items in total, you can find them all around the instance.
			*/
			case 730884: //Flourishing Idium.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000289, 3));
			break;
		   /**
			* Bombs to use the cannons appear in chests around the instance in a different place every time, collect them too.
			*/
			case 730885: //Danuar Cannonballs.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000290, 3));
			break;
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}
	
	private void startIlluminaryTimer() {
		//The weakened protective shield will disappear in 30 minutes.
		this.sendMessage(1402129, 1 * 60 * 1000);
		//The weakened protective shield will disappear in 25 minutes.
		this.sendMessage(1402130, 5 * 60 * 1000);
		//The weakened protective shield will disappear in 20 minutes.
		this.sendMessage(1402131, 10 * 60 * 1000);
		//The weakened protective shield will disappear in 15 minutes.
		this.sendMessage(1402132, 15 * 60 * 1000);
		//The weakened protective shield will disappear in 10 minutes.
		this.sendMessage(1402133, 20 * 60 * 1000);
		//The weakened protective shield will disappear in 5 minutes.
		this.sendMessage(1402134, 25 * 60 * 1000);
		//The weakened protective shield will disappear in 1 minute.
		this.sendMessage(1402235, 29 * 60 * 1000);
		//The shield surrounding the Infernal Illuminary Obelisk has disappeared. Pashid's Destruction Unit has attacked the area.
		this.sendMessage(1402424, 30 * 60 * 1000);
		//The Remodeled Dynatoum has destroyed the shield generator teleporter.
		this.sendMessage(1402429, 31 * 60 * 1000);
    }
	
	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		if (instanceTimer == null) {
			startTime = System.currentTimeMillis();
			instanceTimer = ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					startIlluminaryTimer();
					doors.get(129).setOpen(true);
				}
			}, 30000); //...30Sec
		}
		final int illuminaryVideo = videoRace == Race.ASMODIANS ? 895 : 894;
		PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, illuminaryVideo));
	}
	
   /**
	* Shield Units:
	* Its a good idea to start powering the shields once you have 6 ide shield items.
	* Help collect the remaining pieces together in the other bridges.
	* Once you charge a shield with one of the items, a wave of monster will appear, help that person and kill the mobs.
	* Protect the shield units from monsters while you charge them up to the 3rd phase.
	* Once all shields are at the 3rd phase no more monsters will spawn.
	*/
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 702010: //Eastern Shield Generator.
			    if (player.getInventory().decreaseByItemId(164000289, 3)) {
					startEasternTask();
					startEasternShield1();
					//An Abyss Gate has opened near the eastern power shield generator.
					//Infiltration by Pashid Destruction Unit is underway.
					sendMsgByRace(1402224, Race.PC_ALL, 1000);
					spawn(702014, 255.7926f, 338.22058f, 325.56473f, (byte) 0, 60); //Pashid Infiltration Gate.
				} else {
					//You need a Crystalline Idium Piece to charge the generator.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402211));
				}
			break;
			case 702011: //Western Shield Generator.
			    if (player.getInventory().decreaseByItemId(164000289, 3)) {
					startWesternTask();
					startWesternShield1();
					//An Abyss Gate has opened near the western power shield generator.
					//Infiltration by Pashid Destruction Unit is underway.
					sendMsgByRace(1402225, Race.PC_ALL, 1000);
					spawn(702015, 255.7034f, 171.83853f, 325.81653f, (byte) 0, 18); //Pashid Infiltration Gate.
				} else {
					//You need a Crystalline Idium Piece to charge the generator.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402211));
				}
			break;
			case 702012: //Southern Shield Generator.
			    if (player.getInventory().decreaseByItemId(164000289, 3)) {
					startSouthernTask();
					startSouthernShield1();
					//An Abyss Gate has opened near the southern power shield generator.
					//Infiltration by Pashid Destruction Unit is underway.
					sendMsgByRace(1402226, Race.PC_ALL, 1000);
					spawn(702016, 343.12021f, 254.10585f, 291.62302f, (byte) 0, 34); //Pashid Infiltration Gate.
				} else {
					//You need a Crystalline Idium Piece to charge the generator.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402211));
				}
			break;
			case 702013: //Northern Shield Generator.
			    if (player.getInventory().decreaseByItemId(164000289, 3)) {
					startNorthernTask();
					startNorthernShield1();
					//An Abyss Gate has opened near the northern power shield generator.
					//Infiltration by Pashid Destruction Unit is underway.
					sendMsgByRace(1402227, Race.PC_ALL, 1000);
					spawn(702017, 169.55626f, 254.52907f, 293.04276f, (byte) 0, 17); //Pashid Infiltration Gate.
				} else {
					//You need a Crystalline Idium Piece to charge the generator.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402211));
				}
			break;
			case 730886: //Shield Control Room Teleporter.
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.isOnline()) {
							illuminaryToDynatoum(player);
						}
					}
				});
			break;
		   /**
			* Defense Cannons:
			* Each Shield Unit has a defense cannon that can be used.
			* This cannons do powerful wide area damage attacks.
			* In order to use them you need to have Bomb items.
			* When a shield is charged completely a cannon will spawn to help in the defense of the area.
			* Determining a person to use the cannon and positioning before the mobs come is a recommended.
			* Bombs to use the cannons appear in chests around the instance in a different place every time, collect them too.
			*/
			case 702009: //Danuar Cannon.
			case 702021: //Danuar Cannon.
			case 702022: //Danuar Cannon.
			case 702023: //Danuar Cannon.
			    despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21511, 60, player).useNoAnimationSkill();
			break;
		}
	}
	
   /**
	* If a "Shield" is destroyed, you must start again from the 1st phase
	* You can heal the shield with a restoration skill.
	*/
	@Override
    public void onDie(Npc npc) {
        Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 702010: //Eastern Shield Generator.
		        despawnNpc(npc);
				stopInstanceTask1();
				deleteNpc(702014); //Eastern Pashid Infiltration Gate.
				deleteNpc(702218); //Eastern Defence Charge 01.
				deleteNpc(702219); //Eastern Defence Charge 02.
				deleteNpc(702220); //Eastern Defence Charge 03.
				killNpc(getNpcs(234720)); //Pashid Destruction Unit Contender.
			    killNpc(getNpcs(234721)); //Pashid Destruction Unit Special Ambusher.
			    killNpc(getNpcs(234722)); //Pashid Destruction Unit Battlesage.
				killNpc(getNpcs(234723)); //Pashid Destruction Unit Cavalier.
				killNpc(getNpcs(234724)); //Pashid Destruction Unit Hidestitcher.
				killNpc(getNpcs(234725)); //Pashid Destruction Unit Reserve.
				killNpc(getNpcs(234726)); //Pashid Destruction Unit Skirmisher.
				killNpc(getNpcs(234727)); //Pashid Destruction Unit Grunt.
				killNpc(getNpcs(234728)); //Pashid Destruction Unit Prime Contender.
				killNpc(getNpcs(234729)); //Pashid Destruction Unit Prime Drakenblade.
				killNpc(getNpcs(234730)); //Pashid Destruction Unit Prime Battlesage.
				killNpc(getNpcs(234731)); //Pashid Destruction Unit Prime Cavalier.
				killNpc(getNpcs(234732)); //Pashid Destruction Unit Prime Fleshmender.
				killNpc(getNpcs(234733)); //Pashid Destruction Unit Prime Reserve.
				killNpc(getNpcs(234734)); //Pashid Destruction Unit Prime Skirmisher.
				//The eastern shield power generator has been destroyed.
				sendMsgByRace(1402139, Race.PC_ALL, 0);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						spawn(702010, 255.47392f, 293.56177f, 321.18497f, (byte) 89); //Eastern Shield Generator.
				    }
			    }, 10000);
			break;
		    case 702011: //Western Shield Generator.
		        despawnNpc(npc);
				stopInstanceTask2();
				deleteNpc(702015); //Western Pashid Infiltration Gate.
				deleteNpc(702221); //Western Defence Charge 01.
				deleteNpc(702222); //Western Defence Charge 02.
				deleteNpc(702223); //Western Defence Charge 03.
				killNpc(getNpcs(234720)); //Pashid Destruction Unit Contender.
			    killNpc(getNpcs(234721)); //Pashid Destruction Unit Special Ambusher.
			    killNpc(getNpcs(234722)); //Pashid Destruction Unit Battlesage.
				killNpc(getNpcs(234723)); //Pashid Destruction Unit Cavalier.
				killNpc(getNpcs(234724)); //Pashid Destruction Unit Hidestitcher.
				killNpc(getNpcs(234725)); //Pashid Destruction Unit Reserve.
				killNpc(getNpcs(234726)); //Pashid Destruction Unit Skirmisher.
				killNpc(getNpcs(234727)); //Pashid Destruction Unit Grunt.
				killNpc(getNpcs(234728)); //Pashid Destruction Unit Prime Contender.
				killNpc(getNpcs(234729)); //Pashid Destruction Unit Prime Drakenblade.
				killNpc(getNpcs(234730)); //Pashid Destruction Unit Prime Battlesage.
				killNpc(getNpcs(234731)); //Pashid Destruction Unit Prime Cavalier.
				killNpc(getNpcs(234732)); //Pashid Destruction Unit Prime Fleshmender.
				killNpc(getNpcs(234733)); //Pashid Destruction Unit Prime Reserve.
				killNpc(getNpcs(234734)); //Pashid Destruction Unit Prime Skirmisher.
				//The western shield power generator has been destroyed.
				sendMsgByRace(1402140, Race.PC_ALL, 0);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						spawn(702011, 255.55742f, 216.03549f, 321.21344f, (byte) 30); //Western Shield Generator.
				    }
			    }, 10000);
			break;
		    case 702012: //Southern Shield Generator.
		        despawnNpc(npc);
				stopInstanceTask3();
				deleteNpc(702016); //Southern Pashid Infiltration Gate.
				deleteNpc(702224); //Southern Defence Charge 01.
				deleteNpc(702225); //Southern Defence Charge 02.
				deleteNpc(702226); //Southern Defence Charge 03.
				killNpc(getNpcs(234720)); //Pashid Destruction Unit Contender.
			    killNpc(getNpcs(234721)); //Pashid Destruction Unit Special Ambusher.
			    killNpc(getNpcs(234722)); //Pashid Destruction Unit Battlesage.
				killNpc(getNpcs(234723)); //Pashid Destruction Unit Cavalier.
				killNpc(getNpcs(234724)); //Pashid Destruction Unit Hidestitcher.
				killNpc(getNpcs(234725)); //Pashid Destruction Unit Reserve.
				killNpc(getNpcs(234726)); //Pashid Destruction Unit Skirmisher.
				killNpc(getNpcs(234727)); //Pashid Destruction Unit Grunt.
				killNpc(getNpcs(234728)); //Pashid Destruction Unit Prime Contender.
				killNpc(getNpcs(234729)); //Pashid Destruction Unit Prime Drakenblade.
				killNpc(getNpcs(234730)); //Pashid Destruction Unit Prime Battlesage.
				killNpc(getNpcs(234731)); //Pashid Destruction Unit Prime Cavalier.
				killNpc(getNpcs(234732)); //Pashid Destruction Unit Prime Fleshmender.
				killNpc(getNpcs(234733)); //Pashid Destruction Unit Prime Reserve.
				killNpc(getNpcs(234734)); //Pashid Destruction Unit Prime Skirmisher.
				//The southern shield power generator has been destroyed.
				sendMsgByRace(1402141, Race.PC_ALL, 0);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						spawn(702012, 294.20718f, 254.60352f, 295.7729f, (byte) 60); //Southern Shield Generator.
				    }
			    }, 10000);
			break;
		    case 702013: //Northern Shield Generator.
		        despawnNpc(npc);
				stopInstanceTask4();
				deleteNpc(702017); //Northern Pashid Infiltration Gate.
				deleteNpc(702227); //Northern Defence Charge 01.
				deleteNpc(702228); //Northern Defence Charge 02.
				deleteNpc(702229); //Northern Defence Charge 03.
				killNpc(getNpcs(234720)); //Pashid Destruction Unit Contender.
			    killNpc(getNpcs(234721)); //Pashid Destruction Unit Special Ambusher.
			    killNpc(getNpcs(234722)); //Pashid Destruction Unit Battlesage.
				killNpc(getNpcs(234723)); //Pashid Destruction Unit Cavalier.
				killNpc(getNpcs(234724)); //Pashid Destruction Unit Hidestitcher.
				killNpc(getNpcs(234725)); //Pashid Destruction Unit Reserve.
				killNpc(getNpcs(234726)); //Pashid Destruction Unit Skirmisher.
				killNpc(getNpcs(234727)); //Pashid Destruction Unit Grunt.
				killNpc(getNpcs(234728)); //Pashid Destruction Unit Prime Contender.
				killNpc(getNpcs(234729)); //Pashid Destruction Unit Prime Drakenblade.
				killNpc(getNpcs(234730)); //Pashid Destruction Unit Prime Battlesage.
				killNpc(getNpcs(234731)); //Pashid Destruction Unit Prime Cavalier.
				killNpc(getNpcs(234732)); //Pashid Destruction Unit Prime Fleshmender.
				killNpc(getNpcs(234733)); //Pashid Destruction Unit Prime Reserve.
				killNpc(getNpcs(234734)); //Pashid Destruction Unit Prime Skirmisher.
				//The northern shield power generator has been destroyed.
				sendMsgByRace(1402142, Race.PC_ALL, 0);
			    ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
				    public void run() {
						spawn(702013, 216.97739f, 254.4616f, 295.77353f, (byte) 0); //Northern Shield Generator.
				    }
			    }, 10000);
			break;
			case 234720: //Pashid Destruction Unit Contender.
			case 234721: //Pashid Destruction Unit Special Ambusher.
			case 234722: //Pashid Destruction Unit Battlesage.
			case 234723: //Pashid Destruction Unit Cavalier.
			case 234724: //Pashid Destruction Unit Hidestitcher.
			case 234725: //Pashid Destruction Unit Reserve.
			case 234726: //Pashid Destruction Unit Skirmisher.
			case 234727: //Pashid Destruction Unit Grunt.
			case 234728: //Pashid Destruction Unit Prime Contender.
			case 234729: //Pashid Destruction Unit Prime Drakenblade.
			case 234730: //Pashid Destruction Unit Prime Battlesage.
			case 234731: //Pashid Destruction Unit Prime Cavalier.
			case 234732: //Pashid Destruction Unit Prime Fleshmender.
			case 234733: //Pashid Destruction Unit Prime Reserve.
			case 234734: //Pashid Destruction Unit Prime Skirmisher.
				despawnNpc(npc);
			break;
			case 234686: //Remodeled Dynatoum.
				despawnNpc(npc);
				//sendMsg("[SUCCES]: You have finished <[Infernal] Illuminary Obelisk>");
				spawn(702018, 258.84213f, 251.32626f, 455.12192f, (byte) 105); //Supply Box.
				spawn(730905, 255.36038f, 254.56577f, 455.12015f, (byte) 105); //[Infernal] Illuminary Obelisk Exit.
				switch (Rnd.get(1, 2)) {
		            case 1:
				        spawn(702658, 252.05019f, 257.85583f, 455.12195f, (byte) 105); //Abbey Box.
					break;
					case 2:
					    spawn(702659, 252.05019f, 257.85583f, 455.12195f, (byte) 105); //Noble Abbey Box.
					break;
				}
			break;
		}
    }
	
	//===========================//
	//=== Eastern Shield Task ===//
	//===========================//
	protected void startEasternTask() {
		illuminaryTask1.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startEasternShield2();
				easternTaskE1.cancel(true);
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(702218, 255.56438f, 297.59488f, 321.39154f, (byte) 29); //Eastern Defence Charge 01.
            }
        }, 120000)); //...2Min
		illuminaryTask1.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startEasternShield3();
				easternTaskE2.cancel(true);
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(702219, 255.56438f, 297.59488f, 321.39154f, (byte) 29); //Eastern Defence Charge 02.
            }
        }, 240000)); //...4Min
		illuminaryTask1.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startEasternShield4();
				easternTaskE3.cancel(true);
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
            }
        }, 360000)); //...6Min
		illuminaryTask1.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				spawn(702220, 255.56438f, 297.59488f, 321.39154f, (byte) 29); //Eastern Defence Charge 03.
				instance.doOnAllPlayers(new Visitor<Player>() {
				    @Override
				    public void visit(Player player) {
						illuminaryWave++;
						stopInstance1(player);
						easternTaskE4.cancel(true);
				    }
			    });
            }
        }, 480000)); //...8Min
	}
	
	//===========================//
	//=== Western Shield Task ===//
	//===========================//
	protected void startWesternTask() {
		illuminaryTask2.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startWesternShield2();
				westernTaskW1.cancel(true);
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(702221, 255.38777f, 212.00926f, 321.37292f, (byte) 90); //Western Defence Charge 01.
            }
        }, 120000)); //...2Min
		illuminaryTask2.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startWesternShield3();
				westernTaskW2.cancel(true);
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(702222, 255.38777f, 212.00926f, 321.37292f, (byte) 90); //Western Defence Charge 02.
            }
        }, 240000)); //...4Min
		illuminaryTask2.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startWesternShield4();
				westernTaskW3.cancel(true);
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
            }
        }, 360000)); //...6Min
		illuminaryTask2.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				spawn(702223, 255.38777f, 212.00926f, 321.37292f, (byte) 90); //Western Defence Charge 03.
				instance.doOnAllPlayers(new Visitor<Player>() {
				    @Override
				    public void visit(Player player) {
						illuminaryWave++;
						stopInstance2(player);
						westernTaskW4.cancel(true);
				    }
			    });
            }
        }, 480000)); //...8Min
	}
	
	//==========================//
	//== Southern Shield Task ==//
	//==========================//
	protected void startSouthernTask() {
		illuminaryTask3.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startSouthernShield2();
				southernTaskS1.cancel(true);
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(702224, 298.13452f, 254.48087f, 295.93027f, (byte) 119); //Southern Defence Charge 01.
            }
        }, 120000)); //...2Min
		illuminaryTask3.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startSouthernShield3();
				southernTaskS2.cancel(true);
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(702225, 298.13452f, 254.48087f, 295.93027f, (byte) 119); //Southern Defence Charge 02.
            }
        }, 240000)); //...4Min
		illuminaryTask3.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startSouthernShield4();
				southernTaskS3.cancel(true);
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
            }
        }, 360000)); //...6Min
		illuminaryTask3.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				spawn(702226, 298.13452f, 254.48087f, 295.93027f, (byte) 119); //Southern Defence Charge 03.
				instance.doOnAllPlayers(new Visitor<Player>() {
				    @Override
				    public void visit(Player player) {
						illuminaryWave++;
						stopInstance3(player);
						southernTaskS4.cancel(true);
				    }
			    });
            }
        }, 480000)); //...8Min
	}
	
	//==========================//
	//== Northern Shield Task ==//
	//==========================//
	protected void startNorthernTask() {
		illuminaryTask4.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startNorthernShield2();
				northernTaskN1.cancel(true);
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(702227, 212.96484f, 254.4526f, 295.90784f, (byte) 60); //Northern Defence Charge 01.
            }
        }, 120000)); //...2Min
		illuminaryTask4.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startNorthernShield3();
				northernTaskN2.cancel(true);
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
				spawn(702228, 212.96484f, 254.4526f, 295.90784f, (byte) 60); //Northern Defence Charge 02.
            }
        }, 240000)); //...4Min
		illuminaryTask4.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				startNorthernShield4();
				northernTaskN3.cancel(true);
				//Prepare for combat! More enemies swarming in!
				sendMsgByRace(1402832, Race.PC_ALL, 0);
				//Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 30000);
            }
        }, 360000)); //...6Min
		illuminaryTask4.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				spawn(702229, 212.96484f, 254.4526f, 295.90784f, (byte) 60); //Northern Defence Charge 03.
				instance.doOnAllPlayers(new Visitor<Player>() {
				    @Override
				    public void visit(Player player) {
						illuminaryWave++;
						stopInstance4(player);
						northernTaskN4.cancel(true);
				    }
			    });
            }
        }, 480000)); //...8Min
	}
	
   /**
	* The higher the phase of the charge will spawn more difficult monsters, in the 3rd phase elite monsters will spawn.
	* Charging a shield to the 3rd phase continuously can be hard because of all the mobs you will have to handle.
	* A few easy monsters will spawn after a certain time if you leave the shield unit alone.
	* After all units have been charged to the 3rd phase, defeat the remaining monsters.
	****************************
	* Eastern Shield Generator *
	****************************/
	private void startEasternShield1() {
		easternTaskE1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234720, 252.24573f, 333.1747f, 325.59268f, (byte) 90));
				rushIlluminary((Npc)spawn(234721, 254.23112f, 333.21808f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234722, 258.46628f, 333.40833f, 325.51834f, (byte) 90));
				rushIlluminary((Npc)spawn(234723, 256.2306f, 333.3805f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234724, 259.83197f, 333.34024f, 325.64847f, (byte) 90));
			}
		}, 1000);
		easternTaskE1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234725, 252.24573f, 333.1747f, 325.59268f, (byte) 90));
				rushIlluminary((Npc)spawn(234726, 254.23112f, 333.21808f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234727, 258.46628f, 333.40833f, 325.51834f, (byte) 90));
				rushIlluminary((Npc)spawn(234728, 256.2306f, 333.3805f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234729, 259.83197f, 333.34024f, 325.64847f, (byte) 90));
			}
		}, 30000);
	}
	private void startEasternShield2() {
		easternTaskE2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234730, 252.24573f, 333.1747f, 325.59268f, (byte) 90));
				rushIlluminary((Npc)spawn(234731, 254.23112f, 333.21808f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234732, 258.46628f, 333.40833f, 325.51834f, (byte) 90));
				rushIlluminary((Npc)spawn(234733, 256.2306f, 333.3805f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234734, 259.83197f, 333.34024f, 325.64847f, (byte) 90));
			}
		}, 1000);
		easternTaskE2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234720, 252.24573f, 333.1747f, 325.59268f, (byte) 90));
				rushIlluminary((Npc)spawn(234721, 254.23112f, 333.21808f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234722, 258.46628f, 333.40833f, 325.51834f, (byte) 90));
				rushIlluminary((Npc)spawn(234723, 256.2306f, 333.3805f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234724, 259.83197f, 333.34024f, 325.64847f, (byte) 90));
			}
		}, 30000);
	}
	private void startEasternShield3() {
		easternTaskE3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234725, 252.24573f, 333.1747f, 325.59268f, (byte) 90));
				rushIlluminary((Npc)spawn(234726, 254.23112f, 333.21808f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234727, 258.46628f, 333.40833f, 325.51834f, (byte) 90));
				rushIlluminary((Npc)spawn(234728, 256.2306f, 333.3805f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234729, 259.83197f, 333.34024f, 325.64847f, (byte) 90));
			}
		}, 1000);
		easternTaskE3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234730, 252.24573f, 333.1747f, 325.59268f, (byte) 90));
				rushIlluminary((Npc)spawn(234731, 254.23112f, 333.21808f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234732, 258.46628f, 333.40833f, 325.51834f, (byte) 90));
				rushIlluminary((Npc)spawn(234733, 256.2306f, 333.3805f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234734, 259.83197f, 333.34024f, 325.64847f, (byte) 90));
			}
		}, 30000);
	}
	private void startEasternShield4() {
		easternTaskE4 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234720, 252.24573f, 333.1747f, 325.59268f, (byte) 90));
				rushIlluminary((Npc)spawn(234721, 254.23112f, 333.21808f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234722, 258.46628f, 333.40833f, 325.51834f, (byte) 90));
				rushIlluminary((Npc)spawn(234723, 256.2306f, 333.3805f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234724, 259.83197f, 333.34024f, 325.64847f, (byte) 90));
			}
		}, 1000);
		easternTaskE4 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234725, 252.24573f, 333.1747f, 325.59268f, (byte) 90));
				rushIlluminary((Npc)spawn(234726, 254.23112f, 333.21808f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234727, 258.46628f, 333.40833f, 325.51834f, (byte) 90));
				rushIlluminary((Npc)spawn(234728, 256.2306f, 333.3805f, 325.49332f, (byte) 90));
				rushIlluminary((Npc)spawn(234729, 259.83197f, 333.34024f, 325.64847f, (byte) 90));
			}
		}, 30000);
	}
	
   /****************************
	* Western Shield Generator *
	****************************/
	private void startWesternShield1() {
		westernTaskW1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234720, 258.78595f, 176.05591f, 325.59268f, (byte) 30));
				rushIlluminary((Npc)spawn(234721, 257.29633f, 176.01747f, 325.55893f, (byte) 30));
				rushIlluminary((Npc)spawn(234722, 253.48524f, 175.99721f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234723, 255.67467f, 176.00883f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234724, 251.44252f, 175.98637f, 325.64847f, (byte) 30));
			}
		}, 1000);
		westernTaskW1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234725, 258.78595f, 176.05591f, 325.59268f, (byte) 30));
				rushIlluminary((Npc)spawn(234726, 257.29633f, 176.01747f, 325.55893f, (byte) 30));
				rushIlluminary((Npc)spawn(234727, 253.48524f, 175.99721f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234728, 255.67467f, 176.00883f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234729, 251.44252f, 175.98637f, 325.64847f, (byte) 30));
			}
		}, 30000);
	}
	private void startWesternShield2() {
		westernTaskW2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234730, 258.78595f, 176.05591f, 325.59268f, (byte) 30));
				rushIlluminary((Npc)spawn(234731, 257.29633f, 176.01747f, 325.55893f, (byte) 30));
				rushIlluminary((Npc)spawn(234732, 253.48524f, 175.99721f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234733, 255.67467f, 176.00883f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234734, 251.44252f, 175.98637f, 325.64847f, (byte) 30));
			}
		}, 1000);
		westernTaskW2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234720, 258.78595f, 176.05591f, 325.59268f, (byte) 30));
				rushIlluminary((Npc)spawn(234721, 257.29633f, 176.01747f, 325.55893f, (byte) 30));
				rushIlluminary((Npc)spawn(234722, 253.48524f, 175.99721f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234723, 255.67467f, 176.00883f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234724, 251.44252f, 175.98637f, 325.64847f, (byte) 30));
			}
		}, 30000);
	}
	private void startWesternShield3() {
		westernTaskW3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234725, 258.78595f, 176.05591f, 325.59268f, (byte) 30));
				rushIlluminary((Npc)spawn(234726, 257.29633f, 176.01747f, 325.55893f, (byte) 30));
				rushIlluminary((Npc)spawn(234727, 253.48524f, 175.99721f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234728, 255.67467f, 176.00883f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234729, 251.44252f, 175.98637f, 325.64847f, (byte) 30));
			}
		}, 1000);
		westernTaskW3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234730, 258.78595f, 176.05591f, 325.59268f, (byte) 30));
				rushIlluminary((Npc)spawn(234731, 257.29633f, 176.01747f, 325.55893f, (byte) 30));
				rushIlluminary((Npc)spawn(234732, 253.48524f, 175.99721f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234733, 255.67467f, 176.00883f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234734, 251.44252f, 175.98637f, 325.64847f, (byte) 30));
			}
		}, 30000);
	}
	private void startWesternShield4() {
		westernTaskW4 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234720, 258.78595f, 176.05591f, 325.59268f, (byte) 30));
				rushIlluminary((Npc)spawn(234721, 257.29633f, 176.01747f, 325.55893f, (byte) 30));
				rushIlluminary((Npc)spawn(234722, 253.48524f, 175.99721f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234723, 255.67467f, 176.00883f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234724, 251.44252f, 175.98637f, 325.64847f, (byte) 30));
			}
		}, 1000);
		westernTaskW4 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234725, 258.78595f, 176.05591f, 325.59268f, (byte) 30));
				rushIlluminary((Npc)spawn(234726, 257.29633f, 176.01747f, 325.55893f, (byte) 30));
				rushIlluminary((Npc)spawn(234727, 253.48524f, 175.99721f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234728, 255.67467f, 176.00883f, 325.49332f, (byte) 30));
				rushIlluminary((Npc)spawn(234729, 251.44252f, 175.98637f, 325.64847f, (byte) 30));
			}
		}, 30000);
	}
	
	/***************************
	* Southern Shield Generator *
	****************************/
	private void startSouthernShield1() {
		southernTaskS1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234720, 336.21823f, 258.05798f, 292.4295f, (byte) 60));
				rushIlluminary((Npc)spawn(234721, 336.28296f, 256.22827f, 292.3325f, (byte) 60));
				rushIlluminary((Npc)spawn(234722, 336.35062f, 252.48618f, 292.33862f, (byte) 60));
				rushIlluminary((Npc)spawn(234723, 336.3128f, 254.57924f, 292.33252f, (byte) 60));
				rushIlluminary((Npc)spawn(234724, 336.38608f, 250.51807f, 292.46326f, (byte) 60));
			}
		}, 1000);
		southernTaskS1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234725, 336.21823f, 258.05798f, 292.4295f, (byte) 60));
				rushIlluminary((Npc)spawn(234726, 336.28296f, 256.22827f, 292.3325f, (byte) 60));
				rushIlluminary((Npc)spawn(234727, 336.35062f, 252.48618f, 292.33862f, (byte) 60));
				rushIlluminary((Npc)spawn(234728, 336.3128f, 254.57924f, 292.33252f, (byte) 60));
				rushIlluminary((Npc)spawn(234729, 336.38608f, 250.51807f, 292.46326f, (byte) 60));
			}
		}, 30000);
	}
	private void startSouthernShield2() {
		southernTaskS2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234730, 336.21823f, 258.05798f, 292.4295f, (byte) 60));
				rushIlluminary((Npc)spawn(234731, 336.28296f, 256.22827f, 292.3325f, (byte) 60));
				rushIlluminary((Npc)spawn(234732, 336.35062f, 252.48618f, 292.33862f, (byte) 60));
				rushIlluminary((Npc)spawn(234733, 336.3128f, 254.57924f, 292.33252f, (byte) 60));
				rushIlluminary((Npc)spawn(234734, 336.38608f, 250.51807f, 292.46326f, (byte) 60));
			}
		}, 1000);
		southernTaskS2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234720, 336.21823f, 258.05798f, 292.4295f, (byte) 60));
				rushIlluminary((Npc)spawn(234721, 336.28296f, 256.22827f, 292.3325f, (byte) 60));
				rushIlluminary((Npc)spawn(234722, 336.35062f, 252.48618f, 292.33862f, (byte) 60));
				rushIlluminary((Npc)spawn(234723, 336.3128f, 254.57924f, 292.33252f, (byte) 60));
				rushIlluminary((Npc)spawn(234724, 336.38608f, 250.51807f, 292.46326f, (byte) 60));
			}
		}, 30000);
	}
	private void startSouthernShield3() {
		southernTaskS3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234725, 336.21823f, 258.05798f, 292.4295f, (byte) 60));
				rushIlluminary((Npc)spawn(234726, 336.28296f, 256.22827f, 292.3325f, (byte) 60));
				rushIlluminary((Npc)spawn(234727, 336.35062f, 252.48618f, 292.33862f, (byte) 60));
				rushIlluminary((Npc)spawn(234728, 336.3128f, 254.57924f, 292.33252f, (byte) 60));
				rushIlluminary((Npc)spawn(234729, 336.38608f, 250.51807f, 292.46326f, (byte) 60));
			}
		}, 1000);
		southernTaskS3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234730, 336.21823f, 258.05798f, 292.4295f, (byte) 60));
				rushIlluminary((Npc)spawn(234731, 336.28296f, 256.22827f, 292.3325f, (byte) 60));
				rushIlluminary((Npc)spawn(234732, 336.35062f, 252.48618f, 292.33862f, (byte) 60));
				rushIlluminary((Npc)spawn(234733, 336.3128f, 254.57924f, 292.33252f, (byte) 60));
				rushIlluminary((Npc)spawn(234734, 336.38608f, 250.51807f, 292.46326f, (byte) 60));
			}
		}, 30000);
	}
	private void startSouthernShield4() {
		southernTaskS4 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234720, 336.21823f, 258.05798f, 292.4295f, (byte) 60));
				rushIlluminary((Npc)spawn(234721, 336.28296f, 256.22827f, 292.3325f, (byte) 60));
				rushIlluminary((Npc)spawn(234722, 336.35062f, 252.48618f, 292.33862f, (byte) 60));
				rushIlluminary((Npc)spawn(234723, 336.3128f, 254.57924f, 292.33252f, (byte) 60));
				rushIlluminary((Npc)spawn(234724, 336.38608f, 250.51807f, 292.46326f, (byte) 60));
			}
		}, 1000);
		southernTaskS4 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234725, 336.21823f, 258.05798f, 292.4295f, (byte) 60));
				rushIlluminary((Npc)spawn(234726, 336.28296f, 256.22827f, 292.3325f, (byte) 60));
				rushIlluminary((Npc)spawn(234727, 336.35062f, 252.48618f, 292.33862f, (byte) 60));
				rushIlluminary((Npc)spawn(234728, 336.3128f, 254.57924f, 292.33252f, (byte) 60));
				rushIlluminary((Npc)spawn(234729, 336.38608f, 250.51807f, 292.46326f, (byte) 60));
			}
		}, 30000);
	}
	
	/****************************
	* Northern Shield Generator *
	****************************/
	private void startNorthernShield1() {
		northernTaskN1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234720, 176.56479f, 251.09068f, 292.42026f, (byte) 119));
				rushIlluminary((Npc)spawn(234721, 176.4995f, 252.93555f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234722, 176.41188f, 257.24088f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234723, 176.4588f, 254.93521f, 292.33252f, (byte) 0));
				rushIlluminary((Npc)spawn(234724, 176.37492f, 259.05646f, 292.55435f, (byte) 0));
			}
		}, 1000);
		northernTaskN1 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234725, 176.56479f, 251.09068f, 292.42026f, (byte) 119));
				rushIlluminary((Npc)spawn(234726, 176.4995f, 252.93555f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234727, 176.41188f, 257.24088f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234728, 176.4588f, 254.93521f, 292.33252f, (byte) 0));
				rushIlluminary((Npc)spawn(234729, 176.37492f, 259.05646f, 292.55435f, (byte) 0));
			}
		}, 30000);
	}
	private void startNorthernShield2() {
		northernTaskN2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234730, 176.56479f, 251.09068f, 292.42026f, (byte) 119));
				rushIlluminary((Npc)spawn(234731, 176.4995f, 252.93555f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234732, 176.41188f, 257.24088f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234733, 176.4588f, 254.93521f, 292.33252f, (byte) 0));
				rushIlluminary((Npc)spawn(234734, 176.37492f, 259.05646f, 292.55435f, (byte) 0));
			}
		}, 1000);
		northernTaskN2 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234720, 176.56479f, 251.09068f, 292.42026f, (byte) 119));
				rushIlluminary((Npc)spawn(234721, 176.4995f, 252.93555f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234722, 176.41188f, 257.24088f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234723, 176.4588f, 254.93521f, 292.33252f, (byte) 0));
				rushIlluminary((Npc)spawn(234724, 176.37492f, 259.05646f, 292.55435f, (byte) 0));
			}
		}, 30000);
	}
	private void startNorthernShield3() {
		northernTaskN3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234725, 176.56479f, 251.09068f, 292.42026f, (byte) 119));
				rushIlluminary((Npc)spawn(234726, 176.4995f, 252.93555f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234727, 176.41188f, 257.24088f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234728, 176.4588f, 254.93521f, 292.33252f, (byte) 0));
				rushIlluminary((Npc)spawn(234729, 176.37492f, 259.05646f, 292.55435f, (byte) 0));
			}
		}, 1000);
		northernTaskN3 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234730, 176.56479f, 251.09068f, 292.42026f, (byte) 119));
				rushIlluminary((Npc)spawn(234731, 176.4995f, 252.93555f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234732, 176.41188f, 257.24088f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234733, 176.4588f, 254.93521f, 292.33252f, (byte) 0));
				rushIlluminary((Npc)spawn(234734, 176.37492f, 259.05646f, 292.55435f, (byte) 0));
			}
		}, 30000);
	}
	private void startNorthernShield4() {
		northernTaskN4 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234720, 176.56479f, 251.09068f, 292.42026f, (byte) 119));
				rushIlluminary((Npc)spawn(234721, 176.4995f, 252.93555f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234722, 176.41188f, 257.24088f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234723, 176.4588f, 254.93521f, 292.33252f, (byte) 0));
				rushIlluminary((Npc)spawn(234724, 176.37492f, 259.05646f, 292.55435f, (byte) 0));
			}
		}, 1000);
		northernTaskN4 = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				rushIlluminary((Npc)spawn(234725, 176.56479f, 251.09068f, 292.42026f, (byte) 119));
				rushIlluminary((Npc)spawn(234726, 176.4995f, 252.93555f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234727, 176.41188f, 257.24088f, 292.3325f, (byte) 0));
				rushIlluminary((Npc)spawn(234728, 176.4588f, 254.93521f, 292.33252f, (byte) 0));
				rushIlluminary((Npc)spawn(234729, 176.37492f, 259.05646f, 292.55435f, (byte) 0));
			}
		}, 30000);
	}
	
	private void rushIlluminary(final Npc npc) {
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
	
   /**
	* Activate The Seal:
	* When all shield units have been charged up to the 3rd phase, you can activate the passage to the final boss.
	* When you activate the seal the final boss will appear and the fight will begin.
	*/
	private void shieldControl() {
		if (illuminaryWave == 4) {
			deleteNpc(702010); //Eastern Shield Generator.
			deleteNpc(702011); //Western Shield Generator.
			deleteNpc(702012); //Southern Shield Generator.
			deleteNpc(702013); //Northern Shield Generator.
			deleteNpc(702014); //Eastern Pashid Infiltration Gate.
			deleteNpc(702015); //Western Pashid Infiltration Gate.
			deleteNpc(702016); //Southern Pashid Infiltration Gate.
			deleteNpc(702017); //Northern Pashid Infiltration Gate.
			//The shield is activated and the Pashid Destruction Unit is retreating.
			//The Shield Control Room Teleporter has appeared.
			sendMsgByRace(1402202, Race.PC_ALL, 0);
			//Shield Chamber Teleport Device appeared.
			sendMsgByRace(1403146, Race.PC_ALL, 10000);
			//Shield Complete.
			spawn(702217, 255.31036f, 254.66649f, 455.12018f, (byte) 91);
			//Shield Defence Complete.
			spawn(702287, 255.13590f, 254.21944f, 337.96027f, (byte) 109);
			//Shield Control Room Teleporter.
			spawn(730886, 255.47392f, 293.56177f, 321.18497f, (byte) 89);
			spawn(730886, 255.55742f, 216.03549f, 321.21344f, (byte) 30);
			spawn(730886, 294.20718f, 254.60352f, 295.77290f, (byte) 60);
			spawn(730886, 216.97739f, 254.46160f, 295.77353f, (byte) 0);
		}
	}
	
	private void illuminaryToDynatoum(Player player) {
		teleport(player, 266.04742f, 244.20813f, 455.17575f, (byte) 45);
	}
	
	private void teleport(float x, float y, float z, byte h) {
		for (Player playerInside: instance.getPlayersInside()) {
			if (playerInside.isOnline()) {
				illuminaryToDynatoum(playerInside);
			}
		}
	}
	
	protected void teleport(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, mapId, instanceId, x, y, z, h);
	}
	
	protected void stopInstance1(Player player) {
		shieldControl();
		stopInstanceTask1();
	}
	protected void stopInstance2(Player player) {
		shieldControl();
		stopInstanceTask2();
	}
	protected void stopInstance3(Player player) {
		shieldControl();
		stopInstanceTask3();
	}
	protected void stopInstance4(Player player) {
		shieldControl();
		stopInstanceTask4();
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
	
	private void sendMovie(Player player, int movie) {
        if (!movies.contains(movie)) {
             movies.add(movie);
             PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
        }
    }
	
	@Override
	public void onLeaveInstance(Player player) {
		removeItems(player);
	}
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
	}
	
	public void removeItems(Player player) {
        Storage storage = player.getInventory();
        storage.decreaseByItemId(164000289, storage.getItemCountByItemId(164000289));
		storage.decreaseByItemId(164000290, storage.getItemCountByItemId(164000290));
    }
	
	@Override
	public void onInstanceDestroy() {
		stopInstanceTask1();
		stopInstanceTask2();
		stopInstanceTask3();
		stopInstanceTask4();
		isInstanceDestroyed = true;
		doors.clear();
		movies.clear();
	}
	
	private void stopInstanceTask1() {
        for (FastList.Node<Future<?>> n = illuminaryTask1.head(), end = illuminaryTask1.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	private void stopInstanceTask2() {
        for (FastList.Node<Future<?>> n = illuminaryTask2.head(), end = illuminaryTask2.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	private void stopInstanceTask3() {
        for (FastList.Node<Future<?>> n = illuminaryTask3.head(), end = illuminaryTask3.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	private void stopInstanceTask4() {
        for (FastList.Node<Future<?>> n = illuminaryTask4.head(), end = illuminaryTask4.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
	protected void despawnNpc(Npc npc) {
        if (npc != null) {
            npc.getController().onDelete();
        }
    }
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
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
	
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
}