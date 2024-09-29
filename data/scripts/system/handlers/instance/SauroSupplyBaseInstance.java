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
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
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
import javolution.util.FastMap;

import java.util.Map;
import java.util.Set;

/****/
/** Author (Encom)
/****/

@InstanceID(301130000)
public class SauroSupplyBaseInstance extends GeneralInstanceHandler
{
	private Map<Integer, StaticDoor> doors;
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 233258: //Deranak The Reaver.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053219, 1)); //[Event] Sauro Commander's Accessory Box.
			break;
			case 230846: //Sauro Base Grave Robber.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052578, 1)); //Looted Sauro Supplies.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 186000237, 20)); //Ancient Coin.
					}
				}
			break;
			case 230847: //Mystery Box Key.
				//Be careful in your selection. The key cannot be changed once it is chosen.
				sendMsgByRace(1401946, Race.PC_ALL, 0);
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000179, 1)); //Danuar Omphanium Key.
					}
				}
			break;
			case 230852: //Commander Ranodim.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052582, 1)); //Dragon's Conquerer Mark Box.
					} switch (Rnd.get(1, 3)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000176, 1)); //Red Storeroom Key.
				        break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000177, 1)); //Blue Storeroom Key.
				        break;
					    case 3:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000178, 1)); //Green Storeroom Key.
				        break;
					}
				}
			break;
			case 230849: //Guard Captain Rohuka.
			case 230850: //Research Teselik.
			case 230851: //Chief Gunner Kurmata.
			case 230853: //Chief Of Staff Moriata.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052582, 1)); //Dragon's Conquerer Mark Box.
					}
				}
			break;
			case 230857: //Guard Captain Ahuradim.
			case 230858: //Brigade General Sheba.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052582, 1)); //Dragon's Conquerer Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
					} switch (Rnd.get(1, 2)) {
				        case 1:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053211, 1)); //[Event] Sauro Commander's Weapon Box.
				        break;
					    case 2:
				            dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053219, 1)); //[Event] Sauro Commander's Accessory Box.
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
			case 802181: //Sauro Supply Base Opportunity Bundle.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000051, 30)); //Major Ancient Crown.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000052, 30)); //Greater Ancient Crown.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000236, 50)); //Blood Mark.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000237, 50)); //Ancient Coin.
			break;
		}
	}
	
	/**
	 * Bonus Monster: "Sauro Base Grave Robber"
	 * They can appear in "5 different rooms" and give:
	 * Ancient Coins.
	 * Ancient Manastones.
	 * Skins.
     */
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
		doors = instance.getDoors();
		//An intruder alarm has sounded. The Sauro Elite Protectorate are gathering.
		sendMsgByRace(1401810, Race.PC_ALL, 10000);
		//The Sauro Elite Protectorate has assembled.
		sendMsgByRace(1401811, Race.PC_ALL, 30000);
		//The Sauro Elite Protectorate approaches.
		sendMsgByRace(1401812, Race.PC_ALL, 50000);
		//The Sauro Elite Protectorate is one minute out.
		sendMsgByRace(1401813, Race.PC_ALL, 70000);
		//The Sauro Elite Protectorate is upon you.
		sendMsgByRace(1401814, Race.PC_ALL, 130000);
		switch (Rnd.get(1, 5)) {
		    case 1:
				spawn(230846, 464.07788f, 401.3575f, 182.15321f, (byte) 10);
			break;
			case 2:
				spawn(230846, 496.30792f, 412.814f, 182.13792f, (byte) 73);
			break;
			case 3:
				spawn(230846, 497.15717f, 392.34656f, 182.14955f, (byte) 75);
			break;
			case 4:
				spawn(230846, 496.2902f, 358.0765f, 182.14955f, (byte) 48);
			break;
			case 5:
				spawn(230846, 464.15985f, 389.7157f, 182.15321f, (byte) 109);
			break;
		}
    }
	
    @Override
    public void onDie(Npc npc) {
        Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
		   /**
			* Area 1: Guardroom And Rune Hall.
		    */
			case 230849: //Guard Captain Rohuka.
				doors.get(383).setOpen(true);
				//The door to the Defiled Danuar Temple has opened.
				sendMsgByRace(1401914, Race.PC_ALL, 0);
			break;
			case 230851: //Chief Gunner Kurmata.
				doors.get(59).setOpen(true);
				//The door to the Danuar Meditation Garden has opened.
				sendMsgByRace(1401915, Race.PC_ALL, 0);
				switch (Rnd.get(1, 2)) {
				    case 1:
				        doors.get(382).setOpen(true);
						//With the gatekeeper down, the door on the left is open!
						sendMsgByRace(1401229, Race.PC_ALL, 5000);
						spawn(230797, 610.7328f, 518.80884f, 191.2776f, (byte) 75); //Sheban Legion Elite Ambusher.
					break;
			        case 2:
						doors.get(387).setOpen(true);
						//With the gatekeeper down, the door on the right is open!
						sendMsgByRace(1401230, Race.PC_ALL, 5000);
						spawn(230797, 611.1872f, 452.91882f, 191.2776f, (byte) 39); //Sheban Legion Elite Ambusher.
					break;
				}
			break;
			
		   /**
			* Area 2: Rune Cloister And Logistic Base.
		    */
			case 230818: //Sheban Legion Elite Gunner.
				doors.get(372).setOpen(true);
				//The door to the Head Researcher's Office has opened.
				sendMsgByRace(1401916, Race.PC_ALL, 0);
			break;
			case 230850: //Research Teselik.
				doors.get(375).setOpen(true);
				//The door to the Lost Tree of Devotion has opened.
				sendMsgByRace(1401917, Race.PC_ALL, 0);
			break;
			
		   /**
			* Area 3: Rune Bridge And Logistic Base Arsenal.
		    */
			case 233255: //Gatekeeper Stranir.
				doors.get(378).setOpen(true);
				//The door to the Sauro Armory has opened.
				sendMsgByRace(1401918, Race.PC_ALL, 0);
			break;
			case 230852: //Commander Ranodim.
				doors.get(388).setOpen(true);
				//The door to the Heavy Storage Area has opened.
				sendMsgByRace(1401919, Race.PC_ALL, 0);
			break;
			
			/**
			 * Area 4: Chiefs Chamber.
		     */
			case 230791: //Sheban Legion Elite Assaulter.
				doors.get(376).setOpen(true);
				//The door to Moriata's Quarters has opened.
				sendMsgByRace(1401920, Race.PC_ALL, 0);
			break;
			case 230853: //Chief Of Staff Moriata.
				//A device leading to the Danuar Omphanium has been activated.
				sendMsgByRace(1401921, Race.PC_ALL, 0);
				//The passage to the Danuar Omphanium will be open for five minutes.
				sendMsgByRace(1401922, Race.PC_ALL, 5000);
				spawn(730872, 127.77696f, 432.75684f, 151.69659f, (byte) 0, 3);
			break;
			
			/**
			 * Area 5: Final Boss.
		     */
			case 230857: //Guard Captain Ahuradim.
				switch (Rnd.get(1, 2)) {
		            case 1:
				        spawn(702658, 703.3344f, 883.07666f, 411.5939f, (byte) 90); //Abbey Box.
			        break;
			        case 2:
				        spawn(702659, 703.3344f, 883.07666f, 411.5939f, (byte) 90); //Noble Abbey Box.
					break;
				}
				spawn(801967, 708.9197f, 884.59625f, 411.57986f, (byte) 45); //Sauro Supply Base Exit.
				spawn(802181, 710.25726f, 889.6806f, 411.59103f, (byte) 0); //Sauro Supply Base Opportunity Bundle.
				//sendMsg("[SUCCES]: You have finished <Sauro Supply Base>");
			break;
			case 230858: //Brigade General Sheba.
				switch (Rnd.get(1, 2)) {
		            case 1:
				        spawn(702658, 900.2497f, 896.3568f, 411.3568f, (byte) 30); //Abbey Box.
			        break;
			        case 2:
				        spawn(702659, 900.2497f, 896.3568f, 411.3568f, (byte) 30); //Noble Abbey Box.
					break;
				}
				spawn(801967, 905.3781f, 895.2461f, 411.57785f, (byte) 75); //Sauro Supply Base Exit.
				spawn(802181, 906.9721f, 889.6604f, 411.59854f, (byte) 0); //Sauro Supply Base Opportunity Bundle.
				//sendMsg("[SUCCES]: You have finished <Sauro Supply Base>");
			break;
		}
    }
	
	public void removeItems(Player player) {
        Storage storage = player.getInventory();
        storage.decreaseByItemId(185000176, storage.getItemCountByItemId(185000176)); //Red Storeroom Key.
		storage.decreaseByItemId(185000177, storage.getItemCountByItemId(185000177)); //Blue Storeroom Key.
        storage.decreaseByItemId(185000178, storage.getItemCountByItemId(185000178)); //Green Storeroom Key.
		storage.decreaseByItemId(185000179, storage.getItemCountByItemId(185000179)); //Danuar Stone Room Key.
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
        doors.clear();
    }
}