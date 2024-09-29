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

import java.util.*;
import javolution.util.FastMap;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.*;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.utils.*;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author (Encom)
/** Source: https://www.youtube.com/watch?v=8Qt-ZODwhoA
/****/

@InstanceID(301570000)
public class ArchivesOfEternityQInstance extends GeneralInstanceHandler
{
	private int IDEternityQSadoWi65An01;
	private int IDEternityQSadoFi65An01;
	private int IDEternityQSadoPr65An01;
	private int IDEternityQSadoWi65An02;
	private Map<Integer, StaticDoor> doors;
	private List<Integer> movies = new ArrayList<Integer>();
	
	@Override
	public void onEnterInstance(Player player) {
		sendMovie(player, 935);
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
		   /**
			* MOBS
			*/
			case 857782: //Lesser Fleshgolem.
			    IDEternityQSadoWi65An01++;
				if (IDEternityQSadoWi65An01 == 3) {
					doors.get(53).setOpen(true);
					doors.get(56).setOpen(true);
					//Move to where the door is open.
					sendMsgByRace(1403296, Race.PC_ALL, 0);
				}
			break;
			case 857783: //Crystalized Shardgolem.
				doors.get(252).setOpen(true);
				doors.get(33).setOpen(true);
				//The door to the Atreia History Archives is now open.
				sendMsgByRace(1403297, Race.PC_ALL, 0);
			break;
			case 857901: //Lesser Shardgolem.
				IDEternityQSadoFi65An01++;
				if (IDEternityQSadoFi65An01 == 2) {
				    doors.get(67).setOpen(true);
				    doors.get(54).setOpen(true);
				    //The door to the next library is now open.
				    sendMsgByRace(1403298, Race.PC_ALL, 0);
				}
			break;
			case 857902: //Lesser Shardgolem.
				IDEternityQSadoPr65An01++;
				if (IDEternityQSadoPr65An01 == 5) {
				    doors.get(449).setOpen(true);
				    doors.get(55).setOpen(true);
				    //The door to the Human History Archives is now open.
				    sendMsgByRace(1403299, Race.PC_ALL, 0);
				}
			break;
			case 857784: //Relic Techgolem.
				doors.get(64).setOpen(true);
				doors.get(27).setOpen(true);
				//The door to the next library is now open.
				sendMsgByRace(1403300, Race.PC_ALL, 0);
			break;
			case 857948: //Lesser Fleshgolem.
			    IDEternityQSadoWi65An02++;
				if (IDEternityQSadoWi65An02 == 5) {
					doors.get(311).setOpen(true);
					doors.get(57).setOpen(true);
					//The door to the Empyrean Lord's Archives is now open.
					sendMsgByRace(1403301, Race.PC_ALL, 0);
				}
			break;
			case 857903: //Augmented Fleshgolem.
			    doors.get(77).setOpen(true);
				doors.get(52).setOpen(true);
				//The door to the next library is now open.
				sendMsgByRace(1403302, Race.PC_ALL, 0);
			break;
			case 857916: //Violent Bust.
			    doors.get(421).setOpen(true);
				doors.get(65).setOpen(true);
				//The door to the Archive of All Knowledge is now open.
				sendMsgByRace(1403303, Race.PC_ALL, 0);
			break;
			
		   /**
			* MALE ELYOS
			*/
			case 857788: //Archdaeva Of Eternal Storms.
				despawnNpc(npc);
				//You are graced with the aura of Blessed Spring.
			    sendMsgByRace(1403365, Race.PC_ALL, 0);
				spawn(857786, 231.63109f, 511.9707f, 468.80215f, (byte) 0);
			break;
			case 857786: //Archdaeva Of Eternal Waves.
			    despawnNpc(npc);
				//You are graced with the aura of Blessed Earth.
				sendMsgByRace(1403366, Race.PC_ALL, 0);
				spawn(857787, 231.63109f, 511.9707f, 468.80215f, (byte) 0);
			break;
			case 857787: //Archdaeva Of Eternal Earth.
			    despawnNpc(npc);
				spawn(857785, 231.63109f, 511.9707f, 468.80215f, (byte) 0);
			break;
			case 857785: //Archdaeva Of Eternal Flames.
			    despawnNpc(npc);
				deleteNpc(703130);
				sendMovie(player, 927);
				doors.get(90).setOpen(true);
				spawn(806179, 222.71474f, 511.98355f, 468.78000f, (byte) 0, 35); //Eternity Rift.
				//An Eternity Rift has opened, allowing you to leave the Archives of Eternity.
				sendMsgByRace(1403304, Race.PC_ALL, 0);
				//sendMsg("[SUCCES]: you are a <Archdaeva>");
			break;
		   /**
			* FEMALE ELYOS
			*/
			case 857795: //Archdaeva Of Eternal Storms.
				despawnNpc(npc);
				//You are graced with the aura of Blessed Spring.
			    sendMsgByRace(1403365, Race.PC_ALL, 0);
				spawn(857793, 231.63109f, 511.9707f, 468.80215f, (byte) 0);
			break;
			case 857793: //Archdaeva Of Eternal Waves.
			    despawnNpc(npc);
				//You are graced with the aura of Blessed Earth.
				sendMsgByRace(1403366, Race.PC_ALL, 0);
				spawn(857794, 231.63109f, 511.9707f, 468.80215f, (byte) 0);
			break;
			case 857794: //Archdaeva Of Eternal Earth.
			    despawnNpc(npc);
				spawn(857792, 231.63109f, 511.9707f, 468.80215f, (byte) 0);
			break;
			case 857792: //Archdaeva Of Eternal Flames.
			    despawnNpc(npc);
				deleteNpc(703130);
				sendMovie(player, 928);
				doors.get(90).setOpen(true);
				spawn(806179, 222.71474f, 511.98355f, 468.78000f, (byte) 0, 35); //Eternity Rift.
				//An Eternity Rift has opened, allowing you to leave the Archives of Eternity.
				sendMsgByRace(1403304, Race.PC_ALL, 0);
				//sendMsg("[SUCCES]: you are a <Archdaeva>");
			break;
		   /**
			* MALE ASMODIANS
			*/
			case 857799: //Archdaeva Of Eternal Storms.
				despawnNpc(npc);
				//You are graced with the aura of Blessed Spring.
			    sendMsgByRace(1403365, Race.PC_ALL, 0);
				spawn(857797, 231.63109f, 511.9707f, 468.80215f, (byte) 0);
			break;
			case 857797: //Archdaeva Of Eternal Waves.
			    despawnNpc(npc);
				//You are graced with the aura of Blessed Earth.
				sendMsgByRace(1403366, Race.PC_ALL, 0);
				spawn(857798, 231.63109f, 511.9707f, 468.80215f, (byte) 0);
			break;
			case 857798: //Archdaeva Of Eternal Earth.
			    despawnNpc(npc);
				spawn(857796, 231.63109f, 511.9707f, 468.80215f, (byte) 0);
			break;
			case 857796: //Archdaeva Of Eternal Flames.
			    despawnNpc(npc);
				deleteNpc(703130);
				sendMovie(player, 927);
				doors.get(90).setOpen(true);
				spawn(806180, 222.71474f, 511.98355f, 468.78000f, (byte) 0, 35); //Eternity Rift.
				//An Eternity Rift has opened, allowing you to leave the Archives of Eternity.
				sendMsgByRace(1403304, Race.PC_ALL, 0);
				//sendMsg("[SUCCES]: you are a <Archdaeva>");
			break;
		   /**
			* FEMALE ASMODIANS
			*/
			case 857803: //Archdaeva Of Eternal Storms.
				despawnNpc(npc);
				//You are graced with the aura of Blessed Spring.
			    sendMsgByRace(1403365, Race.PC_ALL, 0);
				spawn(857801, 231.63109f, 511.9707f, 468.80215f, (byte) 0);
			break;
			case 857801: //Archdaeva Of Eternal Waves.
			    despawnNpc(npc);
				//You are graced with the aura of Blessed Earth.
				sendMsgByRace(1403366, Race.PC_ALL, 0);
				spawn(857802, 231.63109f, 511.9707f, 468.80215f, (byte) 0);
			break;
			case 857802: //Archdaeva Of Eternal Earth.
			    despawnNpc(npc);
				spawn(857800, 231.63109f, 511.9707f, 468.80215f, (byte) 0);
			break;
			case 857800: //Archdaeva Of Eternal Flames.
			    despawnNpc(npc);
				deleteNpc(703130);
				sendMovie(player, 928);
				doors.get(90).setOpen(true);
				spawn(806180, 222.71474f, 511.98355f, 468.78000f, (byte) 0, 35); //Eternity Rift.
				//An Eternity Rift has opened, allowing you to leave the Archives of Eternity.
				sendMsgByRace(1403304, Race.PC_ALL, 0);
				//sendMsg("[SUCCES]: you are a <Archdaeva>");
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
	
	private void sendMovie(Player player, int movie) {
		if (!movies.contains(movie)) {
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}
	
	@Override
    public void onInstanceDestroy() {
        doors.clear();
    }
}