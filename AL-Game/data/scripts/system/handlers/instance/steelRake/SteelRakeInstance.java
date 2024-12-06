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
package instance.steelRake;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
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
import javolution.util.FastMap;

import java.util.Set;

/****/
/** Author (Encom)
/****/

@InstanceID(300100000)
public class SteelRakeInstance extends GeneralInstanceHandler
{
	private boolean isInstanceDestroyed;
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
			case 215056: //Warden Tantaka.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000049, 1)); //Engine Room Key.
					}
				}
			break;
			case 215057: //Kedomke The Drinker.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000054, 1)); //Mercenary Quarters Key.
					}
				}
			break;
			case 215058: //Timid Alakin.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000055, 1)); //The Brig Key.
					}
				}
			break;
			case 215062: //Sweeper Nunukin.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000042, 1)); //Sailor Waiting Room Key.
					}
				}
			break;
			case 215063: //Bhagwaninerk.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000043, 1)); //Starboard Deck Key.
					}
				}
			break;
			case 215064: //Collector Memekin.
			case 215065: //Discemer Werikiki.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000046, 1)); //Grogget's Safe Key.
					}
				}
			break;
			case 215066: //Technician Binukin.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000050, 1)); //Generator Room Key.
					}
				}
			break;
			case 215067: //Largimark The Smoker.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000052, 1)); //Largimark's Flint.
			break;
			case 215069: //Chief Mate Menekiki.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000053, 1)); //Weapon Military Supply Base Key.
					}
				}
			break;
			case 215070: //Chief Gunner Koakoa.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000074, 1)); //Gun Repair Deck Key.
					}
				}
			break;
			case 215078: //Madame Bovariki.
				for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
						switch (Rnd.get(1, 2)) {
							case 1:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000044, 1)); //Tavern Key 2nd Floor.
							break;
							case 2:
							    dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000045, 1)); //Loot Depository Key.
							break;
						}
					}
				}
			break;
			case 215401: //Sturdy Bubukin.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000048, 1)); //Menagerie Key.
					}
				}
			break;
			case 215411: //Zerkin The One-Eyed.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000073, 1)); //Large Gun Deck Key.
					}
				}
			break;
			case 215412: //Tamer Anikiki.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 182209084, 1)); //Taming A Manduri.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 182209099, 1)); //Taming A Manduri.
					}
				}
			break;
			case 215080: //Engineer Lahulahu.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000051, 1)); //Anchor Point Key.
					}
				}
			break;
			case 215081: //Brass-Eye Grogget.
			    for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053787, 1)); //Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188051416, 1)); //Grogget's Fabled Weapon Chest.
					}
				}
			break;
			case 215489: //Treasure Box.
			case 700554: //Pirate Ship Treasure Box.
				switch (Rnd.get(1, 2)) {
					case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050007, 1)); //Glossy Idian: Physical Attack
					break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050008, 1)); //Glossy Idian: Magical Attack.
					break;
				}
			break;
			case 700555: //Captain Treasure Box.
				switch (Rnd.get(1, 6)) {
					case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050017, 2)); //Blue Idian: Physical Attack.
					break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050018, 2)); //Blue Idian: Magical Attack.
					break;
					case 3:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050019, 2)); //Blue Idian: Physical Defense.
					break;
					case 4:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050020, 2)); //Blue Idian: Magical Defense.
					break;
					case 5:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050021, 2)); //Blue Idian: Assistance.
					break;
					case 6:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050022, 2)); //Blue Idian: Resistance.
					break;
				}
			break;
			case 215421: //Treasure Box.
				switch (Rnd.get(1, 4)) {
					case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050059, 2)); //Sparkling Idian: Physical Attack.
					break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050060, 2)); //Sparkling Idian: Magical Attack.
					break;
					case 3:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050061, 2)); //Sparkling Idian: Physical Defense.
					break;
					case 4:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166050062, 2)); //Sparkling Idian: Magical Defense.
					break;
				}
			break;
        }
    }
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		switch (Rnd.get(1, 2)) {
		    case 1:
				spawn(215064, 747.065f, 458.293f, 942.354f, (byte) 60); //Collector Memekin.
			break;
			case 2:
				spawn(215065, 728.008f, 541.524f, 942.354f, (byte) 59); //Discerner Werikiki.
			break;
		} switch (Rnd.get(1, 2)) {
		    case 1:
				spawn(215067, 532.5612f, 465.69833f, 1021.82465f, (byte) 16); //Largimark The Smoker.
			break;
			case 2:
				spawn(215067, 628.5236f, 546.44525f, 1031.04870f, (byte) 66); //Largimark The Smoker.
			break;
		} switch (Rnd.get(1, 2)) {
			case 1:
				spawn(215078, 462.71558f, 512.5599f, 952.5455f, (byte) 1); //Madame Bovariki.
			break;
			case 2:
				spawn(215078, 506.1134f, 545.7197f, 952.4226f, (byte) 74); //Madame Bovariki.
			break;
		} switch (Rnd.get(1, 2)) {
			case 1:
				spawn(798375, 516.83344f, 453.87836f, 951.70465f, (byte) 44); //Sommelikinerk.
			break;
			case 2:
				spawn(798376, 236.48676f, 515.11304f, 948.6737f, (byte) 103); //Pegureronerk.
			break;
		} switch (Rnd.get(1, 6)) { //Special Delivery.
			case 1:
				spawn(215054, 461.933350f, 510.545654f, 877.618103f, (byte) 90);
			break;
			case 2:
				spawn(215055, 461.933350f, 510.545654f, 877.618103f, (byte) 90);
			break;
			case 3:
				spawn(215074, 461.933350f, 510.545654f, 877.618103f, (byte) 90);
			break;
			case 4:
				spawn(215075, 461.933350f, 510.545654f, 877.618103f, (byte) 90);
			break;
			case 5:
				spawn(215076, 461.933350f, 510.545654f, 877.618103f, (byte) 90);
			break;
			case 6: 
				spawn(215077, 461.933350f, 510.545654f, 877.618103f, (byte) 90);
			break;
		}
	}
	
	@Override
	public void onDie(Npc npc)  {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 700549: //Air Vent Cover.
			    despawnNpc(npc);
			break;
			case 730208: //Prison Door.
			    despawnNpc(npc);
			break;
			case 215056: //Warden Tantaka.
				spawn(215421, 471.095f, 576.663f, 887.46f, (byte) 30); //Treasure Box.
				spawn(215421, 451.161f, 575.938f, 887.41f, (byte) 30); //Treasure Box.
				//The door is open and you can now access The Brig.
				sendMsgByRace(1400249, Race.PC_ALL, 2000);
				spawn(730200, 461.898f, 487.228f, 877.713f, (byte) 0, 30); //The Brig Entrance.
			break;
			case 215064: //Collector Memekin.
			case 215065: //Discerner Werikiki.
				//The door is open and you can now access Grogget's Safe.
				sendMsgByRace(1400248, Race.PC_ALL, 2000);
			break;
			case 215066: //Technician Binukin.
				//The door is open and you can now access the Drana Generator Chamber.
				sendMsgByRace(1400250, Race.PC_ALL, 2000);
				spawn(730202, 657.111f, 509.11f, 872.948f, (byte) 0, 10); //Drana Generator Chamber Access Door.
			break;
			case 215079: //Golden Eye Mantutu.
				////sendMsg("[SUCCES]: You have finished <Steel Rake>");
				spawn(700554, 736.64728f, 493.73834f, 941.4781f, (byte) 45); //Pirate Ship Treasure Box.
				spawn(700554, 720.41028f, 511.63718f, 939.7604f, (byte) 90); //Pirate Ship Treasure Box.
		        spawn(700554, 739.51251f, 506.14313f, 941.4781f, (byte) 77); //Pirate Ship Treasure Box.
				spawn(700554, 721.76172f, 491.83142f, 939.6068f, (byte) 32); //Pirate Ship Treasure Box.
				spawn(730766, 734.18994f, 484.61578f, 941.70868f, (byte) 0, 61); //Hidden Passage.
			break;
			case 215081: //Brass-Eye Grogget.
				////sendMsg("[SUCCES]: You have finished <Steel Rake>");
				spawn(700509, 403.25793f, 510.25354f, 1071.736f, (byte) 1); //Shining Box.
				spawn(700555, 426.47424f, 509.34625f, 1075.3801f, (byte) 0); //Captain Treasure Box.
				spawn(730198, 428.06598f, 486.64233f, 1075.4449f, (byte) 0, 87); //Escape Anchor.
			break;
			case 215411: //Zerkin The One-Eyed.
				//The door is open and you can now access the Large Gun Deck.
				sendMsgByRace(1400251, Race.PC_ALL, 2000);
				spawn(730203, 722.564f, 508.877f, 1012.93f, (byte) 0, 88); //Large Gun Deck Entrance.
			break;
		}
	}
	
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
	@Override
    public void onInstanceDestroy() {
		isInstanceDestroyed = true;
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
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
}