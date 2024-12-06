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
/** Source: https://www.youtube.com/watch?v=wqwUrBYN85A
/****/

@InstanceID(301600000)
public class AdmaFallInstance extends GeneralInstanceHandler
{
	private Map<Integer, StaticDoor> doors;
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
			case 220418: //Lady Karemiwen Adma.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000263, 1)); //Cursed Key.
			break;
			case 220427: //Reaper Of Adma Castle.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188057620, 1)); //Chaotic Dimension Stone Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058413, 1)); //ì?´ê³„ ì•”ë£¡ì?˜ ë¬´ê¸° ìƒ?ìž?.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 166040001, 1)); //Essence Core Solution.
						switch (Rnd.get(1, 2)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188054906, 1)); //Adma Weapon Box.
				            break;
					        case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188054907, 1)); //Adma Armor Box.
				            break;
						}
					}
                }
            break;
			case 806220: //Adma Family Coffers.
				switch (Rnd.get(1, 2)) {
				    case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 110000048, 1)); //Karemiwen's Gown.
					break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 125004517, 1)); //Karemiwen's Hairpin.
					break;
				}
			break;
        }
    }
	
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        doors = instance.getDoors();
    }
	
	@Override
	public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 220417: //Steward Zeetrum.
			    doors.get(1).setOpen(true);
				//A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 2000);
			break;
			case 220418: //Lady Karemiwen Adma.
			    doors.get(28).setOpen(true);
				//A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 2000);
			break;
			case 220427: //Reaper Of Adma Castle.
			    spawn(806205, 532.3307f, 510.2517f, 197.94453f, (byte) 60); //Adma's Fall Exit.
				spawn(806220, 525.2205f, 510.08893f, 197.72095f, (byte) 44); //Adma Family Coffers.
			    //sendMsg("[SUCCES]: You have finished <Adma's Fall>");
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
	
	@Override
    public void onInstanceDestroy() {
        doors.clear();
    }
}