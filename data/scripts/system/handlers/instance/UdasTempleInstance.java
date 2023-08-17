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

import java.util.Map;
import java.util.Set;

/**
Author (Encom)

Source: http://gameguide.na.aiononline.com/aion/Udas+Temple+Guide

There are three hero rank named monsters inside the Udas Temple.
Each boss is located inside a locked room.
You have to either get a key by defeating a Named Monster on the way to each door or defeating a specific monster in order to open the door.

Three Named Elite class Dragonbound randomly spawn.
You have to weaken the Chamber of Unity by defeating each of them.
One of these resides in the Silent Chapel, which requires a key that can be obtained from "Cota the Gatekeeper".
But "Cota the Gatekeeper" does not affect the seal on the chamber.

Once in the Chamber of Unity, you will face Vallakhan, a Hero grade Dragonbound.
From him, you will obtain the key to the Great Chapel.

Once you defeat Vallakhan, you will receive the key to the Great Chapel.
Anurati resides here and has a chance to drop Heroic accessories and Fabled weapons.

You must defeat another named Dragonbound in order to receive the Key to the Chamber of Guidance.
He is located in the area hilighted above.
Once inside the Chamber of Guidance, you will face Nexus, the final boss of Udas Temple.
**/
@InstanceID(300150000)
public class UdasTempleInstance extends GeneralInstanceHandler
{
	private Map<Integer, StaticDoor> doors;
	
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
		doors = instance.getDoors();
		switch (Rnd.get(1, 2)) {
		    case 1:
				spawn(215787, 778.537f, 661.278f, 134.0f, (byte) 78); //Cota The Gatekeeper.
			break;
			case 2:
				spawn(215787, 689.529f, 669.005f, 134.0f, (byte) 103); //Cota The Gatekeeper.
			break;
		} switch (Rnd.get(1, 2)) {
		    case 1:
				spawn(215788, 807.969f, 560.979f, 130.902f, (byte) 60); //Kiya The Protector.
			break;
			case 2:
				spawn(215788, 749.2811f, 559.79895f, 131.29901f, (byte) 0); //Kiya The Protector.
			break;
		}
    }
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 215782: //Vallakhan.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000084, 1)); //Great Chapel Key.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053788, 1)); //Greater Stigma Support Bundle.
		    break;
			case 215787: //Cota The Gatekeeper.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000083, 1)); //Silent Chapel Key.
		    break;
			case 215791: //Agra The Guide.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000085, 1)); //Chamber Of Guidance Key.
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
        Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 215782: //Vallakhan.
				//Devoted Anurati has appeared in the Great Chapel.
				sendMsgByRace(1400442, Race.PC_ALL, 2000);
				spawn(215793, 636.641f, 439.788f, 138.0f, (byte) 30); //Devoted Anurati.
			break;
			case 215783: //Nexus.
				sendMsg("[SUCCES]: You have finished <Udas Temple>");
				spawn(730255, 508.3610f, 362.7170f, 137.0000f, (byte) 31); //Udas Temple Exit.
				switch (Rnd.get(1, 2)) {
		            case 1:
				        spawn(702658, 508.4381f, 374.57526f, 135.88919f, (byte) 30); //Abbey Box.
					break;
					case 2:
					    spawn(702659, 508.4381f, 374.57526f, 135.88919f, (byte) 30); //Noble Abbey Box.
					break;
				}
            break;
			case 215787: //Cota The Gatekeeper.
				//The Seal of Uniformity has been weakened.
				sendMsgByRace(1400366, Race.PC_ALL, 2000);
			break;
			case 215790: //Tala The Protector.
			    doors.get(99).setOpen(true);
				//You can now enter the Chamber of Unity.
				sendMsgByRace(1400367, Race.PC_ALL, 2000);
			break;
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
	
	@Override
    public void onInstanceDestroy() {
        doors.clear();
    }
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(185000083, storage.getItemCountByItemId(185000083)); //Silent Chapel Key.
		storage.decreaseByItemId(185000084, storage.getItemCountByItemId(185000084)); //Great Chapel Key.
		storage.decreaseByItemId(185000085, storage.getItemCountByItemId(185000085)); //Chamber Of Guidance Key.
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