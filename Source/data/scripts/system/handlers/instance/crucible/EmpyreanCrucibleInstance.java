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
package instance.crucible;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.NpcActions;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.StageType;
import com.aionemu.gameserver.model.instance.playerreward.CruciblePlayerReward;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_STAGE_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.DispelCategoryType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/****/
/** Author (Encom)
/****/

@InstanceID(300300000)
public class EmpyreanCrucibleInstance extends CrucibleInstance
{
	private byte stage;
	private boolean isDoneStage4 = false;
	private boolean isDoneStage6Round2 = false;
	private boolean isDoneStage6Round1 = false;
	private List<Npc> npcs = new ArrayList<Npc>();
	private List<EmpyreanStage> empyreanStage = new ArrayList<EmpyreanStage>();
	
	private class EmpyreanStage {
		private List<Npc> npcs = new ArrayList<Npc>();
		
		public EmpyreanStage(List<Npc> npcs) {
			this.npcs = npcs;
		}
		
		private boolean containNpc() {
			for (Npc npc : npcs) {
				if (instance.getNpcs().contains(npc)) {
					return true;
				}
			}
			return false;
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		stage = 0;
		sp(799567, 345.25107f, 349.40176f, 96.09097f, (byte) 0, 10000);
	}
	
	private void addItems(Player player) {
        ItemService.addItem(player, 186000124, 5); //Worthiness Ticket.
    }
	
	@Override
	public void onEnterInstance(Player player) {
		boolean isNew = !instanceReward.containPlayer(player.getObjectId());
		super.onEnterInstance(player);
		addItems(player);
		if (isNew && stage > 0) {
			moveToReadyRoom(player);
			//Training is in progress. You must stay in the Ready Room until you can join.
			sendMsgByRace(1401082, Race.PC_ALL, 2000);
		}
		CruciblePlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (playerReward.isPlayerLeave()) {
			onExitInstance(player);
			return;
		} else if (playerReward.isRewarded()) {
			doReward(player);
		}
		PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(instanceReward));
		PacketSendUtility.sendPacket(player, new SM_INSTANCE_STAGE_INFO(2, stageType.getId(), stageType.getType()));
	}
	
	private void sendPacket(final int points, final int nameId) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					CruciblePlayerReward playerReward = getPlayerReward(player.getObjectId());
					if (nameId != 0) {
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId(nameId * 2 + 1), points));
					} if (!playerReward.isRewarded()) {
						playerReward.addPoints(points);
					}
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(instanceReward));
				}
			}
		});
	}
	
	private void sendEventPacket(final StageType type, final int time) {
		this.stageType = type;
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, new SM_INSTANCE_STAGE_INFO(2, type.getId(), type.getType()));
					}
				});
			}
		}, time);
	}
	
	@Override
	public void onDie(Npc npc) {
		if (npcs.contains(npc)) {
			npcs.remove(npc);
		}
		EmpyreanStage es = getEmpyreanStage(npc);
		int point = 0;
		switch (npc.getNpcId()) {
			//STAGE 1 [Kaisinel Version - End Of Stage: ~10,000 Points]
			case 205395: //Kaisinel Gladiator Apprentice.
			case 205396: //Kaisinel Templar Apprentice.
			case 205397: //Kaisinel Assassin Apprentice.
			case 205398: //Kaisinel Ranger Apprentice.
			case 205399: //Kaisinel Sorcerer Apprentice.
			case 205400: //Kaisinel Spiritmaster Apprentice.
			case 205401: //Kaisinel Cleric Apprentice.
			case 205402: //Kaisinel Chanter Apprentice.
			case 217476: //Kaisinel Gladiator Apprentice.
			case 217477: //Kaisinel Templar Apprentice.
			case 217478: //Kaisinel Assassin Apprentice.
			case 217479: //Kaisinel Ranger Apprentice.
			case 217480: //Kaisinel Sorcerer Apprentice.
			case 217481: //Kaisinel Spiritmaster Apprentice.
			case 217482: //Kaisinel Cleric Apprentice.
			case 217483: //Kaisinel Chanter Apprentice.
			//STAGE 1 [Marchutan Version - End Of Stage: ~10,000 Points]
			case 205404: //Marchutan Gladiator Apprentice.
			case 205405: //Marchutan Templar Apprentice.
			case 205406: //Marchutan Assassin Apprentice.
			case 205407: //Marchutan Ranger Apprentice.
			case 205408: //Marchutan Sorcerer Apprentice.
			case 205409: //Marchutan Spiritmaster Apprentice.
			case 205410: //Marchutan Cleric Apprentice.
			case 205411: //Marchutan Chanter Apprentice.
			case 217485: //Marchutan Gladiator Apprentice.
			case 217486: //Marchutan Templar Apprentice.
			case 217487: //Marchutan Assassin Apprentice.
			case 217488: //Marchutan Ranger Apprentice.
			case 217489: //Marchutan Sorcerer Apprentice.
			case 217490: //Marchutan Spiritmaster Apprentice.
			case 217491: //Marchutan Cleric Apprentice.
			case 217492: //Marchutan Chanter Apprentice.
			    point += 500;
			break;
			case 217484: //Instructor Munus.
			case 217493: //Instructor Geor.
			    point += 2000;
			break;
			//STAGE 2 [End Of Stage: ~21,000 Points]
			case 217502: //Veteran Nunu Rake Warrior.
			case 217503: //Veteran Nunu Gatherer.
			case 217504: //Seasoned Nunu Lookout.
			case 217505: //Mist Mane Secret Shaman.
			case 217506: //Mist Mane Secret Healer.
			case 217507: //Brutal Mist Mane Watcher.
			case 217508: //Brutal Mist Mane Scratcher.
			    point += 500;
			break;
			case 217500: //Chieftain Kurka.
			case 217501: //Grand Chieftain Saendukal.
			case 217509: //Chieftain Nuaka.
			case 217510: //Grand Chieftain Kasika.
			    point += 2500;
			break;
			//STAGE 3 [End Of Stage: ~34,000 Points]
			case 217511: //Dust Spirit.
            case 217512: //Blaze Spirit.
            case 217513: //Waterdrop Spirit.
            case 217514: //Zephyr Spirit.
			    point += 130;
            break;
			case 217515: //Soil Spirit.
			case 217516: //Fire Spirit.
			case 217517: //Water Spirit.
			case 217518: //Wind Spirit.
			    point += 150;
            break;
            case 217520: //Flame Spirit.
			case 217522: //Cyclone Spirit.
			    point += 250;
            break;
			case 217519: //Earth Spirit.
            case 217521: //Lake Spirit.
			case 217523: //Terra Spirit.
			case 217524: //Magma Spirit.
			case 217525: //Sea Spirit.
			case 217526: //Storm Spirit.
                point += 500;
            break;
			case 217527: //Tran Of Fire.
			case 217528: //Tran Of Wind.
                point += 2040;
            break;
			//STAGE 4 [End Of Stage: ~48,000 Points]
			case 217557: //Assailant.
            case 217558: //Trooper.
			    point += 500;
            break;
            case 217559: //Bloodbinder.
			case 217563: //Drakan Destroyer.
			    point += 750;
            break;
            case 217560: //Scourge.
			case 217561: //Snakepriest.
			case 217562: //Sorcerer.
			case 217564: //Elite Drakan Outrider.
			case 217565: //Elite Drakan Mage.
			case 217566: //Elite Drakan Healer.
			    point += 800;
            break;
			case 217652: //Elite Drakan Outrider.
			case 217653: //Elite Drakan Mage.
                point += 250;
            break;
			case 217567: //Commander Bakarma.
			    point += 1200;
            break;
			//STAGE 5 [Azoturan Version - [End Of Stage: ~63,000 Points]
			case 217529: //Enhanced Pretor.
			    point += 500;
			break;
			case 217530: //Trained Tipolid.
			    point += 1000;
			break;
			case 217531: //Azoturan Zealfighter.
			case 217532: //Azoturan Zealdefender.
			case 217533: //Azoturan Zealambusher.
			case 217534: //Azoturan Zealmage.
			case 217535: //Azoturan Zealsniper.
			case 217536: //Azoturan Zealmedic.
			    point += 1500;
			break;
			case 217543: //Lepharist Captain.
			case 217544: //Supervisor Magnis.
			    point += 2500;
			break;
			//STAGE 5 [Steel Rake Version - [End Of Stage: ~63,000 Points]
			case 217545: //Steel Rake Sailor.
			case 217547: //Steel Rake Elite Vaegir.
			case 217548: //Steel Rake Archer.
			case 217549: //Steel Rake Shaman.
			case 217550: //Steel Rake Healer.
			    point += 1000;
			break;
			case 217551: //Freed Genie.
			case 217552: //Madame Bovariki.
			case 217553: //Golden Eye Mantutu.
			case 217554: //Engineer Lahulahu.
			case 217555: //Chief Gunner Koakoa.
			case 217556: //Brass-Eye Grogget.
			    point += 2500;
			break;
			//STAGE 6 [End Of Stage: ~81,000 Points]
			case 217568: //Elite Graveknight Warrior.
			    point += 1000;
			break;
			case 217569: //Lich Necromancer.
                point += 500;
			break;
			case 217570: //Stinking Zombie.
            case 217572: //Equitatus Warrior.
			case 217573: //Spectral Warrior.
                point += 2000;
			break;
			//STAGE 7 [Asmodians Version - [End Of Stage: ~105,000 Points]
			case 217578: //Boreas.
			case 217579: //Jumentis.
			case 217580: //Charna.
			case 217581: //Thrasymedes.
			case 217586: //Miriya.
			    point += 4800;
			break;
			//STAGE 7 [Elyos Version - [End Of Stage: ~105,000 Points]
			case 217582: //Traufnir.
			case 217583: //Sigyn.
			case 217584: //Sif.
			case 217585: //Freyr.
			case 217587: //Aud.
                point += 4800;
			break;
            //STAGE 8 [End Of Stage: ~141,000 Points]
            case 217588: //Kromede The Corrupt.
			case 217589: //Vile Judge Kromede.
			case 217590: //Queen Alukina.
			case 217591: //Bollvig Blackheart.
			case 217592: //RM-138C.
			case 217593: //RM-1337C.
			    point += 7200;
			break;
			//STAGE 9 [End Of Stage: ~199,000 Points]
            case 217594: //Crab Norris.
			case 217595: //King Consierd.
			case 217596: //Takun The Terrible.
			case 217597: //Gojira.
			case 217598: //Andre.
			case 217599: //Kamara.
                point += 9666;
			break;
			//STAGE 10 [End Of Stage: ~498,000 Points]
			//(players will earn about 193,000 Points by slaying Tahabata Pyrelord)
			case 217600: //Anuhart Scourge.
			case 217601: //Anuhart Willwarper.
			case 217602: //Anuhart Priest.
			case 217603: //Anuhart Vindicator.
			case 217604: //Anuhart Shadow.
			case 217605: //Anuhart Aionbane.
			case 217606: //Anuhart Dark Healer.
			    point += 2000;
			break;
			case 217607: //Marabata Of Strength.
			    point += 10000;
			break;
            case 217608: //Tahabata Pyrelord.
			    point += 193000;
			break;
			case 217609: //Vanktrist.
                point += 82000;
			break;
			//STAGE BONUS 2//
			case 217737: //King Saam.
			    point += 3500;
			break;
			//STAGE BONUS 3//
			case 217740: //Seismik.
			case 217741: //Splashdown.
			case 217742: //Crematorux.
			case 217743: //Windlash.
                point += 500;
			break;
			//STAGE BONUS 4//
			case 217745: //Wind Drakie.
			case 217746: //Water Drakie.
			case 217747: //Earth Drakie.
			case 217748: //Thunder Drakie.
                point += 50;
			break;
			//STAGE BONUS 6//
			case 217750: //Administrator Arminos.
                point += 4000;
			break;
		} if (point != 0) { 
			sendPacket(point, npc.getObjectTemplate().getNameId());
		} switch (npc.getNpcId()) {
		   /**
			* Kaisinel Version
			*/
			case 217476:
			case 217477:
			case 217478:
			case 217479:
				despawnNpc(npc);
				if (getNpcs(217476).isEmpty() &&
				    getNpcs(217477).isEmpty() &&
					getNpcs(217478).isEmpty() &&
				    getNpcs(217479).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_1_ROUND_2, 2000);
					//Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
					sp(217480, 336.585f, 341.21777f, 96.090935f, (byte) 11, 2000);
				    sp(217481, 333.94592f, 349.2856f, 96.090935f, (byte) 119, 3000);
				    sp(217482, 335.89075f, 356.3005f, 96.090935f, (byte) 109, 4000);
				    sp(217483, 346.5103f, 336.82236f, 96.090935f, (byte) 29, 5000);
				}
			break;
			case 217480:
			case 217481:
			case 217482:
			case 217483:
                despawnNpc(npc);
				if (getNpcs(217480).isEmpty() &&
				    getNpcs(217481).isEmpty() &&
					getNpcs(217482).isEmpty() &&
				    getNpcs(217483).isEmpty()) {
				    sendEventPacket(StageType.START_STAGE_1_ROUND_3, 2000);
				    //Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
                    sp(205395, 336.585f, 341.21777f, 96.090935f, (byte) 11, 2000);
				    sp(205396, 333.94592f, 349.2856f, 96.090935f, (byte) 119, 3000);
				    sp(205397, 335.89075f, 356.3005f, 96.090935f, (byte) 109, 4000);
				    sp(205398, 346.5103f, 336.82236f, 96.090935f, (byte) 29, 5000);
				}
            break;
			case 205395:
			case 205396:
			case 205397:
			case 205398:
                despawnNpc(npc);
				if (getNpcs(205395).isEmpty() &&
				    getNpcs(205396).isEmpty() &&
					getNpcs(205397).isEmpty() &&
				    getNpcs(205398).isEmpty()) {
				    sendEventPacket(StageType.START_STAGE_1_ROUND_4, 2000);
				    //Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
                    sp(205399, 336.585f, 341.21777f, 96.090935f, (byte) 11, 2000);
				    sp(205400, 333.94592f, 349.2856f, 96.090935f, (byte) 119, 3000);
				    sp(205401, 335.89075f, 356.3005f, 96.090935f, (byte) 109, 4000);
				    sp(205402, 346.5103f, 336.82236f, 96.090935f, (byte) 29, 5000);
				}
            break;
			case 205399:
			case 205400:
			case 205401:
			case 205402:
                despawnNpc(npc);
				if (getNpcs(205399).isEmpty() &&
				    getNpcs(205400).isEmpty() &&
					getNpcs(205401).isEmpty() &&
				    getNpcs(205402).isEmpty()) {
				    sendEventPacket(StageType.START_STAGE_1_ROUND_5, 2000);
				    //Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
				    sp(217484, 345.3899f, 349.10034f, 96.09097f, (byte) 0, 2000); //Instructor Munus.
				}
            break;
		   /**
			* Marchutan Version
			*/
			case 217485:
			case 217486:
			case 217487:
			case 217488:
				despawnNpc(npc);
				if (getNpcs(217485).isEmpty() &&
				    getNpcs(217486).isEmpty() &&
					getNpcs(217487).isEmpty() &&
				    getNpcs(217488).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_1_ROUND_2, 2000);
					//Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
					sp(217489, 336.585f, 341.21777f, 96.090935f, (byte) 11, 2000);
				    sp(217490, 333.94592f, 349.2856f, 96.090935f, (byte) 119, 3000);
				    sp(217491, 335.89075f, 356.3005f, 96.090935f, (byte) 109, 4000);
				    sp(217492, 346.5103f, 336.82236f, 96.090935f, (byte) 29, 5000);
				}
			break;
			case 217489:
			case 217490:
			case 217491:
			case 217492:
                despawnNpc(npc);
				if (getNpcs(217489).isEmpty() &&
				    getNpcs(217490).isEmpty() &&
					getNpcs(217491).isEmpty() &&
				    getNpcs(217492).isEmpty()) {
				    sendEventPacket(StageType.START_STAGE_1_ROUND_3, 2000);
				    //Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
                    sp(205404, 336.585f, 341.21777f, 96.090935f, (byte) 11, 2000);
				    sp(205405, 333.94592f, 349.2856f, 96.090935f, (byte) 119, 3000);
				    sp(205406, 335.89075f, 356.3005f, 96.090935f, (byte) 109, 4000);
				    sp(205407, 346.5103f, 336.82236f, 96.090935f, (byte) 29, 5000);
				}
            break;
			case 205404:
			case 205405:
			case 205406:
			case 205407:
                despawnNpc(npc);
				if (getNpcs(205404).isEmpty() &&
				    getNpcs(205405).isEmpty() &&
					getNpcs(205406).isEmpty() &&
				    getNpcs(205407).isEmpty()) {
				    sendEventPacket(StageType.START_STAGE_1_ROUND_4, 2000);
				    //Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
                    sp(205408, 336.585f, 341.21777f, 96.090935f, (byte) 11, 2000);
				    sp(205409, 333.94592f, 349.2856f, 96.090935f, (byte) 119, 3000);
				    sp(205410, 335.89075f, 356.3005f, 96.090935f, (byte) 109, 4000);
				    sp(205411, 346.5103f, 336.82236f, 96.090935f, (byte) 29, 5000);
				}
            break;
			case 205408:
			case 205409:
			case 205410:
			case 205411:
                despawnNpc(npc);
				if (getNpcs(205408).isEmpty() &&
				    getNpcs(205409).isEmpty() &&
					getNpcs(205410).isEmpty() &&
				    getNpcs(205411).isEmpty()) {
				    sendEventPacket(StageType.START_STAGE_1_ROUND_5, 2000);
				    //Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
					sp(217493, 345.3899f, 349.10034f, 96.09097f, (byte) 0, 2000); //Instructor Geor.
				}
            break;
			case 217484: //Instructor Munus.
			case 217493: //Instructor Geor.
				despawnNpc(npc);
				sendEventPacket(StageType.PASS_GROUP_STAGE_1, 0);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 2000);
				//You have passed Stage %0!
				sendMsgByRace(1400930, Race.PC_ALL, 4000);
				//A Worthiness Ticket Box has appeared in the Illusion Stadium.
				sendMsgByRace(1400975, Race.PC_ALL, 6000);
                spawn(217756, 345.66763f, 355.47922f, 96.154961f, (byte) 0, 79); //Worthiness Ticket Box.
				spawn(217756, 349.04822f, 357.50226f, 96.154961f, (byte) 0, 280); //Worthiness Ticket Box.
				spawn(217756, 345.66718f, 359.32202f, 96.154961f, (byte) 0, 282); //Worthiness Ticket Box.
                spawn(217756, 342.62552f, 357.31348f, 96.154961f, (byte) 0, 283); //Worthiness Ticket Box.
				spawn(217756, 342.56638f, 353.64630f, 96.154961f, (byte) 0, 289); //Worthiness Ticket Box.
				spawn(217756, 342.57877f, 360.80563f, 96.154961f, (byte) 0, 290); //Worthiness Ticket Box.
				spawn(799568, 345.25f, 349.24f, 96.09097f, (byte) 0); //Empyrean RecordKeeper.
			break;
			case 217502:
			    despawnNpc(npc);
			    switch (stageType) {
				    case START_STAGE_2_ROUND_1:
				    if (getNpc(217503) == null &&
					    getNpc(217504) == null) {
					    startStage2Round2();
					    //You have eliminated all enemies in Round %0.
					    sendMsgByRace(1400929, Race.PC_ALL, 0);
				    }
				    break;
				    case START_STAGE_2_ROUND_2 :
				    if (getNpc(217508) == null &&
					    getNpc(217507) == null &&
					    getNpc(217504) == null) {
					    startStage2Round3();
					    //You have eliminated all enemies in Round %0.
					    sendMsgByRace(1400929, Race.PC_ALL, 0);
				    }
				    break;
			    }
			break;
			case 217503:
			    despawnNpc(npc);
			    if (stageType == StageType.START_STAGE_2_ROUND_1 &&
			        getNpc(217502) == null &&
				    getNpc(217504) == null) {
				    startStage2Round2();
				    //You have eliminated all enemies in Round %0.
				    sendMsgByRace(1400929, Race.PC_ALL, 0);
			    }
			break;
			case 217504:
			    despawnNpc(npc);
			    switch (stageType) {
				    case START_STAGE_2_ROUND_1:
				    if (getNpc(217502) == null &&
				        getNpc(217503) == null) {
				        startStage2Round2();
					    //You have eliminated all enemies in Round %0.
					    sendMsgByRace(1400929, Race.PC_ALL, 0);
				    }
				    break;
				    case START_STAGE_2_ROUND_2 :
				    if (getNpc(217502) == null &&
					    getNpc(217507) == null &&
					    getNpc(217508) == null) {
					    startStage2Round3();
					    //You have eliminated all enemies in Round %0.
					    sendMsgByRace(1400929, Race.PC_ALL, 0);
				    }
				    break;
				}
			break;
			case 217507:
			    despawnNpc(npc);
			    switch (stageType) {
				    case START_STAGE_2_ROUND_2:
				    if (getNpc(217502) == null &&
				        getNpc(217504) == null &&
				        getNpc(217508) == null) {
				        startStage2Round3();
					    //You have eliminated all enemies in Round %0.
					    sendMsgByRace(1400929, Race.PC_ALL, 0);
				    }
				    break;
			    } if (es != null && !es.containNpc()) {
				    startStage2Round5();
				    //You have eliminated all enemies in Round %0.
				    sendMsgByRace(1400929, Race.PC_ALL, 0);
			    }
			break;
			case 217508:
			    despawnNpc(npc);
			    if (es != null) {
				    return;
			    } switch (stageType) {
			        case START_STAGE_2_ROUND_2:
			            if (getNpc(217502) == null &&
				            getNpc(217504) == null &&
					        getNpc(217507) == null) {
					        startStage2Round3();
					        //You have eliminated all enemies in Round %0.
					        sendMsgByRace(1400929, Race.PC_ALL, 0);
						}
					break;
					case START_STAGE_2_ROUND_4:
					    if (getNpc(217505) == null) {
					        startStage4Round4_1();
					        //You have eliminated all enemies in Round %0.
					        sendMsgByRace(1400929, Race.PC_ALL, 0);
						}
					break;
				}
			break;
			case 217500:
			case 217509:
				despawnNpc(npc);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				sendEventPacket(StageType.START_STAGE_2_ROUND_4, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				sp(217505, 341.95056f, 334.77692f, 96.09093f, (byte) 0, 2000);
                sp(217508, 344.17813f, 334.42462f, 96.090935f, (byte) 0, 2000);
			break;
			case 217505:
			    despawnNpc(npc);
			    //You have eliminated all enemies in Round %0.
			    sendMsgByRace(1400929, Race.PC_ALL, 0);
			    if (getNpc(217508) == null) {
				    startStage4Round4_1();
			    }
			break;
			case 217506:
			    despawnNpc(npc);
			    //You have eliminated all enemies in Round %0.
			    sendMsgByRace(1400929, Race.PC_ALL, 0);
			    if (es != null && !es.containNpc()) {
				   startStage2Round5();
			    }
			break;
			case 217510:
			case 217501:
				despawnNpc(npc);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						sp(217737, 334.49496f, 349.2322f, 96.090935f, (byte) 0, 4000);
						sendEventPacket(StageType.START_BONUS_STAGE_2, 2000);
						//Round %0 begins!
						sendMsgByRace(1400928, Race.PC_ALL, 4000);
						//You have eliminated all enemies in Round %0.
						sendMsgByRace(1400929, Race.PC_ALL, 0);
						//You can earn an additional reward if you catch the Saam King.
						sendMsgByRace(1400978, Race.PC_ALL, 6000);
						//King Saam will disappear in 30 seconds!
						sendMsgByRace(1400979, Race.PC_ALL, 10000);
                        sp(799569, 345.25f, 349.24f, 96.09097f, (byte) 0, 60000);
						ThreadPoolManager.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								if (getNpc(217737) != null) {
									ThreadPoolManager.getInstance().schedule(new Runnable() {
										@Override
										public void run() {
											if (getNpc(217737) != null) {
												despawnNpc(getNpc(217737));
												sendEventPacket(StageType.PASS_GROUP_STAGE_2, 0);
												//You have eliminated all enemies in Round %0.
												sendMsgByRace(1400929, Race.PC_ALL, 2000);
												//You have passed Stage %0!
												sendMsgByRace(1400930, Race.PC_ALL, 4000);
											}
										}
									}, 30000);
								}
							}
						}, 30000);
					}
				}, 8000);
			break;
			case 217737:
				sendEventPacket(StageType.PASS_GROUP_STAGE_2, 0);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 2000);
				//You have passed Stage %0!
				sendMsgByRace(1400930, Race.PC_ALL, 4000);
			break;
			case 217511:
			case 217512:
			case 217513:
			case 217514:
				despawnNpc(npc);
				if (getNpcs(217511).isEmpty() &&
				    getNpcs(217512).isEmpty() &&
					getNpcs(217513).isEmpty() &&
					getNpcs(217514).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_3_ROUND_2, 2000);
					//Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
					sp(217515, 336.32092f, 345.0251f, 96.090935f, (byte) 0, 6000);
					sp(217516, 347.16144f, 361.89084f, 96.09093f, (byte) 0, 6000);
					sp(217518, 352.77557f, 360.97845f, 96.09091f, (byte) 0, 6000);
					sp(217518, 340.2231f, 351.10208f, 96.09098f, (byte) 0, 6000);
					sp(217517, 354.132f, 337.14255f, 96.09089f, (byte) 0, 6000);
					sp(217517, 353.7888f, 354.4324f, 96.091064f, (byte) 0, 6000);
					sp(217516, 350.0108f, 342.09482f, 96.090935f, (byte) 0, 6000);
					sp(217515, 349.16327f, 335.63864f, 96.09095f, (byte) 0, 6000);
					sp(217517, 341.23633f, 344.55603f, 96.09096f, (byte) 0, 6000);
					sp(217518, 354.66513f, 343.31537f, 96.091095f, (byte) 0, 6000);
					sp(217516, 334.60898f, 352.01447f, 96.09095f, (byte) 0, 6000);
					sp(217515, 348.87338f, 354.90146f, 96.09096f, (byte) 0, 6000);
				}
			break;
			case 217515:
			case 217516:
			case 217517:
			case 217518:
			    despawnNpc(npc);
			    if (getNpcs(217515).isEmpty() &&
			        getNpcs(217516).isEmpty() &&
				    getNpcs(217517).isEmpty() &&
				    getNpcs(217518).isEmpty()) {
				    sendEventPacket(StageType.START_STAGE_3_ROUND_3, 2000);
				    //Round %0 begins!
				    sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
				    sp(217519, 351.08026f, 341.61298f, 96.090935f, (byte) 0, 2000);
				    sp(217521, 333.4532f, 354.7357f, 96.09094f, (byte) 0, 2000);
				    sp(217522, 342.1805f, 360.534f, 96.09092f, (byte) 0, 2000);
				    sp(217520, 334.2686f, 342.60797f, 96.09091f, (byte) 0, 2000);
				    sp(217522, 350.34537f, 356.18558f, 96.09094f, (byte) 0, 2000);
				    sp(217520, 343.7485f, 336.2869f, 96.09092f, (byte) 0, 2000);
			    }
			break;
			case 217519:
			case 217520:
			case 217521:
			case 217522:
			    despawnNpc(npc);
				if (getNpcs(217519).isEmpty() &&
				    getNpcs(217520).isEmpty() &&
					getNpcs(217521).isEmpty() &&
					getNpcs(217522).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_3_ROUND_4, 2000);
					//Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
					sp(217524, 349.66446f, 341.4752f, 96.090965f, (byte) 0, 2000);
					sp(217525, 338.32742f, 356.29636f, 96.090935f, (byte) 0, 2000);
					sp(217526, 349.31473f, 358.43762f, 96.09096f, (byte) 0, 2000);
					sp(217523, 338.73138f, 342.35876f, 96.09094f, (byte) 0, 2000);
				}
			break;
			case 217523:
			case 217524:
			case 217525:
			case 217526:
			    despawnNpc(npc);
			    if (getNpcs(217523).isEmpty() &&
			        getNpcs(217524).isEmpty() &&
				    getNpcs(217525).isEmpty() &&
				    getNpcs(217526).isEmpty()) {
				    sendEventPacket(StageType.START_STAGE_3_ROUND_5, 2000);
				    //Round %0 begins!
				    sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
				    sp(217527, 335.37524f, 346.34567f, 96.09094f, (byte) 0, 2000);
				    sp(217528, 335.36105f, 353.16922f, 96.09094f, (byte) 0, 2000);
			    }
			break;
			case 217527:
			case 217528:
			    despawnNpc(npc);
			    if (getNpcs(217527).isEmpty() &&
			        getNpcs(217528).isEmpty()) {
				    sendEventPacket(StageType.START_BONUS_STAGE_3, 2000);
				    //Round %0 begins!
				    sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
				    sp(217744, 342.45215f, 349.339f, 96.09096f, (byte) 0, 2000); //Administrator Arminos.
				    ThreadPoolManager.getInstance().schedule(new Runnable() {
					    @Override
					    public void run() {
						    startBonusStage3();
					    }
				    }, 39000);
			    }
			break;
			case 217557:
			case 217559:
			case 217562:
			    despawnNpc(npc);
			    switch (stageType) {
				    case START_STAGE_4_ROUND_1:
				    if (getNpcs(217557).isEmpty() &&
					    getNpcs(217559).isEmpty()) {
						//You have eliminated all enemies in Round %0.
						sendMsgByRace(1400929, Race.PC_ALL, 0);
					    sp(217558, 330.27792f, 339.2779f, 96.09093f, (byte) 6);
					    sp(217558, 328.08972f, 346.3553f, 96.090904f, (byte) 1);
				    }
				    break;
				    case START_STAGE_4_ROUND_2:
				        if (es!= null && !es.containNpc()) {
					        startStage4Round3();
				        }
				    break;
			    }
			break;
			case 217558:
			case 217561:
			    despawnNpc(npc);
			    switch (stageType) {
				    case START_STAGE_4_ROUND_1:
					    if (getNpcs(217558).isEmpty()) {
						    sendEventPacket(StageType.START_STAGE_4_ROUND_2, 2000);
						    //Round %0 begins!
						    sendMsgByRace(1400928, Race.PC_ALL, 4000);
						    sp(217559, 330.53665f, 349.23523f, 96.09093f, (byte) 0, 6000);
						    sp(217562, 334.89508f, 363.78442f, 96.090904f, (byte) 105, 6000);
						    sp(217560, 334.61942f, 334.80353f, 96.090904f, (byte) 15, 6000);
						    ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
							    public void run() {
								    List<Npc> round = new ArrayList<Npc>();
								    round.add(sp(217557, 357.24625f, 338.30093f, 96.09104f, (byte) 65));
								    round.add(sp(217558, 357.20663f, 359.28714f, 96.091064f, (byte) 75));
								    round.add(sp(217561, 365.109f, 349.1218f, 96.09114f, (byte) 60));
								    empyreanStage.add(new EmpyreanStage(round));
							    }
						    }, 47000);
					    }
				    break;
				    case START_STAGE_4_ROUND_2:
				        if (es!= null && !es.containNpc()) {
					        startStage4Round3();
				        }
				    break;
				}
			break;
			case 217563:
			case 217565:
			case 217566:
				despawnNpc(npc);
				if (es != null && !es.containNpc()) {
					sendEventPacket(StageType.START_STAGE_4_ROUND_4, 2000);
					//Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
					sp(217567, 345.73895f, 349.49786f, 96.09097f, (byte) 0, 6000);
				}
			break;
			case 217564:
			case 217560:
			case 217745:
			case 217746:
			case 217747:
			case 217748:
			case 217576:
			case 217577:
				despawnNpc(npc);
			break;
			case 217567:
				despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_4_ROUND_5, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 3000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				//A large number of Balaur Troopers descend from the Dredgion.
				sendMsgByRace(1400999, Race.PC_ALL, 6000);
				//A large number of Balaur Troopers descend from the Dredgion.
				sendMsgByRace(1400999, Race.PC_ALL, 54000);
				//A large number of Balaur Troopers descend from the Dredgion.
				sendMsgByRace(1400999, Race.PC_ALL, 110000);
				sp(217653, 327.76917f, 349.26215f, 96.09092f, (byte) 0, 6000);
				sp(217651, 364.8972f, 349.25653f, 96.09114f, (byte) 60, 18000);
				sp(217652, 361.1795f, 339.99252f, 96.09112f, (byte) 50, 35000);
				sp(217653, 354.4119f, 333.6749f, 96.09091f, (byte) 40, 54000);
				sp(217651, 331.61502f, 358.4374f, 96.09091f, (byte) 110, 69000);
				sp(217652, 338.38858f, 364.91507f, 96.090904f, (byte) 100, 83000);
				sp(217651, 346.39847f, 368.19427f, 96.090904f, (byte) 90, 99000);
				sp(217652, 353.92606f, 364.92636f, 96.090904f, (byte) 80, 110000);
				sp(217653, 361.13452f, 358.90424f, 96.091156f, (byte) 65, 130000);
				sp(217652, 346.34402f, 329.9449f, 96.09091f, (byte) 30, 142000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						sp(217653, 331.53894f, 339.8832f, 96.09091f, (byte) 10);
						isDoneStage4 = true;
					}
				}, 174000);
			break;
			case 217651:
			case 217652:
			case 217653:
				despawnNpc(npc);
				if (isDoneStage4 &&
				    getNpcs(217651).isEmpty() &&
				    getNpcs(217652).isEmpty() &&
					getNpcs(217653).isEmpty()) {
					sp(217749, 340.59f, 349.32166f, 96.09096f, (byte) 0, 2000); //Administrator Arminos.
					sendEventPacket(StageType.START_BONUS_STAGE_4, 2000);
					//Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
					ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							startBonusStage4();
						}
					}, 33000);
				}
			break;
		   /**
			* STAGE 5 [Azoturan Version]
			*/
			case 217529:
				despawnNpc(npc);
				if (getNpcs(217529).isEmpty()) {
				    sendEventPacket(StageType.START_STAGE_5_ROUND_2, 2000);
				    //Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
				    sp(217530, 1263.1987f, 778.4129f, 358.6056f, (byte) 30, 4000);
					sp(217531, 1260.1381f, 778.84644f, 358.60562f, (byte) 30, 4000);
					sp(217530, 1257.3065f, 778.35016f, 358.60562f, (byte) 30, 4000);
				}
			break;
			case 217530:
			case 217531:
				despawnNpc(npc);
				if (getNpcs(217530).isEmpty() &&
				    getNpcs(217531).isEmpty()) {
				    sendEventPacket(StageType.START_STAGE_5_ROUND_3, 2000);
				    //Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
				    sp(217543, 1260.0372f, 796.80334f, 358.60562f, (byte) 30, 4000);
				}
			break;
			case 217543:
			    despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_5_ROUND_4, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				sp(217532, 1246.4855f, 796.90735f, 358.6056f, (byte) 2000);
                sp(217533, 1259.5508f, 784.5548f, 358.60562f, (byte) 3000);
                sp(217534, 1276.6561f, 812.5499f, 358.60565f, (byte) 4000);
                sp(217535, 1243.2113f, 813.0927f, 358.60565f, (byte) 5000);
                sp(217536, 1272.9266f, 797.1055f, 358.60562f, (byte) 6000);
			break;
			case 217532:
			case 217533:
			case 217534:
			case 217535:
			case 217536:
				despawnNpc(npc);
				if (getNpcs(217532).isEmpty() &&
				    getNpcs(217533).isEmpty() &&
					getNpcs(217534).isEmpty() &&
					getNpcs(217535).isEmpty() &&
				    getNpcs(217536).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_5_ROUND_5, 2000);
					//Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
				    sendMsgByRace(1400929, Race.PC_ALL, 0);
					sp(217544, 1260.0372f, 796.80334f, 358.60562f, (byte) 30, 4000);
				}
			break;
			case 217544:
				despawnNpc(npc);
				sendEventPacket(StageType.PASS_GROUP_STAGE_5, 0);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 2000);
				//You have passed Stage %0!
				sendMsgByRace(1400930, Race.PC_ALL, 4000);
				sp(205339, 1260.1465f, 795.07495f, 358.60562f, (byte) 30);
			break;
		   /**
			* STAGE 5 [Steel Rake Version]
			*/
			case 217547:
			case 217548:
			case 217549:
				despawnNpc(npc);
				if (getNpcs(217547).isEmpty() &&
				    getNpcs(217548).isEmpty() &&
				    getNpcs(217549).isEmpty()) {
					sp(217550, 1266.293f, 778.3254f, 358.60574f, (byte) 30, 4000);
					sp(217545, 1254.261f, 778.3817f, 358.6056f, (byte) 30, 4000);
				}
			break;
			case 217550:
			case 217545:
				despawnNpc(npc);
				if (getNpcs(217550).isEmpty() &&
				    getNpcs(217545).isEmpty()) {
				    sendEventPacket(StageType.START_STAGE_5_ROUND_2, 2000);
				    //Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
				    sendMsgByRace(1400929, Race.PC_ALL, 0);
				    switch (Rnd.get(1, 2)) {
				        case 1:
					        sp(217551, 1260.0372f, 796.80334f, 358.60562f, (byte) 30, 4000); //Freed Genie.
						break;
						case 2:
						    sp(217552, 1260.0372f, 796.80334f, 358.60562f, (byte) 30, 4000); //Madame Bovariki.
						break;
					}
				}
			break;
			case 217551: //Freed Genie.
			case 217552: //Madame Bovariki.
				despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_5_ROUND_3, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				sp(281128, 1253.3123f, 789.38385f, 358.60562f, (byte) 119); //Manduri Feed.
                sp(281129, 1260.2015f, 800.14886f, 358.6056f, (byte) 16); //Manduri Water Barrel.
                sp(217553, 1260.0372f, 796.80334f, 358.60562f, (byte) 30, 4000);
			break;
			case 217553:
				despawnNpc(npc);
				despawnNpcs(getNpcs(281128)); //Manduri Feed.
                despawnNpcs(getNpcs(281129)); //Manduri Water Barrel.
				sendEventPacket(StageType.START_STAGE_5_ROUND_4, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				switch (Rnd.get(1, 2)) {
				    case 1:
					    sp(281111, 1246.4855f, 796.90735f, 358.6056f, (byte) 0);
						sp(281110, 1259.5508f, 784.5548f, 358.60562f, (byte) 0);
						sp(281112, 1276.6561f, 812.5499f, 358.60565f, (byte) 0);
						sp(281322, 1243.2113f, 813.0927f, 358.60565f, (byte) 0);
						sp(281109, 1272.9266f, 797.1055f, 358.60562f, (byte) 0);
						sp(281113, 1275.894f, 780.51544f, 358.60565f, (byte) 0);
						sp(281108, 1260.003f, 810.555f, 358.6056f, (byte) 0);
						sp(281114, 1244.3293f, 780.4284f, 358.60562f, (byte) 0);
						sp(217554, 1260.0372f, 796.80334f, 358.60562f, (byte) 30, 2000); //Engineer Lahulahu.
					break;
					case 2:
						sp(217555, 1260.0372f, 796.80334f, 358.60562f, (byte) 30, 2000); //Chief Gunner Koakoa.
						sp(701199, 1273.2186f, 797.2602f, 358.60562f, (byte) 60, 2000); //Rideable Antiaircraft Gun.
						sp(701199, 1247.782f, 797.38104f, 358.60562f, (byte) 0, 2000); //Rideable Antiaircraft Gun.
					break;
				}
			break;
			case 217554:
				despawnNpc(npc);
				despawnNpc(getNpc(281108));
				despawnNpc(getNpc(281109));
				despawnNpc(getNpc(281110));
				despawnNpc(getNpc(281111));
				despawnNpc(getNpc(281112));
				despawnNpc(getNpc(281113));
				despawnNpc(getNpc(281114));
				despawnNpc(getNpc(281322));
				sendEventPacket(StageType.START_STAGE_5_ROUND_5, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				sp(217556, 1260.0372f, 796.80334f, 358.60562f, (byte) 30, 4000);
			break;
			case 217555: //Chief Gunner Koakoa.
				despawnNpc(npc);
				despawnNpcs(getNpcs(701199));
				sendEventPacket(StageType.START_STAGE_5_ROUND_5, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				sp(217556, 1260.0372f, 796.80334f, 358.60562f, (byte) 30, 4000);
			break;
			case 217556:
				despawnNpc(npc);
				sendEventPacket(StageType.PASS_GROUP_STAGE_5, 0);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 2000);
				//You have passed Stage %0!
				sendMsgByRace(1400930, Race.PC_ALL, 4000);
				sp(205339, 1260.1465f, 795.07495f, 358.60562f, (byte) 30);
			break;
			case 217568:
				despawnNpc(npc);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				if (isDoneStage6Round1 && getNpcs(217568).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_6_ROUND_2, 2000);
					sp(217570, 1629.4642f, 154.8044f, 126f, (byte) 30, 6000);
					sp(217569, 1643.7776f, 161.63562f, 126f, (byte) 46, 6000);
					sp(217569, 1639.7843f, 142.09268f, 126f, (byte) 40, 6000);
					ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							sp(217569, 1614.6377f, 164.04999f, 126.00113f, (byte) 3);
							sp(217569, 1625.8965f, 135.62509f, 126f, (byte) 30);
							isDoneStage6Round2 = true;
						}
					}, 12000);
				}
			break;
			case 217569:
			case 217570:
				despawnNpc(npc);
				if (stageType == StageType.START_STAGE_6_ROUND_2 && isDoneStage6Round2 &&
				    getNpcs(217569).isEmpty() &&
					getNpcs(217570).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_6_ROUND_3, 2000);
					//Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
					sendMsgByRace(1400929, Race.PC_ALL, 0);
					sp(217572, 1629.5837f, 138.38435f, 126f, (byte) 30, 2000);
                    sp(217569, 1635.01535f, 150.01535f, 126f, (byte) 45, 2000);
					sp(217569, 1638.3817f, 152.84074f, 126f, (byte) 45, 2000);
				}
			break;
			case 217572:
                despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_6_ROUND_4, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
                sp(217573, 1626.7312f, 156.94821f, 126.0f, (byte) 91, 2000); //Spectral Warrior.
				//A Worthiness Ticket Box has appeared in the Ready Room.
				sendMsgByRace(1400977, Race.PC_ALL, 1000);
				spawn(218783, 1589.1693f, 149.36415f, 128.20398f, (byte) 0, 86); //Treasure Box.
				spawn(218783, 1590.0830f, 153.50758f, 128.20398f, (byte) 0, 292); //Treasure Box.
            break;
			case 217573: //Spectral Warrior.
                despawnNpc(npc);
				sendEventPacket(StageType.START_BONUS_STAGE_6, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				//Administrator Arminos will disappear in 30 seconds!
				sendMsgByRace(1401016, Race.PC_ALL, 8000);
				//Administrator Arminos will disappear in 10 seconds!
				sendMsgByRace(1401017, Race.PC_ALL, 20000);
				//Administrator Arminos will disappear in 5 seconds!
				sendMsgByRace(1401018, Race.PC_ALL, 25000);
				sp(217750, 1626.7312f, 156.94821f, 126.0f, (byte) 91, 2000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						if (getNpc(217750) != null) { //Administrator Arminos.
							ThreadPoolManager.getInstance().schedule(new Runnable() {
								@Override
								public void run() {
									if (getNpc(217750) != null) {
										despawnNpc(getNpc(217750));
										sendEventPacket(StageType.PASS_GROUP_STAGE_6, 0);
										//You have eliminated all enemies in Round %0.
										sendMsgByRace(1400929, Race.PC_ALL, 2000);
										//You have passed Stage %0!
										sendMsgByRace(1400930, Race.PC_ALL, 4000);
										sp(205340, 1625.08f, 159.15f, 126f, (byte) 0);
									}
								}
							}, 30000);
						}
					}
				}, 30000);
            break;
			case 217750: //Administrator Arminos.
				sendEventPacket(StageType.PASS_GROUP_STAGE_6, 0);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 2000);
				//You have passed Stage %0!
				sendMsgByRace(1400930, Race.PC_ALL, 4000);
				sp(205340, 1625.08f, 159.15f, 126f, (byte) 0);
            break;
		   /**
			* Stage 7 is not same for player "Elyos/Asmodians"
			* [Asmodians Version]
			*/
			case 217578:
                despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_7_ROUND_2, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
                sp(217579, 1783.0873f, 796.8426f, 469.35013f, (byte) 0, 2000);
            break;
            case 217579:
                despawnNpc(npc);
				sendMsg(1400929);
				sendEventPacket(StageType.START_STAGE_7_ROUND_3, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
                sp(217580, 1783.0873f, 796.8426f, 469.35013f, (byte) 0, 2000);
            break;
            case 217580:
                despawnNpc(npc);
				sendMsg(1400929);
				sendEventPacket(StageType.START_STAGE_7_ROUND_4, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
                sp(217581, 1783.0873f, 796.8426f, 469.35013f, (byte) 0, 2000);
            break;
            case 217581:
                despawnNpc(npc);
				sendMsg(1400929);
				sendEventPacket(StageType.START_STAGE_7_ROUND_5, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
                sp(217586, 1783.0873f, 796.8426f, 469.35013f, (byte) 0, 2000);
            break;
            case 217586:
                despawnNpc(npc);
				sendEventPacket(StageType.PASS_GROUP_STAGE_7, 0);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 2000);
				//You have passed Stage %0!
				sendMsgByRace(1400930, Race.PC_ALL, 4000);
                sp(205341, 1783.0873f, 796.8426f, 469.35013f, (byte) 0);
				sp(217759, 1784.4686f, 792.8891f, 469.35013f, (byte) 0); //Empyrean Box.
            break;
		   /**
			* [Elyos Version]
			*/
			case 217582:
                despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_7_ROUND_2, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
                sp(217583, 1783.0873f, 796.8426f, 469.35013f, (byte) 0, 2000);
            break;
            case 217583:
                despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_7_ROUND_3, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
                sp(217584, 1783.0873f, 796.8426f, 469.35013f, (byte) 0, 2000);
            break;
            case 217584:
                despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_7_ROUND_4, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
                sp(217585, 1783.0873f, 796.8426f, 469.35013f, (byte) 0, 2000);
            break;
            case 217585:
                despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_7_ROUND_5, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
                sp(217587, 1783.0873f, 796.8426f, 469.35013f, (byte) 0, 2000);
            break;
            case 217587:
                despawnNpc(npc);
				sendEventPacket(StageType.PASS_GROUP_STAGE_7, 0);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 2000);
				//You have passed Stage %0!
				sendMsgByRace(1400930, Race.PC_ALL, 4000);
                sp(205341, 1783.0873f, 796.8426f, 469.35013f, (byte) 0);
				sp(217759, 1784.4686f, 792.8891f, 469.35013f, (byte) 0); //Empyrean Box.
            break;
			case 217588: //Kromede The Corrupt.
			case 217589: //Vile Judge Kromede.
				despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_8_ROUND_2, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				sp(217590, 1794.9225f, 1756.2131f, 304.1f, (byte) 55, 2000);
			break;
			case 217590:
				despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_8_ROUND_3, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				sp(217591, 1791.1407f, 1777.92f, 304.1f, (byte) 76, 2000);
			break;
			case 217591:
				despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_8_ROUND_4, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				sp(217592, 1764.3282f, 1744.4377f, 304.1f, (byte) 80, 2000);
			break;
			case 217592:
				despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_8_ROUND_5, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				sp(217593, 1786.7078f, 1757.4915f, 303.8f, (byte) 49, 2000);
			break;
			case 217593:
				despawnNpc(npc);
				sendEventPacket(StageType.PASS_GROUP_STAGE_8, 0);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 2000);
				//You have passed Stage %0!
				sendMsgByRace(1400930, Race.PC_ALL, 4000);
				sp(205342, 1776.757f , 1764.624f, 303.695f, (byte) 90);
			break;
			case 217594:
				despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_9_ROUND_2, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				sp(217595, 1328.4663f, 1711.2297f, 317.6f, (byte) 44, 2000);
			break;
			case 217595:
				despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_9_ROUND_3, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
				sp(217596, 1311.87f, 1731.36f, 315.674f, (byte) 43, 2000);
				sp(217597, 1298.89f, 1743.77f, 316.07f, (byte) 108, 2000);
			break;
			case 217596:
			case 217597:
                Npc counterpart = getNpc(npc.getNpcId() == 217596 ? 217597 : 217596);
				if (counterpart != null && !NpcActions.isAlreadyDead(counterpart)) {
					SkillEngine.getInstance().getSkill(counterpart, 19624, 10, counterpart).useNoAnimationSkill();
				}
				despawnNpc(npc);
                if (getNpcs(217596).isEmpty() &&
				    getNpcs(217597).isEmpty()) {
					sendEventPacket(StageType.START_STAGE_9_ROUND_4, 2000);
					//Round %0 begins!
					sendMsgByRace(1400928, Race.PC_ALL, 4000);
					//You have eliminated all enemies in Round %0.
				    sendMsgByRace(1400929, Race.PC_ALL, 0);
                    sp(217598, 1311.5238f, 1755.2079f, 317.1f, (byte) 97, 2000);
                }
            break;
			case 217598:
			    despawnNpc(npc);
				sendEventPacket(StageType.START_STAGE_9_ROUND_5, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
                sp(217599, 1304.2659f, 1722.2467f, 316.5f, (byte) 23, 2000);
            break;
            case 217599:
                despawnNpc(npc);
				sendEventPacket(StageType.PASS_GROUP_STAGE_9, 0);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 2000);
				//You have passed Stage %0!
				sendMsgByRace(1400930, Race.PC_ALL, 4000);
				sp(205343, 1324.5742f, 1739.683f, 316.4109f, (byte) 8, 2000);
            break;
			case 217600:
			case 217601:
			case 217602:
                despawnNpc(npc);
				if (getNpcs(217600).isEmpty() &&
				    getNpcs(217601).isEmpty() &&
					getNpcs(217602).isEmpty()) {
                    sendEventPacket(StageType.START_STAGE_10_ROUND_2, 2000);
					//Round %0 begins!
				    sendMsgByRace(1400928, Race.PC_ALL, 4000);
				    //You have eliminated all enemies in Round %0.
				    sendMsgByRace(1400929, Race.PC_ALL, 0);
				    sp(217603, 1744.6332f, 1280.0349f, 394.3f, (byte) 9, 2000);
				    sp(217604, 1756.2661f, 1305.561f, 394.3f, (byte) 97, 6000);
					sp(217605, 1763.1177f, 1268.2404f, 394.3f, (byte) 22, 10000);
				    sp(217606, 1765.2681f, 1306.5621f, 394.3f, (byte) 89, 14000);
				}
			break;
			case 217603:
			case 217604:
			case 217605:
			case 217606:
                despawnNpc(npc);
				if (getNpcs(217603).isEmpty() &&
				    getNpcs(217604).isEmpty() &&
					getNpcs(217605).isEmpty() &&
					getNpcs(217606).isEmpty()) {
                    sendEventPacket(StageType.START_STAGE_10_ROUND_3, 2000);
					//Round %0 begins!
				    sendMsgByRace(1400928, Race.PC_ALL, 4000);
				    //You have eliminated all enemies in Round %0.
				    sendMsgByRace(1400929, Race.PC_ALL, 0);
				    sp(700441, 1742.39f, 1289.59f, 394.237f, (byte) 9, 2000);
			        sp(700441, 1782.32f, 1272.74f, 394.237f, (byte) 36, 2000);
					sp(217607, 1771.2675f, 1304.7964f, 394.3f, (byte) 82, 2000);
				}
			break;
			case 217607:
                despawnNpc(npc);
				despawnNpcs(getNpcs(700441));
				sendEventPacket(StageType.START_STAGE_10_ROUND_4, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
                sp(217608, 1754.8065f, 1303.702f, 394.3f, (byte) 100, 2000);
            break;
			case 217608:
			    despawnNpc(npc);
                sendEventPacket(StageType.START_STAGE_10_ROUND_5, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 0);
                sp(217609, 1765.6692f, 1288.092f, 394.3f, (byte) 30, 2000);
            break;
			case 217609:
			    despawnNpc(npc);
				sendEventPacket(StageType.PASS_GROUP_STAGE_10, 0);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 2000);
				//You have passed Stage %0!
				sendMsgByRace(1400930, Race.PC_ALL, 4000);
                sp(205344, 1764.6368f, 1288.831f, 394.23755f, (byte) 77);
            break;
		}
	}
	
	private void startBonusStage3() {
		sp(217740, 360.76f, 349.42f, 96.1f, (byte) 0);
		sp(217741, 346.27f, 363.35f, 96.1f, (byte) 11);
		sp(217742, 332.12f, 349.22f, 96.1f, (byte) 0);
		sp(217743, 346.42f, 335.1f, 96.1f, (byte) 87);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				//Spirits will disappear in 30 seconds!
				sendMsgByRace(1401010, Race.PC_ALL, 0);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						//Spirits will disappear in 10 seconds!
						sendMsgByRace(1401011, Race.PC_ALL, 0);
						ThreadPoolManager.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								//Spirits will disappear in 5 seconds!
								sendMsgByRace(1401012, Race.PC_ALL, 0);
								ThreadPoolManager.getInstance().schedule(new Runnable() {
									@Override
									public void run() {
										despawnNpc(getNpc(217740));
										despawnNpc(getNpc(217741));
										despawnNpc(getNpc(217742));
										despawnNpc(getNpc(217743));
										despawnNpc(getNpc(217744)); //Administrator Arminos.
										sendEventPacket(StageType.PASS_GROUP_STAGE_3, 0);
										//You have eliminated all enemies in Round %0.
										sendMsgByRace(1400929, Race.PC_ALL, 2000);
										//You have passed Stage %0!
										sendMsgByRace(1400930, Race.PC_ALL, 4000);
										//A Worthiness Ticket Box has appeared in the Ready Room.
										sendMsgByRace(1400976, Race.PC_ALL, 6000);
										sp(205331, 345.25f, 349.24f, 96.09097f, (byte) 0);
										sp(217735, 378.9331f, 346.74878f, 96.74762f, (byte) 0);
									}
								}, 5000);
							}
						}, 5000);
					}
				}, 20000);
			}
		}, 30000);
	}
	
	private void startBonusStage4() {
		//Round %0 begins!
		sendMsgByRace(1400928, Race.PC_ALL, 3000);
		sp(217778, 346.2f, 366.85f, 96.55f, (byte) 1);
		sp(217747, 346.2204f, 367.52002f, 96.090904f, (byte) 60, 3000);
		sp(217747, 346.2204f, 367.52002f, 96.090904f, (byte) 60, 6000);
		sp(217748, 346.65222f, 366.3634f, 96.09092f, (byte) 60, 9000);
		sp(217748, 346.65222f, 366.3634f, 96.09092f, (byte) 60, 12000);
		sp(217748, 345.7578f, 366.7986f, 96.09094f, (byte) 60, 15000);
		sp(217748, 345.863f, 367.471f, 96.09094f, (byte) 60, 18000);
		sp(217747, 346.9996f, 366.3978f, 96.09092f, (byte) 60, 21000);
		sp(217746, 345.7872f, 366.3056f, 96.09092f, (byte) 60, 24000);
		sp(217745, 346.4504f, 367.8004f, 96.09094f, (byte) 60, 27000);
		sp(217747, 346.75043f, 367.467f, 96.09094f, (byte) 60, 30000);
		sp(217747, 346.535f, 367.3128f, 96.090904f, (byte) 60, 33000);
		sp(217747, 345.8452f, 367.2468f, 96.09092f, (byte) 60, 36000);
		sp(217746, 345.428f, 366.2954f, 96.09093f, (byte) 60, 39000);
		sp(217747, 346.71082f, 366.7156f, 96.090904f, (byte) 60, 42000);
		sp(217747, 346.38782f, 366.1606f, 96.09093f, (byte) 60, 45000);
		sp(217747, 345.36002f, 366.0284f, 96.09093f, (byte) 60, 48000);
		sp(217747, 345.5378f, 366.1876f, 96.09092f, (byte) 60, 51000);
		sp(217747, 346.5176f, 365.8592f, 96.09092f, (byte) 60, 54000);
		sp(217745, 345.8434f, 367.8082f, 96.090904f, (byte) 60, 57000);
		sp(217747, 345.297f, 366.3014f, 96.09093f, (byte) 60, 60000);
		sp(217747, 346.0346f, 367.2426f, 96.090904f, (byte) 60, 63000);
		sp(217747, 345.52863f, 366.62622f, 96.09092f, (byte) 60, 66000);
		sp(217745, 345.80862f, 366.9388f, 96.09092f, (byte) 60, 69000);
		sp(217747, 346.393f, 366.9766f, 96.090904f, (byte) 60, 72000);
		sp(217746, 345.5726f, 366.3462f, 96.09092f, (byte) 60, 75000);
		sp(217745, 345.2004f, 366.36902f, 96.09092f, (byte) 60, 78000);
		sp(217746, 346.2528f, 365.9208f, 96.09093f, (byte) 60, 81000);
		sp(217747, 346.0686f, 366.9096f, 96.090904f, (byte) 60, 84000);
		sp(217746, 345.4606f, 367.14862f, 96.090904f, (byte) 60, 87000);
		sp(217747, 345.8016f, 367.7212f, 96.090904f, (byte) 60, 90000);
		sp(217747, 347.1144f, 365.875f, 96.09092f, (byte) 60, 93000);
		sp(217747, 345.3226f, 367.7414f, 96.0909f, (byte) 60, 96000);
		sp(217747, 345.4836f, 367.3886f, 96.090904f, (byte) 60, 99000);
		sp(217747, 345.80862f, 366.0682f, 96.09092f, (byte) 60, 102000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				despawnNpcs(getNpcs(217745));
				despawnNpcs(getNpcs(217746));
				despawnNpcs(getNpcs(217747));
				despawnNpcs(getNpcs(217748));
				despawnNpc(getNpc(217749)); //Administrator Arminos.
				despawnNpc(getNpc(217778)); //Gate.
				sendEventPacket(StageType.PASS_GROUP_STAGE_4, 0);
				//You have eliminated all enemies in Round %0.
				sendMsgByRace(1400929, Race.PC_ALL, 2000);
				//You have passed Stage %0!
				sendMsgByRace(1400930, Race.PC_ALL, 4000);
				sp(205338, 345.25f, 349.24f, 96.09097f, (byte) 0);
			}
		}, 102000);
	}
	
	private void startStage4Round4_1() {
		List<Npc> round = new ArrayList<Npc>();
		round.add(sp(217508, 334.06754f, 339.84393f, 96.09091f, (byte) 0));
		empyreanStage.add(new EmpyreanStage(round));
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				List<Npc> round1 = new ArrayList<Npc>();
				round1.add(sp(217506, 342.12405f, 364.4922f, 96.09093f, (byte) 0));
				round1.add(sp(217507, 344.4953f, 365.14444f, 96.09092f, (byte) 0));
				empyreanStage.add(new EmpyreanStage(round1));
			}
		}, 5000);
	}
	
	private void startStage4Round3() {
		sendEventPacket(StageType.START_STAGE_4_ROUND_3, 2000);
		//Round %0 begins!
		sendMsgByRace(1400928, Race.PC_ALL, 4000);
		sp(217563, 339.70975f, 333.54272f, 96.090904f, (byte) 20, 6000);
		sp(217564, 342.92892f, 333.43994f, 96.09092f, (byte) 18, 6000);
		sp(217565, 341.55396f, 330.70847f, 96.09093f, (byte) 23, 16000);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				List<Npc> round = new ArrayList<Npc>();
				round.add(sp(217566, 362.87164f, 357.87164f, 96.091125f, (byte) 73));
				round.add(sp(217563, 359.1135f, 359.6953f, 96.091125f, (byte) 80));
				empyreanStage.add(new EmpyreanStage(round));
			}
		}, 43000);
	}
	
	private void startStage2Round2() {
		sendEventPacket(StageType.START_STAGE_2_ROUND_2, 2000);
		//Round %0 begins!
		sendMsgByRace(1400928, Race.PC_ALL, 4000);
		sp(217502, 328.78433f, 348.77353f, 96.09092f, (byte) 0, 2000);
		sp(217508, 329.01874f, 343.79257f, 96.09092f, (byte) 0, 2000);
		sp(217507, 329.2849f, 355.2314f, 96.090935f, (byte) 0, 2000);
		sp(217504, 328.90808f, 351.6184f, 96.09092f, (byte) 0, 2000);
	}
	
	private void startStage2Round3() {
		//Round %0 begins!
		sendMsgByRace(1400928, Race.PC_ALL, 4000);
		sendEventPacket(StageType.START_STAGE_2_ROUND_3, 2000);
		switch (Rnd.get(1, 2)) {
		    case 1:
				sp(217500, 332.24298f, 349.49286f, 96.090935f, (byte) 0, 2000); //Chieftain Kurka.
			break;
			case 2:
				sp(217509, 332.24298f, 349.49286f, 96.090935f, (byte) 0, 2000); //Chieftain Nuaka.
			break;
		}
	}
	
	private void startStage2Round5() {
		//Round %0 begins!
		sendMsgByRace(1400928, Race.PC_ALL, 4000);
		sendEventPacket(StageType.START_STAGE_2_ROUND_5, 2000);
		switch (Rnd.get(1, 2)) {
		    case 1:
				sp(217501, 332.0035f, 349.55893f, 96.09093f, (byte) 0, 2000); //Grand Chieftain Saendukal.
			break;
			case 2:
				sp(217510, 332.0035f, 349.55893f, 96.09093f, (byte) 0, 2000); //Grand Chieftain Kasika.
			break;
		}
	}
	
	private void rewardGroup() {
		for (Player p : instance.getPlayersInside()) {
			doReward(p);
		}
	}
	
	@Override
	public void doReward(Player player) {
		CruciblePlayerReward playerReward = getPlayerReward(player.getObjectId());
		float reward = 0.02f * playerReward.getPoints();
		if (!playerReward.isRewarded()) {
			playerReward.setRewarded();
			playerReward.setInsignia((int) reward);
			ItemService.addItem(player, 186000130, (int) reward); //Crucible Insignia.
		} else {
			TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
		}
		PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(instanceReward, InstanceScoreType.END_PROGRESS));
	}
	
	@Override
	public void onInstanceDestroy() {
		super.onInstanceDestroy();
		npcs.clear();
		empyreanStage.clear();
	}
	
	@Override
	public boolean onReviveEvent(final Player player) {
		super.onReviveEvent(player);
		moveToReadyRoom(player);
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player p) {
				if (player.getObjectId() == p.getObjectId()) {
					//You failed the training and have been sent to the Ready Room.
					PacketSendUtility.sendPacket(p, new SM_SYSTEM_MESSAGE(1400932));
				} else {
					//"Player Name" failed the training and has been sent to the Ready Room.
					PacketSendUtility.sendPacket(p, new SM_SYSTEM_MESSAGE(1400933, player.getName()));
				}
			}
		});
		return true;
	}
	
	private EmpyreanStage getEmpyreanStage(Npc npc) {
		for (EmpyreanStage es: empyreanStage) {
			if (es.npcs.contains(npc)) {
				return es;
			}
		}
		return null;
	}
	
	private boolean isSpawn(List<Integer> round) {
		for (Npc n: npcs) {
			if (round.contains(n.getNpcId())) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void onChangeStage(final StageType type) {
		switch (type) {
			case START_STAGE_1_ELEVATOR:
				sendEventPacket(type, 0);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				stage = 1;
				sp(799573, 384.51f, 352.61078f, 96.747635f, (byte) 83);
				switch (Rnd.get(1, 2)) {
					case 1: //Stage 1 [Kaisinel Version]
						sendEventPacket(StageType.START_KAISINEL_STAGE_1_ROUND_1, 2000);
						sp(217476, 336.585f, 341.21777f, 96.090935f, (byte) 11, 2000);
				        sp(217477, 333.94592f, 349.2856f, 96.090935f, (byte) 119, 3000);
				        sp(217478, 335.89075f, 356.3005f, 96.090935f, (byte) 109, 4000);
				        sp(217479, 346.5103f, 336.82236f, 96.090935f, (byte) 29, 5000);
					break;
					case 2: //Stage 1 [Marchutan Version]
						sendEventPacket(StageType.START_MARCHUTAN_STAGE_1_ROUND_1, 2000);
						sp(217485, 336.585f, 341.21777f, 96.090935f, (byte) 11, 2000);
				        sp(217486, 333.94592f, 349.2856f, 96.090935f, (byte) 119, 3000);
				        sp(217487, 335.89075f, 356.3005f, 96.090935f, (byte) 109, 4000);
				        sp(217488, 346.5103f, 336.82236f, 96.090935f, (byte) 29, 5000);
					break;
				}
			break;
			case START_STAGE_2_ELEVATOR:
				sendEventPacket(type, 0);
				despawnNpcs(getNpcs(217756)); //Worthiness Ticket Box.
				stage = 2;
				sendEventPacket(StageType.START_STAGE_2_ROUND_1, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				sp(217503, 325.71194f, 352.81027f, 96.09092f, (byte) 0, 2000);
				sp(217502, 325.78696f, 346.07263f, 96.090904f, (byte) 0, 3000);
				sp(217504, 325.06122f, 349.4784f, 96.090904f, (byte) 0, 4000);
			break;
			case START_STAGE_3_ELEVATOR:
				sendEventPacket(type, 0);
				despawnNpcs(getNpcs(217737)); //King Saam.
				despawnNpcs(getNpcs(799569));
				stage = 3;
				sendEventPacket(StageType.START_STAGE_3_ROUND_1, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				sp(217512, 344.23056f, 347.89594f, 96.09096f, (byte) 0, 3000);
				sp(217513, 341.09082f, 337.95187f, 96.09097f, (byte) 0, 3000);
				sp(217512, 342.06656f, 361.16135f, 96.090935f, (byte) 0, 3000);
				sp(217511, 356.75006f, 335.27487f, 96.09096f, (byte) 0, 3000);
				sp(217514, 345.4355f, 365.05215f, 96.09093f, (byte) 0, 3000);
				sp(217512, 352.8222f, 358.33463f, 96.09092f, (byte) 0, 3000);
				sp(217513, 342.32755f, 365.00473f, 96.09093f, (byte) 0, 3000);
				sp(217514, 356.19113f, 362.22543f, 96.090965f, (byte) 0, 3000);
				sp(217511, 344.25127f, 334.1194f, 96.090935f, (byte) 0, 3000);
				sp(217511, 344.07086f, 346.8839f, 96.09092f, (byte) 0, 3000);
				sp(217514, 334.01746f, 350.76382f, 96.090935f, (byte) 0, 3000);
				sp(217513, 344.49155f, 351.73932f, 96.09093f, (byte) 0, 3000);
				sp(217513, 353.0832f, 362.178f, 96.09092f, (byte) 0, 3000);
				sp(217511, 356.24454f, 358.34552f, 96.09103f, (byte) 0, 3000);
				sp(217512, 330.64853f, 346.87302f, 96.09091f, (byte) 0, 3000);
				sp(217512, 353.32773f, 335.26398f, 96.09092f, (byte) 0, 3000);
				sp(217514, 356.69666f, 339.1548f, 96.09103f, (byte) 0, 3000);
				sp(217511, 347.6529f, 347.90683f, 96.09098f, (byte) 0, 3000);
				sp(217514, 347.5995f, 351.78674f, 96.09099f, (byte) 0, 3000);
				sp(217512, 340.82983f, 334.1085f, 96.09093f, (byte) 0, 3000);
				sp(217514, 344.19876f, 337.9993f, 96.09094f, (byte) 0, 3000);
				sp(217513, 353.5887f, 339.10763f, 96.09092f, (byte) 0, 3000);
				sp(217511, 345.4889f, 361.17224f, 96.090935f, (byte) 0, 3000);
				sp(217513, 330.90952f, 350.7164f, 96.09093f, (byte) 0, 3000);
			break;
			case START_STAGE_4_ELEVATOR:
				sendEventPacket(type, 0);
				despawnNpc(getNpc(217735));
				sendEventPacket(StageType.START_STAGE_4_ROUND_1, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				stage = 4;
				sp(217557, 328.88104f, 349.55392f, 96.090904f, (byte) 0, 3000);
				sp(217559, 328.38922f, 342.39066f, 96.09091f, (byte) 5, 3000);
				sp(217557, 333.17947f, 336.4504f, 96.090904f, (byte) 8, 3000);
			break;
			case START_STAGE_5:
				stage = 5;
				sp(205426, 1256.2872f, 834.28986f, 358.60565f, (byte) 103);
				sp(205332, 1260.1292f, 795.06964f, 358.60562f, (byte) 30, 10000);
				crucibleTeleport(1260.15f, 812.34f, 358.6056f, (byte) 90);
				sendEventPacket(type, 2000);
			break;
			//Stage 5 [Azoturan Version]
			case START_AZOTURAN_STAGE_5_ROUND_1:
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				sendEventPacket(type, 2000);
				sp(217529, 1263.1987f, 778.4129f, 358.6056f, (byte) 30, 2000);
                sp(217529, 1260.1381f, 778.84644f, 358.60562f, (byte) 30, 2000);
                sp(217529, 1257.3065f, 778.35016f, 358.60562f, (byte) 30, 2000);
			break;
			//Stage 5 [Steel Rake Version]
			case START_STEEL_RAKE_STAGE_5_ROUND_1:
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				sendEventPacket(type, 2000);
				sp(217549, 1263.1987f, 778.4129f, 358.6056f, (byte) 30, 2000);
                sp(217548, 1260.1381f, 778.84644f, 358.60562f, (byte) 30, 2000);
                sp(217547, 1257.3065f, 778.35016f, 358.60562f, (byte) 30, 2000);
			break;
			case START_STAGE_6:
				stage = 6;
				sp(205427, 1594.4756f, 145.26898f, 128.67778f, (byte) 16);
				sp(205333, 1625.1771f, 159.15244f, 126f, (byte) 70, 10000);
				crucibleTeleport(1616.0248f, 154.43837f, 126f, (byte) 10);
				sendEventPacket(type, 2000);
			break;
			case START_STAGE_6_ROUND_1:
				sendEventPacket(type, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				sp(217568, 1636.7102f, 166.87984f, 126f, (byte) 60, 2000);
                sp(217568, 1619.4432f, 153.83188f, 126f, (byte) 60, 2000);
                sp(217568, 1636.6416f, 164.15344f, 126f, (byte) 60, 2000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						sp(217568, 1638.7107f, 165.40533f, 126f, (byte) 60);
						sp(217568, 1638.6783f, 162.67389f, 126f, (byte) 60);
						isDoneStage6Round1 = true;
					}
				}, 12000);
			break;
			case START_STAGE_7:
				stage = 7;
				sp(205428, 1820.39f, 800.81805f, 470.1394f, (byte) 86);
				sp(205334, 1781.6106f, 796.9224f, 469.35016f, (byte) 0, 10000);
				crucibleTeleport(1793.9233f, 796.92f, 469.36542f, (byte) 60);
				sendEventPacket(type, 2000);
			break;
			case START_STAGE_7_ROUND_1:
				sendEventPacket(type, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
			break;
			case START_STAGE_8:
				stage = 8;
				sp(205429, 1784.7867f, 1726.0922f, 304.17697f, (byte) 51);
                sp(205335, 1771.407f, 1762.9862f, 303.6954f, (byte) 102, 10000);
                crucibleTeleport(1776.4169f, 1749.9952f, 303.69553f, (byte) 0);
				sendEventPacket(type, 2000);
			break;
			case START_STAGE_8_ROUND_1:
				sendEventPacket(type, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				switch (Rnd.get(1, 2)) {
				    case 1:
					    sp(217588, 1758.1841f, 1770.473f, 303.7f, (byte) 107, 2000); //Kromede The Corrupt.
					break;
					case 2:
					    sp(217589, 1758.1841f, 1770.473f, 303.7f, (byte) 107, 2000); //Vile Judge Kromede.
					break;
				}
			break;
			case START_STAGE_9:
				stage = 9;
				sp(205430, 1356.3973f, 1759.6832f, 319.625f, (byte) 83);
                sp(205336, 1324.5742f, 1739.683f, 316.4109f, (byte) 8, 10000);
                crucibleTeleport(1328.935f, 1742.0771f, 316.74188f, (byte) 0);
                sendEventPacket(type, 2000);
			break;
			case START_STAGE_9_ROUND_1:
				sendEventPacket(type, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
				sp(217594, 1282.5399f, 1755.8711f, 317.4f, (byte) 105, 2000);
			break;
			case START_STAGE_10:
                stage = 10;
				sp(205431, 1755.709f, 1253.4136f, 394.2378f, (byte) 33);
                sp(205337, 1766.5986f, 1291.2572f, 394.23755f, (byte) 82, 10000);
                crucibleTeleport(1760.9441f, 1278.033f, 394.23764f, (byte) 0);
                sendEventPacket(type, 2000);
            break;
            case START_STAGE_10_ROUND_1:
                sendEventPacket(type, 2000);
				//Round %0 begins!
				sendMsgByRace(1400928, Race.PC_ALL, 4000);
                sp(217600, 1771.213f, 1302.0781f, 394.3f, (byte) 82, 2000);
                sp(217601, 1774.4563f, 1302.1516f, 394.3f, (byte) 82, 2000);
                sp(217602, 1765.1488f, 1305.1216f, 394.3f, (byte) 84, 2000);
            break;
		}
	}
	
	private void crucibleTeleport(float x, float y, float z, byte h) {
		for (Player player: instance.getPlayersInside()) {
			if (player.isOnline()) {
				TeleportService2.teleportTo(player, mapId, instanceId, x, y, z, h);
			}
		}
	}
	
	protected void readyRoomTeleport(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, mapId, instanceId, x, y, z, h);
	}
	
	private void moveToReadyRoom(Player player) {
		switch (stage) {
			case 1:
			case 2:
			case 3:
			case 4:
				readyRoomTeleport(player, 381.41684f, 346.78162f, 96.74763f, (byte) 43);
			break;
			case 5:
				readyRoomTeleport(player, 1260.9495f, 832.87317f, 358.60562f, (byte) 92);
			break;
			case 6:
				readyRoomTeleport(player, 1592.8813f, 149.78166f, 128.81355f, (byte) 117);
			break;
			case 7:
				readyRoomTeleport(player, 1820.8805f, 795.80914f, 470.18304f, (byte) 51);
			break;
			case 8:
				readyRoomTeleport(player, 1780.103f, 1723.458f, 304.039f, (byte) 53);
			break;
			case 9:
				readyRoomTeleport(player, 1359.5046f, 1751.7952f, 319.59406f, (byte) 30);
			break;
			case 10:
			    readyRoomTeleport(player, 1755.709f, 1253.4136f, 394.2378f, (byte) 33);
			break;
		}
	}
	
	@Override
	public void onLeaveInstance(Player player) {
		CruciblePlayerReward reward = getPlayerReward(player.getObjectId());
		removeItems(player);
		if (reward != null) {
			reward.setPlayerLeave();
		}
	}
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
	}
	
	@Override
	public void onExitInstance(Player player) {
		removeItems(player);
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
		//"Player Name" dropped out of training and left the Crucible.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400962, player.getName()));
	}
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
        storage.decreaseByItemId(186000124, storage.getItemCountByItemId(186000124)); //Worthiness Ticket.
		storage.decreaseByItemId(186000125, storage.getItemCountByItemId(186000125)); //Worthiness Ticket.
		storage.decreaseByItemId(186000134, storage.getItemCountByItemId(186000134)); //Worthiness Ticket.
	}
	
	@Override
	public void onStopTraining(Player player) {
		doReward(player);
	}
	
	private void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					npcs.add((Npc) spawn(npcId, x, y, z, h));
				}
			}
		}, time);
	}
	
	private Npc sp(int npcId ,float x, float y, float z, byte h) {
		Npc npc = null;
		if (!isInstanceDestroyed) {
			npc = (Npc) spawn(npcId, x, y, z, h);
			npcs.add(npc);
		}
		return npc;
	}
	
	@Override
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
		   /**
			* Give to a "Crucible Arbiter" in order to rejoin the battle and prove your worthiness.
			*/
			case 217735: //Worthiness Ticket Box (Fin Stage 3)
			case 217756: //Worthiness Ticket Box (Fin Stage 1)
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000124, 1)); //Worthiness Ticket.
					}
				}
			break;
			case 217737: //King Saam.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 125001563, 1)); //Saam's Laurel.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 123000768, 1)); //Golden Saam's Belt.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 115000674, 1)); //Golden Saam's Shield.
				switch (Rnd.get(1, 4)) {
					case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190020070, 1)); //Centennial Golden Saam Egg.
				    break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190020071, 1)); //Ten-Thousand-Year-Old Golden Saam Egg.
				    break;
					case 3:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190020180, 1)); //Golden Pack Saam.
				    break;
					case 4:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 170170031, 1)); //[Souvenir] Centennial Golden Saam Statue.
				    break;
				}
			break;
			case 217738: //Cut Saam.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162000107, 10)); //Saam King's Herbs.
					}
				}
			break;
			case 217740: //Seismik.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 125002593, 1)); //Kagas's Hat.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 162000108, 5)); //Spirit Rune.
			break;
			case 217741: //Splashdown.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 125002595, 1)); //Splashdown's Hat.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 162000108, 5)); //Spirit Rune.
			break;
			case 217742: //Crematorux.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 125002592, 1)); //Crematorux's Mask.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 162000108, 5)); //Spirit Rune.
			break;
			case 217743: //Windlash.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 125002594, 1)); //Windlash's Hat.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 162000108, 5)); //Spirit Rune.
			break;
			case 217750: //Administrator Arminos.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 162000109, 5)); //Soul Crystal.
				switch (Rnd.get(1, 13)) {
					case 1:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 102100723, 1)); //Arminos' Mirage Cipher-Blade.
					break;
					case 2:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 101900830, 1)); //Arminos' Mirage Aethercannon.
					break;
					case 3:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 101700911, 1)); //Arminos' Mirage Bow.
					break;
					case 4:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 100201003, 1)); //Arminos' Mirage Dagger.
					break;
					case 5:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 100900869, 1)); //Arminos' Mirage Greatsword.
					break;
					case 6:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 102000864, 1)); //Arminos' Mirage Harp.
					break;
					case 7:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 100100874, 1)); //Arminos' Mirage Mace.
					break;
					case 8:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 100500886, 1)); //Arminos' Mirage Orb.
					break;
					case 9:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 101800826, 1)); //Arminos' Mirage Pistol.
					break;
					case 10:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 101300836, 1)); //Arminos' Mirage Polearm.
					break;
					case 11:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 100600944, 1)); //Arminos' Mirage Spellbook.
					break;
					case 12:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 101500895, 1)); //Arminos' Mirage Staff.
					break;
					case 13:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 100001135, 1)); //Arminos' Mirage Sword.
					break;
				}
			break;
			case 217759: //Empyrean Box.
				switch (Rnd.get(1, 4)) {
				    case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190020080, 1)); //Worg Of The Dead Egg.
				    break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190000020, 1)); //Button-Eye Mookie Egg.
				    break;
					case 3:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 168000130, 1)); //Godstone: Tiamat's Fury.
				    break;
					case 4:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 168000132, 1)); //Godstone: Vaizel's Vow.
				    break;
				}
			break;
			case 218783: //Ride Treasure Box.
				switch (Rnd.get(1, 10)) {
					case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190100128, 1)); //Snowkissed Aetherboard.
				    break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190100127, 1)); //Flamekissed Aetherboard.
				    break;
					case 3:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190100115, 1)); //Palomeno Heorn.
				    break;
					case 4:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190100109, 1)); //Ruddytail Heorn.
				    break;
					case 5:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190100101, 1)); //[Stick On] Sharptooth Cruiser.
				    break;
					case 6:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190100100, 1)); //[Stick On] Flying Pagati.
				    break;
					case 7:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190100099, 1)); //[Stick On] Quick Crestlich.
				    break;
					case 8:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190100096, 1)); //[Event] Touring Pagati.
				    break;
					case 9:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190100015, 1)); //[Event] Stratowisp.
				    break;
					case 10:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190100018, 1)); //[Event] Cirruspeed.
				    break;
				}
			break;
		}
	}
}