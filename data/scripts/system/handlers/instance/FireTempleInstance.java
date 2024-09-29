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

/**
* Author (Encom)
* Rework: MATTY (ADev.Team)
* Retail: https://youtu.be/kCY-3m2Mukw
**/

@InstanceID(320100000)
public class FireTempleInstance extends GeneralInstanceHandler
{
	@Override
    public void onDie(Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId()) {
            case 212846: //Kromede The Corrupt.
			    spawnKromedeTreasureChest();
				//sendMsg("[SUCCES]: You have finished <Fire Temple>");
            break;
			case 214621: //Vile Judge Kromede.
				spawnKromedeTreasureChest();
				//sendMsg("[SUCCES]: You have finished <Fire Temple>");
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
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053994, 1)); //Kromede's Key Bundle.
                    }
                }
				break;
			case 214621: //Vile Judge Kromede.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053787, 1)); //Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053994, 1)); //Kromede's Key Bundle.
                    }
                }
				break;
			case 833523: //Kromede's Ornate Treasure Chest.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 170030000, 1)); //[Souvenir] Kromede's Mirror.
                    }
                }
				break;
			case 833524: //Kromede's Brilliant Treasure Chest.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 170030000, 1)); //[Souvenir] Kromede's Mirror.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052826, 1)); //Judge's Fabled Weapon Chest
                    }
                }
				break;
			case 833525: //Kromede's Dazzling Treasure Chest.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 170030000, 1)); //[Souvenir] Kromede's Mirror.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052826, 1)); //Judge's Fabled Weapon Chest
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
        }
    }
	
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
		
        // Random spawns of bosses
        if (Rnd.get(1, 100) > 25) { // Blue Crystal Molgat
            spawn(212839, 127.1218f, 176.1912f, 99.67548f, (byte) 15);
        } else { // elite mob spawns
            spawn(212790, 127.1218f, 176.1912f, 99.67548f, (byte) 15);
        }

        if (Rnd.get(1, 100) > 25) { // Black Smoke Asparn
            spawn(212842, 322.3193f, 431.2696f, 134.5296f, (byte) 80);
        } else { // elite mob spawns
            spawn(212799, 322.3193f, 431.2696f, 134.5296f, (byte) 80);
        }

        if (Rnd.get(1, 100) > 25) { // Lava Gatneri
            spawn(212840, 153.0038f, 299.7786f, 123.0186f, (byte) 30);
        } else { // elite mob spawns
            spawn(212794, 153.0038f, 299.7786f, 123.0186f, (byte) 30);
        }

        if (Rnd.get(1, 100) > 25) { // Tough Sipus
            spawn(212843, 296.6911f, 201.9092f, 119.3652f, (byte) 30);
        } else { // elite mob spawns
            spawn(212803, 296.6911f, 201.9092f, 119.3652f, (byte) 15);
        }

        if (Rnd.get(1, 100) > 25) { // Flame Branch Flavi
            spawn(212841, 350.9276f, 351.7389f, 146.8498f, (byte) 45);
        } else { // elite mob spawns
            spawn(212799, 350.9276f, 351.7389f, 146.8498f, (byte) 45);
        }

        if (Rnd.get(1, 100) > 25) { // Broken Wing Kutisen
            spawn(212845, 298.7095f, 89.42245f, 128.7143f, (byte) 15);
        } else { // elite mob spawns
            spawn(214094, 298.7095f, 89.42245f, 128.7143f, (byte) 15);
        }

        if (Rnd.get(1, 100) > 90) {// stronger kromede
            spawn(214621, 421.9935f, 93.18915f, 117.3053f, (byte) 46);
        } else { // normal kromede
            spawn(212846, 421.9935f, 93.18915f, 117.3053f, (byte) 46);
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