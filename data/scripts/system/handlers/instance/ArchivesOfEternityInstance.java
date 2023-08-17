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
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.Map;
import java.util.Set;

/****/
/** Author (Encom)
/** Video: https://www.youtube.com/watch?v=HjAJng-e72I
/** Source: http://aionpowerbook.com/powerbook/Library_of_Knowledge
/****/

@InstanceID(301540000)
public class ArchivesOfEternityInstance extends GeneralInstanceHandler
{
	private Race spawnRace;
	private Map<Integer, StaticDoor> doors;
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 857452: //Relic Techgolem.
			case 857456: //Augmented Fleshgolem.
			case 857459: //Crystalized Shardgolem.
			case 857460: //Ancient Relic Techgolem.
			case 857462: //Fleshgolem Captain.
			case 857464: //Mountainous Shardgolem.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058413, 1)); //ì?´ê³„ ì•”ë£¡ì?˜ ë¬´ê¸° ìƒ?ìž?.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 166040001, 1)); //Essence Core Solution.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100344, 1)); //Ruby Starlight Particle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100345, 1)); //Sapphire Starlight Particle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100346, 1)); //Emerald Starlight Crystal Dust.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100347, 1)); //Amethyst Starlight Crystal Dust.
				    } switch (Rnd.get(1, 5)) {
						case 1:
						    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080005, 3)); //Lesser Minion Contract.
						break;
						case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080006, 3)); //Greater Minion Contract.
						break;
						case 3:
						    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080007, 3)); //Major Minion Contract.
						break;
						case 4:
						    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190080008, 3)); //Cute Minion Contract.
						break;
						case 5:
					        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190200000, 50)); //Minium.
					    break;
					}
				}
			break;
			/**
			 * Chosen "Guardian's Set"
			 * Appearance change items obtainable from "Archives Of Eternity"
			 * Can be used on any type of item.
			 * Headgear can be obtained from "í™˜ì˜?ì?˜ ìƒ?ìž?"
			 * Pants, Shoes, Pauldrons and Gloves can be obtained from "Cryptograph Cube"
			 */
			case 806139: //Cryptograph Cube.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        switch (Rnd.get(1, 5)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 110000047, 1)); //Library Guardian's Tunic.
				            break;
					        case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 111000092, 1)); //Library Guardian's Gloves.
				            break;
							case 3:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 112000051, 1)); //Library Guardian's Pauldrons.
				            break;
							case 4:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 113000059, 1)); //Library Guardian's Leggings.
				            break;
							case 5:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 114000097, 1)); //Library Guardian's Shoes.
				            break;
						}
					}
                }
            break;
		   /**
			* Elyos
			*/
			case 703131: //Histories Of Atreia.
			    for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        switch (Rnd.get(1, 5)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100300, 1)); //History Of Southern Atreia Chapter 1.
				            break;
					        case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100301, 1)); //History Of Southern Atreia Chapter 2.
				            break;
							case 3:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100302, 1)); //History Of Southern Atreia Chapter 3.
				            break;
							case 4:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100303, 1)); //History Of Southern Atreia Chapter 4.
				            break;
							case 5:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100304, 1)); //History Of Southern Atreia Chapter 5.
				            break;
						}
					}
                }
			break;
			case 703132: //Records From The Era Of Men.
			    for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        switch (Rnd.get(1, 5)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100310, 1)); //Records Of The Human Races Chapter 1.
				            break;
					        case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100311, 1)); //Records Of The Human Races Chapter 2.
				            break;
							case 3:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100312, 1)); //Records Of The Human Races Chapter 3.
				            break;
							case 4:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100313, 1)); //Records Of The Human Races Chapter 4.
				            break;
							case 5:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100314, 1)); //Records Of The Human Races Chapter 5.
				            break;
						}
					}
                }
			break;
			case 703133: //Empyrean Histories.
			    for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        switch (Rnd.get(1, 5)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100320, 1)); //History Of The Empyrean Lords Chapter 1.
				            break;
					        case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100321, 1)); //History Of The Empyrean Lords Chapter 2.
				            break;
							case 3:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100322, 1)); //History Of The Empyrean Lords Chapter 3.
				            break;
							case 4:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100323, 1)); //History Of The Empyrean Lords Chapter 4.
				            break;
							case 5:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100324, 1)); //History Of The Empyrean Lords Chapter 5.
				            break;
						}
					}
                }
			break;
		   /**
			* Asmodians
			*/
			case 703149: //Histories Of Atreia.
			    for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        switch (Rnd.get(1, 5)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100305, 1)); //History Of Northern Atreia Chapter 1.
				            break;
					        case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100306, 1)); //History Of Northern Atreia Chapter 2.
				            break;
							case 3:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100307, 1)); //History Of Northern Atreia Chapter 3.
				            break;
							case 4:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100308, 1)); //History Of Northern Atreia Chapter 4.
				            break;
							case 5:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100309, 1)); //History Of Northern Atreia Chapter 5.
				            break;
						}
					}
                }
			break;
			case 703150: //Records From The Era Of Men.
			    for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        switch (Rnd.get(1, 5)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100315, 1)); //Records Of The Human Races Chapter 1.
				            break;
					        case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100316, 1)); //Records Of The Human Races Chapter 2.
				            break;
							case 3:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100317, 1)); //Records Of The Human Races Chapter 3.
				            break;
							case 4:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100318, 1)); //Records Of The Human Races Chapter 4.
				            break;
							case 5:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100319, 1)); //Records Of The Human Races Chapter 5.
				            break;
						}
					}
                }
			break;
			case 703151: //Empyrean Histories.
			    for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        switch (Rnd.get(1, 5)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100325, 1)); //History Of The Empyrean Lords Chapter 1.
				            break;
					        case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100326, 1)); //History Of The Empyrean Lords Chapter 2.
				            break;
							case 3:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100327, 1)); //History Of The Empyrean Lords Chapter 3.
				            break;
							case 4:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100328, 1)); //History Of The Empyrean Lords Chapter 4.
				            break;
							case 5:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100329, 1)); //History Of The Empyrean Lords Chapter 5.
				            break;
						}
					}
                }
			break;
			case 703134: //Annals Of Life [Q18627/Q28627].
			    for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        switch (Rnd.get(1, 5)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100330, 1)); //Records Of Life Chapter 1.
				            break;
					        case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100331, 1)); //Records Of Life Chapter 2.
				            break;
							case 3:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100332, 1)); //Records Of Life Chapter 3.
				            break;
							case 4:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100333, 1)); //Records Of Life Chapter 4.
				            break;
							case 5:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188100334, 1)); //Records Of Life Chapter 5.
				            break;
						}
					}
                }
			break;
        }
    }
	
	@Override
	public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		player.getController().updateNearbyQuests();
		//Talk with the Agent.
		sendMsgByRace(1403340, Race.PC_ALL, 5000);
		//You must destroy the Aether seals to enter.
		sendMsgByRace(1403210, Race.PC_ALL, 30000);
		//The Antiquarian has begun activating the Eternity Relics.
		sendMsgByRace(1403212, Race.PC_ALL, 60000);
		//The Antiquarian of Atreia has activated all Eternity Relics.
		sendMsgByRace(1403213, Race.PC_ALL, 120000);
		if (spawnRace == null) {
			spawnRace = player.getRace();
			spawnHistoriesOfAtreia();
			spawnEmpyreanHistories();
			spawnLibraryGuardianRace();
			spawnRecordsFromTheEraOfMen();
		}
	}
	
	private void spawnLibraryGuardianRace() {
        final int libraryGuardian = spawnRace == Race.ASMODIANS ? 806151 : 806150;
		spawn(libraryGuardian, 737.3133f, 490.04956f, 468.99835f, (byte) 31);
        spawn(libraryGuardian, 718.2463f, 501.0919f, 468.99835f, (byte) 9);
        spawn(libraryGuardian, 718.2918f, 522.9931f, 468.99835f, (byte) 111);
        spawn(libraryGuardian, 737.1272f, 533.95374f, 468.99835f, (byte) 90);
        spawn(libraryGuardian, 756.2984f, 523.1093f, 468.99835f, (byte) 71);
        spawn(libraryGuardian, 756.3234f, 501.1647f, 468.99835f, (byte) 51);
    }
	private void spawnHistoriesOfAtreia() {
        final int historiesOfAtreia = spawnRace == Race.ASMODIANS ? 703149 : 703131;
		spawn(historiesOfAtreia, 625.27313f, 500.36285f, 468.95096f, (byte) 0, 133);
		spawn(historiesOfAtreia, 619.94202f, 422.01804f, 468.95096f, (byte) 0, 137);
		spawn(historiesOfAtreia, 620.38477f, 600.65179f, 468.95096f, (byte) 0, 220);
		spawn(historiesOfAtreia, 569.55731f, 526.27197f, 469.02530f, (byte) 0, 229);
    }
	private void spawnRecordsFromTheEraOfMen() {
        final int recordsFromTheEraOfMen = spawnRace == Race.ASMODIANS ? 703150 : 703132;
		spawn(recordsFromTheEraOfMen, 570.76123f, 337.31241f, 468.95096f, (byte) 0, 343);
		spawn(recordsFromTheEraOfMen, 443.34570f, 341.27530f, 469.01694f, (byte) 0, 355);
		spawn(recordsFromTheEraOfMen, 394.60165f, 443.42435f, 468.95096f, (byte) 0, 360);
		spawn(recordsFromTheEraOfMen, 480.38297f, 678.60730f, 469.01431f, (byte) 0, 394);
		spawn(recordsFromTheEraOfMen, 387.74930f, 500.32230f, 468.95096f, (byte) 0, 396);
		spawn(recordsFromTheEraOfMen, 319.92542f, 568.84387f, 468.95096f, (byte) 0, 404);
    }
	private void spawnEmpyreanHistories() {
        final int empyreanHistories = spawnRace == Race.ASMODIANS ? 703151 : 703133;
		spawn(empyreanHistories, 502.64456f, 454.79669f, 468.95096f, (byte) 0, 268);
		spawn(empyreanHistories, 413.44009f, 568.73181f, 468.95096f, (byte) 0, 371);
		spawn(empyreanHistories, 528.64270f, 599.86584f, 468.95096f, (byte) 0, 372);
		spawn(empyreanHistories, 549.72137f, 648.74438f, 468.95096f, (byte) 0, 373);
		spawn(empyreanHistories, 439.38571f, 504.14023f, 468.95096f, (byte) 0, 399);
    }
	
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        doors = instance.getDoors();
		switch (Rnd.get(1, 3)) {
			case 1:
				spawn(857452, 552.1911f, 511.7292f, 468.97675f, (byte) 0); //Relic Techgolem.
				spawn(857456, 460.161f, 672.068f, 468.97745f, (byte) 92); //Augmented Fleshgolem.
				spawn(857459, 460.66083f, 351.61194f, 468.9799f, (byte) 21); //Crystalized Shardgolem.
			break;
			case 2:
				spawn(857456, 552.1911f, 511.7292f, 468.97675f, (byte) 0); //Augmented Fleshgolem.
				spawn(857459, 460.161f, 672.068f, 468.97745f, (byte) 92); //Crystalized Shardgolem.
				spawn(857452, 460.66083f, 351.61194f, 468.9799f, (byte) 21); //Relic Techgolem.
			break;
			case 3:
				spawn(857459, 552.1911f, 511.7292f, 468.97675f, (byte) 0); //Crystalized Shardgolem.
				spawn(857452, 460.161f, 672.068f, 468.97745f, (byte) 92); //Relic Techgolem.
				spawn(857456, 460.66083f, 351.61194f, 468.9799f, (byte) 21); //Augmented Fleshgolem.
			break;
		} switch (Rnd.get(1, 3)) {
			case 1:
				spawn(857460, 255.67651f, 512.3747f, 468.84964f, (byte) 0); //Ancient Relic Techgolem.
			break;
			case 2:
				spawn(857462, 255.67651f, 512.3747f, 468.84964f, (byte) 0); //Fleshgolem Captain.
			break;
			case 3:
				spawn(857464, 255.67651f, 512.3747f, 468.84964f, (byte) 0); //Mountainous Shardgolem.
			break;
		} switch (Rnd.get(1, 8)) {
			case 1:
			    deleteNpc(220334); //Artifact Mimic; Mimic-In-The-Box.
				spawn(806139, 345.74078f, 392.68344f, 469.52179f, (byte) 0); //Cryptograph Cube.
			break;
			case 2:
			    deleteNpc(220334); //Artifact Mimic; Mimic-In-The-Box.
				spawn(806139, 345.26672f, 631.75073f, 469.52179f, (byte) 0); //Cryptograph Cube.
			break;
			case 3:
			    deleteNpc(220334); //Artifact Mimic; Mimic-In-The-Box.
				spawn(806139, 668.62073f, 630.28986f, 469.52179f, (byte) 0); //Cryptograph Cube.
			break;
			case 4:
			    deleteNpc(220334); //Artifact Mimic; Mimic-In-The-Box.
			    spawn(806139, 414.77441f, 352.00488f, 469.52179f, (byte) 0); //Cryptograph Cube.
			break;
			case 5:
			    deleteNpc(220334); //Artifact Mimic; Mimic-In-The-Box.
			    spawn(806139, 599.04608f, 352.67654f, 469.52179f, (byte) 0); //Cryptograph Cube.
			break;
			case 6:
			    deleteNpc(220334); //Artifact Mimic; Mimic-In-The-Box.
			    spawn(806139, 414.85263f, 671.28998f, 469.52179f, (byte) 0); //Cryptograph Cube.
			break;
			case 7:
			    deleteNpc(220334); //Artifact Mimic; Mimic-In-The-Box.
			    spawn(806139, 668.36761f, 392.10706f, 469.52179f, (byte) 0); //Cryptograph Cube.
			break;
			case 8:
			    deleteNpc(220334); //Artifact Mimic; Mimic-In-The-Box.
			    spawn(806139, 598.98456f, 672.07361f, 469.52179f, (byte) 0); //Cryptograph Cube.
			break;
		} switch (Rnd.get(1, 8)) {
			case 1:
			    deleteNpc(806139); //Cryptograph Cube.
				spawn(220334, 345.74078f, 392.68344f, 469.52179f, (byte) 0); //Artifact Mimic; Mimic-In-The-Box.
			break;
			case 2:
			    deleteNpc(806139); //Cryptograph Cube.
				spawn(220334, 345.26672f, 631.75073f, 469.52179f, (byte) 0); //Artifact Mimic; Mimic-In-The-Box.
			break;
			case 3:
			    deleteNpc(806139); //Cryptograph Cube.
				spawn(220334, 668.62073f, 630.28986f, 469.52179f, (byte) 0); //Artifact Mimic; Mimic-In-The-Box.
			break;
			case 4:
			    deleteNpc(806139); //Cryptograph Cube.
			    spawn(220334, 414.77441f, 352.00488f, 469.52179f, (byte) 0); //Artifact Mimic; Mimic-In-The-Box.
			break;
			case 5:
			    deleteNpc(806139); //Cryptograph Cube.
			    spawn(220334, 599.04608f, 352.67654f, 469.52179f, (byte) 0); //Artifact Mimic; Mimic-In-The-Box.
			break;
			case 6:
			    deleteNpc(806139); //Cryptograph Cube.
			    spawn(220334, 414.85263f, 671.28998f, 469.52179f, (byte) 0); //Artifact Mimic; Mimic-In-The-Box.
			break;
			case 7:
			    deleteNpc(806139); //Cryptograph Cube.
			    spawn(220334, 668.36761f, 392.10706f, 469.52179f, (byte) 0); //Artifact Mimic; Mimic-In-The-Box.
			break;
			case 8:
			    deleteNpc(806139); //Cryptograph Cube.
			    spawn(220334, 598.98456f, 672.07361f, 469.52179f, (byte) 0); //Artifact Mimic; Mimic-In-The-Box.
			break;
		}
    }
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 701432: //IDEternity_01_Secret_Door_01.
			    despawnNpc(npc);
			break;
			case 703009: //Shedim Eternity Relic.
			    despawnNpc(npc);
				//Shedim Seal has been destroyed.
				sendMsgByRace(1403269, Race.PC_ALL, 0);
			break;
			case 703010: //Seraphim Eternity Relic.
			    despawnNpc(npc);
				//Seraphim Seal has been destroyed.
				sendMsgByRace(1403270, Race.PC_ALL, 0);
				deleteNpc(703017);
			break;
			case 703011: //Shedim Eternity Relic.
			    despawnNpc(npc);
				//Shedim Seal has been destroyed.
				sendMsgByRace(1403269, Race.PC_ALL, 0);
			break;
			case 703012: //Seraphim Eternity Relic.
			    despawnNpc(npc);
				//Seraphim Seal has been destroyed.
				sendMsgByRace(1403270, Race.PC_ALL, 0);
				deleteNpc(703018);
			break;
			case 703013: //Shedim Eternity Relic.
			    despawnNpc(npc);
				//Shedim Seal has been destroyed.
				sendMsgByRace(1403269, Race.PC_ALL, 0);
			break;
			case 703014: //Seraphim Eternity Relic.
				despawnNpc(npc);
				//Seraphim Seal has been destroyed.
				sendMsgByRace(1403270, Race.PC_ALL, 0);
				deleteNpc(703019);
			break;
			case 703015: //Shedim Eternity Relic.
			    despawnNpc(npc);
				//Shedim Seal has been destroyed.
				sendMsgByRace(1403269, Race.PC_ALL, 0);
			break;
			case 703016: //Seraphim Eternity Relic.
			    despawnNpc(npc);
				//Seraphim Seal has been destroyed.
				sendMsgByRace(1403270, Race.PC_ALL, 0);
				deleteNpc(703020);
			break;
			case 857460: //Ancient Relic Techgolem.
			case 857462: //Fleshgolem Captain.
			case 857464: //Mountainous Shardgolem.
			    doors.get(33).setOpen(true);
				//The Antiquarian of Atreia is defeated and the Eternity Relics ceased functioning.
				sendMsgByRace(1403214, Race.PC_ALL, 0);
				final int ArchivesExit = spawnRace == Race.ASMODIANS ? 806192 : 806191;
				spawn(ArchivesExit, 222.88667f, 511.78955f, 468.80215f, (byte) 0);
				final int ArchivesToCradle = spawnRace == Race.ASMODIANS ? 806057 : 806055;
				spawn(ArchivesToCradle, 256.28693f, 512.5591f, 468.84964f, (byte) 118);
				spawn(806153, 245.83438f, 512.4957f, 468.80215f, (byte) 119); //Cryptograph Cube.
				sendMsg("[SUCCES]: You have finished <Archives Of Eternity>");
			break;
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
	
	@Override
    public void onInstanceDestroy() {
        doors.clear();
    }
}