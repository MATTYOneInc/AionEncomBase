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
/** Source: https://www.youtube.com/watch?v=9TDk-tBgtHE
/****/

@InstanceID(301610000)
public class TheobomosTestChamberInstance extends GeneralInstanceHandler
{
	private Map<Integer, StaticDoor> doors;
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
			case 220425: //Galateia The Living.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000264, 1)); //Blood-Sealed Treasure Box Key.
			break;
			case 220426: //Desecrated Ifrit.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188058413, 1)); //ì?´ê³„ ì•”ë£¡ì?˜ ë¬´ê¸° ìƒ?ìž?.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188057620, 1)); //Chaotic Dimension Stone Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 166040001, 1)); //Essence Core Solution.
						switch (Rnd.get(1, 4)) {
				            case 1:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188057618, 1)); //Theobomos's Weapon Box.
				            break;
							case 2:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188057619, 1)); //Theobomos's Armor Box.
				            break;
							case 3:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188054908, 1)); //Laboratory Weapon Box.
				            break;
					        case 4:
				                dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188054909, 1)); //Laboratory Armor Box.
				            break;
						}
					}
                }
            break;
			case 806221: //Blood-Sealed Coffer.
				switch (Rnd.get(1, 2)) {
				    case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 110000049, 1)); //Galateia's Dress.
					break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 125004518, 1)); //Galateia's Hairpin.
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
			case 220424: //Blood-Swollen Arachne.
			    doors.get(129).setOpen(true);
				//A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 2000);
			break;
			case 220425: //Galateia The Living.
			    doors.get(2).setOpen(true);
				//A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 2000);
			break;
			case 220426: //Desecrated Ifrit.
			    spawn(806206, 298.48328f, 120.17713f, 196.02815f, (byte) 68); //Theobomos Test Chamber Exit.
				spawn(806221, 284.93094f, 119.47065f, 196.01285f, (byte) 1); //Blood-Sealed Coffer.
			    //sendMsg("[SUCCES]: You have finished <Theobomos Test Chamber>");
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