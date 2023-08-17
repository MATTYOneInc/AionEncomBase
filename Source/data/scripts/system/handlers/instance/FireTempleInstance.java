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
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.Set;

/****/
/** Author (Encom)
/****/

@InstanceID(320100000)
public class FireTempleInstance extends GeneralInstanceHandler
{
	@Override
    public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
            case 212846: //Kromede The Corrupt.
			    spawnKromedeTreasureChest();
				sendMsg("[SUCCES]: You have finished <Fire Temple>");
            break;
			case 214621: //Vile Judge Kromede.
				spawnKromedeTreasureChest();
				sendMsg("[SUCCES]: You have finished <Fire Temple>");
            break;
		}
	}
	
	private void spawnKromedeTreasureChest() {
		switch (Rnd.get(1, 3)) {
		    case 1:
			    announceKromedeOrnate();
				spawn(833523, 418.16385f, 95.81711f, 117.3052f, (byte) 50); //Kromede's Ornate Treasure Chest.
			break;
			case 2:
			    announceKromedeBrilliant();
				spawn(833524, 418.16385f, 95.81711f, 117.3052f, (byte) 50); //Kromede's Brilliant Treasure Chest.
			break;
			case 3:
			    announceKromedeDazzling();
				spawn(833525, 418.16385f, 95.81711f, 117.3052f, (byte) 50); //Kromede's Dazzling Treasure Chest.
			break;
		}
	}
	
	private void announceKromedeOrnate() {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//I need a Kromede's Ornate Key to open it.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(false, 1111313, player.getObjectId(), 2));
				}
			}
		});
	}
	private void announceKromedeBrilliant() {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//I need a Kromede's Brilliant Key to open it.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(false, 1111314, player.getObjectId(), 2));
				}
			}
		});
	}
	private void announceKromedeDazzling() {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//I need a Kromede's Dazzling Key to open it.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(false, 1111315, player.getObjectId(), 2));
				}
			}
		});
	}
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
            case 212846: //Kromede The Corrupt.
			case 214621: //Vile Judge Kromede.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053787, 1)); //Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053994, 1)); //Kromede's Key Bundle.
                    }
                }
            break;
			case 833523: //Kromede's Ornate Treasure Chest.
			case 833524: //Kromede's Brilliant Treasure Chest.
			case 833525: //Kromede's Dazzling Treasure Chest.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 170030000, 1)); //[Souvenir] Kromede's Mirror.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); //Tempering Solution Chest.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053295, 1)); //Empyrean Plume Chest.
                    }
                }
            break;
			case 212840: //Lava Gatneri.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188051411, 1)); //Gatneri's Fabled Weapon Chest.
                    }
                }
            break;
			case 212842: //Black Smoke Asparn.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188051412, 1)); //Asparn's Fabled Weapon Chest.
                    }
                }
            break;
			case 212844: //Silver Blade Rotan.
                switch (Rnd.get(1, 3)) {
				    case 1:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190000016, 1)); //Sawteeth Rotan Egg.
					break;
					case 2:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190000037, 1)); //Sawteeth Rotan Egg.
					break;
					case 3:
				        dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190000038, 1)); //Sawteeth Rotan Egg.
					break;
				}
            break;
        }
    }
	
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
		spawn(212846, 421.96918f, 93.555084f, 117.30522f, (byte) 51); //Kromede The Corrupt.
        switch (Rnd.get(1, 9)) {
		    case 1:
				spawn(212839, 153.20758f, 161.87619f, 101.83779f, (byte) 32); //Blue Crystal Molgat.
			break;
			case 2:
				spawn(212840, 151.76723f, 302.36328f, 122.874084f, (byte) 24); //Lava Gatneri.
			break;
			case 3:
				spawn(212784, 151.76723f, 302.36328f, 122.874084f, (byte) 24); //Inferno Spirit.
			break;
			case 4:
				spawn(212841, 350.2237f, 354.806f, 146.613f, (byte) 65); //Flame Branch Flavi.
			break;
			case 5:
				spawn(212842, 304.33118f, 419.86487f, 133.9806f, (byte) 17); //Black Smoke Asparn.
			break;
			case 6:
				spawn(212843, 297.6783f, 202.19652f, 119.36518f, (byte) 61); //Tough Sipus.
			break;
			case 7:
				spawn(212845, 293.1684f, 97.34582f, 128.40712f, (byte) 91); //Broken Wing Kutisen.
			break;
			case 8:
				deleteNpc(212846); //Kromede The Corrupt.
				spawn(214621, 421.96918f, 93.555084f, 117.30522f, (byte) 51); //Vile Judge Kromede.
			break;
			case 9:
				spawn(798109, 144.581f, 162.128f, 100.931f, (byte) 23); //Liurerk.
			break;
        }
    }
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
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
}