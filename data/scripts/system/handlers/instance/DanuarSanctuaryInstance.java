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
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/****/
/** Author (Encom)
/****/

@InstanceID(301380000)
public class DanuarSanctuaryInstance extends GeneralInstanceHandler
{
	private Race spawnRace;
	private Map<Integer, StaticDoor> doors;
	private List<Integer> movies = new ArrayList<Integer>();
	private FastMap<Integer, VisibleObject> objects = new FastMap<Integer, VisibleObject>();
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		spawnDanuarSanctuaryBoss();
    }
	
	@Override
    public void onEnterInstance(Player player) {
		super.onInstanceCreate(instance);
		//The Beritran Special Research Team commanders are nearing The Chamber of Ruin.
		sendMsgByRace(1401855, Race.PC_ALL, 300000);
		//The Beritran Special Research Team commanders have discovered The Chamber of Ruin.
		sendMsgByRace(1401856, Race.PC_ALL, 600000);
		//The Beritran Special Research Team commanders have entered The Chamber of Ruin.
		sendMsgByRace(1401857, Race.PC_ALL, 900000);
		//The Beritran Special Research Team commanders are collecting Danuar relics.
		sendMsgByRace(1401858, Race.PC_ALL, 1200000);
		//The Beritran Special Research Team commanders have departed with their treasures.
		sendMsgByRace(1401859, Race.PC_ALL, 1500000);
		//The Chir Grave Robbers are almost finished digging.
		sendMsgByRace(1401860, Race.PC_ALL, 1800000);
		//The Chir Grave Robbers have left.
		sendMsgByRace(1401861, Race.PC_ALL, 2100000);
		switch (player.getRace()) {
		    case ELYOS:
			    sendMovie(player, 910);
			break;
			case ASMODIANS:
			    sendMovie(player, 911);
			break;
		} if (spawnRace == null) {
			spawnRace = player.getRace();
			SpawnDanuarRace();
		}
    }
	
	private void SpawnDanuarRace() {
		final int danuarGuard1 = spawnRace == Race.ASMODIANS ? 233126 : 233129;
        final int danuarGuard2 = spawnRace == Race.ASMODIANS ? 233127 : 233130;
		final int danuarGuard3 = spawnRace == Race.ASMODIANS ? 233128 : 233131;
		spawn(danuarGuard1, 911.333f, 904.6127f, 284.5891f, (byte) 110);
        spawn(danuarGuard1, 917.35785f, 901.0081f, 284.5891f, (byte) 50);
        spawn(danuarGuard1, 1025.9675f, 474.7492f, 290.26837f, (byte) 0);
        spawn(danuarGuard1, 1033.9897f, 474.7517f, 290.26837f, (byte) 61);
		spawn(danuarGuard2, 1029.233f, 484.0199f, 290.52118f, (byte) 31);
        spawn(danuarGuard2, 978.1413f, 1337.8359f, 335.875f, (byte) 34);
        spawn(danuarGuard2, 1019.45715f, 1367.1343f, 337.25f, (byte) 52);
        spawn(danuarGuard2, 881.45166f, 892.719f, 284.55508f, (byte) 109);
        spawn(danuarGuard2, 885.13104f, 898.88446f, 284.50986f, (byte) 109);
		spawn(danuarGuard3, 1103.6545f, 439.36285f, 284.61642f, (byte) 66);
        spawn(danuarGuard3, 833.283f, 961.50146f, 304.86777f, (byte) 79);
        spawn(danuarGuard3, 824.21826f, 967.07446f, 304.86777f, (byte) 79);
        spawn(danuarGuard3, 932.1827f, 876.7008f, 305.45746f, (byte) 92);
        spawn(danuarGuard3, 949.92975f, 903.508f, 299.75253f, (byte) 93);
    }
	
	public void onDropRegistered(Npc npc) {
		Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId) {
			case 702658: //Abbey Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053579, 1)); //[Event] Abbey Bundle.
		    break;
			case 702659: //Noble Abbey Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053580, 1)); //[Event] Noble Abbey Bundle.
		    break;
			case 235600: //Shulack Mercenary Cannon Chief.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000254, 1)); //Seal Breaking Magic Cannonball.
			break;
			case 235658: //Bodyguard Yatakin.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000174, 1)); //Sentry Post Of Eternity Key.
			break;
			case 235624: //Warmage Suyaroka.
			case 235625: //Chief Medic Tagnu.
			case 235626: //Virulent Ukahim.
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053789, 1)); //Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052572, 1)); //Conquerer's Ancient Manastone Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053998, 1)); //Ornate Enchantment Stone Bundle.
					}
				}
			break;
			case 233391: //Sanctuary Keybox.
				//Be careful in your selection. The key cannot be changed once it is chosen.
				sendMsgByRace(1401946, Race.PC_ALL, 0);
				for (Player player: instance.getPlayersInside()) {
				    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000181, 1)); //The Catacombs Key.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000182, 1)); //The Crypts Key.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000183, 1)); //The Charnels Key.
					}
				}
			break;
			case 233185: //Danuar Sanctuary Jar.
			case 233190: //Stone Treasure Box I.
			case 233191: //Stone Treasure Box II.
			case 233192: //Stone Treasure Box III.
				switch (Rnd.get(1, 5)) {
				    case 1:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 169405254, 2)); //Earth Trace.
				    break;
					case 2:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012592, 2)); //Earth Scrap.
				    break;
					case 3:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012613, 2)); //Burning Vitality.
				    break;
					case 4:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 169405267, 2)); //Flame Vitality.
				    break;
					case 5:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 169405268, 2)); //Lightning Vitality.
				    break;
				} switch (Rnd.get(1, 12)) {
				    case 1:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012580, 2)); //Fire Mote.
				    break;
					case 2:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012581, 2)); //Fire Breath.
				    break;
					case 3:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012582, 2)); //Fire Fragment.
					break;
					case 4:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012583, 2)); //Fire Source.
				    break;
					case 5:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012584, 2)); //Water Source.
				    break;
					case 6:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012585, 2)); //Wind Mote.
				    break;
					case 7:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012586, 2)); //Wind Breath.
				    break;
					case 8:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012587, 2)); //Wind Eternity.
				    break;
					case 9:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012589, 2)); //Wind Source.
					break;
					case 10:
					    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012588, 2)); //Wind Fragment.
					break;
					case 11:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012590, 2)); //Wind Origin.
					break;
					case 12:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152012591, 2)); //Water Fragment.
				    break;
				}
			break;
		}
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 701859: //Metallic Mystic KeyStone.
				if (player.getInventory().isFull()) {
					sendMsgByRace(1390149, Race.PC_ALL, 0);
				}
				despawnNpc(npc);
				ItemService.addItem(player, 188052613, 1); //Sanctuary Treasure Crate.
			break;
			case 701860: //Golden Mystic KeyStone.
				if (player.getInventory().isFull()) {
					sendMsgByRace(1390149, Race.PC_ALL, 0);
				}
				despawnNpc(npc);
				ItemService.addItem(player, 188052613, 1); //Sanctuary Treasure Crate.
			break;
			case 701863: //Spherical Mystic KeyStone.
				//A door has opened somewhere.
				sendMsgByRace(1401838, Race.PC_ALL, 0);
			break;
			case 701864: //Pyramidal Mystic KeyStone.
				//A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 0);
			break;
		}
	}
	
	@Override
    public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
		    case 233084: //Ancien Danuar Coffin.
				despawnNpc(npc);
				switch (Rnd.get(1, 2)) {
					case 1:
					    spawn(233085, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); //Ancient Danuar Remains.
					break;
					case 2:
					break;
				}
			break;
		   /**
			* Attack the rocks to activate the updraft.
			*/
			case 233188: //Sturdy Boulder.
				despawnNpc(npc);
				spawnInfernalBoulder();
			break;
			case 235624: //Warmage Suyaroka.
			case 235625: //Chief Medic Tagnu.
			case 235626: //Virulent Ukahim.
				spawnAbbeyNobleBox();
				//sendMsg("[SUCCES]: You have finished <Danuar Sanctuary>");
				spawn(701876, 1057.1633f, 557.6902f, 284.73123f, (byte) 30); //Danuar Sanctuary Exit.
			break;
        }
    }
	
	private void spawnAbbeyNobleBox() {
	    switch (Rnd.get(1, 2)) {
		    case 1:
				spawn(702658, 1053.4221f, 565.259f, 282.28778f, (byte) 19); //Abbey Box.
			break;
			case 2:
				spawn(702659, 1060.8652f, 565.46436f, 282.2873f, (byte) 41); //Noble Abbey Box.
			break;
		}
	}
	
	private void spawnDanuarSanctuaryBoss() {
	    switch (Rnd.get(1, 3)) {
		    case 1:
				spawn(235624, 1056.5698f, 693.86584f, 282.0391f, (byte) 30); //Warmage Suyaroka.
			break;
			case 2:
				spawn(235625, 1045.4534f, 682.2679f, 282.0391f, (byte) 60); //Chief Medic Tagnu.
			break;
			case 3:
				spawn(235626, 1056.4889f, 670.9826f, 282.0391f, (byte) 91); //Virulent Ukahim.
			break;
		}
	}
	
	private void spawnInfernalBoulder() {
		SpawnTemplate sturdyInfernalBoulder = SpawnEngine.addNewSingleTimeSpawn(301380000, 233187, 906.1991f, 859.88177f, 278.64731f, (byte) 37);
		sturdyInfernalBoulder.setEntityId(1699);
		objects.put(233187, SpawnEngine.spawnObject(sturdyInfernalBoulder, instanceId));
	}
	
	public void removeItems(Player player) {
        Storage storage = player.getInventory();
        storage.decreaseByItemId(185000181, storage.getItemCountByItemId(185000181)); //The Catacombs Key.
		storage.decreaseByItemId(185000182, storage.getItemCountByItemId(185000182)); //The Crypts Key.
        storage.decreaseByItemId(185000183, storage.getItemCountByItemId(185000183)); //The Charnels Key.
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
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	@Override
    public void onInstanceDestroy() {
        doors.clear();
		movies.clear();
    }
}